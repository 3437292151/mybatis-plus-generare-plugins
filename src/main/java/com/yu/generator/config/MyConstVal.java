package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.ConstVal;

import java.io.File;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-19
 * @Description: 自定义定义常量
 */
public interface MyConstVal extends ConstVal {
    String CONTROLLER = "Resource";

    String DAO = "Dao";

    String DTO = "DTO";

    String XML = "mapper";

    String TEMPLATE_MAPPER = "/templates/mapper.java";

    String TEMPLATE_DAO = "/templates/dao.java";

    String TEMPLATE_XML = "/templates/dao.xml";

    String TEMPLATE_DTO = "/templates/entityDTO.java";

    String TEMPLATE_BASE_ENTITY = "/templates/baseentity.java";

    String TEMPLATE_BASE_MAPPER = "/templates/basemapper.java";

    String TEMPLATE_BASE_SERIVCE = "/templates/baseservice.java";

    String TEMPLATE_BASE_SERIVCE_IMPL = "/templates/baseserviceimpl.java";

    String MAVEN_JAVA_PATH = "src" + File.separator + "main" + File.separator + "java";

    String MAVEN_RESOURCES_PATH = "src" + File.separator + "main" + File.separator + "resources";

    String PARENT_JAVA_PATH = System.getProperty("user.dir") + File.separator + MyConstVal.MAVEN_JAVA_PATH;

    String PARENT_RESOURCES_PATH = System.getProperty("user.dir") + File.separator + MyConstVal.MAVEN_RESOURCES_PATH;

    String BASE_ENTITY_CLASS = "PrimaryKey.java";

    String BASE_ENTITY_PATH = "base_entity_path";

    String BASE_ENTITY = "PrimaryKey";

    String DOMAIN_PACKAGE = "domain";

    String MAPPER_PACKAGE = "service.mapper";

    String SERVICE_PACKAGE = "service";

    String BASE_SERVICE_CLASS = "BaseService.java";

    String SERVICE_IMPL_PACKAGE = "service.impl";

    String BASE_SERVICE_IMPL_CLASS = "BaseServiceImpl.java";

    String DAO_PACKAGE = "repository.dao";

    String DAO_PATH = "dao_path";

    String BASE_ENTITY_MAPPER_CLASS = "EntityMapper.java";

    String BASE_ENTITY_MAPPER_PATH = "entity_maper_path";

    String BASE_ENTITY_MAPPER = "EntityMapper";

    String XML_MAPPER = "mapper";

    String DTO_PACKAGE = "service.dto";

    String DTO_PATH = "dto_path";

    String DTO_SUFFIX = "DTO";

    String ENTITY_MAPPER_SUFFIX = "Mapper";

    String BASE_SERVICE_PATH = "base_serivce_path";

    String BASE_SERVICE = "BaseSerivce";

    String BASE_SERVICE_IMPL_PATH = "base_serivce_impl_path";

    String BASE_SERVICE_IMPL = "BaseSerivceImpl";
}
