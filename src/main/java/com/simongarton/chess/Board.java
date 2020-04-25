package com.simongarton.chess;

import com.simongarton.chess.model.Move;
import com.simongarton.chess.model.Piece;
import com.simongarton.chess.model.PieceOnBoard;
import com.simongarton.chess.model.Side;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.simongarton.chess.model.Piece.*;
import static com.simongarton.chess.model.Side.BLACK;
import static com.simongarton.chess.model.Side.WHITE;

@Getter
@Setter
public class Board {

    private String board;
    public static final String EMPTY_SYMBOL = ".";

    public Board() {
        clearBoard();
    }

    public void clearBoard() {
        board = fillString(EMPTY_SYMBOL.charAt(0), 64);
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

    public String showDebugBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int rank = 8; rank > 0; rank--) {
            stringBuilder.append("\"").append(getRank(rank)).append("\";\n");
        }
        return stringBuilder.toString();
    }

    private String getRank(int rank) {
        return board.substring((rank - 1) * 8, (rank * 8));
    }

    public void setupDefaultBoard() {
        board = fillString(EMPTY_SYMBOL.charAt(0), 64);
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

    protected void addPiece(Side side, Piece piece, String square) {
        int file = fileNumberFromSquare(square);
        int rank = rankNumberFromSquare(square);
        int index = boardIndex(file, rank);
        board = board.substring(0, index) + getPieceSymbol(side, piece) + board.substring(index + 1, 64);
    }

    private void removePiece(String square) {
        int file = fileNumberFromSquare(square);
        int rank = rankNumberFromSquare(square);
        int index = boardIndex(file, rank);
        board = board.substring(0, index) + EMPTY_SYMBOL + board.substring(index + 1, 64);
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

    public PieceOnBoard getPieceOnBoard(int file, int rank) {
        if ((file < 1) || (file > 8)) return null;
        if ((rank < 1) || (rank > 8)) return null;
        Piece piece = getPiece(file, rank);
        if (piece == null) {
            return null;
        }
        Side side = getSide(file, rank);
        return PieceOnBoard.builder()
                .piece(piece)
                .side(side)
                .square(square(file, rank))
                .build();
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
        // get the right now what pieces do I have value
        int pieceValue = getPieces(side).stream().mapToInt(c -> c.getPiece().getValue()).sum();
        if (inCheck(side)) pieceValue = pieceValue = KING.getValue();
        int moveValue = 0;
        // add in the I will then be able to take pieces from the other side value
//        for (Move move : getMoves(side, false, inCheck(side))) {
//            moveValue = moveValue + move.getInstantValue();
//        }
        return pieceValue + moveValue;
    }

    public List<PieceOnBoard> getPieces(Side side) {
        List<PieceOnBoard> pieces = new ArrayList<>();
        for (int file = 1; file <= 8; file++) {
            for (int rank = 1; rank <= 8; rank++) {
                Piece piece = getPiece(file, rank);
                if (piece == null) {
                    continue;
                }
                Side pieceSide = getSide(file, rank);
                if (side.equals(pieceSide)) {
                    pieces.add(PieceOnBoard.builder()
                            .piece(piece)
                            .side(side)
                            .square(square(file, rank))
                            .build());
                }
            }
        }
        return pieces;
    }

    private String square(int file, int rank) {
        return fileLetter(file) + rank;
    }

    public List<Move> getMoves(Side side, boolean calculateFutureValue, boolean inCheck) {
        Side thisSide;
        Side otherSide;
        if (side == WHITE) {
            thisSide = WHITE;
            otherSide = BLACK;
        } else {
            thisSide = BLACK;
            otherSide = WHITE;
        }
        List<Move> moves = new ArrayList<>();
        for (PieceOnBoard piece : getPieces(side)) {
            moves.addAll(getMovesForPiece(piece));
        }
        if (calculateFutureValue) {
            for (Move move : moves) {
                Board newBoard = copyWithMove(move);
                int newValue = newBoard.getBoardValue(thisSide) - newBoard.getBoardValue(otherSide);
                move.setFutureValue(newValue);
            }
        }
        List<Move> topMoves;
        if (inCheck) {
            topMoves = moves;
            topMoves.sort(Comparator.comparing(m -> ((Move) m).getInstantValue() + ((Move) m).getFutureValue()).reversed());
        } else {
            int topValue = moves.stream().mapToInt(m -> m.getInstantValue() + m.getFutureValue()).max().orElse(0);
            topMoves = moves.stream().filter(m -> (m.getInstantValue() + m.getFutureValue()) == topValue).collect(Collectors.toList());
            Collections.shuffle(topMoves);
        }
        // This comparator wasn't working correctly, it was sorting on instantValue normally, not reversed.
        //topMoves.sort(Comparator.comparing(Move::getInstantValue).reversed().thenComparing(Move::getFutureValue).reversed());
        return topMoves;
    }

    public List<Move> getMoves(Side side) {
        List<Move> moves = new ArrayList<>();
        for (PieceOnBoard piece : getPieces(side)) {
            moves.addAll(getMovesForPiece(piece));
        }
        return moves;
    }

    private Board copyWithMove(Move move) {
        Board board = new Board();
        board.setBoard("" + getBoard());
        board.makeMove(move);
        return board;
    }

    private List<Move> getMovesForPiece(PieceOnBoard piece) {
        switch (piece.getPiece()) {
            case KING:
                return getMovesForKing(piece);
            case QUEEN:
                return getMovesForQueen(piece);
            case BISHOP:
                return getMovesForBishop(piece);
            case KNIGHT:
                return getMovesForKnight(piece);
            case ROOK:
                return getMovesForRook(piece);
            case PAWN:
                return getMovesForPawn(piece);
            default:
                return Collections.emptyList();
        }
    }

    protected List<Move> getMovesForQueen(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(getSlidingMove(-1, -1, piece));
        moves.addAll(getSlidingMove(1, -1, piece));
        moves.addAll(getSlidingMove(-1, 1, piece));
        moves.addAll(getSlidingMove(1, 1, piece));
        moves.addAll(getSlidingMove(-1, 0, piece));
        moves.addAll(getSlidingMove(1, 0, piece));
        moves.addAll(getSlidingMove(0, -1, piece));
        moves.addAll(getSlidingMove(0, 1, piece));
        return moves;
    }

    protected List<Move> getMovesForBishop(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(getSlidingMove(-1, -1, piece));
        moves.addAll(getSlidingMove(1, -1, piece));
        moves.addAll(getSlidingMove(-1, 1, piece));
        moves.addAll(getSlidingMove(1, 1, piece));
        return moves;
    }

    protected List<Move> getMovesForKnight(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        int rank = rankNumberFromSquare(piece.getSquare());
        int file = fileNumberFromSquare(piece.getSquare());
        Move move;
        move = getMove(file - 2, rank - 1, piece);
        if (move != null) moves.add(move);
        move = getMove(file - 2, rank + 1, piece);
        if (move != null) moves.add(move);
        move = getMove(file + 2, rank - 1, piece);
        if (move != null) moves.add(move);
        move = getMove(file + 2, rank + 1, piece);
        if (move != null) moves.add(move);
        move = getMove(file - 1, rank - 2, piece);
        if (move != null) moves.add(move);
        move = getMove(file - 1, rank + 2, piece);
        if (move != null) moves.add(move);
        move = getMove(file + 1, rank - 2, piece);
        if (move != null) moves.add(move);
        move = getMove(file + 1, rank + 2, piece);
        if (move != null) moves.add(move);
        return moves;
    }

    protected List<Move> getMovesForRook(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(getSlidingMove(-1, 0, piece));
        moves.addAll(getSlidingMove(1, 0, piece));
        moves.addAll(getSlidingMove(0, -1, piece));
        moves.addAll(getSlidingMove(0, 1, piece));
        return moves;
    }

    private List<Move> getSlidingMove(int fileDelta, int rankDelta, PieceOnBoard piece) {
        Move move;
        List<Move> moves = new ArrayList<>();
        int rank = rankNumberFromSquare(piece.getSquare());
        int file = fileNumberFromSquare(piece.getSquare());
        while (true) {
            file = file + fileDelta;
            rank = rank + rankDelta;
            if ((file == 0) || (file == 9)) break;
            if ((rank == 0) || (rank == 9)) break;
            move = getAttackMove(file, rank, piece);
            if (move != null) {
                moves.add(move);
                break;
            }
            move = getMoveOnly(file, rank, piece);
            if (move != null) {
                moves.add(move);
            } else {
                break;
            }
        }
        return moves;
    }

    protected List<Move> getMovesForPawn(PieceOnBoard piece) {
        if (piece.getSide().equals(WHITE)) {
            return getMovesForWhitePawn(piece);
        } else {
            return getMovesForBlackPawn(piece);
        }
    }

    private List<Move> getMovesForWhitePawn(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        int rank = rankNumberFromSquare(piece.getSquare());
        int file = fileNumberFromSquare(piece.getSquare());
        Move move;
        move = getMoveOnly(file, rank + 1, piece);
        if (move != null) moves.add(move);
        if ((rank == 2) && !moves.isEmpty()) {
            move = getMoveOnly(file, rank + 2, piece);
            if (move != null) moves.add(move);
        }
        move = getAttackMove(file - 1, rank + 1, piece);
        if (move != null) moves.add(move);
        move = getAttackMove(file + 1, rank + 1, piece);
        if (move != null) moves.add(move);
        // todo ENPASSANT
        // todo PROMOTE
        return moves;
    }

    private List<Move> getMovesForBlackPawn(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        int rank = rankNumberFromSquare(piece.getSquare());
        int file = fileNumberFromSquare(piece.getSquare());
        Move move;
        move = getMoveOnly(file, rank - 1, piece);
        if (move != null) moves.add(move);
        if ((rank == 2) && !moves.isEmpty()) {
            move = getMoveOnly(file, rank - 2, piece);
            if (move != null) moves.add(move);
        }
        move = getAttackMove(file - 1, rank - 1, piece);
        if (move != null) moves.add(move);
        move = getAttackMove(file + 1, rank - 1, piece);
        if (move != null) moves.add(move);
        // todo ENPASSANT
        // todo PROMOTE
        return moves;
    }

    private Move getAttackMove(int file, int rank, PieceOnBoard piece) {
        if (!onBoard(file, rank)) {
            return null;
        }
        String symbol = getSymbol(file, rank);
        if (symbol == null) {
            return null;
        }
        PieceOnBoard pieceOnBoard = getPieceOnBoard(file, rank);
        if (pieceOnBoard.getSide().equals(piece.getSide())) {
            return null;
        }
        String toSquare = fileLetter(file) + rank;
        Move move = Move.moveFromSquares(piece.description(), piece.getSquare(), toSquare);
        move.setNotes("takes " + pieceOnBoard.description());
        move.setInstantValue(pieceOnBoard.getPiece().getValue());
        return move;
    }

    protected List<Move> getMovesForKing(PieceOnBoard piece) {
        List<Move> moves = new ArrayList<>();
        int rank = rankNumberFromSquare(piece.getSquare());
        int file = fileNumberFromSquare(piece.getSquare());
        Move move;
        // TODO Castling
        move = getMove(file - 1, rank - 1, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file - 1, rank - 0, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file - 1, rank + 1, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file - 0, rank - 1, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file - 0, rank + 1, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file + 1, rank - 1, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file + 1, rank - 0, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        move = getMove(file + 1, rank + 1, piece);
        if (move != null) if (legalForKing(piece, move)) moves.add(move);
        return moves;
    }

    private boolean legalForKing(PieceOnBoard piece, Move move) {
        int rank = rankNumberFromSquare(move.getMove().to);
        int file = fileNumberFromSquare(move.getMove().to);
        PieceOnBoard pieceOnBoard;
        pieceOnBoard = getPieceOnBoard(file - 1, rank - 0);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file - 1, rank - 1);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file - 0, rank - 1);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file + 1, rank - 1);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file + 1, rank - 0);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file + 1, rank + 1);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file + 0, rank + 1);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        pieceOnBoard = getPieceOnBoard(file - 1, rank + 1);
        if ((pieceOnBoard != null) && (pieceOnBoard.getPiece().equals(KING)) && (!pieceOnBoard.getSide().equals(piece.getSide())))
            return false;
        return true;
    }

    private Move getMoveOnly(int file, int rank, PieceOnBoard piece) {
        if (onBoard(file, rank)) {
            String symbol = getSymbol(file, rank);
            if (symbol == null) {
                String toSquare = fileLetter(file) + rank;
                Move move = Move.moveFromSquares(piece.description(), piece.getSquare(), toSquare);
                return move;
            }
        }
        return null;
    }

    private Move getMove(int file, int rank, PieceOnBoard piece) {
        if (onBoard(file, rank)) {
            String symbol = getSymbol(file, rank);
            if (symbol == null) {
                String toSquare = fileLetter(file) + rank;
                return Move.moveFromSquares(piece.description(), piece.getSquare(), toSquare);
            } else {
                return getAttackMove(file, rank, piece);
            }
        }
        return null;
    }

    private boolean onBoard(int file, int rank) {
        if ((file <= 0) || file > 8) return false;
        if ((rank <= 0) || rank > 8) return false;
        return true;
    }

    public boolean hasKing(Side side) {
        return getPieces(side).stream().anyMatch(p -> p.getPiece() == KING);
    }

    public boolean makeMove(Move bestMove) {
        int fileFrom = fileNumberFromSquare(bestMove.getMove().from);
        int rankFrom = rankNumberFromSquare(bestMove.getMove().from);
        int rankTo = rankNumberFromSquare(bestMove.getMove().to);
        Side side = getSide(fileFrom, rankFrom);
        Piece piece = getPiece(fileFrom, rankFrom);
        addPiece(side, piece, bestMove.getMove().to);
        if ((piece == PAWN)) {
            if ((side == WHITE) && (rankTo == 8)) {
                addPiece(side, QUEEN, bestMove.getMove().to);
            }
            if ((side == BLACK) && (rankTo == 1)) {
                addPiece(side, QUEEN, bestMove.getMove().to);
            }
        }
        removePiece(bestMove.getMove().from);
        if (inCheck(side)) {
            return false;
        }
        return true;
    }

    public void addPiece(PieceOnBoard pieceOnBoard) {
        addPiece(pieceOnBoard.getSide(), pieceOnBoard.getPiece(), pieceOnBoard.getSquare());
    }

    public void loadFromRanks(String rank1, String rank2, String rank3, String rank4, String rank5, String rank6, String rank7, String rank8) {
        clearBoard();
        board = rank1 + rank2 + rank3 + rank4 + rank5 + rank6 + rank7 + rank8;
    }

    public void loadFromRanksReverse(String rank8, String rank7, String rank6, String rank5, String rank4, String rank3, String rank2, String rank1) {
        clearBoard();
        board = rank1 + rank2 + rank3 + rank4 + rank5 + rank6 + rank7 + rank8;
    }

    public boolean inCheck(Side side) {
        Side thisSide;
        Side otherSide;
        if (side == WHITE) {
            thisSide = WHITE;
            otherSide = BLACK;
        } else {
            thisSide = BLACK;
            otherSide = WHITE;
        }
        List<Move> otherMoves = getMoves(otherSide);
        boolean inCheck = otherMoves.stream().filter(m -> m.getNotes() != null).anyMatch(m -> m.getNotes().toLowerCase().contains("king"));
        return inCheck;
    }

    public int totalPieceCount() {
        return getPieces(WHITE).size() + getPieces(BLACK).size();
    }
}
