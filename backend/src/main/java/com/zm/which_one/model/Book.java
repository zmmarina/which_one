package com.zm.which_one.model;

import java.util.List;

public record Book(
        String title,
        String summary,
        String storeLink,
        List<String> tags
) {}