package com.yu.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.yu.generator.config.CodeGeneratorConfig;
import com.yu.generator.config.builder.MyConfigBuilder;
import com.yu.generator.engin.MyFreemarkerTemplateEngine;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

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
        configBuilder.getPackageInfo().remove(ConstVal.XML);
        AbstractTemplateEngine  abstractTemplateEngine = this.getTemplateEngine().init(configBuilder).mkdirs();
        abstractTemplateEngine.batchOutput();
        log.info("open()");
        abstractTemplateEngine.open();

        log.info("==========================文件生成完成！！！==========================");

    }


}
