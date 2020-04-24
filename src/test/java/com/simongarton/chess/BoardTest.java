package com.simongarton.chess;

import com.simongarton.chess.model.Piece;
import com.simongarton.chess.model.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.simongarton.chess.model.Piece.QUEEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BoardTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board();
        board.setupDefaultBoard();
    }

    @Test
    void rankNumberFromSquare() {
        assertEquals(4, board.rankNumberFromSquare("a4"));
        assertEquals(5, board.rankNumberFromSquare("b5"));
    }

    @Test
    void fileNumberFromSquare() {
        assertEquals(1, board.fileNumberFromSquare("a4"));
        assertEquals(2, board.fileNumberFromSquare("b5"));
    }

    @Test
    void fileLetter() {
        assertEquals("a", board.fileLetter(1));
        assertEquals("h", board.fileLetter(8));
    }

    @Test
    void boardIndex() {
        assertEquals(0, board.boardIndex(1, 1));
        assertEquals(1, board.boardIndex(2, 1));
        assertEquals(8, board.boardIndex(1, 2));
    }

    @Test
    void getSymbol() {
        assertEquals("P", board.getSymbol(2, 2));
        assertEquals("p", board.getSymbol(2, 7));
        assertNull(board.getSymbol(5, 5));
    }

    @Test
    void getPiece() {
        assertEquals(Piece.PAWN, board.getPiece(2, 2));
        assertEquals(QUEEN, board.getPiece(4, 8));
        assertNull(board.getPiece(5, 5));
    }

    @Test
    void getSide() {
        assertEquals(Side.WHITE, board.getSide(2, 2));
        assertEquals(Side.BLACK, board.getSide(2, 7));
        assertNull(board.getSide(5, 5));
    }

    @Test
    void setBoard() {
    }
}