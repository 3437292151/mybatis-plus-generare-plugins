package com.yu.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.yu.generator.util.ObjectUtil;
import freemarker.template.Configuration;
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
        PackageConfig pc = new PackageConfig();
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
            pc.setMapper("dao");
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
        String property = System.getProperty("user.dir");
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                    String superDtoClass = properties.getProperty("sc.superDtoClass");
                    String superEntityMapperClass = properties.getProperty("sc.superEntityMapperClass");
                    if (!org.apache.commons.lang.StringUtils.isBlank(superDtoClass)){
                        map.put("superDtoClassPackage", superDtoClass.substring(0, superDtoClass.lastIndexOf(".")));
                        map.put("superDtoClass", superDtoClass.substring(superDtoClass.lastIndexOf(".") + 1, superDtoClass.length()));
                    }
                    if (org.apache.commons.lang.StringUtils.isBlank(superEntityMapperClass)){
                        superEntityMapperClass = getPackageConfig().getParent() + getPackageConfig().getService() +".mapper.EntityMapper";
                    }
                    map.put("superEntityMapperClassPackage", superEntityMapperClass.substring(0, superEntityMapperClass.lastIndexOf(".")));
                    map.put("superEntityMapperClass", superEntityMapperClass.substring(superEntityMapperClass.lastIndexOf(".") + 1, superEntityMapperClass.length()));
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
                String out = property + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Dao" + StringPool.DOT_XML;
                return out;
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
                return out;
            }
        });


        cfg.setFileOutConfigList(focList);
        injectionConfig = cfg;
    }

   public static void setTemplateConfig(){
        // 配置模板
        TemplateConfig tc = new TemplateConfig();
        String service = properties.getProperty("tc.service");
        if (!StringUtils.isBlank(service)){
           tc.setService(service);
        }else {
            tc.setService("/templates/myservice.java");
        }
       String serviceImpl = properties.getProperty("tc.serviceImpl");
        if (!StringUtils.isBlank(serviceImpl)){
            tc.setServiceImpl(serviceImpl);
        }else {
            tc.setServiceImpl("/templates/myserviceImpl.java");
        }
        String dao = properties.getProperty("tc.dao");
        if (!StringUtils.isBlank(dao)){
            tc.setMapper(dao);
        }
        String xml = properties.getProperty("tc.xml");
       if (!StringUtils.isBlank(xml)){
           tc.setXml(xml);
       }
        templateConfig = tc;
   }

   public static void setStrategyConfig(){
       // 策略配置
       StrategyConfig strategy = new StrategyConfig();
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
