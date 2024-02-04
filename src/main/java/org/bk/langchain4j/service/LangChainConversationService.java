package org.bk.langchain4j.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.bk.langchain4j.OpenApiProperties;
import org.bk.langchain4j.model.Message;
import org.bk.langchain4j.model.MessageSender;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@SessionScope
public class LangChainConversationService implements ConversationService {
    private final CustomConversationChain chain;

    private Sinks.Many<Message> sink = Sinks.many().multicast().onBackpressureBuffer();

    public LangChainConversationService(OpenApiProperties openApiProperties) {
        final ChatMemoryStore memoryStore = new InMemoryChatMemoryStore();
        final ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryStore(memoryStore).maxMessages(20).build();
        final ChatLanguageModel openAiChatModel = OpenAiChatModel.withApiKey(openApiProperties.apiKey());
        final PromptTemplate promptTemplate = PromptTemplate.from("""
                Answer the question based on the context below and use the history of the conversation to continue
                If the question cannot be answered using the information provided answer with "I don't know"
                              
                Context:
                You are deeply knowledgeable about all the National Parks in the United states and you want to 
                suggest the parks to visit based on the question.
                              
                History of the conversation:
                {{history}}
                
                Question: 
                {{question}}

                Answer: 
                """);
        this.chain = CustomConversationChain.Builder.newBuilder().withChatLanguageModel(openAiChatModel)
                .withMemoryKey("history")
                .withChatMemory(chatMemory)
                .withPromptTemplate(promptTemplate)
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
