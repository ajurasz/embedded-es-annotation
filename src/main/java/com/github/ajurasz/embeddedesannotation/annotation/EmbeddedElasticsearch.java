package com.github.ajurasz.embeddedesannotation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EmbeddedElasticsearch {

    /**
     * @return elasticsearch version
     */
    String version() default "6.0.1";

    /**
     * @return cluster name
     */
    String clusterName() default "testCluster";

    /**
     * @return list of indices to be created
     */
    String[] indices() default {};

    /**
     * @return list of plugins to be installed
     */
    String[] plugins() default {};

    /**
     * @return start timeout in seconds
     */
    long startTimeout() default 120;

    /**
     * @return http port (when 0 random port will be assigned)
     */
    int httpPort() default 0;

    /**
     * @return tcp port (when 0 random port will be assigned)
     */
    int tcpPort() default 0;

    /**
     * @return java opts used by elasticsearch process
     */
    String javaOpts() default "-Xms128m -Xmx512m";
}
