package com.caremao.codebreaker.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Letter {
    private char text;
    private int score;
}
