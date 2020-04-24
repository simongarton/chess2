package com.simongarton.chess;

import com.simongarton.chess.model.Piece;
import com.simongarton.chess.model.Side;
import lombok.Getter;

import static com.simongarton.chess.model.Piece.*;
import static com.simongarton.chess.model.Side.BLACK;
import static com.simongarton.chess.model.Side.WHITE;

@Getter
public class Board {

    private String board;

    public Board() {
        clearBoard();
    }

    public void clearBoard() {
        board = fillString('.', 64);
    }

    private String fillString(char fillChar, int count) {
        char[] chars = new char[count];
        while (count > 0) chars[--count] = fillChar;
        return new String(chars);
    }

    public String showFormattedBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int rank = 8; rank > 0; rank--) {
            stringBuilder.append(rank).append(" ").append(getRank(rank)).append("\n");
        }
        //stringBuilder.append("\n");
        stringBuilder.append("  abcdefgh\n");
        return stringBuilder.toString();
    }

    public String showBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int rank = 8; rank > 0; rank--) {
            stringBuilder.append(getRank(rank)).append("\n");
        }
        return stringBuilder.toString();
    }

    private String getRank(int rank) {
        return board.substring((rank - 1) * 8, (rank * 8));
    }

    public void setupDefaultBoard() {
        board = fillString('.', 64);
        for (int file = 1; file <= 8; file++) {
            addPiece(WHITE, PAWN, fileLetter(file) + "2");
            addPiece(BLACK, PAWN, fileLetter(file) + "7");
        }
        addPiece(WHITE, ROOK, fileLetter(1) + "1");
        addPiece(BLACK, ROOK, fileLetter(1) + "8");
        addPiece(WHITE, KNIGHT, fileLetter(2) + "1");
        addPiece(BLACK, KNIGHT, fileLetter(2) + "8");
        addPiece(WHITE, BISHOP, fileLetter(3) + "1");
        addPiece(BLACK, BISHOP, fileLetter(3) + "8");
        addPiece(WHITE, QUEEN, fileLetter(4) + "1");
        addPiece(BLACK, QUEEN, fileLetter(4) + "8");
        addPiece(WHITE, KING, fileLetter(5) + "1");
        addPiece(BLACK, KING, fileLetter(5) + "8");
        addPiece(WHITE, BISHOP, fileLetter(6) + "1");
        addPiece(BLACK, BISHOP, fileLetter(6) + "8");
        addPiece(WHITE, KNIGHT, fileLetter(7) + "1");
        addPiece(BLACK, KNIGHT, fileLetter(7) + "8");
        addPiece(WHITE, ROOK, fileLetter(8) + "1");
        addPiece(BLACK, ROOK, fileLetter(8) + "8");
    }

    private void addPiece(Side side, Piece piece, String square) {
        int file = fileNumberFromSquare(square);
        int rank = rankNumberFromSquare(square);
        int index = boardIndex(file, rank);
        //System.out.println(square + " " + file + " " + rank + " " + index);
        board = board.substring(0, index) + getPieceSymbol(side, piece) + board.substring(index + 1, 64);
    }

    public String getSymbol(int file, int rank) {
        int index = boardIndex(file, rank);
        String symbol = board.substring(index, index + 1);
        if (Piece.fromSymbol(symbol) == null) {
            return null;
        }
        return symbol;
    }

    public Piece getPiece(int file, int rank) {
        String symbol = getSymbol(file, rank);
        if (symbol == null) {
            return null;
        }
        return Piece.fromSymbol(symbol);
    }

    public Side getSide(int file, int rank) {
        String symbol = getSymbol(file, rank);
        if (symbol == null) {
            return null;
        }
        return symbol.toLowerCase().equals(symbol) ? BLACK : WHITE;
    }

    private String getPieceSymbol(Side side, Piece piece) {
        if (side == WHITE) {
            return piece.getSymbol().toUpperCase();
        }
        return piece.getSymbol().toLowerCase();
    }

    public int boardIndex(int file, int rank) {
        return (file + (rank - 1) * 8) - 1;
    }

    public int rankNumberFromSquare(String square) {
        return Integer.parseInt(square.toLowerCase().substring(1, 2));
    }

    public int fileNumberFromSquare(String square) {
        return square.toLowerCase().charAt(0) - 96;
    }

    public String fileLetter(int file) {
        return Character.toString((char) (96 + file));
    }

    public int getBoardValue(Side side) {
        int value = 0;
        for (int file = 1; file <= 8; file++) {
            for (int rank = 1; rank <= 8; rank++) {
                Piece piece = getPiece(file, rank);
                if (piece == null) {
                    continue;
                }
                Side pieceSide = getSide(file, rank);
                if (side.equals(pieceSide)) {
                    value = value + piece.getValue();
                }
            }
        }
        return value;
    }
}
