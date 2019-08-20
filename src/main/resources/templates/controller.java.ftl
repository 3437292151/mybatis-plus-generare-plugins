package ${package.Controller};

import com.baomidou.mybatisplus.core.metadata.IPage;

import ${package.Service}.dto.${entity}DTO;
import ${package.Service}.${table.serviceName};

import com.yu.web.rest.util.HeaderUtil;
import com.yu.web.rest.util.MybatisPaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private static final String ENTITY_NAME = "mcrTDictItem";

    @Autowired
    private ${table.serviceName} targetService;

    /**
    * 获取数据列表
    */
    @GetMapping("/list")
    public ResponseEntity<List<${entity}DTO>> findListByPage(@PageableDefault(page = 0, size = 20, sort = {"updDt"}, direction = Sort.Direction.DESC) Pageable pageable){

        IPage<${entity}DTO> result = targetService.page(pageable);
        HttpHeaders headers = MybatisPaginationUtil.generatePaginationHttpHeaders(result,"/api/mcrTCcApplys/cols/auth");
        return new ResponseEntity<>(result.getRecords(), headers, HttpStatus.OK);
    }


    /**
    * 获取全部数据
    */
    @GetMapping("/all")
    public ResponseEntity<List<${entity}DTO>> findAll(){
        List<${entity}DTO> result = targetService.list();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
    * 根据ID查找数据
    */
    @GetMapping("/find")
    public ResponseEntity<${entity}DTO> find(@RequestParam("id") String id){
        ${entity}DTO result = targetService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
    * 添加数据
    */
    @PostMapping(value = "/add")
    public ResponseEntity<${entity}DTO> addItem(@RequestBody ${entity}DTO ${entity}DTO)throws URISyntaxException {
        ${entity}DTO result = targetService.save(${entity}DTO);
        return ResponseEntity.created(new URI("/api/mstDesignSelects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
    * 更新数据
    */
    @PutMapping(value = "/update")
    public ResponseEntity<${entity}DTO> updateItem(@RequestBody ${entity}DTO ${entity}DTO)throws URISyntaxException {
        ${entity}DTO result = targetService.saveOrUpdate(${entity}DTO);
        return ResponseEntity.created(new URI("/api/mstDesignSelects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
    * 删除数据
    */
    @DeleteMapping("/del")
    public ResponseEntity<Void> deleteItems(@RequestParam("ids") List<Long> ids){
        boolean isOk = targetService.removeByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, ids.toString())).build();
    }
}
</#if>
