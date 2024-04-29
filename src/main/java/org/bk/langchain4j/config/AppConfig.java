package org.bk.langchain4j.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bk.langchain4j.OllamaProperties;
import org.bk.langchain4j.OpenAiApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    @Profile("openai")
    public ChatLanguageModel openAiChatLanguageModel(OpenAiApiProperties openAiProperties) {
        final ChatLanguageModel openAiChatLanguageModel = OpenAiChatModel
                .withApiKey(openAiProperties.apiKey());
        return openAiChatLanguageModel;
    }

    @Bean
    @Profile("ollama")
    public ChatLanguageModel ollamaChatLanguageModel(OllamaProperties ollamaProperties) {
        final ChatLanguageModel ollamaChatLanguageModel =
                OllamaChatModel.builder().modelName(ollamaProperties.modelName()).baseUrl(ollamaProperties.baseUrl()).build();
        return ollamaChatLanguageModel;
    }
}
