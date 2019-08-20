package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import lombok.Data;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description:
 */
@Data
public class MyStrategyConfig extends StrategyConfig {

    private String superDtoMapperClass ;

    private String superDtoClass;
}
