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

public class Queen extends Piece {

    final static int[] CANDIDATE_MOVE_VECTOR_COORD = {-9,-8,-7,-1,1,7,8,9};

    public Queen(int piecePos, Color pieceColor) {
        super(piecePos, pieceColor,PieceType.QUEEN,true);
    }

    public Queen(int piecePos, Color pieceColor, boolean isFirstMove) {
        super(piecePos, pieceColor, PieceType.QUEEN, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORD){

            int candidateDestinationCoord = this.piecePos;
            while(BoardUtils.isValidTileCoord(candidateDestinationCoord)){
                if(isFirstColumnExclusion(candidateDestinationCoord,candidateCoordinateOffset)
                        ||isEighthColumnExclusion(candidateDestinationCoord,candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoord += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoord(candidateDestinationCoord)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoord);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoord));
                    }
                    else{
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece  ();
                        final Color pieceColor = pieceAtDestination.getPieceColor();
                        if(this.pieceColor != pieceColor){
                            legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoord,
                                    pieceAtDestination));
                        }
                        break;
                    }

                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoord(),move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
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
