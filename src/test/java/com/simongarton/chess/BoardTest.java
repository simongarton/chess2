package com.simongarton.chess;

import com.simongarton.chess.model.Move;
import com.simongarton.chess.model.Piece;
import com.simongarton.chess.model.PieceOnBoard;
import com.simongarton.chess.model.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertEquals(Piece.QUEEN, board.getPiece(4, 8));
        assertNull(board.getPiece(5, 5));
    }

    @Test
    void getSide() {
        assertEquals(Side.WHITE, board.getSide(2, 2));
        assertEquals(Side.BLACK, board.getSide(2, 7));
        assertNull(board.getSide(5, 5));
    }

    @Test
    void getBoardValue() {
        assertEquals(49, board.getBoardValue(Side.WHITE));
    }

    @Test
    void testPawnMoves() {
        board.clearBoard();
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.PAWN)
                .square("a2")
                .build();
        board.addPiece(pieceOnBoard);
        List<Move> moves = board.getMovesForPawn(pieceOnBoard);
        assertEquals(2, moves.size());
        assertEquals("a3", moves.get(0).getMove().getTo());
        assertEquals("a4", moves.get(1).getMove().getTo());
    }

    @Test
    void testRookMoves() {
        board.clearBoard();
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.ROOK)
                .square("b6")
                .build();
        board.addPiece(pieceOnBoard);
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.ROOK).square("b8").build());
        board.addPiece(PieceOnBoard.builder().side(Side.BLACK).piece(Piece.ROOK).square("e6").build());
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.PAWN).square("b5").build());
        List<Move> moves = board.getMovesForRook(pieceOnBoard);
        assertEquals(5, moves.size());
    }

    @Test
    void testBishopMoves() {
        board.clearBoard();
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.BISHOP)
                .square("b6")
                .build();
        board.addPiece(pieceOnBoard);
        board.addPiece(PieceOnBoard.builder().side(Side.BLACK).piece(Piece.ROOK).square("c5").build());
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.PAWN).square("d8").build());
        List<Move> moves = board.getMovesForBishop(pieceOnBoard);
        assertEquals(4, moves.size());
    }

    @Test
    void testQueenMoves() {
        board.clearBoard();
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.QUEEN)
                .square("b6")
                .build();
        board.addPiece(pieceOnBoard);
        board.addPiece(PieceOnBoard.builder().side(Side.BLACK).piece(Piece.ROOK).square("c5").build());
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.PAWN).square("d8").build());
        board.addPiece(PieceOnBoard.builder().side(Side.BLACK).piece(Piece.ROOK).square("f6").build());
        List<Move> moves = board.getMovesForQueen(pieceOnBoard);
        for (Move move : moves) {
            System.out.println(move.description());
        }
        assertEquals(16, moves.size());
    }

    @Test
    void testKnightMoves() {
        board.clearBoard();
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.KNIGHT)
                .square("b6")
                .build();
        board.addPiece(pieceOnBoard);
        board.addPiece(PieceOnBoard.builder().side(Side.BLACK).piece(Piece.ROOK).square("d7").build());
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.PAWN).square("d5").build());
        List<Move> moves = board.getMovesForKnight(pieceOnBoard);
        assertEquals(5, moves.size());
    }

    @Test
    void testKingMoves() {
        board.clearBoard();
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.KING)
                .square("b6")
                .build();
        board.addPiece(pieceOnBoard);
        board.addPiece(PieceOnBoard.builder().side(Side.BLACK).piece(Piece.KING).square("b8").build());
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.PAWN).square("c8").build());
        board.addPiece(PieceOnBoard.builder().side(Side.WHITE).piece(Piece.PAWN).square("a7").build());
        List<Move> moves = board.getMovesForKing(pieceOnBoard);
        assertEquals(5, moves.size());
    }

    @Test
    void testBoardValue() {
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.KNIGHT)
                .square("b6")
                .build();
        int value = board.getBoardValue(Side.WHITE);
        assertEquals(49, value);
        board.addPiece(pieceOnBoard);
        value = board.getBoardValue(Side.WHITE);
        assertEquals(61, value);
        value = board.getBoardValue(Side.BLACK);
        assertEquals(55, value);
    }

    @Test
    void testBadRookMove() {
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.ROOK)
                .square("a1")
                .build();
        board.addPiece(pieceOnBoard);
        List<Move> moves = board.getMovesForRook(pieceOnBoard);
        assertEquals(0, moves.size());
    }

    @Test
    void strangeMove1() {
        board.loadFromRanksReverse(
                "rnb.kbnr",
                "pppp.ppp",
                "....p...",
                "........",
                "P.......",
                ".....N..",
                ".PPPPqPP",
                "RNBQKB.R"

        );
        System.out.println(board.showFormattedBoard());
        System.out.println(board.inCheck(Side.BLACK));
        PieceOnBoard pieceOnBoard = PieceOnBoard.builder()
                .side(Side.WHITE)
                .piece(Piece.KING)
                .square("e1")
                .build();
        board.addPiece(pieceOnBoard);
        List<Move> pieceMoves = board.getMovesForKing(pieceOnBoard);
        for (Move move : pieceMoves) {
            System.out.println(move.description());
        }
    }
}