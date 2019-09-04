package com.yu.generator;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.yu.generator.config.CodeGeneratorConfig;
import com.yu.generator.config.builder.MyConfigBuilder;
import com.yu.generator.engin.MyFreemarkerTemplateEngine;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description: 代码生成器
 */
public class MyAutoGenerator extends AutoGenerator {

    private static Log log = new SystemStreamLog();

    public MyAutoGenerator(String generatorConfigFile) {
        CodeGeneratorConfig.init(generatorConfigFile);
        this.setGlobalConfig(CodeGeneratorConfig.getGlobalConfig());
        this.setDataSource(CodeGeneratorConfig.getDataSourceConfig());
        this.setPackageInfo(CodeGeneratorConfig.getPackageConfig());
        this.setTemplate(CodeGeneratorConfig.getTemplateConfig());
        this.setStrategy(CodeGeneratorConfig.getStrategyConfig());
        this.setCfg(CodeGeneratorConfig.getInjectionConfig());
        this.setTemplateEngine(new MyFreemarkerTemplateEngine());
    }

    @Override
    public void execute() {
        log.info("==========================准备生成文件...==========================");

        if(null == this.config) {

            this.config = new MyConfigBuilder(this.getPackageInfo(), this.getDataSource(), this.getStrategy(), this.getTemplate(), this.getGlobalConfig());
            if(null != this.injectionConfig) {
                this.injectionConfig.setConfig(this.config);
            }
        }
        if(null == this.getTemplateEngine()) {
            this.setTemplateEngine( new VelocityTemplateEngine());
        }

        ConfigBuilder configBuilder = this.pretreatmentConfigBuilder(this.config);
        //configBuilder.getPackageInfo().remove(ConstVal.XML);
        this.getTemplateEngine().init(configBuilder).mkdirs().batchOutput().open();

        log.info("==========================文件生成完成！！！==========================");

    }

    /**
     * 预处理配置
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        MyConfigBuilder myConfigBuilder = (MyConfigBuilder) config;
        /*
         * 注入自定义配置
         */
        if (null != injectionConfig) {
            injectionConfig.initMap();
            myConfigBuilder.setInjectionConfig(injectionConfig);
        }
        /*
         * 表信息列表
         */
        List<TableInfo> tableList = this.getAllTableInfoList(config);
        List<TableInfo> dtoTableInfoList = this.getAllDtoTableInfoList(config);
        for (TableInfo tableInfo : tableList) {
            /* ---------- 添加导入包 ---------- */
            if (myConfigBuilder.getGlobalConfig().isActiveRecord()) {
                // 开启 ActiveRecord 模式
                tableInfo.setImportPackages(Model.class.getCanonicalName());
            }
            if (tableInfo.isConvert()) {
                // 表注解
                tableInfo.setImportPackages(TableName.class.getCanonicalName());
            }
            if (myConfigBuilder.getStrategyConfig().getLogicDeleteFieldName() != null && tableInfo.isLogicDelete(myConfigBuilder.getStrategyConfig().getLogicDeleteFieldName())) {
                // 逻辑删除注解
                tableInfo.setImportPackages(TableLogic.class.getCanonicalName());
            }
            if (StringUtils.isNotEmpty(myConfigBuilder.getStrategyConfig().getVersionFieldName())) {
                // 乐观锁注解
                tableInfo.setImportPackages(Version.class.getCanonicalName());
            }
            boolean importSerializable = true;
            if (StringUtils.isNotEmpty(myConfigBuilder.getSuperEntityClass())) {
                // 父实体
                tableInfo.setImportPackages(myConfigBuilder.getSuperEntityClass());
                importSerializable = false;
            }
            if (myConfigBuilder.getGlobalConfig().isActiveRecord()) {
                importSerializable = true;
            }
            if (importSerializable) {
                tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            }
            // Boolean类型is前缀处理
            if (myConfigBuilder.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()) {
                tableInfo.getFields().stream().filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
                        .filter(field -> field.getPropertyName().startsWith("is"))
                        .forEach(field -> {
                            field.setConvert(true);
                            field.setPropertyName(StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                        });
            }
        }

        for (TableInfo tableInfo : dtoTableInfoList) {
            /* ---------- 添加导入包 ---------- */
            if (StringUtils.isNotEmpty(myConfigBuilder.getSuperDtoClass())) {
                // 父实体
                tableInfo.setImportPackages(myConfigBuilder.getSuperDtoClass());
            }
            // Boolean类型is前缀处理
            if (myConfigBuilder.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()) {
                tableInfo.getFields().stream().filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
                        .filter(field -> field.getPropertyName().startsWith("is"))
                        .forEach(field -> {
                            field.setConvert(true);
                            field.setPropertyName(StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                        });
            }
        }
        myConfigBuilder.setDtoTableInfoList(dtoTableInfoList);
        return myConfigBuilder.setTableInfoList(tableList);
    }

    /**
     * 开放表信息、预留子类重写
     *
     * @param config 配置信息
     * @return ignore
     */
    protected List<TableInfo> getAllDtoTableInfoList(ConfigBuilder config) {
        MyConfigBuilder configBuilder = (MyConfigBuilder) config;
        return configBuilder.getDtoTableInfoList();
    }
}
