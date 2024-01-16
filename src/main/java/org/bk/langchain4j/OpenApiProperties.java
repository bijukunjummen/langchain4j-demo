package org.bk.langchain4j;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openapi")
public record OpenApiProperties(String apiKey) {
}
