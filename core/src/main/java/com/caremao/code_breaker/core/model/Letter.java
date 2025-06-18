package com.caremao.code_breaker.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Letter {
    private char text;
    private int score;
}
