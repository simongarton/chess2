package com.simongarton.chess.model;

import lombok.Getter;

@Getter
public enum Outcome {

    PLAY("Play"),
    WHITE_WINS("White wins"),
    BLACK_WINS("Black wins"),
    STALEMATE_REPEAT("Stalemate repeat moves"),
    STALEMATE_KINGS("Stalemate only Kings"),
    STALEMATE_MOVES("Stalemate too many moves"),
    STALEMATE("Stalemate trapped");

    private String name;

    Outcome(String name) {
        this.name = name;
    }
}
