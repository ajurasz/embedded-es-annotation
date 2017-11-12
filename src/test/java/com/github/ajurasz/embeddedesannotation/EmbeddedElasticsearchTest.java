package com.github.ajurasz.embeddedesannotation;

import com.github.ajurasz.embeddedesannotation.annotation.EmbeddedElasticsearch;
import com.github.ajurasz.embeddedesannotation.bean.ElasticsearchEmbedded;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import static com.github.ajurasz.embeddedesannotation.bean.ElasticsearchEmbedded.SPRING_EMBEDDED_ELASTICSEARCH_HTTP_PORT;
import static com.github.ajurasz.embeddedesannotation.bean.ElasticsearchEmbedded.SPRING_EMBEDDED_ELASTICSEARCH_TCP_PORT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@EmbeddedElasticsearch(
    indices = { "foo", "bar" }
)
public class EmbeddedElasticsearchTest {

    @Autowired
    ElasticsearchEmbedded elasticsearchEmbedded;

    @Test
    public void should_inject_elasticsearch_embedded() {
        assertThat(elasticsearchEmbedded).isNotNull();
    }

    @Test
    public void should_register_environment_variable_with_http_port_number() {
        int httpPort = elasticsearchEmbedded.getHttpPort();
        assertThat(System.getProperty(SPRING_EMBEDDED_ELASTICSEARCH_HTTP_PORT)).contains("" + httpPort);
    }

    @Test
    public void should_use_random_http_port() {
        assertThat(elasticsearchEmbedded.getHttpPort()).isNotZero();
    }

    @Test
    public void should_register_environment_variable_with_tcp_port_number() {
        int tcpPort = elasticsearchEmbedded.getTcpPort();
        assertThat(System.getProperty(SPRING_EMBEDDED_ELASTICSEARCH_TCP_PORT)).contains("" + tcpPort);
    }

    @Test
    public void should_use_random_tcp_port() {
        assertThat(elasticsearchEmbedded.getTcpPort()).isNotZero();
    }

    @Test
    public void should_list_all_indices() throws IOException {
        Response response = Request
            .Get("http://localhost:" + elasticsearchEmbedded.getHttpPort() + "/_cat/indices?v")
            .execute();

        assertThat(response.returnContent().asString(Charset.forName("UTF-8"))).contains("foo", "bar");
    }

    @Test
    public void should_delete_all_existing_documents() throws UnknownHostException {
        elasticsearchEmbedded.index("foo", "foo_type", "{\"value\": \"test\"}");
        elasticsearchEmbedded.index("foo", "foo_type_2", "{\"value\": \"test\"}");
        elasticsearchEmbedded.index("bar", "bar_type", "{\"value\": \"test\"}");
        elasticsearchEmbedded.index("bar", "bar_type_2", "{\"value\": \"test\"}");

        assertThat(elasticsearchEmbedded.fetchAllDocuments("foo", "bar")).hasSize(4);

        elasticsearchEmbedded.clearIndex("foo");
        assertThat(elasticsearchEmbedded.fetchAllDocuments("foo", "bar")).hasSize(2);

        elasticsearchEmbedded.clearIndices();
        assertThat(elasticsearchEmbedded.fetchAllDocuments("foo", "bar")).hasSize(0);
    }
}
