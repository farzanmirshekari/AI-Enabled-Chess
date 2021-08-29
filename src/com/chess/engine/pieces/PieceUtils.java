package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.BoardUtils;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

enum PieceUtils {

    INSTANCE;

    private final Table<Color, Integer, Queen> ALL_POSSIBLE_QUEENS = PieceUtils.createAllPossibleMovedQueens();
    private final Table<Color, Integer, Rook> ALL_POSSIBLE_ROOKS = PieceUtils.createAllPossibleMovedRooks();
    private final Table<Color, Integer, Knight> ALL_POSSIBLE_KNIGHTS = PieceUtils.createAllPossibleMovedKnights();
    private final Table<Color, Integer, Bishop> ALL_POSSIBLE_BISHOPS = PieceUtils.createAllPossibleMovedBishops();
    private final Table<Color, Integer, Pawn> ALL_POSSIBLE_PAWNS = PieceUtils.createAllPossibleMovedPawns();

    Pawn getMovedPawn(final Color Color,
                      final int destinationCoordinate) {
        return ALL_POSSIBLE_PAWNS.get(Color, destinationCoordinate);
    }

    Knight getMovedKnight(final Color Color,
                          final int destinationCoordinate) {
        return ALL_POSSIBLE_KNIGHTS.get(Color, destinationCoordinate);
    }

    Bishop getMovedBishop(final Color Color,
                          final int destinationCoordinate) {
        return ALL_POSSIBLE_BISHOPS.get(Color, destinationCoordinate);
    }

    Rook getMovedRook(final Color Color,
                      final int destinationCoordinate) {
        return ALL_POSSIBLE_ROOKS.get(Color, destinationCoordinate);
    }

    Queen getMovedQueen(final Color Color,
                        final int destinationCoordinate) {
        return ALL_POSSIBLE_QUEENS.get(Color, destinationCoordinate);
    }

    private static Table<Color, Integer, Pawn> createAllPossibleMovedPawns() {
        final ImmutableTable.Builder<Color, Integer, Pawn> pieces = ImmutableTable.builder();
        for(final Color Color : Color.values()) {
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(Color, i, new Pawn(Color, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Color, Integer, Knight> createAllPossibleMovedKnights() {
        final ImmutableTable.Builder<Color, Integer, Knight> pieces = ImmutableTable.builder();
        for(final Color Color : Color.values()) {
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(Color, i, new Knight(Color, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Color, Integer, Bishop> createAllPossibleMovedBishops() {
        final ImmutableTable.Builder<Color, Integer, Bishop> pieces = ImmutableTable.builder();
        for(final Color Color : Color.values()) {
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(Color, i, new Bishop(Color, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Color, Integer, Rook> createAllPossibleMovedRooks() {
        final ImmutableTable.Builder<Color, Integer, Rook> pieces = ImmutableTable.builder();
        for(final Color Color : Color.values()) {
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(Color, i, new Rook(Color, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Color, Integer, Queen> createAllPossibleMovedQueens() {
        final ImmutableTable.Builder<Color, Integer, Queen> pieces = ImmutableTable.builder();
        for(final Color Color : Color.values()) {
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(Color, i, new Queen(Color, i, false));
            }
        }
        return pieces.build();
    }

}
