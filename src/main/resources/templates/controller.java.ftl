package com.yu.mybatisplus.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.DTO}.${entityDTO};
import ${package.Service}.${table.serviceName};

import ${package.Controller}.util.HeaderUtil;
import ${package.Controller}.util.MybatisPaginationUtil;
import lombok.extern.slf4j.Slf4j;
import com.codahale.metrics.annotation.Timed;
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
@RequestMapping("/api<#if package.ModuleName??>/${package.ModuleName}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
@Slf4j
public class ${table.controllerName} {
</#if>
    private static final String ENTITY_NAME = "${entity}";

    @Autowired
    private ${table.serviceName} targetService;

    /**
     * 添加[${table.comment!}]数据
     */
    @PostMapping(value = "/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
    @Timed
    public ResponseEntity<${entityDTO}> create${entity}(@RequestBody ${entity}DTO ${entityDTO?uncap_first})throws URISyntaxException {
        log.info("create${entity}() REST request to create ${entity} : {}", ${entityDTO?uncap_first});
        ${entityDTO} result = targetService.save(${entityDTO?uncap_first});
        return ResponseEntity.created(new URI("/api//<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 更新[${table.comment!}]数据
     */
    @PutMapping(value = "/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
    @Timed
    public ResponseEntity<${entityDTO}> update${entity}(@RequestBody ${entityDTO} ${entityDTO?uncap_first})throws URISyntaxException {
        log.info("update${entity}() REST request to create ${entity} : {}", ${entityDTO?uncap_first});
        ${entity}DTO result = targetService.saveOrUpdate(${entityDTO?uncap_first});
        return ResponseEntity.created(new URI("/api//<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
    * 获取[${table.comment!}]数据列表
    */
    @GetMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>s")
    @Timed
    public ResponseEntity<List<${entityDTO}>> get${entity}ByCriteria(${entityDTO} ${entityDTO?uncap_first}, @PageableDefault(page = 0, size = 20, sort = {"updDt"}, direction = Sort.Direction.DESC) Pageable pageable){
        log.info("get${entity}ByCriteria() Rest request to GET a page of ${entity}s param ：{}; pageable: {}", ${entityDTO?uncap_first}, pageable);

        IPage<${entityDTO}> result = targetService.page(pageable, ${entityDTO?uncap_first});
        HttpHeaders headers = MybatisPaginationUtil.generatePaginationHttpHeaders(result,"/api/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>s");
        return new ResponseEntity<>(result.getRecords(), headers, HttpStatus.OK);
    }


    /**
    * 根据ID查找[${table.comment!}]数据
    */
    @GetMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/{id}")
    @Timed
    public ResponseEntity<${entityDTO}> get${entity}ById(@PathVariable String id){
        log.info("get${entity}ById() Rest request to GET ${entity} by id ：{};", id);
        ${entityDTO} result = targetService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
    * 删除[${table.comment!}]数据通过id数组
    */
    @DeleteMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>s/ids")
    @Timed
    public ResponseEntity<Void> delete${entity}ByIds(@RequestBody List<String> ids){
        log.debug("delete${entity}ByIds() REST request to delete ${entity}s : {}", ids);
        boolean isOk = targetService.removeByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, ids.toString())).build();
    }

    /**
     * 删除[${table.comment!}]数据通过id
     */
    @DeleteMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/{id}")
    @Timed
    public ResponseEntity<Void> delete${entity}ById(@PathVariable String id){
        log.debug("delete${entity}ById() REST request to delete ${entity} : {}", id);
        boolean isOk = targetService.removeById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
</#if>