# Embedded ES annotation

[![Build](https://api.travis-ci.org/ajurasz/embedded-es-annotation.svg)](https://travis-ci.org/ajurasz/embedded-es-annotation)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ajurasz/embedded-es-annotation/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.ajurasz/embedded-es-annotation)

**Embedded ES annotation** provides annotation that simplify running embedded `Elasticsearch` (ES) instance in `Spring`s test context by making ES instance part of Spring context lifecycle.

This annotation was build on top of a great project called [embedded-elasticsearch](https://github.com/allegro/embedded-elasticsearch) by [Allegro Tech](https://github.com/allegro). Because this annotation
do not expose all configuration possibilities if you need more control over created ES instance then please use [embedded-elasticsearch](https://github.com/allegro/embedded-elasticsearch) instead.

## Maven setup

```
<dependencies>
  <dependency>
    <groupId>com.github.ajurasz</groupId>
    <artifactId>embedded-es-annotation</artifactId>
    <version>0.2</version>
    <scope>test</test>
  </dependency>
</dependencies>
```

or is case you need latest code use `jitpack`

```
<project>
    <dependencies>
		<dependency>
			<groupId>com.github.ajurasz</groupId>
			<artifactId>embedded-es-annotation</artifactId>
			<version>746f8f12a01ae5e35b91a498fe9dafe241a5d213</version>
			<scope>test</scope>
		</dependency>
    </dependencies>

	<repositories>
		<repository>
			<id>jitpack.io</id>
			<url>https://jitpack.io</url>
		</repository>
	</repositories>
</project>
```


## Usage

You can annotate your base integration test class with `@EmbeddedElasticsearch`


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