package GUIGo;

import com.GO.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public class DrawingBoard extends JComponent implements DrawingBoardI{

    //General Objects
    DrawMathObject dmo_calculate = new DrawMathObject();
    private Board board;
    private Play play;
    private BoardOnClickListener mouseListener;
    //LOGIC INFORMATIONS
    private boolean DeadTable[][];
    private PLACE[][] TerritoryTable;
    private PLACE[][] gameboard;
    private int[] KO_Points;
    private boolean ko_detected = false;
    //Drawing Mouse Informations
    private int[][][] Table_Intersection;
    int[] intersectionPoint;
    int[] relasedPoint;
    //Boolean-Drawing Informations
    boolean drawIntersection = false;
    private boolean drawDeadPools = true;
    private boolean drawTerritory = true;
    //----???---
    private boolean firsttime_setTerritory = true;
    private boolean firsttime_setDead = true;
    //DrawingBoard self-use
    private BufferedImage image,controlerImage,im_black,im_white,im_ko,im_dead,im_territoryBlack,im_territoryWhite;
    private ImageResize imageResizer;
    private Graphics2D g2;
    private int allow_to_drawing = 0;
    private final String abc[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","R","S","T","U"};
    private final String numbers[] = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
    private int strokeOne;
    private int strokeTwo;
    //DrawingBoard Informations
    int distance;
    private int criclefilled;
    private static int[] StartPoint = new int[2];
    //General Informations
    boolean isClicable;
    private int sizeGameBoard;




    DrawingBoard(Board board,Play play){
        this.play=play;
        this.board=board;
        intersectionPoint = new int[2];
        intersectionPoint[0]=-1;
        intersectionPoint[1] =-1;
    }

    /**
     * Setter for Dead Table - which holds information about Dead Groups
     */
    public void SetterDeadTable(){
        this.DeadTable = play.getPlayBoard().getDeadTable();
    }

    /**
     * Setter for Terrirtory Table - which holds information about Territory of players
     */
    public void SetterTerritoryTable(){
        this.TerritoryTable = play.getPlayBoard().getTerritoryTable();
    }

    /**
     * Method that is getter for background image
     * @return image that is printed on background
     */
    private BufferedImage gettempimage(){
        if(controlerImage != null){
            return controlerImage;
        }
        return image;
    }
    @Override
    public void filledCircle(Graphics2D g2,PLAYER player, int[] cordinates) {
        if ((cordinates[0] >= 0) && (cordinates[1] >= 0)) {
            if (player.equals(PLAYER.BLACK)) {
                g2.drawImage(im_black, Table_Intersection[cordinates[0]][cordinates[1]][0], Table_Intersection[cordinates[0]][cordinates[1]][1], null);
            } else
                g2.drawImage(im_white, Table_Intersection[cordinates[0]][cordinates[1]][0], Table_Intersection[cordinates[0]][cordinates[1]][1], null);
        }
    }

    private void allertKO(Graphics2D g2){
        int from[] = {Table_Intersection[KO_Points[0]][KO_Points[1]][0], Table_Intersection[KO_Points[0]][KO_Points[1]][1]};
        g2.drawImage(im_ko,from[0],from[1],null);
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
            g2.setStroke(new BasicStroke(strokeOne));
            g2.drawLine(X[i],Y[sizeGameBoard-1],X[i],Y[0]);
            g2.drawString(abc[i],X[i],Y[sizeGameBoard-1]+criclefilled);
            g2.drawString(abc[i],X[i],Y[0]-criclefilled/2);
            g2.drawLine(X[sizeGameBoard-1],Y[i],X[0],Y[i]);
            g2.drawString(numbers[i],X[sizeGameBoard-1]+criclefilled/2,Y[i]);
            g2.drawString(numbers[i],X[0]-criclefilled,Y[i]);
        }
    }

    @Override
    public void drawBorders(Graphics2D g2) {
        int offset = strokeTwo/2;
        g2.setStroke(new BasicStroke(strokeTwo));
        g2.drawLine(offset,offset,this.getWidth()-offset,offset);
        g2.drawLine(offset,offset,offset,this.getHeight()-offset);
        g2.drawLine(this.getWidth()-offset,this.getHeight()-offset,this.getWidth()-offset,offset);
        g2.drawLine(this.getWidth()-offset,this.getHeight()-offset,offset,this.getHeight()-offset);
    }

    @Override
    public void drawTerritory(Graphics2D g2) {
        if(firsttime_setTerritory){
            SetterTerritoryTable();
            firsttime_setTerritory=false;
        }
        for(int i = 0; i < sizeGameBoard; i++){
            for(int j = 0; j < sizeGameBoard; j++){
                if(TerritoryTable[i][j] == PLACE.BLACK){
                    g2.drawImage(im_territoryBlack,Table_Intersection[i][j][0],Table_Intersection[i][j][1],null);
                }
                else if(TerritoryTable[i][j] == PLACE.WHITE){
                    g2.drawImage(im_territoryWhite,Table_Intersection[i][j][0],Table_Intersection[i][j][1], null);
                }
            }
        }

    }

    @Override
    public void drawDeadPools(Graphics2D g2) {
        if(firsttime_setDead){
            SetterDeadTable();
            firsttime_setDead = false;
        }
        for(int i = 0; i < sizeGameBoard ; i++){
            for(int j = 0 ; j < sizeGameBoard; j++){
                if(DeadTable[i][j] == true){
                    g2.drawImage(im_dead,Table_Intersection[i][j][0]+criclefilled/4,Table_Intersection[i][j][1]+criclefilled/4,null);
                }
            }
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
        if(image==null) {
            try {
                if(controlerImage != null){
                    image = gettempimage();
                }
                else{
                    InputStream imageInputStream = this.getClass().getResourceAsStream("/GoGraphics/DrawingBoardTexture.png");
                    BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                    image = imageResizer.scale(bufferedImage,this.getWidth(),this.getHeight());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            g2 = (Graphics2D) image.getGraphics();
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(120, 120, 120));
        }
        g2.drawImage(gettempimage(),0,0,null);
        if(allow_to_drawing == 1)
            drawLines(g2,distance);
            drawBorders(g2);
        if(drawIntersection){
               filledCircle(g2,board.getPLayerColor(),intersectionPoint);

            drawIntersection = false;
        }
        if(allow_to_drawing == 1)
            fillBoard(g2,sizeGameBoard,gameboard);
        if(ko_detected)
            allertKO(g2);
        if(drawDeadPools){
            drawDeadPools(g2);
        }
        if(drawTerritory){
            drawTerritory(g2);
        }
        g.drawImage(image, 0, 0, null);
    }

    /**
     * Function for changing state of Drawing Board
     * @param state given state
     */
    public void changeState(STATE state){
        switch(state){
            case GAME:
                drawIntersection=true;
                drawDeadPools=false;
                drawTerritory=false;
                break;
            case ADD_DEAD_GROUPS:
                drawDeadPools=true;
                drawIntersection=false;
                drawTerritory=false;
                break;
            case REMOVE_DEAD_GROUPS:
                drawDeadPools=true;
                drawIntersection=false;
                drawTerritory=false;
                break;
            case BEFORE_GAME:
                drawIntersection=true;
                drawDeadPools=false;
                drawTerritory=false;
                break;
            default:
                break;
        }
    }


    public boolean getterMouseListener(){
        return isClicable;
    }

    /**
     * Setter for boolean which tell if player's clicks are allowed
     * @param click takes a boolean true/false
     */
    public void setterMouseListener(boolean click){
        System.out.println("\n1.state of bla a-active u->unactive : " + click);
        this.isClicable = click;
    }

    /**
     * Method that initialize custom MouseListener
     * set it's properties
     */
    private void initializeMouseListener(){
        mouseListener = new BoardOnClickListener(board,this,play,isClicable);
        mouseListener.setBoardSize(this.sizeGameBoard);
        mouseListener.setHeight(this.getHeight());
        mouseListener.initialize();
        this.addMouseMotionListener(mouseListener);
        this.addMouseListener(mouseListener);
        System.out.println("Added Mouse Motion Listener + Mouse Listener!");
    }
    public BoardOnClickListener getBoardOnClickListener()
    {
        return mouseListener;
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
        repaint();
        //paintImmediately(this.get,this.getWidth(),this.getHeight());
    }

    /**
     * Method which sets start values of drawing such as distance, start point of drawing
     * table of intersections of lines ect.
     */
    private void setStartValues(){
        sizeGameBoard = gameboard.length;
        distance = dmo_calculate.calculateDistance(this.getHeight(),sizeGameBoard);
        StartPoint = dmo_calculate.calculateStartPoint(this.getHeight(),sizeGameBoard,distance);
        criclefilled = dmo_calculate.calculateSizeOfCircle(distance);
        Table_Intersection = dmo_calculate.calculateTableIntersection(StartPoint,distance,sizeGameBoard,criclefilled);
        setImages(this);
        strokeOne = 2;
        strokeTwo = 2;
        allow_to_drawing = 1;
        initializeMouseListener();
    }

    /**
     * Method that reads images and sets graphic of buttons
     * @param window takes drawingboard table
     */

    private void setImages(DrawingBoard window){
            imageResizer = new ImageResize();
            im_black = imageSetter(window,imageResizer,"/GoGraphics/black_button.png",criclefilled,criclefilled);
            im_white = imageSetter(window,imageResizer,"/GoGraphics/white_button2.png",criclefilled,criclefilled);
            controlerImage = imageSetter(window,imageResizer,"/GoGraphics/DrawingBoardTexture.png",this.getWidth(),this.getHeight());
            im_ko = imageSetter(window,imageResizer,"/GoGraphics/KOsituationButton.png",criclefilled,criclefilled);
            im_dead = imageSetter(window,imageResizer,"/GoGraphics/KOsituationButton.png",criclefilled*1/2,criclefilled*1/2);
            im_territoryBlack = imageSetter(window,imageResizer,"/GoGraphics/Territory_Black.png",criclefilled,criclefilled);
            im_territoryWhite = imageSetter(window,imageResizer,"/GoGraphics/Territory_White.png",criclefilled,criclefilled);
    }
    public BufferedImage imageSetter(DrawingBoard window, ImageResize imageResizer, String name, int prefW, int prefH){
        try {
            InputStream imageInputStream = window.getClass().getResourceAsStream(name);
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            return imageResizer.scale(bufferedImage,prefW,prefH);
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;

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
    public void update(){
        repaint();
    }

    /**
     * Method that initialize KO Situation
     * @param ko_detected boolean which says if ko situation was detected
     */
    public void set_KO_Situation(boolean ko_detected){
        this.ko_detected = ko_detected;
    }

    /**
     * Setter for ko situation
     * @param KO_Points takes a point where KO situation was detected
     */
    public void set_KO_Points(int[] KO_Points){
        this.KO_Points = KO_Points;
    }

}
