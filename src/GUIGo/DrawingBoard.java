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

    public void SetterDeadTable(){
        this.DeadTable = play.getPlayBoard().getDeadTable();
    }
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
            g2.drawLine(X[i],Y[sizeGameBoard-1],X[i],Y[0]);
            g2.drawString(abc[i],X[i],Y[sizeGameBoard-1]+distance);
            g2.drawString(abc[i],X[i],Y[0]-criclefilled/2);
            g2.drawLine(X[sizeGameBoard-1],Y[i],X[0],Y[i]);
            g2.drawString(numbers[i],X[sizeGameBoard-1]+criclefilled/2,Y[i]);
            g2.drawString(numbers[i],X[0]-distance,Y[i]);
        }
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

        /*
         poniższe wywołanie metody w celu odświeżania natychmiastowego obrazka
        Adnote: tutaj g2 chyba nigdy nie będzie nullem, bo jeżeli
        image = null, to tworzymy image, dodajemy do niego grafikę i więcej razy nie wchodzimy
        w image == null, bo nigdzie go nie zerujemy, tak samo nigdy nie zerujemy grafiki
        ale trzeba to sprawdzić, żeby usunąć update() i wszędzie zamiast niej wywoływać
        metodę paintImmedietntly()
         */
        g2.drawImage(gettempimage(),0,0,null);
        if(allow_to_drawing == 1)
            drawLines(g2,distance);
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


    public boolean getterMouseListener(){
        return isClicable;
    }
    public void setterMouseListener(boolean bla){
        System.out.println("\n1.state of bla a-active u->unactive : " + bla);
        this.isClicable = bla;
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
        setBlackandWhite(this);
        allow_to_drawing = 1;
        initializeMouseListener();
        //paintImmediately(0,0,this.getWidth(),this.getHeight());
        //update();
    }

    /**
     * Method that reads images and sets graphic of buttons
     * @param window takes drawingboard table
     */

    private void setBlackandWhite(DrawingBoard window){
        try{
            imageResizer = new ImageResize();
            InputStream imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/black_button.png");
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            im_black = imageResizer.scale(bufferedImage,criclefilled,criclefilled);
            imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/white_button2.png");
            bufferedImage = ImageIO.read(imageInputStream);
            im_white = imageResizer.scale(bufferedImage,criclefilled,criclefilled);
            imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/DrawingBoardTexture.png");
            bufferedImage = ImageIO.read(imageInputStream);
            controlerImage = imageResizer.scale(bufferedImage,this.getWidth(),this.getHeight());
            imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/KOsituationButton.png");
            bufferedImage = ImageIO.read(imageInputStream);
            im_ko = imageResizer.scale(bufferedImage,criclefilled,criclefilled);
            imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/KOsituationButton.png");
            bufferedImage = ImageIO.read(imageInputStream);
            im_dead = imageResizer.scale(bufferedImage,(criclefilled*1/2),(criclefilled*1/2));
            imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/Territory_Black.png");
            bufferedImage = ImageIO.read(imageInputStream);
            im_territoryBlack = imageResizer.scale(bufferedImage,criclefilled,criclefilled);
            imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/Territory_White.png");
            bufferedImage = ImageIO.read(imageInputStream);
            im_territoryWhite = imageResizer.scale(bufferedImage,criclefilled,criclefilled);
        } catch (IOException exception){
            exception.printStackTrace();
        }
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
   /* @Override
    public void update() {
        if(g2 != null) {
            g2.setColor(new Color(160, 160, 160));
            g2.drawImage(gettempimage(),0,0,null);
        } else{
            if(image != null){
                g2 = (Graphics2D) image.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        }
        //paintImmediately(0,0,getWidth(),getHeight());
        repaint();
    }*/

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
