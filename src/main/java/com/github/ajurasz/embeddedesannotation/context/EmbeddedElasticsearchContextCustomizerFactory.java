package com.github.ajurasz.embeddedesannotation.context;

import com.github.ajurasz.embeddedesannotation.annotation.EmbeddedElasticsearch;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class EmbeddedElasticsearchContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> configAttributes) {
        EmbeddedElasticsearch embeddedElasticsearch =
            AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedElasticsearch.class);
        return embeddedElasticsearch != null ? new EmbeddedElasticsearchContextCustomizer(
            embeddedElasticsearch) : null;
    }

}
