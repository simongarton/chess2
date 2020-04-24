package com.simongarton.chess;

public class Main {

    public static void main(String[] argv) {
        Board board = new Board();
        board.setupDefaultBoard();
        System.out.println("");
        System.out.println(board.showFormattedBoard());

        Game game = new Game(board);
        game.run();

    }
}
