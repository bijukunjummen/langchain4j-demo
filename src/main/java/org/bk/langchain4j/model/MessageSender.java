package org.bk.langchain4j.model;

public enum MessageSender {
    SYSTEM("System"), USER("User");

    private String literal;

    MessageSender(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
