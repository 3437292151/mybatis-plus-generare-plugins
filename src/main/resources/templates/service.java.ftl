package ${package.Service};

import ${package.Entity}.${entity};
import ${package.DTO}.${entityDTO};
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
public interface ${table.serviceName} extends ${BaseService}<${entityDTO}, ${entity}> {

}
</#if>
