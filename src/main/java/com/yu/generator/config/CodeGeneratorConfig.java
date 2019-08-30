package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.yu.generator.MyInjectionConfig;
import com.yu.generator.util.ObjectUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;


import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-9
 * @Description:
 */
@Data
public class CodeGeneratorConfig {

    private static Properties properties = new Properties();

    private static Log log = new SystemStreamLog();

    private static GlobalConfig globalConfig;

    private static DataSourceConfig dataSourceConfig;

    private static PackageConfig packageConfig;

    private static InjectionConfig injectionConfig;

    private static TemplateConfig templateConfig;

    private static StrategyConfig strategyConfig;

    public static TemplateConfig getTemplateConfig() {
        return templateConfig;
    }

    public static StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public static DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public static PackageConfig getPackageConfig() {
        return packageConfig;
    }

    public static InjectionConfig getInjectionConfig() {
        return injectionConfig;
    }

    /**
     * @Author yuchanglong
     * @Date 2019-8-23
     * @Description 设置全局参数
     * @Param
     * @return com.baomidou.mybatisplus.generator.config.GlobalConfig
     **/
    public static void setGlobalConfig(){
        GlobalConfig gc = new GlobalConfig();
        Set<String> propertyNames = properties.stringPropertyNames();
        boolean outputDirflag = true;
        boolean authorFlag = true;
        boolean openFlag = true;
        boolean baseRseultMap = true;
        boolean baseColumnList = true;

        for (String propertyName : propertyNames){
            if (propertyName.startsWith("global.")){
                String property = properties.getProperty(propertyName);
                propertyName = propertyName.replace("global.", "");
                if (StringUtils.equals("outputDir", propertyName) && !StringUtils.isBlank(property)){
                    outputDirflag = false;
                }
                if (StringUtils.equals("author",propertyName)&& !StringUtils.isBlank(property)){
                    authorFlag = false;
                }
                if (StringUtils.equals("open", propertyName) && !StringUtils.isBlank(property)){
                    openFlag = false;
                }
                if (StringUtils.equals("baseRseultMap",propertyName)&& !StringUtils.isBlank(property)){
                    authorFlag = false;
                }
                if (StringUtils.equals("baseColumnList", propertyName) && !StringUtils.isBlank(property)){
                    openFlag = false;
                }
                if (StringUtils.equals(property, "true") || StringUtils.equals(property, "false")){
                    ObjectUtil.setEntity(gc, propertyName, Boolean.valueOf(property));
                    continue;
                }
                ObjectUtil.setEntity(gc, propertyName, property);
            }
        }
        if (outputDirflag){
            gc.setOutputDir(System.getProperty("user.dir") + File.separator + "src" + File.separator +"main" + File.separator + "java");
        }
        if (authorFlag){
            gc.setAuthor("yuchanglong");
        }
        if (openFlag){
            gc.setOpen(false);
        }
        if (baseRseultMap){
            gc.setBaseResultMap(baseRseultMap);
        }
        if (baseColumnList){
            gc.setBaseColumnList(baseColumnList);
        }
        log.info(gc.toString());
        globalConfig = gc;
    }

