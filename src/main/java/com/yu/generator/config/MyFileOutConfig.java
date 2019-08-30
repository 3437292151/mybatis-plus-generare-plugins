package com.yu.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.File;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-27
 * @Description: 文件输出配置
 */
@Data
public class MyFileOutConfig extends FileOutConfig {

    private static Log log = new SystemStreamLog();

    private String projectOutPath = MyConstVal.PARENT_JAVA_PATH ;//项目输出路径

    private String projectPackagePath = CodeGeneratorConfig.getPackageConfig().getParent();//项目路径

    private String packagePath ;//包路径

    private String fileName;//文件名称

    private String fileNameSuffix;//文件后缀名称

    private FileType fileType; //文件类型

    public MyFileOutConfig(String templatePath, String projectOutPath, String projectPackagePath, String packagePath, String fileNameSuffix, FileType fileType) {
        super(templatePath);
        this.projectOutPath = projectOutPath;
        this.projectPackagePath = projectPackagePath;
        this.packagePath = packagePath;
        this.fileNameSuffix = fileNameSuffix;
        this.fileType = fileType;
    }

    public MyFileOutConfig(String templatePath, String packagePath, String fileNameSuffix, FileType fileType) {
        super(templatePath);
        this.packagePath = packagePath ;
        this.fileNameSuffix = fileNameSuffix;
        this.fileType = fileType;
    }

    public MyFileOutConfig(String templatePath, String packagePath, String fileName) {
        super(templatePath);
        this.packagePath = packagePath ;
        this.fileName = fileName;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {

        if (!StringUtils.isBlank(projectOutPath)){
            if (!projectOutPath.endsWith(File.separator)){
                projectOutPath = projectOutPath + File.separator;
            }
        }else {
            projectOutPath = "";
        }

        if (!StringUtils.isBlank(projectPackagePath)){
            if (!projectPackagePath.endsWith(File.separator)){
                projectPackagePath = projectPackagePath.replace(StringPool.DOT, File.separator) + File.separator;
            }
        }else {
            projectPackagePath = "";
        }

        if (!StringUtils.isBlank(packagePath)){
            if (!packagePath.endsWith(File.separator)){
                packagePath = packagePath.replace(StringPool.DOT, File.separator) + File.separator;
            }
        }else {
            packagePath = "";
        }

        if (StringUtils.isBlank(fileName)){
            StringBuffer fileNameBuffer = new StringBuffer(tableInfo.getEntityName());
            fileName = fileNameBuffer.toString();
        }

        if (StringUtils.isBlank(fileNameSuffix)){
            fileNameSuffix = "";
        }

        String fileTypeName;
        if (FileType.XML.equals(fileType)){
            fileTypeName = MyConstVal.XML_SUFFIX;
        }else if(fileType == null) {
            fileTypeName = "";
        }else {
            fileTypeName = StringPool.DOT_JAVA;
        }

        String out = projectOutPath + projectPackagePath + packagePath + fileName + fileNameSuffix + fileTypeName;
        log.info("模板: " + super.getTemplatePath() + "  文件: " + out);
        return out;
    }
}
