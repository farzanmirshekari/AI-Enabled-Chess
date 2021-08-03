package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePos;
    protected final Color pieceColor;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final int piecePos,final Color pieceColor,final PieceType pieceType,final boolean isFirstMove){
        this.piecePos = piecePos;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;

        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceColor.hashCode();
        result = 31 * result + piecePos;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePos == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()
                && pieceColor == otherPiece.getPieceColor() && isFirstMove == otherPiece.isFirstMove();
    }

    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public int getPiecePosition(){return this.piecePos; }

    public Color getPieceColor(){
        return this.pieceColor;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public PieceType getPieceType() { return this.pieceType; }

    public abstract Piece movePiece(Move move);

    public enum PieceType {

        BISHOP("B",300){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K",10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        PAWN("P",100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q",900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();

        public int getPieceValue(){
            return this.pieceValue;
        };
    }

}
