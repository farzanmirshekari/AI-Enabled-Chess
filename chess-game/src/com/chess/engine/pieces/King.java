package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class King extends Piece {

    private final int[] CANDIDATE_MOVE_CORD = {-9,-8,-7,-1,1,7,8,9};
    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final int piecePosition,
                final Color Color,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(piecePosition, Color, PieceType.KING, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final int piecePosition,
                final Color Color,
                final boolean isFirstMove,
                final boolean isCastled,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(piecePosition, Color, PieceType.KING, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int candidateOffset : CANDIDATE_MOVE_CORD){
            final int candidateDestinationCoord = this.piecePos + candidateOffset;

            if(isFirstColumnExclusion(this.piecePos,candidateOffset) ||
                    isEighthColumnExclusion(this.piecePos,candidateOffset)){
                continue;
            }

            if(BoardUtils.isValidTileCoord(candidateDestinationCoord)){
                final Tile candidateDestTile = board.getTile(candidateDestinationCoord);
                if(!candidateDestTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoord));
                }
                else{
                    final Piece pieceOnTile = candidateDestTile.getPiece();
                    final Color pieceColor = pieceOnTile.getPieceColor();
                    if(this.pieceColor != pieceColor){
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoord,pieceOnTile));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoord(),move.getMovedPiece().getPieceColor(),
                false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPos,final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPos] && ( candidateOffset == -1 || candidateOffset == -9 ||
                candidateOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPos,final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPos] && (candidateOffset == 1 || candidateOffset == -7 ||
                candidateOffset == 9);
    }
}
