package com.github.ajurasz.embeddedesannotation;

import com.github.ajurasz.embeddedesannotation.annotation.EmbeddedElasticsearch;
import com.github.ajurasz.embeddedesannotation.bean.ElasticsearchEmbedded;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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
        HttpClient client = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet(
                "http://localhost:" + elasticsearchEmbedded.getHttpPort() + "/_cat/indices?v");
        HttpResponse response = client.execute(getRequest);
        String text = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
            .lines().collect(Collectors.joining("\n"));

        assertThat(text).contains("foo", "bar");
    }
}
