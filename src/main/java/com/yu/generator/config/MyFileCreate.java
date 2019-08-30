package com.yu.generator.config;

import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.FileType;

import java.io.File;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-27
 * @Description:
 */
public class MyFileCreate implements IFileCreate {
    @Override
    public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
        }
        return !exist || configBuilder.getGlobalConfig().isFileOverride();
    }
}
