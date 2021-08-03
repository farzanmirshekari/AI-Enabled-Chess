package com.chess.engine.board;

import com.chess.engine.Color;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    public Tile getTile(final int tileCoord){
        return gameBoard.get(tileCoord);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+1)%BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();

    }

    public Player WhitePlayer(){
        return this.whitePlayer;
    }

    public Player BlackPlayer(){
        return this.blackPlayer;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    public Iterable<Move> getAllLegalMoves(){
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                                                               this.blackPlayer.getLegalMoves()));
    }

    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard,Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard,Color.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection <Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    private Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Color Color) {
        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceColor() == Color){
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    private static List<Tile> createGameBoard(Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i < tiles.length; i ++){
            tiles[i] = Tile.createTile(i,builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //Black setup
        builder.setPiece(new Rook(0,Color.BLACK));
        builder.setPiece(new Knight(1,Color.BLACK));
        builder.setPiece(new Bishop(2,Color.BLACK));
        builder.setPiece(new Queen(3,Color.BLACK));
        builder.setPiece(new King(4,Color.BLACK,true,true));
        builder.setPiece(new Bishop(5,Color.BLACK));
        builder.setPiece(new Knight(6,Color.BLACK));
        builder.setPiece(new Rook(7,Color.BLACK));
        builder.setPiece(new Pawn(8,Color.BLACK));
        builder.setPiece(new Pawn(9,Color.BLACK));
        builder.setPiece(new Pawn(10,Color.BLACK));
        builder.setPiece(new Pawn(11,Color.BLACK));
        builder.setPiece(new Pawn(12,Color.BLACK));
        builder.setPiece(new Pawn(13,Color.BLACK));
        builder.setPiece(new Pawn(14,Color.BLACK));
        builder.setPiece(new Pawn(15,Color.BLACK));
        //White setup
        builder.setPiece(new Rook(56,Color.WHITE));
        builder.setPiece(new Knight(57,Color.WHITE));
        builder.setPiece(new Bishop(58,Color.WHITE));
        builder.setPiece(new Queen(59,Color.WHITE));
        builder.setPiece(new King(60,Color.WHITE,true,true));
        builder.setPiece(new Bishop(61,Color.WHITE));
        builder.setPiece(new Knight(62,Color.WHITE));
        builder.setPiece(new Rook(63,Color.WHITE));
        builder.setPiece(new Pawn(48,Color.WHITE));
        builder.setPiece(new Pawn(49,Color.WHITE));
        builder.setPiece(new Pawn(50,Color.WHITE));
        builder.setPiece(new Pawn(51,Color.WHITE));
        builder.setPiece(new Pawn(52,Color.WHITE));
        builder.setPiece(new Pawn(53,Color.WHITE));
        builder.setPiece(new Pawn(54,Color.WHITE));
        builder.setPiece(new Pawn(55,Color.WHITE));

        //white is first to move
        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Color nextMoveMaker;
        Pawn enPassantPawn;

        public Builder(){
            this.boardConfig = new HashMap<>();

        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }

        public Builder setMoveMaker(final Color nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }

}
