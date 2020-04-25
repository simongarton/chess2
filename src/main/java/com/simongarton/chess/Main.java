package com.simongarton.chess;

import com.simongarton.chess.model.GameResult;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] argv) {
        if (argv.length == 0) {
            runOneGame(true);
            return;
        }
        int iterations = Integer.parseInt(argv[0]);
        Map<String, Integer> outcomes = new HashMap<>();
        for (int i = 0; i < iterations; i++) {
            GameResult gameResult = runOneGame(false);
            outcomes.put(gameResult.getOutcome().getName(),outcomes.getOrDefault(gameResult.getOutcome().getName(), 0) + 1);
            System.out.println(i + " " + gameResult.getOutcome().getName() + " in " + gameResult.getMoves() + " moves.");
        }
        System.out.println("");
        for (Map.Entry<String, Integer> entry : outcomes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

    }

    private static GameResult runOneGame(boolean display) {
        Board board = new Board();
        board.setupDefaultBoard();
        if (display) {
            System.out.println("");
            System.out.println(board.showFormattedBoard());
        }

        Game game = new Game(board);
        game.setDebug(display);
        GameResult gameResult = game.run();
        if (display) System.out.println(gameResult.getOutcome().getName() + " in " + gameResult.getMoves() +" moves.");
        return gameResult;
    }
}
