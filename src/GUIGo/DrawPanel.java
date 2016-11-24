package GUIGo;

import com.GO.DrawingBoardI;
import com.GO.PLACE;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public class DrawPanel extends JComponent implements DrawingBoardI{

    private Image image;
    private Graphics2D g2;
    private int sizeGameBoard;
    private Point StartPoint = new Point(10,10);
    private int WhatDoDraw;
    int every;

    @Override
    public void drawLine(Graphics gr, int[] X, int[] Y) {

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
        X[0] = (int) StartPoint.getX();
        Y[0] = (int) StartPoint.getY();
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
    public void countLines(int tableSize) {

    }

    @Override
    public int setStartConditions() {
        int height,width,linesEvery;
        height = this.getHeight();
        width = this.getWidth();
        linesEvery = (int) (height/sizeGameBoard);
        StartPoint.setLocation(10,10);
        return linesEvery;

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
            drawLines(g2,every);
        g.drawImage(image, 0, 0, null);
    }

    public DrawPanel(){

    }
    public void startDrawing(PLACE GameBoard[][]){
        sizeGameBoard = GameBoard.length;
        System.out.println(sizeGameBoard);
        every = setStartConditions();
        WhatDoDraw=1;
        //drawLines(every);
    }

    @Override
    public void update() {

    }
}
