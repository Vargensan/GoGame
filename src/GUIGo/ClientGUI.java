package GUIGo;

import com.GO.Board;
import com.GO.PLACE;
import com.GO.PLAYER;
import com.GO.Play;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public class ClientGUI extends JFrame{

    /*
      * Constants declared to JFrame build
     */
    private final int X_OF_DRAWPANEL = 450;
    private final int Y_OF_DRAWPANEL = 450;
    private final int JPANEL_HEIGHT = 40;
    private final int JPANEL_WIDTH = 360;
    private final int X_WINDOW_SIZE = 600;
    private final int Y_WINDOW_SIZE = 600;
    //END OF CONSTANTS
    //BEGIN OF THE OBJECTS NEEDED TO BUILD A GUI WINDOW
    private Container content;
    private JFrame jClient = new JFrame();
    private JPanel jOptionPanel = new JPanel();
    private DrawingBoard jDrawingBoard;
    private JButton jOptionPass,jOptionAddTerritory,jOptionRemoveTerritory,jOptionEnd,jOptionAddDeadGroup,jOptionRemoveDeadGroup;
    private Play play;

    private JComboBox<String> jBoardSelect;
    private ButtonListener bonl_ClickListener;
    private Board board;
    private PLACE[][] gameboard;
    private JLabel turn;
    private JLabel color_of_player;
    private boolean ismine = true;
    private boolean onetime = true;
    /**
     * Constructor of the ClientGUI class
     * responsible for Creating GUI, invokes a method createWindow()
     */
    public ClientGUI(Board board,Play play) {
        this.play=play;
        this.board=board;
        bonl_ClickListener = new ButtonListener(play);
        gameboard=board.getGameTable();
        createWindow();
        startDrawing();
    }

    public void showEndDialog(int option){
        if(option == 1){
            JOptionPane.showMessageDialog(content,"Game Over", "Congratulations!\n" +
                    "You have won with ammount of points: "+play.getPlayBoard().getPoints(play.get_player_color()),
                    JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(content,"Game Over", "We are sorry!\n" +
                            "You have lost. With points: "+play.getPlayBoard().getPoints(play.get_player_color()),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Method that shows warning if KO Situation was detected in game
     */
    public void showWarningKO_Situation(){
        JOptionPane.showMessageDialog(content,"Situation KO has been detected," +
                " please make diffrent move!","KO Detected!",JOptionPane.WARNING_MESSAGE);
    }

    public void WarnningMessage(){
        JOptionPane.showMessageDialog(content,"It is not your turn! ","I" +
                "nvaild move detected!",JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Method responsiable for initialize jDrawing Board
     */

    public void startDrawing() {
        jDrawingBoard.startDrawing(gameboard);
    }

    /**
     * Method that modify JLabel, to inform users whom turn it is
     //* @param player takes a color of player
     */
    /*
    play daje do przeciwnika, czyli wywołujemy zawsze u przeciwnika, nie zmieniamy u siebie

     */
    public void setTurn(boolean active){
        if(active == true){
            turn.setText("Your Turn! PLAYER: "+play.get_player_color());
        }
        else if(active == false){
            turn.setText("Turn of Enemy! PLAYER: "+play.get_player_color());
        }
        if(!onetime){
            turn.paintImmediately(turn.getVisibleRect());
        }
        onetime = false;
    }
    /**
     * Method responsiable for creating window
     *
     */
    public DrawingBoard getDrawingBoard()
    {
        return jDrawingBoard;
    }
    private void createWindow(){
        //jClient.setContentPane(new JLabel(new ImageIcon("C:\\Users\\cp24\\IdeaProjects\\Game\\src\\GoGraphics\\addgraphics.jpg")));
        //InputStream imageInputStream = jClient.getClass().getResourceAsStream("/GoGraphics/black_button.png");
        //BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        //image = imageResizer.scale(bufferedImage,getWidth(),getHeight());
        content = jClient.getContentPane();
        content.setLayout(null);
        //set JFrame look
        jClient.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jClient.setTitle("Go Game");
        jClient.setResizable(false);
        jClient.setSize(X_WINDOW_SIZE,Y_WINDOW_SIZE);
        setIconImage(jClient);
        //set components
        setStartButtons();
        setjOptionPanel();
        setjDrawPanel();
        setJLabel();
        addjOptionPanelButtons();
        //add components to GUI of User
        content.add(turn);
        content.add(jOptionStart);
        content.add(jOptionPanel);
        content.add(jDrawingBoard);
        content.add(jBoardSelect);
        jClient.setLocationRelativeTo(null);
        jClient.setVisible(true);

    }
    private void setJLabel(){
        turn = new JLabel("Waiting for enemy");
        turn.setBounds(120,10,250,50);
        turn.setBackground(new Color(249, 224, 75));
        turn.setForeground(Color.BLACK);
        turn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
    }

    private static void setIconImage(JFrame window){
        try{
            InputStream imageInputStream = window.getClass().getResourceAsStream("/GoGraphics/go_icon.png");
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            window.setIconImage(bufferedImage);
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
    /**
     * Method which add Start game button and list of playable games
     */

    private void setStartButtons() {
        String[] boards = new String[]{"19x19","9x9","7x7"};
        jBoardSelect = new JComboBox<>(boards);
        jBoardSelect.setBounds(10,90,100,30);
        jOptionStart = new JButton("Start");
        setButtonLook(jOptionStart,"Start");
        jOptionStart.setBounds(10,50,100,30);

    }
    /**
     * Method that sets DrawingBoard attached to GUI of the
     * client
     */

    private void setjDrawPanel() {
        int CenterX,CenterY;
        CenterX = (jClient.getSize().width- X_OF_DRAWPANEL)/2;
        CenterY = (Y_OF_DRAWPANEL- jClient.getWidth())/2;

        jDrawingBoard = new DrawingBoard(board,play);
        System.out.println(CenterX);
        jDrawingBoard.setBounds(CenterX + 40,50,X_OF_DRAWPANEL,Y_OF_DRAWPANEL);
    }
    /**
     * Method which add Buttons to JPanel which is added to GUI
     * of the client
     */

    private void addjOptionPanelButtons(){
        setJOptionButtonsNames();
        content.add(jOptionAddDeadGroup);
        content.add(jOptionRemoveDeadGroup);
        content.add(jOptionAddTerritory);
        content.add(jOptionRemoveTerritory);
        jOptionPanel.add(jOptionPass);
        jOptionPanel.add(jOptionEnd);
    }
    /**
     * Method which configure settings of JPanel attached to
     * the GUI of the client
     */
    private void setjOptionPanel(){
        jOptionPanel.setLayout(null);
        jOptionPanel.setBounds(0,530,JPANEL_WIDTH,JPANEL_HEIGHT);
    }

    /**
     * Method which sets default name of buttons added to
     * JPanel, also alocate their's position on JPanel
     */
    private void setJOptionButtonsNames(){
        int height = jOptionPanel.getHeight();
        int h1=30;

        jOptionPass = new JButton("Pass");
        setButtonLook(jOptionPass, "Pass");
        jOptionPass.setBounds(10, height - 40, 110,30);

        jOptionAddTerritory = new JButton("AddTerritory");
        setButtonLook(jOptionAddTerritory, "AddTerritory");
        jOptionAddTerritory.setBounds(10, 40, 110,30);

        jOptionRemoveTerritory = new JButton("RemoveTerritory");
        setButtonLook(jOptionRemoveTerritory, "RemoveTerritory");
        jOptionRemoveTerritory.setBounds(10,4*h1 , 110,30);

        jOptionAddDeadGroup = new JButton("AddDeadGroup");
        setButtonLook(jOptionAddDeadGroup, "AddDeadGroup");
        jOptionAddDeadGroup.setBounds(10, 6*h1, 110,30);

        jOptionRemoveDeadGroup = new JButton("RemoveDeadGroup");
        setButtonLook(jOptionRemoveDeadGroup, "RemoveDeadGroup");
        jOptionRemoveDeadGroup.setBounds(130, 8*h1, 110,30);

        jOptionEnd = new JButton("GiveUp");
        setButtonLook(jOptionEnd, "GiveUp");
        jOptionEnd.setBounds(250, height - 40, 110,30);
    }

    /**
     * Method which sets the look of JButtons attached
     * to JPanel, including BackGround, Foreground, Font
     */

    private void setButtonLook(JButton button, String name){
        button.setName(name);
        button.setBackground(new Color(249, 224, 75));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        button.addActionListener(bonl_ClickListener);

    }
}
