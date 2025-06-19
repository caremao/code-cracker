package com.caremao.codebreaker.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Word {

    private String text;
    private int score;
    private List<Letter> letters;
    private int size;

}
