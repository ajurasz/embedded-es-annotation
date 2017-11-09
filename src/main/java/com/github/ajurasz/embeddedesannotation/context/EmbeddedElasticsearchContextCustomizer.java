package com.github.ajurasz.embeddedesannotation.context;

import com.github.ajurasz.embeddedesannotation.annotation.EmbeddedElasticsearch;
import com.github.ajurasz.embeddedesannotation.bean.ElasticsearchEmbedded;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.Assert;

public class EmbeddedElasticsearchContextCustomizer implements ContextCustomizer {

    private final EmbeddedElasticsearch embeddedElasticsearch;

    EmbeddedElasticsearchContextCustomizer(EmbeddedElasticsearch embeddedElasticsearch) {
        this.embeddedElasticsearch = embeddedElasticsearch;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void customizeContext(ConfigurableApplicationContext context,
                                 MergedContextConfiguration mergedConfig) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Assert.isInstanceOf(DefaultSingletonBeanRegistry.class, beanFactory);

        ElasticsearchEmbedded elasticsearchEmbedded = new ElasticsearchEmbedded(
            this.embeddedElasticsearch.version(),
            this.embeddedElasticsearch.clusterName(),
            this.embeddedElasticsearch.httpPort(),
            this.embeddedElasticsearch.tcpPort(),
            this.embeddedElasticsearch.indices(),
            this.embeddedElasticsearch.plugins(),
            this.embeddedElasticsearch.startTimeout(),
            this.embeddedElasticsearch.javaOpts()
        );

        beanFactory.initializeBean(elasticsearchEmbedded, ElasticsearchEmbedded.BEAN_NAME);
        beanFactory.registerSingleton(ElasticsearchEmbedded.BEAN_NAME, elasticsearchEmbedded);
        ((DefaultSingletonBeanRegistry) beanFactory)
            .registerDisposableBean(ElasticsearchEmbedded.BEAN_NAME, elasticsearchEmbedded);
    }
}
