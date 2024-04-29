package org.bk.langchain4j;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public record OpenAiApiProperties(String apiKey) {
}
