package com.chess.engine.board;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

// alot of these variables are final, so that they cannot be edited via public means
//immutable objects cannot be mutated
public abstract class Tile {

    protected final int tileCoord;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
//        Collections.unmodifiableMap(emptyTileMap); essentially same as
        return ImmutableMap.copyOf(emptyTileMap); // so that no one can change this emptytilemap
    }

    public static Tile createTile(final int tileCoord, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoord,piece) : EMPTY_TILES_CACHE.get(tileCoord);
        // if piece is not null, return new occupiedTile, else return emptyTile from the HashMap
        // only way to publically access the tile constructors
    }

    private Tile(final int tileCoord){
        this.tileCoord = tileCoord;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoord(){
        return this.tileCoord;
    }

    public static final class EmptyTile extends Tile{
        private EmptyTile(final int tileCoord){
            super(tileCoord);
        }
       @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString(){
            return "-";
        }
    }
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        private OccupiedTile(final int tileCoord,final Piece pieceOnTile){
            super(tileCoord);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }

        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceColor().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }

    }
}
