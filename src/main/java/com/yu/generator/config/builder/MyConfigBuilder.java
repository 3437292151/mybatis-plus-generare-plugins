package com.yu.generator.config.builder;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.yu.generator.config.MyConstVal;
import com.yu.generator.config.MyStrategyConfig;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description:
 */
public class MyConfigBuilder extends ConfigBuilder {

    private String superDtoClass ;

    private String superDtoMapperClass;

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param template         模板配置
     * @param globalConfig     全局配置
     */
    public MyConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, TemplateConfig template, GlobalConfig globalConfig) {
        super(packageConfig, dataSourceConfig, strategyConfig, template, globalConfig);

        handlerStrategy(strategyConfig, globalConfig);
    }

    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config, GlobalConfig globalConfig) {
        processTypes(config);
        this.getTableInfoList().forEach(e -> {
            if (StringUtils.isNotEmpty(globalConfig.getServiceName())) {
                e.setServiceName(String.format(globalConfig.getServiceName(), e.getEntityName()));
            } else {
                e.setServiceName(e.getEntityName() + ConstVal.SERVICE);
            }
            if (StringUtils.isNotEmpty(globalConfig.getControllerName())) {
                e.setControllerName(String.format(globalConfig.getControllerName(), e.getEntityName()));
            } else {
                e.setControllerName(e.getEntityName() + MyConstVal.CONTROLLER);
            }
        });
    }

    /**
     * 处理superClassName,IdClassType,IdStrategy配置
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {

        if (config instanceof MyStrategyConfig){
            MyStrategyConfig myStrategyConfig = (MyStrategyConfig) config;
            superDtoClass = myStrategyConfig.getSuperDtoClass();
            superDtoMapperClass = myStrategyConfig.getSuperDtoMapperClass();
        }
    }

}
