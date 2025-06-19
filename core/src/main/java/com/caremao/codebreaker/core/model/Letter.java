package com.caremao.codebreaker.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Letter {
    @NonNull
    private Character text;
    private long score;

    @Override
    public String toString() {
        return String.format("%s->%d", text, score);
    }
}
