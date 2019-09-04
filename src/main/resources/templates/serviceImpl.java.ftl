package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.DTO}.${entityDTO};
import ${package.Dao}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${package.Mapper}.${Mapper};
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
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${Mapper}, ${entityDTO}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends BaseServiceImpl<${table.mapperName}, ${Mapper}, ${entityDTO}, ${entity}> implements ${table.serviceName} {

}
</#if>
