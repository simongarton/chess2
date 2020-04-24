package com.simongarton.chess.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PieceOnBoard {

    private Piece piece;
    private Side side;
    private String square;

    public String description() {
        return side.getName() + " " + piece.getName();
    }
}
