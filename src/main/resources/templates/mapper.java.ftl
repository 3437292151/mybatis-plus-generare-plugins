package ${package.Mapper};

import ${package.Entity}.${entity};
import ${package.DTO}.${entityDTO};
import ${superDtoEntityMapperClassPackage};

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
interface ${table.mapperName} : ${superDtoEntityMapperClass}<${entity}>
<#else>
public interface ${Mapper} extends ${superDtoEntityMapperClass}<${entityDTO}, ${entity}> {

}
</#if>
