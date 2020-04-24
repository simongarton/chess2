package com.simongarton.chess;

import com.simongarton.chess.model.Move;
import com.simongarton.chess.model.Outcome;
import com.simongarton.chess.model.Side;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Game {

    private Board board;

    public Game(Board board) {
        this.board = board;
    }

    public Outcome run() {
        Outcome outcome;
        while (true) {
            outcome = playWhite();
            System.out.println(board.showFormattedBoard());
            if (outcome != Outcome.PLAY) {
                return outcome;
            }
            outcome = playBlack();
            System.out.println(board.showFormattedBoard());
            if (outcome != Outcome.PLAY) {
                return outcome;
            }
        }
    }

    private Outcome playWhite() {
        List<Move> moves = board.getMoves(Side.WHITE);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        Move bestMove = moves.get(0);
        board.makeMove(bestMove);
        if (!board.hasKing(Side.BLACK)) {
            return Outcome.WHITE_WINS;
        }
        return Outcome.PLAY;
    }

    private Outcome playBlack() {
        List<Move> moves = board.getMoves(Side.BLACK);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        Move bestMove = moves.get(0);
        board.makeMove(bestMove);
        if (!board.hasKing(Side.WHITE)) {
            return Outcome.BLACK_WINS;
        }
        return Outcome.PLAY;
    }
}
