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

public class Rook extends Piece {

    final static int[] CANDIDATE_MOVE_VECTOR_COORD = {-8,-1,1,8};

    public Rook(int piecePos, Color pieceColor) {
        super(piecePos, pieceColor,PieceType.ROOK,true);
    }

    public Rook(final int piecePos,Color pieceColor,final boolean isFirstMove){
        super(piecePos,pieceColor,PieceType.ROOK,isFirstMove);
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
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoord(),move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPos,final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPos] && (candidateOffset == -1 );
    }
    private static boolean isEighthColumnExclusion(final int currentPos,final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPos] && (candidateOffset == 1 );
    }
}
