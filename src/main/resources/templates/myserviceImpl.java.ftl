package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Service}.dto.${entity}DTO;
import ${package.Mapper}.${entity}Dao;
import ${package.Service}.${table.serviceName};
import ${package.Service}.mapper.${entity}Mapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${entity}Dao, ${cfg.superServiceMapperClass}<${entity}DTO, ${entity}>, ${entity}DTO, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${entity}Dao, ${entity}Mapper, ${entity}DTO, ${entity}> implements ${table.serviceName} {

}
</#if>
