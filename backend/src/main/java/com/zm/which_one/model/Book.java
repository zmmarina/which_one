package com.zm.which_one.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String title;
    private String summary;
    private String storeLink;
    private List<String> tags;
}
