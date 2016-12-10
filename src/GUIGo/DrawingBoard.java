package GUIGo;

import com.GO.DrawingBoardI;
import com.GO.PLACE;
import com.GO.PLAYER;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public class DrawingBoard extends JComponent implements DrawingBoardI{

    //Private
    private Image image;
    private Graphics2D g2;
    private PLAYER player;
    private BoardOnClickListener set_MouseListener;
    private int criclefilled;
    private int sizeGameBoard;
    private int allow_to_drawing = 0;
    private static int[] StartPoint = new int[2];
    private int[][][] Table_Intersection;
    //Package-public
    int distance;
    boolean drawIntersection = false;
    PLACE[][] gameboard;
    int[] intersectionPoint;
    int[] relasedPoint;
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
        X[0] = StartPoint[0];
        Y[0] = StartPoint[1];
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
     * @param g graphics added to override method
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
        if(allow_to_drawing == 1)
            drawLines(g2,distance);
        if(drawIntersection){
            filledCircle(g2,PLAYER.BLACK,intersectionPoint);
            drawIntersection = false;
        }
        if(allow_to_drawing == 1)
            fillBoard(g2,sizeGameBoard,gameboard);
        g.drawImage(image, 0, 0, null);
    }

    public DrawingBoard(){

    }

    /**
     * Method that initialize custom MouseListener
     * set it's properties
     */
    public void initializeMouseListener(){
        set_MouseListener = new BoardOnClickListener(this);
        set_MouseListener.setBoardSize(this.sizeGameBoard);
        set_MouseListener.setHeight(this.getHeight());
        set_MouseListener.initialize();
        this.addMouseMotionListener(set_MouseListener);
        this.addMouseListener(set_MouseListener);
        System.out.println("Added Mouse Motion Listener + Mouse Listener!");
    }

    /**
     * Method which sets game board, and starts method responsible for computing start vales
     * of drawing
     *
     * @param GameBoard takes actual gameboard
     */
    public void startDrawing(PLACE GameBoard[][]){
        this.gameboard = GameBoard;
        setStartValues();
    }

    /**
     * Method which sets start values of drawing such as distance, start point of drawing
     * table of intersections of lines ect.
     */
    public void setStartValues(){
        sizeGameBoard = gameboard.length;
        distance = dmo_calculate.calculateDistance(this.getHeight(),sizeGameBoard);
        StartPoint = dmo_calculate.calculateStartPoint(this.getHeight(),sizeGameBoard,distance);
        criclefilled = dmo_calculate.calculateSizeOfCircle(distance);
        Table_Intersection = dmo_calculate.calculateTableIntersection(StartPoint,distance,sizeGameBoard,criclefilled);
        intersectionPoint = new int[2];
        //gameboard[0][0] = PLACE.BLACK;
        //gameboard[1][3] = PLACE.WHITE;
        //gameboard[4][8] = PLACE.BLACK;
        allow_to_drawing = 1;
        initializeMouseListener();
        update();
    }

    /**
     * Method responsible for checking the state of each point in game table
     * and depending on it's state, change the color of mentioned point to the color of
     * the player whom sets it on the game board table.
     * @param g2 takes a graphic
     * @param sizeGameBoard takes a size of game table
     * @param GameBoard takes a game board
     */
    private void fillBoard(Graphics2D g2, int sizeGameBoard, PLACE GameBoard[][]){
        for(int i = 0; i < sizeGameBoard; i++){
            for(int j = 0; j < sizeGameBoard; j++){
                if(GameBoard[i][j] == PLACE.BLACK){
                    intersectionPoint = dmo_calculate.calculateIntersection(Table_Intersection[i][j],sizeGameBoard,StartPoint,distance);
                    filledCircle(g2, PLAYER.BLACK, intersectionPoint);
                } else if(GameBoard[i][j] == PLACE.WHITE){
                    intersectionPoint = dmo_calculate.calculateIntersection(Table_Intersection[i][j],sizeGameBoard,StartPoint,distance);
                    filledCircle(g2,PLAYER.WHITE,intersectionPoint);
                }
            }
        }
    }

    @Override
    public void update() {
        if(g2 != null) {
            g2.setColor(new Color(160, 160, 160));
        }
        image=null;
        repaint();
    }

}
