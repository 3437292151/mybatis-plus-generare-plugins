package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.PackageConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-30
 * @Description:
 */
@Data
@Accessors(chain = true)
public class MyPackageConfig extends PackageConfig {

    private String entity = MyConstVal.DOMAIN_PACKAGE;

    /**
     * Mapper包名
     */
    private String mapper = MyConstVal.MAPPER_PACKAGE;

    /**
     * Controller包名
     */
    private String controller = MyConstVal.CONTROLLER_PACKAGE;

    /**
     * dto包名
     **/
    private String dto = MyConstVal.DTO_PACKAGE;

    /**
     * 持久层包名
     **/
    private String dao = MyConstVal.DAO_PACKAGE;

    /**
     * xml 文件地址
     **/
    private String xml = MyConstVal.XML_PACKAGE;

}
