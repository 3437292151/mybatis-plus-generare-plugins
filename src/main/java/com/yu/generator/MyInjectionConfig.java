package com.yu.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.yu.generator.config.CodeGeneratorConfig;
import com.yu.generator.config.MyFileCreate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-27
 * @Description:
 */
public class MyInjectionConfig extends InjectionConfig {

    @Override
    public void initMap() {
        super.setFileCreate(new MyFileCreate());

        Map<String, Object> map = this.getMap();
        if (map == null){
            map = new HashMap<>();
        }
        Properties properties = CodeGeneratorConfig.getProperties();
        String superDtoClass = properties.getProperty("sc.superDtoClass");
        String superEntityMapperClass = properties.getProperty("sc.superEntityMapperClass");
        if (!org.apache.commons.lang.StringUtils.isBlank(superDtoClass)){
            map.put("superDtoClassPackage", superDtoClass.substring(0, superDtoClass.lastIndexOf(".")));
            map.put("superDtoClass", superDtoClass.substring(superDtoClass.lastIndexOf(".") + 1, superDtoClass.length()));
        }
        if (org.apache.commons.lang.StringUtils.isBlank(superEntityMapperClass)){
            superEntityMapperClass = CodeGeneratorConfig.getPackageConfig().getParent() + StringPool.DOT + CodeGeneratorConfig.getPackageConfig().getService() +".mapper.EntityMapper";
        }
        map.put("parent",properties.getProperty("package.parent"));
        map.put("superEntityMapperClassPackage", superEntityMapperClass.substring(0, superEntityMapperClass.lastIndexOf(".")));
        map.put("superEntityMapperClass", superEntityMapperClass.substring(superEntityMapperClass.lastIndexOf(".") + 1, superEntityMapperClass.length()));
        this.setMap(map);
    }
}
