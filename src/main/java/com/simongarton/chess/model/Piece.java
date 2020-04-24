package com.simongarton.chess.model;

import lombok.Getter;

@Getter
public enum Piece {

    KING("K","King",10),
    QUEEN("Q","Queen",9),
    BISHOP("B","Bishop",3),
    KNIGHT("N","Knight",3),
    ROOK("R","Rook",5),
    PAWN("P","Pawn",1);

    private String symbol;
    private String name;
    private int value;

    Piece(String symbol, String name, int value) {
        this.symbol = symbol;
        this.name = name;
        this.value = value;
    }

    public static Piece fromSymbol(String symbol) {
        for (Piece piece : values()) {
            if (piece.getSymbol().equalsIgnoreCase(symbol)) {
                return piece;
            }
        }
        return null;
    }
}
