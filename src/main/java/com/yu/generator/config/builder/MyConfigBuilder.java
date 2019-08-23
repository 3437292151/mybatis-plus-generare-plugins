package com.yu.generator.config.builder;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.yu.generator.config.MyConstVal;
import com.yu.generator.config.po.MyTableInfo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.util.List;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description:
 */
public class MyConfigBuilder extends ConfigBuilder {

    /**
     * 数据库表信息
     */
    private List<MyTableInfo> tableInfoList;

    private static Log log = new SystemStreamLog();

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
        //super();
        super(packageConfig, dataSourceConfig, strategyConfig, template, globalConfig);
        super.getTableInfoList();
        handlerStrategy(strategyConfig, globalConfig);
    }

    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config, GlobalConfig globalConfig) {
        log.info("config: "+ config);
        this.getTableInfoList().forEach(e -> {
            e.setMapperName(e.getEntityName() + MyConstVal.MAPPER);
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

}
