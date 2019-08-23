package com.yu.generator.config.po;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-23
 * @Description:
 */
@Data
@Accessors(chain = true)
public class MyTableInfo extends TableInfo {

    private final Set<String> dtoImportPackages = new HashSet<>();

    private String dtoName;

    private String entityMapperName;

    private List<TableField> dtoFields;

    public String getDtoName() {
        return dtoName;
    }

    public void setDtoName(String dtoName) {
        this.dtoName = dtoName;
    }

    public String getEntityMapperName() {
        return entityMapperName;
    }

    public void setEntityMapperName(String entityMapperName) {
        this.entityMapperName = entityMapperName;
    }

    public List<TableField> getDtoFields() {
        return dtoFields;
    }
    public String getdtoPath() {
        return dtoName.substring(0, 1).toLowerCase() + dtoName.substring(1);
    }
    public void setDtoFields(List<TableField> dtoFields) {
        if (CollectionUtils.isNotEmpty(dtoFields)) {
            this.dtoFields = dtoFields;
            // 收集导入包信息
            for (TableField field : dtoFields) {
                if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                    dtoImportPackages.add(field.getColumnType().getPkg());
                }
            }
        }
    }

    public TableInfo setDtoName(StrategyConfig strategyConfig, String dtoName) {
        this.dtoName = dtoName;
        this.setConvert(strategyConfig);
        return this;
    }
    public TableInfo setDtoImportPackages(String pkg) {
        dtoImportPackages.add(pkg);
        return this;
    }

    /**
     * 逻辑删除
     */
    public boolean isLogicDeleteDto(String logicDeletePropertyName) {
        return dtoFields.parallelStream().anyMatch(tf -> tf.getName().equals(logicDeletePropertyName));
    }
}
