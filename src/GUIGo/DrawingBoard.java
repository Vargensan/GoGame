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
public class DrawingBoard extends JComponent implements DrawingBoardI{

    //Private
    private Image image;
    private Graphics2D g2;
    private PLAYER player;
    private BoardOnClickListener set_MouseListener;
    private int WhatDoDraw;
    private int criclefilled;
    private int sizeGameBoard;
    private static int[] StartPoint = new int[2];
    private int[][][] Table_Intersection;
    //Package-public
    int distance;
    boolean drawIntersection = false;
    int[] intersectionPoint;
    DrawMathObject dmo_calculate = new DrawMathObject();

    @Override
    public void filledCircle(Graphics2D g2,PLAYER player, int[] cordinates) {
        this.player = player;
        if(player.equals(PLAYER.BLACK))
            g2.setColor(Color.BLACK);
        else
            g2.setColor(Color.WHITE);
        System.out.println(cordinates[0]);
        System.out.println(cordinates[1]);
        g2.drawOval(Table_Intersection[cordinates[0]][cordinates[1]][0],Table_Intersection[cordinates[0]][cordinates[1]][1],criclefilled,criclefilled);
        g2.fillOval(Table_Intersection[cordinates[0]][cordinates[1]][0],Table_Intersection[cordinates[0]][cordinates[1]][1],criclefilled,criclefilled);
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

    /**
     * Method which allows to draw on the picture, create image
     * and also initialize the other drawing functions
     * @param g graphics added to overided method
     */
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
        if(drawIntersection){
            filledCircle(g2,PLAYER.BLACK,intersectionPoint);
            drawIntersection = false;
        }
        g.drawImage(image, 0, 0, null);
    }

    public DrawingBoard(){

    }

    /**
     * Method that initialize custom MouseListener
     * set it's properties
     */
    public void initalizeMouseListener(){
        set_MouseListener = new BoardOnClickListener(this);
        set_MouseListener.setBoardSize(this.sizeGameBoard);
        set_MouseListener.setHeight(this.getHeight());
        set_MouseListener.initialize();
        this.addMouseMotionListener(set_MouseListener);
        System.out.println("Added Mouse Listener!");
    }

    /**
     * Method which starts calculating of all nessecery objects needed to
     * start drawing
     * @param GameBoard takes actuall gameboard size
     */
    public void startDrawing(PLACE GameBoard[][]){
        sizeGameBoard = GameBoard.length;
        System.out.println(sizeGameBoard);
        distance = dmo_calculate.calculateDistance(this.getHeight(), sizeGameBoard);
        StartPoint = dmo_calculate.calculateStartPoint(this.getHeight(), sizeGameBoard, distance);
        criclefilled = dmo_calculate.calculateSizeOfCircle(distance);
        Table_Intersection = dmo_calculate.calculateTableIntersection(StartPoint,distance,sizeGameBoard,criclefilled);
        WhatDoDraw=1;
        //drawLines(every);
    }

    @Override
    public void update() {
        g2.setColor(new Color(160,160,160));
        image=null;
        repaint();
    }

}
