package com.yu;

import com.yu.generator.MyAutoGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @Auther: yuchanglong
 * @Date: 2019-8-20
 * @Description: mybatis-plus-generator-plugin
 */
@Mojo( name = "mybatis", defaultPhase = LifecyclePhase.PACKAGE)
public class CodeGeneratorSerivce extends AbstractMojo {
    private static Log log = new SystemStreamLog();

    @Parameter
    private String generatorConfigFile ;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info(generatorConfigFile);
        MyAutoGenerator autoGenerator = new MyAutoGenerator(generatorConfigFile);
        autoGenerator.execute();
    }
}
