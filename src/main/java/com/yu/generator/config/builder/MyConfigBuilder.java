package com.yu.generator.config.builder;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.yu.generator.config.*;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.File;
import java.util.*;


/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description:
 */
public class MyConfigBuilder extends ConfigBuilder {

    private static Log log = new SystemStreamLog();

    private List<TableInfo> dtoTableInfoList;

    private String superDtoClass;

    private String superDtoEntityMapper;

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
        handlerStrategy(packageConfig, strategyConfig, globalConfig);
    }

    public List<TableInfo> getDtoTableInfoList() {
        return dtoTableInfoList;
    }

    public void setDtoTableInfoList(List<TableInfo> dtoTableInfoList) {
        this.dtoTableInfoList = dtoTableInfoList;
    }

    public String getSuperDtoClass() {
        return superDtoClass;
    }

    public String getSuperDtoEntityMapper() {
        return superDtoEntityMapper;
    }

    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(PackageConfig packageConfig, StrategyConfig config, GlobalConfig globalConfig) {

        MyStrategyConfig myStrategyConfig = (MyStrategyConfig) config;

        processTypes(myStrategyConfig);

        handlerPackage(packageConfig);
        getTablesInfo(globalConfig);
        getDtoTablesInfo(myStrategyConfig);
    }

    /**
     * @Author yuchanglong
     * @Date 2019-8-30
     * @Description 获取dto 中的tableinfo
     * @Param myStrategyConfig
     * @return void
     **/
    private void getDtoTablesInfo(MyStrategyConfig myStrategyConfig) {
        List<String> superDtoColumns = Arrays.asList(Optional.ofNullable(myStrategyConfig.getSuperDtoColumns()).orElse(new String[]{}));
        if (this.dtoTableInfoList == null){
            this.dtoTableInfoList = new ArrayList<>();
        }
        //初始化dtoTableInfo数组
        this.getTableInfoList().forEach(e -> {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName(e.getName());
            tableInfo.setXmlName(e.getXmlName());
            tableInfo.setEntityName(e.getEntityName() + MyConstVal.DTO);
            tableInfo.setMapperName(e.getEntityName() + MyConstVal.MAPPER);
            tableInfo.setServiceName(e.getServiceName());
            tableInfo.setServiceImplName(e.getServiceImplName());
            tableInfo.setComment(e.getComment());
            tableInfo.setControllerName(e.getControllerName());
            tableInfo.setConvert(e.isConvert());
            tableInfo.setFieldNames(e.getFieldNames());

            List<TableField> fields = new ArrayList<>();
            List<TableField> commonFields = new ArrayList<>();
            e.getFields().forEach(f -> {
                if (superDtoColumns.contains(f.getName())){
                    commonFields.add(f);
                }else {
                    fields.add(f);
                }
            });
            e.getCommonFields().forEach(f -> {
                if (superDtoColumns.contains(f.getName())){
                    commonFields.add(f);
                }else {
                    fields.add(f);
                }
            });
            tableInfo.setFields(fields);
            tableInfo.setCommonFields(commonFields);
            if (!org.apache.commons.lang.StringUtils.isBlank(superDtoClass)){
                tableInfo.getImportPackages().add(superDtoClass);
            }
            this.dtoTableInfoList.add(tableInfo);
        });
    }

    /**
     * @Author yuchanglong
     * @Date 2019-8-30
     * @Description 获取 tableInfo信息
     * @Param globalConfig
     * @return void
     **/
    private void getTablesInfo(GlobalConfig globalConfig) {
        this.getTableInfoList().forEach(e -> {
            if (StringUtils.isNotEmpty(globalConfig.getMapperName())) {
                e.setMapperName(String.format(globalConfig.getMapperName(), e.getEntityName()));
            } else {
                e.setMapperName(e.getEntityName() + MyConstVal.DAO);
            }
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
     * @Author yuchanglong
     * @Date 2019-8-30
     * @Description processTypes
     * @Param myStrategyConfig
     * @return void
     **/
    private void processTypes(MyStrategyConfig myStrategyConfig) {
        this.superDtoClass = myStrategyConfig.getSuperDtoClass();
        this.superDtoEntityMapper = myStrategyConfig.getSuperEntityMapper();
    }

    private void handlerPackage(PackageConfig config) {

        MyPackageConfig packageConfig = (MyPackageConfig) config;

        Map<String, String> packageInfo = this.getPackageInfo();
        // 包信息
        packageInfo.put(MyConstVal.BASE_ENTITY, joinPackage(packageConfig.getParent(), packageConfig.getEntity()));
        packageInfo.put(MyConstVal.BASE_ENTITY_MAPPER, joinPackage(packageConfig.getParent(), packageConfig.getMapper()));
        packageInfo.put(MyConstVal.BASE_SERVICE, joinPackage(packageConfig.getParent(), packageConfig.getService()));
        packageInfo.put(MyConstVal.BASE_SERVICE_IMPL, joinPackage(packageConfig.getParent(), packageConfig.getServiceImpl()));
        packageInfo.put(MyConstVal.MODULE_NAME, packageConfig.getModuleName());
        packageInfo.put(MyConstVal.ENTITY, joinPackage(packageConfig.getParent(), packageConfig.getEntity()));
        packageInfo.put(MyConstVal.DAO, joinPackage(packageConfig.getParent(), packageConfig.getDao()));
        //packageInfo.put(MyConstVal.XML, joinPackage(packageConfig.getParent(), packageConfig.getXml()));
        packageInfo.put(MyConstVal.SERVICE, joinPackage(packageConfig.getParent(), packageConfig.getService()));
        packageInfo.put(MyConstVal.SERVICE_IMPL, joinPackage(packageConfig.getParent(), packageConfig.getServiceImpl()));
        packageInfo.put(MyConstVal.MAPPER, joinPackage(packageConfig.getParent(), packageConfig.getMapper()));
        packageInfo.put(MyConstVal.DTO, joinPackage(packageConfig.getParent(), packageConfig.getDto()));
        packageInfo.put(MyConstVal.CONTROLLER, joinPackage(packageConfig.getParent(), packageConfig.getController()));


        Map<String, String> pathInfo = this.getPathInfo();
        MyTemplateConfig template = (MyTemplateConfig) this.getTemplate();
        String outputDir = this.getGlobalConfig().getOutputDir();
        // 生成路径信息
        setPathInfo(pathInfo, template.getBaseEntiry(), outputDir, MyConstVal.BASE_ENTITY_PATH, MyConstVal.BASE_ENTITY);
        setPathInfo(pathInfo, template.getBaseMapper(), outputDir, MyConstVal.BASE_ENTITY_MAPPER_PATH, MyConstVal.BASE_ENTITY_MAPPER);
        setPathInfo(pathInfo, template.getBaseService(), outputDir, MyConstVal.BASE_SERVICE_PATH, MyConstVal.BASE_SERVICE);
        setPathInfo(pathInfo, template.getBaseServiceImpl(), outputDir, MyConstVal.BASE_SERVICE_IMPL_PATH, MyConstVal.BASE_SERVICE_IMPL);
        setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), outputDir, MyConstVal.ENTITY_PATH, MyConstVal.ENTITY);
        setPathInfo(pathInfo, template.getDao(), outputDir, MyConstVal.DAO_PATH, MyConstVal.DAO);
        setPathInfo(pathInfo, template.getXml(), outputDir, MyConstVal.XML_PATH, MyConstVal.XML);
        setPathInfo(pathInfo, template.getService(), outputDir, MyConstVal.SERVICE_PATH, MyConstVal.SERVICE);
        setPathInfo(pathInfo, template.getServiceImpl(), outputDir, MyConstVal.SERVICE_IMPL_PATH, MyConstVal.SERVICE_IMPL);
        setPathInfo(pathInfo, template.getMapper(), outputDir, MyConstVal.MAPPER_PATH, MyConstVal.MAPPER);
        setPathInfo(pathInfo, template.getDto(), outputDir, MyConstVal.DTO_PATH, MyConstVal.DTO);
        setPathInfo(pathInfo, template.getController(), outputDir, MyConstVal.CONTROLLER_PATH, MyConstVal.CONTROLLER);
        this.getPackageInfo().put("parent", packageConfig.getParent());
    }

    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isEmpty(parent)) {
            return subPackage;
        }
        return parent + StringPool.DOT + subPackage;
    }

    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotEmpty(template)) {
            if (org.apache.commons.lang.StringUtils.equals(module, MyConstVal.XML)){
                pathInfo.put(path, joinPath(outputDir.replaceAll("java", "resources"), module));
            }else {
                pathInfo.put(path, joinPath(outputDir, this.getPackageInfo().get(module)));
            }
        }
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        /*log.info("项目目录: ["+ parentDir + "]" );
        log.info("包名称: [" + packageName + "]");*/
        return parentDir + packageName;
    }
}
