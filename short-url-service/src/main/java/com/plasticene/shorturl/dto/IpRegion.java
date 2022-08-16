package com.plasticene.shorturl.dto;

import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/16 19:56
 */
@Data
public class IpRegion {
    private String country;
    private String area;
    private String province;
    private String city;
    private String isp;
}
