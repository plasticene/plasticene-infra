package com.plasticene.base.service.impl;

import com.plasticene.base.constant.SmsConstant;
import com.plasticene.base.dao.SmsPlanDAO;
import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.entity.SmsPlan;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.service.SmsPlanService;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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

    /**
     *
     * @param param
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
        // 这里使用监听redis过期key来实现定时发送，这种方式并不稳妥，具体原因看RedisKeyExpiredListener类注释
        // 如果是立即执行的就直接异步执行
        if (Objects.equals(smsPlanDTO.getExecuteType(), SmsConstant.EXECUTE_TYPE_NOW)) {
            executorService.execute(()->{
                smsSendService.addSmsRecordAndSendSms(smsPlanDTO);
            });
        } else {
            Date sendTime = param.getSendTime();
            int second = getSecond(new Date(), sendTime);
            redisTemplate.opsForValue().set(SmsConstant.SMS_PLAN_KEY + id, "", second, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(SmsConstant.SMS_PLAN_VALUE + id, smsPlanDTO);
        }

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

    public static void main(String[] args) throws ParseException {
//        Date date = DateUtils.parseDate("2022-09-07 10:20:00", "yyyy-MM-dd HH:mm:ss");
//        int minute = getMinute(new Date(), date);
//        System.out.println(minute);
    }
}
