package com.simongarton.chess.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {
    private ActualMove move;
    private ActualMove castle;
    private String notes;

    public static Move moveFromSquares(String from, String to) {
        Move move = new Move();
        move.setMove(new ActualMove(from, to));
        return move;
    }

    public static Move moveFromSquares(String from, String to, String castleFrom, String castleTo) {
        Move move = new Move();
        move.setMove(new ActualMove(from, to));
        move.setCastle(new ActualMove(castleFrom, castleTo));
        move.setNotes("castling");
        return move;
    }
}
