package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-23
 * @Description: 自定义策略
 */
@Data
@Accessors(chain = true)
public class MyStrategyConfig extends StrategyConfig{

    private String superDtoClass;//super dto class

    private String superEntityMapper;//super entity mapper class

    private String[] superDtoColumns;//自定义基础的DTO类，公共字段
}
