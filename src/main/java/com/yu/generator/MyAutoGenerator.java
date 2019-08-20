package com.yu.generator;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.yu.generator.config.CodeGeneratorConfig;
import com.yu.generator.config.builder.MyConfigBuilder;
import com.yu.generator.config.MyStrategyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description:
 */
public class MyAutoGenerator extends AutoGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MyAutoGenerator.class);

    public MyAutoGenerator(String generatorConfigFile) {
        CodeGeneratorConfig.init(generatorConfigFile);
        this.setGlobalConfig(CodeGeneratorConfig.getGlobalConfig());
        this.setDataSource(CodeGeneratorConfig.getDataSourceConfig());
        this.setPackageInfo(CodeGeneratorConfig.getPackageConfig());
        this.setCfg(CodeGeneratorConfig.getInjectionConfig());
        this.setTemplate(CodeGeneratorConfig.getTemplateConfig());
        this.setStrategy(CodeGeneratorConfig.getStrategyConfig());
    }

    @Override
    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        if(null == this.config) {
            this.config = new MyConfigBuilder(this.getPackageInfo(), this.getDataSource(), this.getStrategy(), this.getTemplate(), this.getGlobalConfig());
            if(null != this.injectionConfig) {
                this.injectionConfig.setConfig(this.config);
            }
        }

        if(null == this.getTemplateEngine()) {
            this.setTemplateEngine( new VelocityTemplateEngine());
        }

        this.getTemplateEngine().init(this.pretreatmentConfigBuilder(this.config)).mkdirs().batchOutput().open();
        logger.debug("==========================文件生成完成！！！==========================");
    }

    @Override
    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        /*
         * 注入自定义配置
         */
        if (null != injectionConfig) {
            injectionConfig.initMap();
            if (config.getStrategyConfig() instanceof MyStrategyConfig){
                MyStrategyConfig strategyConfig = (MyStrategyConfig) config.getStrategyConfig();
                String superDtoClass = strategyConfig.getSuperDtoClass();
                String superDtoMapperClass = strategyConfig.getSuperDtoMapperClass();
                injectionConfig.getMap().put("superDtoClassPakege", superDtoClass.substring(0, superDtoClass.lastIndexOf(".")));
                injectionConfig.getMap().put("superDtoClass", superDtoClass.substring(superDtoClass.lastIndexOf(".") + 1, superDtoClass.length()));
                injectionConfig.getMap().put("superDtoClassPakege", superDtoMapperClass.substring(0, superDtoMapperClass.lastIndexOf(".")));
                injectionConfig.getMap().put("superDtoMapperClass", superDtoMapperClass.substring(superDtoMapperClass.lastIndexOf(".") + 1, superDtoMapperClass.length()));
            }
            config.setInjectionConfig(injectionConfig);
        }
        /*
         * 表信息列表
         */
        List<TableInfo> tableList = this.getAllTableInfoList(config);
        for (TableInfo tableInfo : tableList) {
            /* ---------- 添加导入包 ---------- */
            if (config.getGlobalConfig().isActiveRecord()) {
                // 开启 ActiveRecord 模式
                tableInfo.setImportPackages(Model.class.getCanonicalName());
            }
            if (tableInfo.isConvert()) {
                // 表注解
                tableInfo.setImportPackages(TableName.class.getCanonicalName());
            }
            if (config.getStrategyConfig().getLogicDeleteFieldName() != null && tableInfo.isLogicDelete(config.getStrategyConfig().getLogicDeleteFieldName())) {
                // 逻辑删除注解
                tableInfo.setImportPackages(TableLogic.class.getCanonicalName());
            }
            if (StringUtils.isNotEmpty(config.getStrategyConfig().getVersionFieldName())) {
                // 乐观锁注解
                tableInfo.setImportPackages(Version.class.getCanonicalName());
            }
            boolean importSerializable = true;
            if (StringUtils.isNotEmpty(config.getSuperEntityClass())) {
                // 父实体
                tableInfo.setImportPackages(config.getSuperEntityClass());
                importSerializable = false;
            }
            if (config.getGlobalConfig().isActiveRecord()) {
                importSerializable = true;
            }
            if (importSerializable) {
                tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            }
            // Boolean类型is前缀处理
            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()) {
                tableInfo.getFields().stream().filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
                        .filter(field -> field.getPropertyName().startsWith("is"))
                        .forEach(field -> {
                            field.setConvert(true);
                            field.setPropertyName(StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                        });
            }
        }
        return config.setTableInfoList(tableList);
    }

}
