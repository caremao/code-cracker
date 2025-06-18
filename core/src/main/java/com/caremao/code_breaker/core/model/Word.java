package com.caremao.code_breaker.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Word {

    private String text;
    private int score;
    private Letter letter;

}
