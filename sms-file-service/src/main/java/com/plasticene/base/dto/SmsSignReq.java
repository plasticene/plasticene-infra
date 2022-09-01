package com.plasticene.base.dto;

import com.aliyuncs.dysmsapi.model.v20170525.AddSmsSignRequest;
import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 09:55
 *
 *  "Remark": "xxxx",
 *     "SignPurpose": 1,
 *     "SignType": 0,
 *     "SignName": "腾讯云",
 *     "DocumentType": 1,
 *     "International": 0,
 *     "ProofImage": "xxxx"
 */
@Data
public class SmsSignReq {

    // 阿里云参数
    private String signName;
    private String remark;
    private Integer signType;
//    private Integer signSource;
//    private List<AddSmsSignRequest.SignFileList> fileList;

    // 腾讯云参数
//    private String SignName;
//    private Integer SignType;
//    private Integer DocumentType;
//    private Integer International;
//    private Integer SignPurpose;
//    private Integer ProofImage;

    // 云片参数
    private String apikey;



}
