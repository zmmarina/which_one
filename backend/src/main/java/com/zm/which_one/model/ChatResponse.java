package com.zm.which_one.model;

import java.util.List;

public record ChatResponse(List<Choice> choices) {
    public record Choice(Message message) {}
    public record Message(String role, String content) {}
}
