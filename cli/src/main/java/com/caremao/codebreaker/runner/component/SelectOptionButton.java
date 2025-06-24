package com.caremao.codebreaker.runner.component;

import com.caremao.codebreaker.core.model.Word;
import com.googlecode.lanterna.gui2.Button;

public class SelectOptionButton extends Button {

    private final Word word;
    private final int matches;

    public SelectOptionButton(String label, Word word, int matches, boolean enabled) {
        super(label);
        this.word = word;
        this.matches = matches;
        this.setEnabled(enabled);
    }
}
