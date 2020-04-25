package com.simongarton.chess;

import com.simongarton.chess.model.GameResult;
import com.simongarton.chess.model.Move;
import com.simongarton.chess.model.Outcome;
import com.simongarton.chess.model.Side;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.simongarton.chess.model.Side.BLACK;
import static com.simongarton.chess.model.Side.WHITE;

@Getter
@Setter
public class Game {

    private Board board;
    private boolean debug = true;
    private List<Move> whiteMoves = new ArrayList<>();
    private List<Move> blackMoves = new ArrayList<>();

    public Game(Board board) {
        this.board = board;
    }

    public GameResult run() {
        Outcome outcome;
        int moves = 1;
        while (true) {
            outcome = playWhite(moves);
            if (debug) System.out.println(board.showDebugBoard());
            if (outcome != Outcome.PLAY) {
                return buildOutcome(outcome);
            }
            outcome = playBlack(moves);
            if (debug) System.out.println(board.showDebugBoard());
            if (outcome != Outcome.PLAY) {
                return buildOutcome(outcome);
            }
            moves++;
            if (moves > 1000) {
                return buildOutcome(Outcome.STALEMATE_MOVES);
            }
        }
    }

    private GameResult buildOutcome(Outcome outcome) {
        GameResult gameResult = new GameResult();
        gameResult.setOutcome(outcome);
        gameResult.setMoves(whiteMoves.size());
        return gameResult;
    }

    private Outcome playWhite(int move) {
        boolean inCheck = board.inCheck(WHITE);
        List<Move> moves = board.getMoves(WHITE, true, inCheck);
        if (moves.isEmpty()) {
            return Outcome.STALEMATE;
        }
        Move bestMove = moves.get(0);
        whiteMoves.add(bestMove);
        String line = move + " : " + WHITE.getName() + " : " + bestMove.description();
        if (inCheck) {
            line = line + " (in check)";
        }
        debugMoves(moves);
        if (debug) System.out.println(line);

        boolean stillAlive = board.makeMove(bestMove);
        if (!board.hasKing(Side.BLACK)) {
            return Outcome.WHITE_WINS;
        }
        if (board.totalPieceCount() == 2) {
            return Outcome.STALEMATE_KINGS;
        }
        if (repeatingMoves(whiteMoves)) {
            return Outcome.STALEMATE_REPEAT;
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
        blackMoves.add(bestMove);
        String line = move + " : " + Side.BLACK.getName() + " : " + bestMove.description();
        if (inCheck) {
            line = line + " (in check)";
        }
        debugMoves(moves);
        if (debug) System.out.println(line);

        boolean stillAlive = board.makeMove(bestMove);
        if (!board.hasKing(WHITE)) {
            return Outcome.BLACK_WINS;
        }
        if (board.totalPieceCount() == 2) {
            return Outcome.STALEMATE_KINGS;
        }
        if (repeatingMoves(blackMoves)) {
            return Outcome.STALEMATE_REPEAT;
        }
        return Outcome.PLAY;
    }

    private boolean repeatingMoves(List<Move> moves) {
        int n = moves.size();
        if (n < 6) return false;
        if (!moves.get(n-1).description().equalsIgnoreCase(moves.get(n-4).description())) return false;
        if (!moves.get(n-2).description().equalsIgnoreCase(moves.get(n-5).description())) return false;
        if (!moves.get(n-3).description().equalsIgnoreCase(moves.get(n-6).description())) return false;
        return true;
    }

    private void debugMoves(List<Move> moves) {
        if (debug) moves.stream().forEach(m -> System.out.println(" - " + m.description()));
    }
}
