package com.yu.generator.engin;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;

import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import com.yu.generator.config.MyConstVal;
import com.yu.generator.config.MyTemplateConfig;
import com.yu.generator.config.builder.MyConfigBuilder;
import freemarker.template.Configuration;

import freemarker.template.Template;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-23
 * @Description:
 */
public class MyFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    private static Log log = new SystemStreamLog();

    private Configuration configuration;

    @Override
    public FreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(MyConstVal.UTF8);
        configuration.setClassForTemplateLoading(MyFreemarkerTemplateEngine.class, StringPool.SLASH);
        return this;
    }

    /**
     * 处理输出目录
     */
    public AbstractTemplateEngine mkdirs() {
        log.info("mkdirs ");
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            File dir = new File(value);
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    log.debug("创建目录： [" + value + "]");
                }
            }
        });
        return this;
    }

    /**
     * 输出 java xml 文件
     */
    public AbstractTemplateEngine batchOutput() {
        log.info("batchOutput ");
        try {
            MyConfigBuilder configBuilder = (MyConfigBuilder) getConfigBuilder();
            List<TableInfo> tableInfoList = configBuilder.getTableInfoList();

            Map<String, TableInfo> dtoTableInfoMap = new HashMap<>();
            List<TableInfo> dtoTableInfoList = configBuilder.getDtoTableInfoList();
            dtoTableInfoList.forEach(e -> dtoTableInfoMap.put(e.getName(), e));

            for (TableInfo tableInfo : tableInfoList) {
                TableInfo dtoTableInfo = dtoTableInfoMap.get(tableInfo.getName());
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = configBuilder.getPathInfo();
                MyTemplateConfig template = (MyTemplateConfig) configBuilder.getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = configBuilder.getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initMap();
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }

                // PrimaryKey.java
                if (null != pathInfo.get(MyConstVal.BASE_ENTITY)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_ENTITY) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_ENTITY_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseEntiry()), entityFile);
                    }
                }

                // EntityMapper.java
                if (null != pathInfo.get(MyConstVal.BASE_ENTITY_MAPPER)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_ENTITY_MAPPER) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_ENTITY_MAPPER_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseMapper()), entityFile);
                    }
                }

                // BaseSerivce.java
                if (null != pathInfo.get(MyConstVal.BASE_SERVICE)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_SERVICE) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_SERVICE_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseService()), entityFile);
                    }
                }

                // BaseSerivceImpl.java
                if (null != pathInfo.get(MyConstVal.BASE_SERVICE_IMPL)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_SERVICE_IMPL) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_SERVICE_IMPL_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseServiceImpl()), entityFile);
                    }
                }

                // Entity.java
                String entityName = tableInfo.getEntityName();
                if (null != entityName && null != pathInfo.get(MyConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writer(objectMap, templateFilePath(template.getEntity(configBuilder.getGlobalConfig().isKotlin())), entityFile);
                    }
                }
                // Dao.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(MyConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(MyConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(MyConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(MyConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + MyConstVal.XML_SUFFIX), entityName);
                    log.info("xmlFile:{}" + xmlFile);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writer(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }
                // IMpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(MyConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(MyConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writer(objectMap, templateFilePath(template.getService()), serviceFile);
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(MyConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(MyConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                    }
                }
                // MpController.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(MyConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(MyConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writer(objectMap, templateFilePath(template.getController()), controllerFile);
                    }
                }
            }
        } catch (Exception e) {
            log.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }
    
    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, MyConstVal.UTF8));
        }
        log.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

}
