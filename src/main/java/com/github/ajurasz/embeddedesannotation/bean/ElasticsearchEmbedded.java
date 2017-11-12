package com.github.ajurasz.embeddedesannotation.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import java.net.UnknownHostException;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;
import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.CLUSTER_NAME;
import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.HTTP_PORT;
import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.TRANSPORT_TCP_PORT;

public class ElasticsearchEmbedded implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchEmbedded.class);

    public static final String BEAN_NAME = "elasticsearchEmbedded";

    public static final String
        SPRING_EMBEDDED_ELASTICSEARCH_HTTP_PORT =
        "spring.embedded.elasticsearch.http.port";

    public static final String
        SPRING_EMBEDDED_ELASTICSEARCH_TCP_PORT =
        "spring.embedded.elasticsearch.tcp.port";

    private final String version;

    private final String clusterName;

    private final int httpPort;

    private final int tcpPort;

    private final String[] indices;

    private final String[] plugins;

    private final long startTimeout;

    private final String javaOpts;

    private EmbeddedElastic embeddedElastic;

    private ElasticsearchRestClient elasticsearchRestClient;

    public ElasticsearchEmbedded(String version, String clusterName, int httpPort, int tcpPort,
                                 String[] indices, String[] plugins, long startTimeout,
                                 String javaOpts) {
        this.version = version;
        this.clusterName = clusterName;
        this.httpPort = httpPort;
        this.tcpPort = tcpPort;
        this.indices = indices;
        this.plugins = plugins;
        this.startTimeout = startTimeout;
        this.javaOpts = javaOpts;
    }

    @Override
    public void destroy() throws Exception {
        try {
            embeddedElastic.stop();
        } catch (Exception ex) {
            logger.warn("failed to stop elasticsearch cleanly \n{}", ex.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int httpPortToUse = this.httpPort == 0 ? findAvailableTcpPort() : this.httpPort;
        int tcpPortToUse = this.tcpPort == 0 ? findAvailableTcpPort() : this.tcpPort;

        this.elasticsearchRestClient = new ElasticsearchRestClient(httpPortToUse, this.indices);

        EmbeddedElastic.Builder embeddedElasticBuilder = EmbeddedElastic.builder()
            .withElasticVersion(this.version)
            .withSetting(CLUSTER_NAME, this.clusterName)
            .withSetting(HTTP_PORT, httpPortToUse)
            .withSetting(TRANSPORT_TCP_PORT, tcpPortToUse)
            .withEsJavaOpts(this.javaOpts)
            .withStartTimeout(this.startTimeout, SECONDS);

        for (String index : this.indices) {
            embeddedElasticBuilder.withIndex(index);
        }

        for (String index : this.plugins) {
            embeddedElasticBuilder.withPlugin(index);
        }

        this.embeddedElastic = embeddedElasticBuilder.build().start();

        System.setProperty(SPRING_EMBEDDED_ELASTICSEARCH_HTTP_PORT, Integer.toString(httpPortToUse));
        System.setProperty(SPRING_EMBEDDED_ELASTICSEARCH_TCP_PORT, Integer.toString(tcpPortToUse));
    }

    public int getHttpPort() {
        return embeddedElastic.getHttpPort();
    }

    public int getTcpPort() {
        return embeddedElastic.getTransportTcpPort();
    }

    public void clearIndices() {
        elasticsearchRestClient.clearIndices();
        embeddedElastic.refreshIndices();
    }

    public void clearIndex(String indexName) {
        elasticsearchRestClient.clearIndex(indexName);
        embeddedElastic.refreshIndices();
    }

    public void recreateIndices() {
        embeddedElastic.recreateIndices();
    }

    public void refreshIndices() {
        embeddedElastic.refreshIndices();
    }

    public void createIndex(String indexName) {
        embeddedElastic.createIndex(indexName);
    }

    public void deleteIndex(String indexName) {
        embeddedElastic.deleteIndex(indexName);
    }

    public List<String> fetchAllDocuments(String... indexNames) throws UnknownHostException {
        return embeddedElastic.fetchAllDocuments(indexNames);
    }

    public void index(String indexName, String indexType, String... json) {
        embeddedElastic.index(indexName, indexType, json);
    }
}
