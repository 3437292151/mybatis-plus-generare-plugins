package com.yu.generator.util;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.lang.reflect.Field;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-21
 * @Description: 对象处理工具
 */
public class ObjectUtil {
    private static Log log = new SystemStreamLog();

    /**
     * @Author yuchanglong
     * @Date 2019-8-21
     * @Description 设置
     * @Param entity
     * @Param fieldName
     * @Param value
     * @return void
     **/
    public static void setEntity(Object entity, String fieldName, Object value){
        Field f = null;
        try {
            f = entity.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                f = entity.getClass().getSuperclass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                log.debug("NoSuchFieldException", e1);
            }
        }
        f.setAccessible(true);
        try {
            f.set(entity, value);
        } catch (IllegalAccessException e) {
            log.debug("IllegalAccessException", e);
        }
    }
}
