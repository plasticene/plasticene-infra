package com.plasticene.shorturl.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/18 16:51
 */
@Data
public class BatchUrlParam {

    @Valid
    @Size(max = 200, min = 1, message = "最少1条，最多200条")
    @ApiModelProperty("长链接url列表")
    private List<ShortUrlParam> urlList;
}
