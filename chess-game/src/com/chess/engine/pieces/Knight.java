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

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORD ={-17,-15,-10,-6,6,10,15,17};

    public Knight(final int piecePos, final Color pieceColor) {
        super(piecePos, pieceColor,PieceType.KNIGHT,true);
    }

    public Knight(int piecePos, Color pieceColor, boolean isFirstMove) {
        super(piecePos, pieceColor, PieceType.KNIGHT, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(int currCandidateOffset : CANDIDATE_MOVE_COORD){
            int candidateDestinationCoordinate = this.piecePos + currCandidateOffset;
            if (BoardUtils.isValidTileCoord(candidateDestinationCoordinate) ){
                if(isFirstColExclusion(this.piecePos, currCandidateOffset)
                        ||isSecondColExclusion(this.piecePos,currCandidateOffset)
                        ||isSeventhColExclusion(this.piecePos,currCandidateOffset)
                        ||isEightColExclusion(this.piecePos,currCandidateOffset)){
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                }
                else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Color pieceColor = pieceAtDestination.getPieceColor();
                    if(this.pieceColor != pieceColor){
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }
            }
        }
    return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoord(),move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    // these are exceptions to the subtraction for position rule
    private static boolean isFirstColExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currPos] && (candidateOffset == -17 || candidateOffset == -10
                || candidateOffset == 6 || candidateOffset == 15);
    }
    private static boolean isSecondColExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currPos] && (candidateOffset == -10 || candidateOffset == 6);
    }
    private static boolean isSeventhColExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.SEVEN_COLUMN[currPos] && (candidateOffset == -6 || candidateOffset == 10);
    }
    private static boolean isEightColExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currPos] && (candidateOffset == -15 || candidateOffset == -6
                || candidateOffset == 10 || candidateOffset == 17);
    }
}
