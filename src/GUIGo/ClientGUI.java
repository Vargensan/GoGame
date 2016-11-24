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

    private final int X_OF_DRAWPANEL = 450;
    private final int Y_OF_DRAWPANEL = 450;
    private final int JPANEL_HEIGHT = 40;
    private final int JPANEL_WIDTH = 360;
    private final int X_WINDOW_SIZE = 600;
    private final int Y_WINDOW_SIZE = 600;
    private Container content;
    private JFrame jClient = new JFrame();
    private JPanel jOptionPanel = new JPanel();
    private DrawPanel jDrawPanel;
    private JButton jOptionPass,jOptionTerritory,jOptionEnd;
    private JButton jStartGame;
    private JComboBox<String> jBoardSelect;

    /*
     * Creating GUI
     */
    public ClientGUI(){
        createWindow();
        test();

    }
    private void test(){
        PLACE[][] GameBoard = new PLACE[19][19];
        jDrawPanel.startDrawing(GameBoard);
    }

    private void createWindow(){
        content = jClient.getContentPane();
        content.setLayout(null);
        jClient.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jClient.setTitle("Go Game");
        jClient.setResizable(false);
        jClient.setSize(X_WINDOW_SIZE,Y_WINDOW_SIZE);

        //set components
        setStartButtons();
        setjOptionPanel();
        setjDrawPanel();
        addjOptionPanelButtons();
        //add components
        content.add(jOptionPanel);
        content.add(jDrawPanel);
        content.add(jStartGame);
        content.add(jBoardSelect);
        jClient.setLocationRelativeTo(null);
        jClient.setVisible(true);

    }

    private void setStartButtons() {
        String[] boards = new String[]{"19x19","9x9","7x7"};
        jBoardSelect = new JComboBox<>(boards);
        jBoardSelect.setBounds(10,90,100,30);
        jStartGame = new JButton("Start Game");
        setButtonLook(jStartGame, "Start");
        jStartGame.setBounds(10,50,100,30);
    }

    private void setjDrawPanel() {
        int CenterX,CenterY;
        CenterX =(int) (jClient.getSize().width- X_OF_DRAWPANEL)/2;
        CenterY =(int) (Y_OF_DRAWPANEL- jClient.getWidth())/2;

        jDrawPanel = new DrawPanel();
        System.out.println(CenterX);
        jDrawPanel.setBounds(CenterX + 40,50,X_OF_DRAWPANEL,Y_OF_DRAWPANEL);
    }

    private void addjOptionPanelButtons(){
        setJOptionButtonsNames();
        jOptionPanel.add(jOptionTerritory);
        jOptionPanel.add(jOptionPass);
        jOptionPanel.add(jOptionEnd);
    }
    private void setjOptionPanel(){
        jOptionPanel.setLayout(null);
        jOptionPanel.setBounds(0,530,JPANEL_WIDTH,JPANEL_HEIGHT);
    }
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
