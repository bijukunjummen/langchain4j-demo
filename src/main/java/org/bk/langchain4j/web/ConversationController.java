package org.bk.langchain4j.web;

import jakarta.servlet.http.HttpSession;
import org.bk.langchain4j.model.Message;
import org.bk.langchain4j.model.MessageSender;
import org.bk.langchain4j.service.ConversationService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/add")
    public Mono<Message> addMessage(@RequestParam("message") String message) {
        return Mono.fromCallable(() -> {
            this.conversationService.sendMessage(message);
            return new Message(MessageSender.USER, message);
        });
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Message>> getRunningMessages() {
        return conversationService.stream()
                .map(message -> ServerSentEvent.builder(message).build());
    }

    @GetMapping("/session")
    public Mono<String> getSessionId(HttpSession httpSession) {
        return Mono.just(httpSession.getId());
    }
}
