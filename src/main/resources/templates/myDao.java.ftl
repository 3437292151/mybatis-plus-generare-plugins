package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper
<#if kotlin>
interface ${entity}Dao : ${superMapperClass}<${entity}>
<#else>
public interface ${entity}Dao extends ${superMapperClass}<${entity}> {

}
</#if>
