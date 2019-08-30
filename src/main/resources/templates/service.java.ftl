package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Service}.dto.${entity}DTO;
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends BaseService<${entity}DTO, ${entity}> {

}
</#if>