    /**
     * @Author yuchanglong
     * @Date 2019-8-23
     * @Description 设置数据库连接
     * @Param
     * @return com.baomidou.mybatisplus.generator.config.DataSourceConfig
     **/
    public static void setDataSourceConfig(){
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames){
            if (propertyName.startsWith("dataSource.")){
                String property = properties.getProperty(propertyName);
                propertyName = propertyName.replace("dataSource.", "");
                ObjectUtil.setEntity(dsc, propertyName, property);
            }
        }
        dsc.setDriverName("com.mysql.jdbc.Driver");
        /*dsc.setUrl("jdbc:mysql://192.168.100.229:3306/cpecmcr?useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true");
        dsc.setUsername("root");
        dsc.setPassword("1qaz@WSX");*/
        log.info(dsc.toString());
        dataSourceConfig = dsc;
    }

    public static void setPackageConfig(){
        // 包配置
        PackageConfig pc = new MyPackageConfig();
        Set<String> propertyNames = properties.stringPropertyNames();
        boolean entityFlag = true;
        boolean controllerFlag = true;
        boolean mapperFlag = true;
        boolean parentFlag = true;
        for (String propertyName : propertyNames){
            if (propertyName.startsWith("package.")){
                String property = properties.getProperty(propertyName);
                propertyName = propertyName.replace("package.", "");
                if (StringUtils.equals("parent", propertyName) && !StringUtils.isBlank(property)){
                    parentFlag = false;
                }
                if (StringUtils.equals(propertyName, "entity") && StringUtils.isBlank(property)){
                    entityFlag = false;
                }
                if (StringUtils.equals(propertyName, "controller") && StringUtils.isBlank(property)){
                    controllerFlag = false;
                }
                if (StringUtils.equals(propertyName, "mapper") && StringUtils.isBlank(property)){
                    mapperFlag = false;
                }
                ObjectUtil.setEntity(pc, propertyName, property);
            }
        }
        if (parentFlag){
            log.debug("请设置项目包路径！！");
            throw new RuntimeException("请设置项目包路径！！");
        }
        if (mapperFlag){
            pc.setMapper("repository.dao");
        }
        if (controllerFlag){
            pc.setController("web.rest");
        }
        if (entityFlag){
            pc.setEntity("domain");
        }
        log.info(pc.toString());
        packageConfig = pc;
    }

    public static void setInjectionConfig(){
        // 自定义配置
        InjectionConfig cfg = new MyInjectionConfig();

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        // 创建 entity super class
        String templatePath = "/templates/entityprimary.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.DOMAIN_PACKAGE , MyConstVal.BASE_ENTITY_CLASS));

        // 创建 super mapper class
        templatePath = "/templates/mybasemapper.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.MAPPER_PACKAGE, MyConstVal.BASE_ENTITY_MAPPER_CLASS));

        // 创建 super serivce interface
        templatePath = "/templates/mybaseservice.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.SERVICE_PACKAGE, MyConstVal.BASE_SERVICE_CLASS));

        // 创建 super serivce implements class
        templatePath = "/templates/mybaseserviceImpl.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.SERVICE_IMPL_PACKAGE, MyConstVal.BASE_SERVICE_IMPL_CLASS));

        // 创建 sql mapper.xml
        templatePath = "/templates/mapper.xml.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.PARENT_RESOURCES_PATH,null, MyConstVal.XML_MAPPER, MyConstVal.MAPPER,  FileType.XML) );


        // 创建 dao class
        templatePath = "/templates/myDao.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.DAO_PACKAGE, MyConstVal.MAPPER, FileType.MAPPER));


        // 创建 dto class
        templatePath = "/templates/entityDTO.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.DTO_PACKAGE, MyConstVal.DTO_SUFFIX, FileType.ENTITY));


        // 创建 entity Mapper class
        templatePath = "/templates/mymapper.java.ftl";
        focList.add(new MyFileOutConfig(templatePath, MyConstVal.MAPPER_PACKAGE, MyConstVal.ENTITY_MAPPER_SUFFIX, FileType.ENTITY) );


        cfg.setFileOutConfigList(focList);
        injectionConfig = cfg;
    }

   public static void setTemplateConfig(){
        // 配置模板
        TemplateConfig tc = new MyTemplateConfig();
        String service = properties.getProperty("tc.service");
        if (!StringUtils.isBlank(service)){
           tc.setService(service);
        }else {
            tc.setService("/templates/service.java");
        }
       String serviceImpl = properties.getProperty("tc.serviceImpl");
        if (!StringUtils.isBlank(serviceImpl)){
            tc.setServiceImpl(serviceImpl);
        }else {
            tc.setServiceImpl("/templates/serviceImpl.java");
        }
        String dao = properties.getProperty("tc.dao");
        if (!StringUtils.isBlank(dao)){
            tc.setMapper(dao);
        }else {
            tc.setMapper(null);
        }
        String xml = properties.getProperty("tc.xml");
       if (!StringUtils.isBlank(xml)){
           tc.setXml(xml);
       }else {
           tc.setXml(null);
       }
        templateConfig = tc;
   }

   public static void setStrategyConfig(){
       // 策略配置
       StrategyConfig strategy = new MyStrategyConfig();
       Set<String> propertyNames = properties.stringPropertyNames();
       for (String propertyName : propertyNames){
           if( propertyName.startsWith("sc")){
               String property = properties.getProperty(propertyName);

               if (StringUtils.isBlank(property)){
                   continue;
               }
               if (StringUtils.equals(propertyName, "sc.tablePrefix")){
                   strategy.setTablePrefix(property.split(","));
                   continue;
               }
               if (StringUtils.equals(propertyName, "sc.fieldPrefix")){
                   strategy.setFieldPrefix(property.split(","));
                   continue;
               }
               // 写于父类中的公共字段
               if (StringUtils.equals(propertyName, "sc.superEntityColumns")){
                   strategy.setSuperEntityColumns(property.split(","));
                   continue;
               }
               if (StringUtils.equals(propertyName, "sc.include")){
                   strategy.setInclude(property.split(","));
                   continue;
               }
               if (StringUtils.equals(propertyName, "sc.exclude")){
                   strategy.setExclude(property.split(","));
               }
               if (StringUtils.equals(propertyName, "sc.nameConvert")){
                   propertyName = propertyName.replace("sc.", "");
                   try {
                       Class obj=Class.forName(property);
                       //找到类的构造方法
                       Constructor constructor = obj.getDeclaredConstructor();
                       //使用找到的构造方法创建实例
                       Object instance = constructor.newInstance();
                       ObjectUtil.setEntity(strategy, propertyName, instance);
                   } catch (ClassNotFoundException e) {
                       e.printStackTrace();
                   }catch (NoSuchMethodException e) {
                       e.printStackTrace();
                   }catch (InstantiationException e) {
                       e.printStackTrace();
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   } catch (InvocationTargetException e) {
                       e.printStackTrace();
                   }
               }
               propertyName = propertyName.replace("sc.", "");
               if (StringUtils.equals(property, "true") || StringUtils.equals(property, "false")){
                   ObjectUtil.setEntity(strategy, propertyName, Boolean.valueOf(property));
                   continue;
               }
               if (StringUtils.equals("superDtoClass", propertyName) || StringUtils.equals("superEntityMapperClass",propertyName))continue;
               ObjectUtil.setEntity(strategy, propertyName, property);
           }
       }
       List<String> naming = propertyNames.stream().filter(e -> StringUtils.equals(e, "sc.naming")).collect(Collectors.toList());
       if (naming.isEmpty()){
           strategy.setNaming(NamingStrategy.underline_to_camel);
       }else {
           if (StringUtils.equals(naming.get(0), "underline_to_camel") || StringUtils.equals(naming.get(0), "no_change")){
               strategy.setNaming(NamingStrategy.valueOf(naming.get(0)));
           }else {
               strategy.setNaming(NamingStrategy.underline_to_camel);
           }
       }
       List<String> columnNaming = propertyNames.stream().filter(e -> StringUtils.equals(e, "sc.columnNaming")).collect(Collectors.toList());
       if (columnNaming.isEmpty()){
           strategy.setColumnNaming(NamingStrategy.underline_to_camel);
       }else {
           if (StringUtils.equals(naming.get(0), "underline_to_camel") || StringUtils.equals(naming.get(0), "no_change")){
               strategy.setColumnNaming(NamingStrategy.valueOf(naming.get(0)));
           }else {
               strategy.setColumnNaming(NamingStrategy.underline_to_camel);
           }
       }
       //lombok是有使用
       List<String> EntityLombokModel = propertyNames.stream().filter(e -> StringUtils.equals(e, "sc.entityLombokModel")).collect(Collectors.toList());
       if (EntityLombokModel.isEmpty()){
           strategy.setEntityLombokModel(true);
       }else {
           strategy.setEntityLombokModel(Boolean.getBoolean(EntityLombokModel.get(0)));
       }

       //REST风格是否使用
       List<String> RestControllerStyle = propertyNames.stream().filter(e -> StringUtils.equals(e, "sc.restControllerStyle")).collect(Collectors.toList());
       if (EntityLombokModel.isEmpty()){
           strategy.setRestControllerStyle(true);
       }else {
           strategy.setRestControllerStyle(Boolean.getBoolean(RestControllerStyle.get(0)));
       }
       strategyConfig = strategy;
   }

   public static void init(String generatorConfigFile){
       generatorConfigFile = generatorConfigFile.replaceAll("/", "\\\\");
       InputStream resourceAsStream = null;
       try {
           resourceAsStream = new FileInputStream(new File(generatorConfigFile));
           properties.load(resourceAsStream);
           setGlobalConfig();
           setDataSourceConfig();
           setPackageConfig();
           setTemplateConfig();
           setStrategyConfig();
           setInjectionConfig();
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
