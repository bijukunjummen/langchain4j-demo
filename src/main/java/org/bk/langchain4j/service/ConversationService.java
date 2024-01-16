package org.bk.langchain4j.service;

import org.bk.langchain4j.model.Message;
import reactor.core.publisher.Flux;

public interface ConversationService {

    void sendMessage(String message);

    Flux<Message> stream();
}
