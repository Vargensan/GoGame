package GUIGo;

import com.GO.DrawingBoardI;
import com.GO.PLACE;
import com.GO.PLAYER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public class DrawingBoard extends JComponent implements DrawingBoardI,MouseMotionListener,GUIMathClass{

    private Image image;
    private Graphics2D g2;
    private int sizeGameBoard;
    private int[] StartPoint = new int[2];
    private int WhatDoDraw;
    private int[] Mouse_Cordinates;
    private int[][] Table_Intersection;
    private int[] IntersetionPoint;
    private int distance;
    private PLAYER player;
    private int[] cordinates;


    @Override
    public void filledCircle(Graphics2D g2,PLAYER player, int[] cordinates) {
        this.player = player;
        if(player.equals(PLAYER.BLACK))
            g2.setColor(Color.BLACK);
        else
            g2.setColor(Color.WHITE);
        g2.drawOval(cordinates[0],cordinates[1],15,15);
        g2.fillOval(cordinates[0],cordinates[1],15,15);
    }

    @Override
    public int getXofPoint() {

        return 0;
    }

    @Override
    public int getYofPoint() {

        return 0;
    }

    @Override
    public void drawLines(Graphics2D g2,int temp) {
        int[] X = new int[sizeGameBoard];
        int[] Y = new int[sizeGameBoard];
        X[0] = (int) StartPoint[0];
        Y[0] = (int) StartPoint[1];
        g2.setColor(new Color(0,0,0));
        for(int i=0; i < sizeGameBoard; i++){
            X[i] = X[0] + i*temp;
            Y[i] = Y[0] + i*temp;
        }
        for(int i=0; i < sizeGameBoard; i++){
            g2.drawLine(X[i],Y[sizeGameBoard-1],X[i],Y[0]);
            g2.drawLine(X[sizeGameBoard-1],Y[i],X[0],Y[i]);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.print("Hi");
        if(image==null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(120, 120, 120));
            g2.fillRect(0, 0, this.getSize().width, this.getSize().height);

        }
        if(WhatDoDraw==1)
            drawLines(g2,distance);
        g.drawImage(image, 0, 0, null);
    }

    public DrawingBoard(){

    }
    public void startDrawing(PLACE GameBoard[][]){
        sizeGameBoard = GameBoard.length;
        System.out.println(sizeGameBoard);
        StartPoint = calculateStartPoint(this.getHeight(),this.getWidth());
        distance = calculateDistance(this.getHeight(),sizeGameBoard);
        Table_Intersection = calculateTableIntersection(StartPoint, distance);
        WhatDoDraw=1;
        //drawLines(every);
    }

    @Override
    public void update() {
        g2.setColor(new Color(160,160,160));
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Mouse_Cordinates[0] = e.getX();
        Mouse_Cordinates[1] = e.getY();
        IntersetionPoint = calculateIntersection(Mouse_Cordinates,this.getHeight(),StartPoint,distance);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public int calculateDistance(int height, int boardSize) {
        return (int) height/boardSize;
    }

    @Override
    public int[][] calculateTableIntersection(int[] startPoint, int distance) {
        return new int[0][];
    }

    @Override
    public int[] calculateIntersection(int[] cordinats, int size, int[] startPoint, int distance) {
        return new int[0];
    }

    @Override
    public int[] calculateStartPoint(int height, int width) {
        int[] X = new int[2];
        X[0] = 10;
        X[1] = 10;
        return X;
    }

    @Override
    public int calculateHeightandWidth(int width, int lenght) {
        return 0;
    }
}
