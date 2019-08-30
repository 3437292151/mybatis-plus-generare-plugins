package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.Data;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-30
 * @Description:
 */
@Data
public class MyTemplateConfig extends TemplateConfig {

    private String mapper = MyConstVal.TEMPLATE_MAPPER;

    private String xml = MyConstVal.TEMPLATE_XML;

    private String dao = MyConstVal.TEMPLATE_DAO;

    private String dto = MyConstVal.TEMPLATE_DTO;

    private String baseMapper = MyConstVal.TEMPLATE_BASE_MAPPER;

    private String baseService = MyConstVal.TEMPLATE_BASE_SERIVCE;

    private String baseServiceImpl = MyConstVal.TEMPLATE_BASE_SERIVCE_IMPL;

    private String baseEntiry = MyConstVal.TEMPLATE_BASE_ENTITY;

}
