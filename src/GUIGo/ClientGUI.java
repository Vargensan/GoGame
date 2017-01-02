package GUIGo;

import com.GO.Board;
import com.GO.PLACE;
import com.GO.PLAYER;
import com.GO.Play;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private JPanel GeneralWoodenPicture;
    private DrawingBoard jDrawingBoard;
    private JButton jOptionPass,jOptionAddTerritory,jOptionRemoveTerritory,jOptionEnd,jOptionAddDeadGroup,jOptionRemoveDeadGroup,jOptionStart;
    private Play play;
    private JButton jConfirmSize, jInformations,jSend,jAccept,jReject;

    private JComboBox<String> jBoardSelect;
    private ButtonListener bonl_ClickListener;
    private Board board;
    private PLACE[][] gameboard;
    private JLabel turn;
    private JLabel color_of_player;
    private boolean ismine = true;
    private boolean onetime = true;
    //Boolean holding the confirmation of the size by user
    private boolean confirmSize = false;
    //
    private int prefferedsize;
    //
    BufferedImage background;
    //
    /**
     * Constructor of the ClientGUI class
     * responsible for Creating GUI, invokes a method createWindow()
     */
    public ClientGUI() {
        prefferedsize = 19;
    }

    /**
     * Method responsiable for initialize Client GUI and also invokes construction of other classes - not directly
     * @param play takes a object with general methods of playing
     */

    public void initialize(Play play){
        //this.board = board;
        this.play = play;
        bonl_ClickListener = new ButtonListener(play);
        //gameboard = board.getGameTable();
        createWindow();
        //createDrawingBoard();
        //startDrawing();

    }

    public void createDrawingBoard(Board board){
        this.board = board;
        gameboard = board.getGameTable();
        setjDrawPanel();
        content.add(jDrawingBoard);
        content.repaint();
        startDrawing();
    }

    /**
     * Setting Boolean to true which holds confirmation of the size
     */
    public void confirmSize(){
        confirmSize = true;
        removeButton(jConfirmSize);
        jConfirmSize.repaint();
    }
    public boolean getConfirmationofSize(){
        return confirmSize;
    }

    /**
     * Method that remove button form Content
     * @param abc takes the button which should be removed
     */
    public void removeButton(JButton abc){
        content.remove(abc);
    }

    /**
     * Getter for user-confirmed size of gameboard
     * @return size of gameboard
     */

    public int getSizeOfPlayBoard(){
        return prefferedsize;
    }

    /**
     * Method that invokes dialog at the end of the game, show, who win and also who lose
     * @param option winner(1)/looser(else)
     */
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

    /**
     * Method that shows warning that it is not turn of player
     */
    public void WarnningMessage(){
        JOptionPane.showMessageDialog(content,"It is not your turn! ","I" +
                "nvaild move detected!",JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Method thats shows the warning, that the move which player made is incorrect
     */
    public void InvaildMove(){
        JOptionPane.showMessageDialog(content,"You cant place a stone on non-empty place! ","I" +
                "nvaild move detected!",JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Method thats shows the warning, that the move which player made is incorrect
     */
    public void InvalidMove_isNotEmpty(){
        JOptionPane.showMessageDialog(content,"You cant place stone here, no breaths detected!","" +
                "Invalid moce detected!",JOptionPane.WARNING_MESSAGE);
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
            ismine = true;
            turn.setText("Your Turn!");
        }
        else if(active == false){
            ismine = false;
            //turn.setText("Turn of Enemy! PLAYER: "+play.get_player_color());
            turn.setText("Enemy Turn!");
        }
        if(!onetime){
            turn.paintImmediately(turn.getVisibleRect());
        }
        onetime = false;
    }

    /**
     * Getter for turn of player
     * @return turn of player yes/no
     */

    public boolean getTurn(){
        return ismine;
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
        setBackground();
        jClient.setLocationRelativeTo(null);
        content = jClient.getContentPane();
        //content.setPreferredSize(jClient.getPreferredSize());

        content.setLayout(null);
        setIconImage(jClient);
        //set components
        setStartButtons();
        setjOptionPanel();
        //setjDrawPanel();
        setJLabel();
        addjOptionPanelButtons();
        //add components to GUI of User
        content.add(turn);
        //content.add(color_of_player);
        content.add(jOptionStart);
        content.add(jOptionPanel);
        //content.add(jDrawingBoard);
        content.add(jBoardSelect);
        //jClient.setLocationRelativeTo(null);
        jClient.setVisible(true);

    }
    private void setBackground(){
        InputStream app = getClass().getResourceAsStream("/GoGraphics/BackGroundTable2.jpg");
        ImageResize resizer = new ImageResize();
        BufferedImage buff = null;
        try {
            buff = ImageIO.read(app);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        Dimension dim_jframe = new Dimension(X_WINDOW_SIZE,Y_WINDOW_SIZE);

        background = resizer.scale(buff,X_WINDOW_SIZE,Y_WINDOW_SIZE);
        jClient.setContentPane(new JLabel(new ImageIcon(buff)));
        jClient.getContentPane().setPreferredSize(dim_jframe);
        jClient.pack();
        System.out.println("JFrame: "+ jClient.getContentPane().getWidth()+ " "+jClient.getContentPane().getHeight());
        jClient.setSize(jClient.getContentPane().getWidth(),jClient.getContentPane().getHeight());
        System.out.println("JFrame2: "+ jClient.getWidth()+ " "+jClient.getHeight());
        jClient.setResizable(false);
        jClient.setTitle("Go Game");
        jClient.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //jClient.setSize(jClient.getContentPane().getWidth(),jClient.getContentPane().getHeight());


    }
    private void setJLabel(){
        turn = new JLabel("Waiting for enemy");
        turn.setBounds(300,0,200,50);
        turn.setBackground(new Color(249, 224, 75));
        //turn.setOpaque(true);
        turn.setForeground(Color.ORANGE);
        turn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
    }
    public void setJPlayerColor(String abc){
        color_of_player = new JLabel(abc);
        color_of_player.setBounds(0,0,200,40);
        color_of_player.setBackground(new Color(249, 224, 75));
        //turn.setOpaque(true);
        color_of_player.setForeground(new Color(249, 224, 75));
        color_of_player.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        content.add(color_of_player);
        color_of_player.repaint();
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
        jBoardSelect.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       JComboBox combo = (JComboBox) e.getSource();
                       String current = (String) combo.getSelectedItem();
                       if(current.equals("19x19")){
                           prefferedsize = 19;
                       } else if(current.equals("9x9")){
                           prefferedsize = 9;
                       } else{
                           prefferedsize = 7;
                       }
                    }
                }
        );
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
        jDrawingBoard.setBounds(CenterX + 60,50,X_OF_DRAWPANEL,Y_OF_DRAWPANEL);
    }
    /**
     * Method which add Buttons to JPanel which is added to GUI
     * of the client
     */

    private void addjOptionPanelButtons(){
        setJOptionButtonsNames();
        //content.add(jOptionAddDeadGroup);
        //content.add(jOptionRemoveDeadGroup);
        //content.add(jOptionAddTerritory);
        //content.add(jOptionRemoveTerritory);
        content.add(jConfirmSize);
        content.add(jInformations);
        //for acepting and sending information about Territory and Dead Groups
        //content.add(jSend);
        //content.add(jReject);
        //content.add(jAccept);
        //---------------------------------------------------------------------
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
        jOptionPanel.setOpaque(false);
    }

    /**
     * Method that shows Send and Accept Buttons for marking dead
     * groups and Territory
     */
    public void showSendandAccept(){
        showButtonandRepaint(jSend);
        showButtonandRepaint(jAccept);
        showButtonandRepaint(jReject);
    }

    /**
     * Method that hide Send and Accept Buttons for marking dead groups
     * and Territory
     */
    public void hideSendandAccept(){
        hideButtonandRepaint(jSend);
        hideButtonandRepaint(jAccept);
        hideButtonandRepaint(jReject);
    }

    public void showTerritoryMarking(){
        showButtonandRepaint(jOptionAddTerritory);
        showButtonandRepaint(jOptionRemoveTerritory);
    }

    public void showMarkAsDead(){
        showButtonandRepaint(jOptionAddDeadGroup);
        showButtonandRepaint(jOptionRemoveDeadGroup);
    }

    public void hideMarkAsDead(){
        hideButtonandRepaint(jOptionAddDeadGroup);
        hideButtonandRepaint(jOptionRemoveDeadGroup);
    }

    /**
     * Method which sets default name of buttons added to
     * JPanel, also alocate their's position on JPanel
     */
    private void setJOptionButtonsNames(){
        int height = jOptionPanel.getHeight();
        int b_height=30;
        int b_width=110;
        int start_height=70;
        int send_w = 100;
        int send_h=30;

        jOptionPass = new JButton("Pass");
        setButtonLook(jOptionPass, "Pass");
        jOptionPass.setBounds(10, height - 40, b_width,b_height);

        jOptionAddTerritory = new JButton("Mark Territory");
        setButtonLook(jOptionAddTerritory, "Mark Territory");
        jOptionAddTerritory.setBounds(5, start_height+2*b_height, b_width+20,b_height);

        jOptionRemoveTerritory = new JButton("Unmark Territory");
        setButtonLook(jOptionRemoveTerritory, "Unmark Territory");
        jOptionRemoveTerritory.setBounds(5,start_height+4*b_height , b_width+20,b_height);

        jOptionAddDeadGroup = new JButton("Mark As Dead");
        setButtonLook(jOptionAddDeadGroup, "Mark As Dead");
        jOptionAddDeadGroup.setBounds(5, start_height+6*b_height, b_width+20,b_height);

        jOptionRemoveDeadGroup = new JButton("Unmark Dead");
        setButtonLook(jOptionRemoveDeadGroup, "Unmark Dead");
        jOptionRemoveDeadGroup.setBounds(5, start_height+8*b_height, b_width+20,b_height);

        jOptionEnd = new JButton("Give Up");
        setButtonLook(jOptionEnd, "Give Up");
        jOptionEnd.setBounds(150, height - 40, b_width,b_height);

        jInformations = new JButton("Game Info");
        setButtonLook(jInformations,"Game Info");
        jInformations.setBounds(X_WINDOW_SIZE-b_width+10,Y_WINDOW_SIZE-2*b_height,b_width-20,b_height);

        jConfirmSize = new JButton("Confirm Size");
        setButtonLook(jConfirmSize,"Confirm Size");
        jConfirmSize.setBounds(5,0,b_width,b_height);

        //Setting look for sending and Rejecting
        jSend = new JButton("Send");
        setButtonLookTransfer(jSend,"Send");
        jSend.setBounds(260,Y_WINDOW_SIZE-3*send_h-10,send_w,send_h);

        jAccept = new JButton("Accept");
        setButtonLookTransfer(jAccept,"Accept");
        jAccept.setBounds(370,Y_WINDOW_SIZE-3*send_h-10,send_w,send_h);

        jReject = new JButton("Reject");
        setButtonLookTransfer(jReject,"Reject");
        jReject.setBounds(480,Y_WINDOW_SIZE-3*send_h-10,send_w,send_h);
        //
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
    private void setButtonLookTransfer(JButton button, String name){
        button.setName(name);
        button.setBackground(new Color(172,21,21));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Times New Roman",Font.PLAIN,12));
        button.addActionListener(bonl_ClickListener);
    }

    private void showButtonandRepaint(JButton button){
        content.add(button);
        //button.repaint();
        button.paintImmediately(button.getVisibleRect());
    }
    private void hideButtonandRepaint(JButton button){
        content.remove(button);
        button.repaint();
        button.paintImmediately(button.getVisibleRect());
    }
    public void repaintFrame(){
    }
}
