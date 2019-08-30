package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.PackageConfig;
import lombok.Data;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-30
 * @Description:
 */
@Data
public class MyPackageConfig extends PackageConfig {

    private String entity = "domain";

    /**
     * Mapper包名
     */
    private String mapper = "serivce.mapper";

    /**
     * Controller包名
     */
    private String controller = "web.rest";

    /**
     * dto包名
     **/
    private String dto = "service.dto";

    /**
     * 持久层包名
     **/
    private String dao = "repository";

    /**
     * xml 文件地址
     **/
    private String xml = "mapper";

}
