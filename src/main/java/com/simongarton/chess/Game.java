package com.simongarton.chess;

import com.simongarton.chess.model.Move;
import com.simongarton.chess.model.Outcome;
import com.simongarton.chess.model.Side;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.simongarton.chess.model.Side.BLACK;
import static com.simongarton.chess.model.Side.WHITE;

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
        boolean inCheck = board.inCheck(WHITE);
        List<Move> moves = board.getMoves(WHITE, true, inCheck);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        Move bestMove = moves.get(0);
        String line = move + " : " + WHITE.getName() + " : " + bestMove.description();
        if (inCheck) {
            line = line + " (in check)";
        }
        System.out.println(line);
        
        boolean stillAlive = board.makeMove(bestMove);
        if (!board.hasKing(Side.BLACK)) {
            return Outcome.WHITE_WINS;
        }
        return Outcome.PLAY;
    }

    private Outcome playBlack(int move) {
        boolean inCheck = board.inCheck(BLACK);
        List<Move> moves = board.getMoves(Side.BLACK, true, inCheck);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        Move bestMove = moves.get(0);
        String line = move + " : " + Side.BLACK.getName() + " : " + bestMove.description();
        if (inCheck) {
            line = line + " (in check)";
        }
        System.out.println(line);

        boolean stillAlive = board.makeMove(bestMove);
        if (!board.hasKing(WHITE)) {
            return Outcome.BLACK_WINS;
        }
        return Outcome.PLAY;
    }
}
