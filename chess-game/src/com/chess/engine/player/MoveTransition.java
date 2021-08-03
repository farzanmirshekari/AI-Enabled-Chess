package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
    //from the move to the transitioning board state
    private final Board transitionBoard;
    private final MoveStatus MoveStatus;

    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatus MoveStatus){
        this.transitionBoard = transitionBoard;
        this.MoveStatus = MoveStatus;
    }

    public MoveStatus getMoveStatus(){
        return this.MoveStatus;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }

}
