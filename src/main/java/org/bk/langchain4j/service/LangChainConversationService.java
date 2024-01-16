package org.bk.langchain4j.service;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import org.bk.langchain4j.OpenApiProperties;
import org.bk.langchain4j.model.Message;
import org.bk.langchain4j.model.MessageSender;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@SessionScope
public class LangChainConversationService implements ConversationService {
    private final ConversationalChain chain;

    private Sinks.Many<Message> sink = Sinks.many().multicast().onBackpressureBuffer();

    public LangChainConversationService(OpenApiProperties openApiProperties) {
        System.out.println("Properties " + openApiProperties);
        final ChatLanguageModel openAiChatModel = OpenAiChatModel.withApiKey(openApiProperties.apiKey());
        this.chain = ConversationalChain.builder()
                .chatLanguageModel(openAiChatModel)
//                 .chatMemory()
                .build();
    }

    @Override
    public void sendMessage(String message) {
        String systemMessage = this.chain.execute(message);
        this.sink.tryEmitNext(new Message(MessageSender.USER, message));
        this.sink.tryEmitNext(new Message(MessageSender.SYSTEM, systemMessage));
    }

    @Override
    public Flux<Message> stream() {
        return sink.asFlux();
    }
}
