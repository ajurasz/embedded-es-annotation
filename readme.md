# Embedded ES annotation

[![Build](https://api.travis-ci.org/ajurasz/embedded-es-annotation.svg)](https://travis-ci.org/ajurasz/embedded-es-annotation)

**Embedded ES annotation** provides annotation that simplify running embedded `Elasticsearch` (ES) instance in `Spring`s test context. Furthermore embedded
ES instance is part of a `Spring` context lifecycle so it can speed up tests execution as only one instance is started across all tests.

This annotation was build on top of a great project called [embedded-elasticsearch](https://github.com/allegro/embedded-elasticsearch) by [Allegro Tech](https://github.com/allegro)

## Maven setup

```
<dependencies>
  <dependency>
    <groupId>com.github.ajurasz</groupId>
    <artifactId>embedded-es-annotation</artifactId>
    <version>0.0</version>
  </dependency>
<dependencies>
```

## Usage

You can annotate you base integration test class with `@EmbeddedElasticsearch`


```
@RunWith(SpringRunner.class)
@EmbeddedElasticsearch()
public class EmbeddedElasticsearchTest {

}
```

if not specified random port will be assigned for `http` and `tcp` and accessible through
following environment variables: `spring.embedded.elasticsearch.http.port` and `spring.embedded.elasticsearch.tcp.port`.
You can use these variables in your `application-test.properties` like:

```
es.endpoint=http://localhost:${spring.embedded.elasticsearch.http.port}
```

To have direct interaction with embedded `Elasticsearch` server you can inject `ElasticsearchEmbedded` bean to your test classes:

```
@Autowired
ElasticsearchEmbedded elasticsearchEmbedded;
```