package com.chess.gui;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color COLOR_PANEL = Color.decode("#e6e0d1");
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private int frameLength = (int) (screenDimension.width*0.45);
    private final Dimension TAKEN_PIECES_DIMENSION = new Dimension(frameLength/10, (frameLength)* (2/15));
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public int pieceIconLength = (int) (screenDimension.width*0.025);

    public TakenPiecesPanel() {
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

    public void redo(final MoveLog moveLog) {
        southPanel.removeAll();
        northPanel.removeAll();
        
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();
        
        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAllegiance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.getPieceAllegiance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(final Piece p1, final Piece p2) {
                return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(final Piece p1, final Piece p2) {
                return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });
        
        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("gui_elements/piece_icons/"
                        + takenPiece.getPieceAllegiance().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".png"));
                this.southPanel.add(new JLabel(new ImageIcon(resize(image, pieceIconLength/2, pieceIconLength/2))));
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("gui_elements/piece_icons/"
                        + takenPiece.getPieceAllegiance().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".png"));
                this.northPanel.add(new JLabel(new ImageIcon(resize(image, pieceIconLength/2, pieceIconLength/2))));

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        
        validate();
    }

    public BufferedImage resize(BufferedImage img, int newWidth, int newHeight) { 
        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return resizedImage;
    }
}