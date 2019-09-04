package com.yu.generator.engin;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
        log.info("========================== 开始输出文件... ==========================");
        int i = 0;
        try {
            MyConfigBuilder configBuilder = (MyConfigBuilder) getConfigBuilder();
            List<TableInfo> tableInfoList = configBuilder.getTableInfoList();

            Map<String, TableInfo> dtoTableInfoMap = new HashMap<>();
            List<TableInfo> dtoTableInfoList = configBuilder.getDtoTableInfoList();
            dtoTableInfoList.forEach(e -> dtoTableInfoMap.put(e.getName(), e));

            for (TableInfo tableInfo : tableInfoList) {
                TableInfo dtoTableInfo = dtoTableInfoMap.get(tableInfo.getName());
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                getObjectMap(configBuilder, dtoTableInfo, objectMap);
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
                                i++;
                            }
                        }
                    }
                }

                // PrimaryKey.java
                if (null != pathInfo.get(MyConstVal.BASE_ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_ENTITY_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseEntiry()), entityFile);
                        i++;
                    }
                }

                // BaseEntityMapper.java
                if (null != pathInfo.get(MyConstVal.BASE_ENTITY_MAPPER_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_ENTITY_MAPPER_PATH) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_ENTITY_MAPPER_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseMapper()), entityFile);
                        i++;
                    }
                }

                // BaseSerivce.java
                if (null != pathInfo.get(MyConstVal.BASE_SERVICE_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_SERVICE_PATH) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_SERVICE_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseService()), entityFile);
                        i++;
                    }
                }

                // BaseSerivceImpl.java
                if (null != pathInfo.get(MyConstVal.BASE_SERVICE_IMPL_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.BASE_SERVICE_IMPL_PATH) + File.separator + "%s" + suffixJavaOrKt()), MyConstVal.BASE_SERVICE_IMPL_CLASS);
                    if (isCreate(FileType.OTHER, entityFile)) {
                        writer(objectMap, templateFilePath(template.getBaseServiceImpl()), entityFile);
                        i++;
                    }
                }

                // Entity.java
                String entityName = tableInfo.getEntityName();
                if (null != entityName && null != pathInfo.get(MyConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writer(objectMap, templateFilePath(template.getEntity(configBuilder.getGlobalConfig().isKotlin())), entityFile);
                        i++;
                    }
                }
                // Dao.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(MyConstVal.DAO_PATH)) {
                    String mapperFile = String.format((pathInfo.get(MyConstVal.DAO_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writer(objectMap, templateFilePath(template.getDao()), mapperFile);
                        i++;
                    }
                }
                // Dao.xml
                if (null != pathInfo.get(MyConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(MyConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + MyConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writer(objectMap, templateFilePath(template.getXml()), xmlFile);
                        i++;
                    }
                }
                // MpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(MyConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(MyConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writer(objectMap, templateFilePath(template.getService()), serviceFile);
                        i++;
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(MyConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(MyConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                        i++;
                    }
                }
                // DTO.java
                if (null != pathInfo.get(MyConstVal.DTO_PATH)) {
                    String entityFile = String.format((pathInfo.get(MyConstVal.DTO_PATH) + File.separator + "%s" + suffixJavaOrKt()), dtoTableInfo.getEntityName());
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writer(objectMap, templateFilePath(template.getDto()), entityFile);
                        i++;
                    }
                }

                // EntityMapper.java
                if (null != pathInfo.get(MyConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(MyConstVal.MAPPER_PATH) + File.separator + "%s" + suffixJavaOrKt()), dtoTableInfo.getMapperName());
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
                        i++;
                    }
                }
                // MpController.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(MyConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(MyConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writer(objectMap, templateFilePath(template.getController()), controllerFile);
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            log.error("无法创建文件，请检查配置信息！", e);
        }
        log.info("========================== 输出文件["+ i +"]个 ==========================");
        return this;
    }

    private void getObjectMap(MyConfigBuilder configBuilder, TableInfo dtoTableInfo, Map<String, Object> objectMap) {
        objectMap.put("dtoTable", dtoTableInfo);
        objectMap.put("BaseEntity", MyConstVal.BASE_ENTITY_CLASS);
        objectMap.put("BaseEntityMapper", MyConstVal.BASE_ENTITY_MAPPER_CLASS);
        objectMap.put("BaseService", MyConstVal.BASE_SERVICE_CLASS);
        objectMap.put("BaseServiceImpl", MyConstVal.BASE_SERVICE_IMPL_CLASS);
        objectMap.put("Mapper", dtoTableInfo.getMapperName());
        objectMap.put("entityDTO", dtoTableInfo.getEntityName());
        if (!org.apache.commons.lang.StringUtils.isBlank(configBuilder.getSuperDtoClass())){
            objectMap.put("superEntityDTOClass", getSuperClassName(configBuilder.getSuperDtoClass()));
            objectMap.put("superEntityDTOClassPackage", configBuilder.getSuperDtoClass());
        }
        if (org.apache.commons.lang.StringUtils.isBlank(configBuilder.getSuperDtoEntityMapper())){
            objectMap.put("superDtoEntityMapperClass", MyConstVal.BASE_ENTITY_MAPPER_CLASS);
            objectMap.put("superDtoEntityMapperClassPackage", configBuilder.getPackageInfo().get("parent") + StringPool.DOT + MyConstVal.MAPPER_PACKAGE + StringPool.DOT + MyConstVal.BASE_ENTITY_MAPPER);
        }else {
            objectMap.put("superDtoEntityMapperClass", getSuperClassName(configBuilder.getSuperDtoClass()));
            objectMap.put("superDtoEntityMapperClassPackage", configBuilder.getSuperDtoClass());
        }
    }

    /**
     * 获取类名
     *
     * @param classPath ignore
     * @return ignore
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isEmpty(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, MyConstVal.UTF8));
        }
        log.info("模板文件: [" + templatePath + "]");
        log.info("输出文件: [" + outputFile + "]");
    }

}
