package com.yu.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang.StringUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-9
 * @Description:
 */
public class CodeGeneratorConfig {

    private GlobalConfig globalConfig;

    private DataSourceConfig dataSourceConfig;

    private PackageConfig packageConfig;

    private TemplateConfig templateConfig;

    private InjectionConfig injectionConfig;

    private StrategyConfig strategyConfig;

    private static Properties properties;

    public static GlobalConfig getGlobalConfig(){
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("yuchanglong");
        gc.setOpen(false);
        return gc;
    }

    public static DataSourceConfig getDataSourceConfig(){
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
       /* dsc.setUrl(dataSourceProperties.getUrl());
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(dataSourceProperties.getUsername());
        dsc.setPassword(dataSourceProperties.getPassword());*/
        return dsc;
    }

    public static PackageConfig getPackageConfig(){
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null);
        pc.setParent("com.yu");
        pc.setEntity("domain");
        pc.setController("web.rest");
        pc.setMapper("dao");
        return pc;
    }

    public static InjectionConfig getInjectionConfig(){
        String property = System.getProperty("user.dir");
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("superDtoClass","");
                map.put("superDtoClassPackage","");
                map.put("superServiceMapperClass", "EntityMapper");
                map.put("superServiceMapperClassPackage","com.yu.service.mapper");

                this.setMap(map);
            }
        };


        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return property + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Dao" + StringPool.DOT_XML;
            }
        });
        templatePath = "/templates/myDao.java.ftl";

        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
               String out = property + "/src/main/java/" +
                        getPackageConfig().getParent().replace(".","/")
                        + "/" +
                        getPackageConfig().getMapper().replace(".","/")
                        + "/"
                        + tableInfo.getEntityName() + "Dao" + StringPool.DOT_JAVA;
                System.out.println(out);
                return out;
            }
        });

        templatePath = "/templates/entityDTO.java.ftl";

        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String out = property + "/src/main/java/" +
                        getPackageConfig().getParent().replace(".","/")
                        + "/"
                        + getPackageConfig().getService().replace(".","/")
                        + "/dto/"
                        + tableInfo.getEntityName() + "DTO" + StringPool.DOT_JAVA;
                System.out.println(out);
                return out;
            }
        });

        templatePath = "/templates/mymapper.java.ftl";

        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String out = property + "/src/main/java/" +
                        getPackageConfig().getParent().replace(".","/")
                        + "/" +
                        getPackageConfig().getService().replace(".","/")
                        + "/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_JAVA;
                System.out.println(out);
                return out;
            }
        });


        cfg.setFileOutConfigList(focList);
        return cfg;
    }

   public static TemplateConfig getTemplateConfig(){
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        String service = properties.getProperty("tc.service");
        if (!StringUtils.isBlank(service)){
           templateConfig.setService(service);
        }
       String serviceImpl = properties.getProperty("tc.serviceImpl");
        if (!StringUtils.isBlank(serviceImpl)){
            templateConfig.setServiceImpl(serviceImpl);
        }
        String dao = properties.getProperty("tc.dao");
        if (!StringUtils.isBlank(dao)){
            templateConfig.setMapper(dao);
        }
        String xml = properties.getProperty("tc.xml");
       if (!StringUtils.isBlank(xml)){
           templateConfig.setXml(xml);
       }
        return templateConfig;
   }

   public static StrategyConfig getStrategyConfig(){
       // 策略配置
       MyStrategyConfig strategy = new MyStrategyConfig();
       strategy.setNaming(NamingStrategy.underline_to_camel);
       strategy.setColumnNaming(NamingStrategy.underline_to_camel);
       Set<String> propertyNames = properties.stringPropertyNames();
       for (String propertyName : propertyNames){
           if( propertyName.startsWith("sc")){
               if (StringUtils.equals(propertyName, "sc.SuperEntityClass")){
                   String property = properties.getProperty(propertyName);
                   if (StringUtils.isBlank(property)){
                       strategy.setSuperEntityClass(property);
                   }
               }
           }
       }
       strategy.setSuperEntityClass("com.yu.domain.PrimaryKey");
       strategy.setSuperServiceClass("com.yu.service.IService");
       strategy.setSuperServiceImplClass("com.yu.service.impl.ServiceImpl");
       strategy.setSuperDtoMapperClass("com.yu.service.mapper.EntityMapper");
       strategy.setSuperDtoClass("com.yu.service.dto.SystemEntityDTO");
       strategy.setEntityLombokModel(true);
       strategy.setRestControllerStyle(true);
       // 公共父类
       //strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
       // 写于父类中的公共字段
       strategy.setSuperEntityColumns("id","def_user", "def_user_name","upd_user","upd_user_name");
       strategy.setInclude("mcr_t_dict_item");
       strategy.setControllerMappingHyphenStyle(true);
       //strategy.setTablePrefix(pc.getModuleName() + "_");
       return  strategy;
   }

   public static void init(String generatorConfigFile){
       InputStream resourceAsStream = CodeGeneratorConfig.class.getClassLoader().getResourceAsStream("generatorConfig.properties");
       try {
           properties.load(resourceAsStream);
       } catch (IOException e) {
           e.printStackTrace();
       }finally {
           try {
               resourceAsStream.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

}
