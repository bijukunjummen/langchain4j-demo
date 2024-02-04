package org.bk.langchain4j.service;

import dev.langchain4j.chain.Chain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomConversationalChain implements Chain<String, String> {
    private static String MEMORY_KEY = "chat_history";

    private static String QUESTION_KEY = "question";
    private final PromptTemplate promptTemplate;
    private final ChatMemory chatMemory;

    private final ChatLanguageModel chatLanguageModel;

    private final String memoryKey;

    private CustomConversationalChain(Builder builder) {
        promptTemplate = builder.promptTemplate;
        chatMemory = builder.chatMemory;
        chatLanguageModel = builder.chatLanguageModel;
        memoryKey = (builder.memoryKey != null) ? builder.memoryKey : MEMORY_KEY;
    }

    @Override
    public String execute(String message) {
        Map<String, Object> withMemory = createMapWithMemory(chatMemory, message);
        String prompt = this.promptTemplate.apply(withMemory).text();
        System.out.println(prompt);
        String aiResponse = chatLanguageModel.generate(prompt);
        chatMemory.add(UserMessage.userMessage(message));
        chatMemory.add(AiMessage.aiMessage(aiResponse));
        return aiResponse;

    }

    private Map<String, Object> createMapWithMemory(ChatMemory chatMemory, String userMessage) {
        Map<String, Object> mapWithMemory = new HashMap<>();
        mapWithMemory.put(memoryKey, chatMemory.messages()
                .stream()
                .map(chatMessage -> chatMessage.type().name() + ":" + chatMessage.text())
                .collect(Collectors.joining("\n")));
        mapWithMemory.put(QUESTION_KEY, userMessage);
        return mapWithMemory;
    }

    public static final class Builder {
        private PromptTemplate promptTemplate;
        private ChatMemory chatMemory;
        private ChatLanguageModel chatLanguageModel;
        private String memoryKey;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withPromptTemplate(PromptTemplate val) {
            promptTemplate = val;
            return this;
        }

        public Builder withChatMemory(ChatMemory val) {
            chatMemory = val;
            return this;
        }

        public Builder withChatLanguageModel(ChatLanguageModel val) {
            chatLanguageModel = val;
            return this;
        }

        public Builder withMemoryKey(String val) {
            memoryKey = val;
            return this;
        }

        public CustomConversationalChain build() {
            return new CustomConversationalChain(this);
        }
    }
}
