package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoord;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece piece, final int destinationCoord){
        this.board = board;
        this.movedPiece = piece;
        this.destinationCoord = destinationCoord;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,final int destinationCoord){
        this.board = board;
        this.movedPiece = null;
        this.destinationCoord = destinationCoord;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destinationCoord;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoord() == otherMove.getCurrentCoord() &&
                getDestinationCoord() == otherMove.getDestinationCoord() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getDestinationCoord(){
        return this.destinationCoord;
    }

    public int getCurrentCoord(){ return this.movedPiece.getPiecePosition();}

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Board getBoard(){
        return this.board;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {

        final Builder builder = new Builder();

        for(final Piece piece: this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        //move the moved piece
        builder.setPiece(this.movedPiece.movePiece(this));
        //set movemaker to opponent as current move
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
        return builder.build();
    }

    public static class MajorAttackMove extends AttackMove{
        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final int destinationCoord,
                               final Piece pieceAttacked){
            super(board,pieceMoved,destinationCoord,pieceAttacked);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType() + BoardUtils.getPosAtCoord(this.destinationCoord);
        }
    }

    public static final class MajorMove extends Move{
        public MajorMove(Board board, Piece piece, int destinationCoord) {
            super(board, piece, destinationCoord);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPosAtCoord(this.destinationCoord);
        }
    }
    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(Board board, Piece piece, int destinationCoord, final Piece attackedPiece) {
            super(board, piece, destinationCoord);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack(){ return true;}

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    //pawn specifics
    public static final class PawnMove extends Move{
        public PawnMove(Board board, Piece piece, int destinationCoord) {
            super(board, piece, destinationCoord);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPosAtCoord(this.destinationCoord);
        }

    }
    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Board board, Piece piece, int destinationCoord, final Piece attackedPiece) {
            super(board, piece, destinationCoord, attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof AttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPosAtCoord(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    BoardUtils.getPosAtCoord(this.destinationCoord);
        }

    }

    public static class PawnEnPassantMove extends PawnAttackMove{

        public PawnEnPassantMove(Board board, Piece piece, int destinationCoord, final Piece attackedPiece) {
            super(board, piece, destinationCoord, attackedPiece);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnEnPassantMove && super.equals(other);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }

    }

    public static final class PawnJump extends Move{
        public PawnJump(Board board, Piece piece, int destinationCoord) {
            super(board, piece, destinationCoord);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }

        @Override
        public String toString(){
            return BoardUtils.getPosAtCoord(this.destinationCoord);
        }

    }

    public static class PawnPromotion extends Move{
        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(),decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoord());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for(final Piece piece: pawnMovedBoard.currentPlayer().getActivePieces()){
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: pawnMovedBoard.currentPlayer().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getOpponent().getColor());
            return builder.build();
        }

        @Override
        public String toString(){
            return "";
        }

        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnPromotion && (this.decoratedMove.equals(other));
        }
    }

    // castling
    static abstract class CastleMove extends Move{

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDest;

        public CastleMove(Board board, Piece piece, int destinationCoord
                          ,Rook castleRook,int castleRookStart,int castleRookDest) {
            super(board, piece, destinationCoord);
            this.castleRook = castleRook;
            this.castleRookDest = castleRookDest;
            this.castleRookStart = castleRookStart;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDest,this.castleRook.getPieceColor(),false));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDest;
            return result;
        }

        @Override
        public boolean equals(Object other){
            if(this==other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }

    }

    public static final class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(Board board, Piece piece, int destinationCoord,
                                  Rook castleRook,int castleRookStart,int castleRookDest) {
            super(board, piece, destinationCoord,castleRook,castleRookStart,castleRookDest);
        }

        @Override
        public String toString(){
            return "0-0";
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

    }

    public static final class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(Board board, Piece piece, int destinationCoord,
                                   Rook castleRook,int castleRookStart,int castleRookDest) {
            super(board, piece, destinationCoord,castleRook,castleRookStart,castleRookDest);
        }

        @Override
        public String toString(){
            return "0-0-0";
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }

    }

    public static final class NullMove extends Move{
        public NullMove() {
            super(null,65);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot run null move");
        }

        @Override
        public int getCurrentCoord(){
            return -1;
        }
    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board,
                                      final int currentCoord,
                                      final int destinationCoord){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoord() == currentCoord &&
                move.getDestinationCoord() == destinationCoord){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }


}
