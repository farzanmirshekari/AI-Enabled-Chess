package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color COLOR_PANEL = Color.decode("#e6e0d1");
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private int frameLength = (int) (screenDimension.width*0.45);
    private final Dimension TAKEN_PIECES_DIMENSION = new Dimension(frameLength/10, (frameLength)* (2/15));
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(COLOR_PANEL);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(16,1));
        this.southPanel = new JPanel(new GridLayout(16,1));
        this.northPanel.setBackground(COLOR_PANEL);
        this.southPanel.setBackground(COLOR_PANEL);
        add(this.northPanel,BorderLayout.NORTH);
        add(this.southPanel,BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog){
        southPanel.removeAll();
        northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move: moveLog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceColor().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }
                else if (takenPiece.getPieceColor().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }
                else{
                    throw new RuntimeException("NOT POSSIBLE TO REACH HERE");
                }
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(),o2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(),o2.getPieceValue());
            }
        });

        for (final Piece takenPiece : whiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("gui_elements/piece_icons/" +
                        takenPiece.getPieceColor().toString().substring(0,1) +
                        takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 725, icon.getIconWidth() - 725, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            }
            catch (final IOException e){
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("gui_elements/piece_icons/" +
                        takenPiece.getPieceColor().toString().substring(0,1) +
                        takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 725, icon.getIconWidth() - 725, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
            }
            catch (final IOException e){
                e.printStackTrace();
            }
        }

        validate();
    }

}
