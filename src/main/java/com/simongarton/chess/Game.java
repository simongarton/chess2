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
        int move = 1;
        while (true) {
            outcome = playWhite(move);
            System.out.println(board.showFormattedBoard());
            if (outcome != Outcome.PLAY) {
                return outcome;
            }
            outcome = playBlack(move);
            System.out.println(board.showFormattedBoard());
            if (outcome != Outcome.PLAY) {
                return outcome;
            }
            move++;
        }
    }

    private Outcome playWhite(int move) {
        List<Move> moves = board.getMoves(Side.WHITE, true);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        List<Move> otherMoves = board.getMoves(Side.BLACK, false);
        boolean inCheck = otherMoves.stream().filter(m -> m.getNotes() !=null).anyMatch(m -> m.getNotes().toLowerCase().contains("king"));
        Move bestMove = moves.get(0);
        String line = move + " : " + Side.WHITE.getName() + " : " + bestMove.description();
        if (inCheck) {
            line = line + " (in check)";
        }
        System.out.println(line);
        for (Move possibleMove: moves) {
            System.out.println("  " + possibleMove.description());
        }

        board.makeMove(bestMove);
        if (!board.hasKing(Side.BLACK)) {
            return Outcome.WHITE_WINS;
        }
        return Outcome.PLAY;
    }

    private Outcome playBlack(int move) {
        List<Move> moves = board.getMoves(Side.BLACK, true);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        List<Move> otherMoves = board.getMoves(Side.WHITE, false);
        boolean inCheck = otherMoves.stream().filter(m -> m.getNotes() !=null).anyMatch(m -> m.getNotes().toLowerCase().contains("king"));
        Move bestMove = moves.get(0);
        String line = move + " : " + Side.BLACK.getName() + " : " + bestMove.description();
        if (inCheck) {
            line = line + " (in check)";
        }

        System.out.println(line);
        for (Move possibleMove: moves) {
            System.out.println("  " + possibleMove.description());
        }
        board.makeMove(bestMove);
        if (!board.hasKing(Side.WHITE)) {
            return Outcome.BLACK_WINS;
        }
        return Outcome.PLAY;
    }
}
