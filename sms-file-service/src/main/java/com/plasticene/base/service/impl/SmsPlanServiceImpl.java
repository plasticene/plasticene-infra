package com.plasticene.base.service.impl;

import com.plasticene.base.constant.SmsConstant;
import com.plasticene.base.dao.SmsPlanDAO;
import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.dto.SmsRecordDTO;
import com.plasticene.base.entity.SmsPlan;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.query.SmsPlanQuery;
import com.plasticene.base.service.SmsPlanService;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/6 09:51
 */
@Service
public class SmsPlanServiceImpl implements SmsPlanService {

    @Resource
    private SmsPlanDAO smsPlanDAO;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SmsSendService smsSendService;
    @Resource
    private ExecutorService executorService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建短信计划之后，接下来就是异步调用短信平台发送短信及后续功能了，由于我们这里可以定时发送短信，即延时执行任务
     * 所以对分布式延时任务方案的确定之前，先看看延时任务和定时任务的区别
     * 1. 定时任务有明确的触发时间，延时任务没有
     * 2. 定时任务有执行周期，而延时任务在某事件触发后一段时间内执行，没有执行周期
     * 3. 定时任务一般执行的是批处理操作是多个任务，而延时任务一般是单个任务
     *
     * 执行延时任务方案大概有三种：
     * 1. 定时扫描：启定时任务每隔一个时间段扫描一次，查询已经过了执行时间的任务进行执行，这里的扫描可以扫数据库或者redis，
     *            取决于你怎么存储，如果用redis存储的话使用zset数据结构比较合适，执行时间为score
     *            缺点：执行时间不准确，有可能你刚刚你的任务快要到执行时间时，定时任务当前周期刚刚执行完，你需要等下一个周期
     *                 数据堆积，造成系统压力过大，有可能你在一个周期内要执行很多任务
     * 2. 监听redis key过期：我们可以通过计算任务定时执行时间，就是我们存入redis key的过期时间，这样我们就可以通过redis的消息
     *                     发布订阅功能去执行任务了
     *                     缺点：这种方式并不稳妥，redis的消息发布订阅不可靠，消息过期也不一定会马上删除监听到，这样也就造成执行时间
     *                          不准确，具体详情看{@link com.plasticene.base.listener.RedisKeyExpiredListener}
     * 3. 消息队列之死信队列：当消息在一个队列中变成死信(dead message)之后，它能被重新发送到另一个交换机中，这个交换机就是DLX，绑定DLX的
     *                    队列就称之为死信队列,rabbitmq能实现死信队列，详见{@link com.plasticene.base.config.RabbitmqConfig}
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsPlan(SmsPlanParam param) {
        SmsPlan smsPlan = PtcBeanUtils.copy(param, SmsPlan.class);
        long id = idGenerator.nextId();
        smsPlan.setId(id);
        smsPlan.setTotalCount(smsPlan.getMobiles().size());
        smsPlanDAO.insert(smsPlan);
        SmsPlanDTO smsPlanDTO = toSmsPlanDTO(smsPlan);
        if (Objects.equals(smsPlanDTO.getExecuteType(), SmsConstant.EXECUTE_TYPE_NOW)) {
            rabbitTemplate.convertAndSend("sms-event-exchange","sms.send.now", smsPlanDTO);
        } else {
            Date sendTime = param.getSendTime();
            int milliSecond = getSecond(new Date(), sendTime) * 1000;
            MessagePostProcessor messagePostProcessor = (Message message) -> {
                // 设置消息过期时间，单位毫秒
                message.getMessageProperties().setExpiration(String.valueOf(milliSecond));
                //设置编码
                message.getMessageProperties().setContentEncoding("UTF-8");
                return message;
            };
            rabbitTemplate.convertAndSend("sms-event-exchange", "sms.send.delay", smsPlanDTO, messagePostProcessor);
        }
        // 下面是监听redis key过期实现
        // 如果是立即执行的就直接异步执行
//        if (Objects.equals(smsPlanDTO.getExecuteType(), SmsConstant.EXECUTE_TYPE_NOW)) {
//            executorService.execute(()->{
//                smsSendService.addSmsRecordAndSendSms(smsPlanDTO);
//            });
//        } else {
//            Date sendTime = param.getSendTime();
//            int second = getSecond(new Date(), sendTime);
//            redisTemplate.opsForValue().set(SmsConstant.SMS_PLAN_KEY + id, "", second, TimeUnit.SECONDS);
//            redisTemplate.opsForValue().set(SmsConstant.SMS_PLAN_VALUE + id, smsPlanDTO);
//        }

    }

    @Override
    public PageResult<SmsPlanDTO> getList(SmsPlanQuery query) {
        LambdaQueryWrapperX<SmsPlan> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(SmsPlan::getName, query.getName());
        PageParam param = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<SmsPlan> pageResult = smsPlanDAO.selectPage(param, queryWrapper);
        List<SmsPlan> smsPlans = pageResult.getList();
        List<SmsPlanDTO> smsPlanDTOList = new ArrayList<>();
        smsPlans.forEach(smsPlan -> {
            smsPlanDTOList.add(toSmsPlanDTO(smsPlan));
        });
        PageResult<SmsPlanDTO> result = new PageResult<>();
        result.setList(smsPlanDTOList);
        result.setPages(pageResult.getPages());
        result.setTotal(pageResult.getTotal());
        return result;
    }

    SmsPlanDTO toSmsPlanDTO(SmsPlan smsPlan) {
        if (Objects.isNull(smsPlan)) {
            return null;
        }
        SmsPlanDTO smsPlanDTO = PtcBeanUtils.copy(smsPlan, SmsPlanDTO.class);
        smsPlanDTO.setPlanId(smsPlan.getId());
        smsPlanDTO.setMobiles(new ArrayList<>(smsPlan.getMobiles()));
        return smsPlanDTO;
    }

    public  int getSecond(Date current, Date sendTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sendTime);
        long sTime = calendar.getTimeInMillis();
        calendar.setTime(current);
        long cTime = calendar.getTimeInMillis();
        long second = (sTime - cTime) / 1000;
        return (int)second;
    }


}
