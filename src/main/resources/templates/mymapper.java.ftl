package ${package.Service}.mapper;

import ${package.Entity}.${entity};

import ${package.Service}.dto.${entity}DTO;

import org.mapstruct.Mapper;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper(componentModel = "spring", uses = {})
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${cfg.superServiceMapperClass}<${entity}DTO, ${entity}> {

}
</#if>
