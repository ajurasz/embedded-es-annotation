package com.github.ajurasz.embeddedesannotation.bean;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.nio.charset.Charset;

import static java.lang.String.format;
import static java.util.Arrays.stream;

class ElasticsearchRestClient {

    private final int httpPort;

    private final String[] indices;

    ElasticsearchRestClient(int httpPort, String[] indices) {
        this.httpPort = httpPort;
        this.indices = indices;
    }

    void clearIndices() {
        stream(indices).forEach(this::clearIndex);
    }

    void clearIndex(String indexName) {
        execute(Request
            .Post(url("/" + indexName + "/_delete_by_query"))
            .bodyString("{\"query\" : {\"match_all\" : {}}}", ContentType.APPLICATION_JSON));
    }

    private String url(String path) {
        return "http://localhost:" + httpPort + path;
    }

    private void execute(Request request) {
        try {
            Response response = request.execute();
            if (response.returnResponse().getStatusLine().getStatusCode() != 200) {
                throw new RequestExecutionException(format("Request finished with error: %s",
                    response.returnContent().asString(Charset.forName("UTF-8"))));
            }
        } catch (Exception e) {
            throw new RequestExecutionException(format("Request failed to execute with error: %s",
                e.getMessage()));
        }
    }

    static class RequestExecutionException extends RuntimeException {
        RequestExecutionException(String message) {
            super(message);
        }
    }
}
