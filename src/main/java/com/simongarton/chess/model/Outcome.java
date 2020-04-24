package com.simongarton.chess.model;

import lombok.Getter;

@Getter
public enum Outcome {

    PLAY("Play"),
    WHITE_WINS("White wins"),
    BLACK_WINS("Black wins"),
    STALEMATE("Stalemate");

    private String name;

    Outcome(String name) {
        this.name = name;
    }
}
