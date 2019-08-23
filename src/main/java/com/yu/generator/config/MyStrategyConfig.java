package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-23
 * @Description:
 */
@Data
@Accessors(chain = true)
public class MyStrategyConfig extends StrategyConfig{

    private String superDtoMapperClass ;

    private String superDtoClass;

    private String[] superDtoColumns;//自定义基础的DTO类，公共字段
}
