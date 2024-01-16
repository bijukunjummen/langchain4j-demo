package org.bk.langchain4j.model;

public record Message(MessageSender from, String payload) {
}
