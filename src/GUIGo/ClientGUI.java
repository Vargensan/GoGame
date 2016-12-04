package GUIGo;

import com.GO.PLACE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public class ClientGUI extends JFrame implements ActionListener{

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
    protected DrawingBoard jDrawingBoard;
    private JButton jOptionPass,jOptionTerritory,jOptionEnd;
    private JButton jStartGame;
    private JComboBox<String> jBoardSelect;
    //END OF THE OBJECTS NEEDED TO BUILD A GUI WINDOW

    /**
     * Constructor of the ClientGUI class
     * responsible for Creating GUI, invokes a method createWindow()
     */
    public ClientGUI(){
        createWindow();

    }

    public void startDrawing(PLACE[][] board){
        jDrawingBoard.startDrawing(board);
    }

    /*
    * Method responsiable for creating window
    *
     */
    private void createWindow(){
        content = jClient.getContentPane();
        content.setLayout(null);
        //set JFrame look
        jClient.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jClient.setTitle("Go Game");
        jClient.setResizable(false);
        jClient.setSize(X_WINDOW_SIZE,Y_WINDOW_SIZE);

        //set components
        setStartButtons();
        setjOptionPanel();
        setjDrawPanel();
        addjOptionPanelButtons();
        //add components to GUI of User
        content.add(jOptionPanel);
        content.add(jDrawingBoard);
        content.add(jStartGame);
        content.add(jBoardSelect);
        jClient.setLocationRelativeTo(null);
        jClient.setVisible(true);

    }

    /*
    * Method which add Start game button and list of playable games
     */

    private void setStartButtons() {
        String[] boards = new String[]{"19x19","9x9","7x7"};
        jBoardSelect = new JComboBox<>(boards);
        jBoardSelect.setBounds(10,90,100,30);
        jStartGame = new JButton("Start Game");
        setButtonLook(jStartGame, "Start");
        jStartGame.setBounds(10,50,100,30);
    }
    /*
    * Method that sets DrawingBoard attached to GUI of the
    * client
     */

    private void setjDrawPanel() {
        int CenterX,CenterY;
        CenterX =(int) (jClient.getSize().width- X_OF_DRAWPANEL)/2;
        CenterY =(int) (Y_OF_DRAWPANEL- jClient.getWidth())/2;

        jDrawingBoard = new DrawingBoard();
        System.out.println(CenterX);
        jDrawingBoard.setBounds(CenterX + 40,50,X_OF_DRAWPANEL,Y_OF_DRAWPANEL);
    }
    /*
    * Method which add Buttons to JPanel which is added to GUI
    * of the client
     */

    private void addjOptionPanelButtons(){
        setJOptionButtonsNames();
        jOptionPanel.add(jOptionTerritory);
        jOptionPanel.add(jOptionPass);
        jOptionPanel.add(jOptionEnd);
    }
    /*
    * Method which configure settings of JPanel attached to
    * the GUI of the client
     */
    private void setjOptionPanel(){
        jOptionPanel.setLayout(null);
        jOptionPanel.setBounds(0,530,JPANEL_WIDTH,JPANEL_HEIGHT);
    }

    /*
    * Method which sets default name of buttons added to
    * JPanel, also alocate their's position on JPanel
     */
    private void setJOptionButtonsNames(){
        int height = jOptionPanel.getHeight();
        jOptionPass = new JButton("Pass");
        setButtonLook(jOptionPass, "Pass");
        jOptionPass.setBounds(10, height - 40, 110,30);

        jOptionTerritory = new JButton("Territory");
        setButtonLook(jOptionTerritory, "Territory");
        jOptionTerritory.setBounds(130, height - 40, 110,30);

        jOptionEnd = new JButton("End");
        setButtonLook(jOptionEnd, "End Game");
        jOptionEnd.setBounds(250, height - 40, 110,30);
    }

    /*
    * Method which sets the look of JButtons attached
    * to JPanel, including BackGround, Foreground, Font
     */

    private void setButtonLook(JButton button, String name){
        button.setName(name);
        button.setBackground(new Color(249, 224, 75));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
