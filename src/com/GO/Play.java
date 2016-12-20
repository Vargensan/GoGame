package com.GO;

import GUIGo.BoardOnClickListener;
import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * Kinga Krata 2016-12-05.
 */
public class Play {

    private Board playBoard;
    private Client clientSocket;
    private ClientGUI window;
    private PLAYER player_color;
    private BoardOnClickListener clickListener;
    

    Play()
    {


         clientSocket=new Client();
         clientSocket.listenSocket();
         String color="";

         try {
              color=clientSocket.in.readLine();

         }
         catch(IOException ex)
         {
            System.out.println("Problem z polaczeniem");
            exit(-1);
         }
         if(color.substring(0,1).equals("b"))
        {
            player_color=PLAYER.BLACK;
        }
        else
        {
            player_color=PLAYER.WHITE;
        }
        clientSocket.out.println("ok");
        playBoard=new Board(19,this);

         window=new ClientGUI(playBoard,this);
         this.clickListener=window.getDrawingBoard().getBoardOnClickListener();
         if(player_color==PLAYER.BLACK)
         {
             startBlackPlayer();
         }


    }
    public void startBlackPlayer()
    {
        int x=0,y=0;


        try {
            if(clientSocket.in.readLine().substring(0,1).equals("p"))
            {
                x=Integer.parseInt(clientSocket.in.readLine().substring(1));
                y=Integer.parseInt(clientSocket.in.readLine().substring(1));
            }

        }
        catch(NumberFormatException ex)
        {

        }
        catch(IOException ex)
        {

        }
        playBoard.addStone(player_color.getEnemyColor(),x,y);
        window.getDrawingBoard().update();

    }

    public void game(int x, int y)
    {
         clientSocket.out.println("p");

        clientSocket.out.println("x"+x);
        clientSocket.out.println("y"+y);


            try {
                if(clientSocket.in.readLine().substring(0,1).equals("p"))
                {
                    x=Integer.parseInt(clientSocket.in.readLine().substring(1));
                    System.out.println(x);
                    y=Integer.parseInt(clientSocket.in.readLine().substring(1));
                    System.out.println(y);
                }

            }
            catch(NumberFormatException ex)
            {

            }
            catch(IOException ex)
            {

            }
            /*Metoda nam "poprawnie" oblicza obiekty do kasacji
            Przez poprawnie mam na myśli tak samo dla obu plansz
            Pytanie teraz dlaczego?
            Bo nie przekazujemy punktów z tabeli-do-usunięcia clienta 1 do clienta 2, więc musimy je obliczyć
            to obliczanie jej silnie powiązane z metodąmi canBreatheHere(), która w dużym uproszczeniu jeżeli
            kamień może oddychać po uduszeniu kamieni przeciwnika, to można go dodać ale trzeba kamienie gracza
            przeciwnego, więc tworzy nam które kamienie przeciwnika trzeba usunąć
             */
            playBoard.canAddHere(player_color.getEnemyColor(),x,y);
            /*
            I po wywołaniu, czy też zdecydowaniu że chcemy postawić ten kamień usuwa wszystkie kamienie przeciwnika
             */
            playBoard.addStone(player_color.getEnemyColor(),x,y);
            /*
            Dlaczego nie działa?
            Najpierw musimy przerysować obrazek - na nowo.
            Metoda paint Immadiately tego nie robi, ona po prostu rysuje linie i kółka i póżniej nie odświeża
            obrazka i dalej rysuje linie na kółkach, gdzie tych kółek już nie ma
            Sposób poradzenia: repaint planszy albo w paint commponent albo wywoływanie metody update

             Update nie działa... może działa i jest lepiej ale nie tak jak powinien

             TODO: Add note 2: trzeba zrobić odświeżanie 2 plansz na raz, metoda poniższa działa prawie poprawnie
             TODO: gdy w paintComponent dodałem g2.drawimage() od obrazka który funkcjonuje jako dodanie tła
             TODO: wtedy zasada działania jest taka jak w update tyle że "na stałe"

             TODO: Ad note 3: zamienić wszystkie update() na paintImmedientely() i poprzez skomplikowanie tej metody
             TODO: nie matrwić się o synchornizacje update() z paintImmedientyly()
            */
            //window.getDrawingBoard().update();
            window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
            //tutaj chcę żeby wyświetlało na JFrame, czyja tura
            //coś nie działa xd
            window.setTurn(player_color.getEnemyColor(),true);


    }

    /**
     * Method that is responsiable for getting play board
     * @return play board
     */
    public Board getPlayBoard()
    {
        return playBoard;
    }

    /**
     * Getter for player color
     * @return color of player
     */
    public PLAYER get_player_color()
    {
        return player_color;
    }




}
