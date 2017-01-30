package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
    //Okno Startowe -> Czyli w sumie nasza plansza
    //Click Start -> Czyli w sumie stworzenie nowego gniazda
    //          TWORZENIE NOWEGO GNIAZDA
    // JAVA SCRIPT -> ON CLICK BUTTON -> FORM ACTION Application new Websocket -> Server.join()
    // -> create Client -> create Normal Socket -> wait for connection with server -> connection made?
    // Then create Thread.
    // Thread should hold reference to Websocket....
    // So it can communicate with each other...
    // we need to know the Websocket first and Websocket Secound?
    // No That because they are connected by normal socket, "Client " will communicate with other one by
    // sending some messages
    // KK DONE.
    //Odpowiadanie na wiadomości na starej bazie
    //Just play send messages can add here add here etc. Click on Board send also some moves
    //
    //Komunikacja przeglądaki z Clientem na nowej bazie (Websocket, Java Script, Scala, PlayFramework)
    //          ^^^^^IMPLEMENTACJA W PLAY
    //Zmiana metod Play, Board On Click Listener, Button Listener -> Odpowiednie in,out na Websocket
    //          ^^^^^IMPLEMENTACJA W PLAY
    //          KLASA PLAY
    //RECIVE FROM SERVER -> IF ACTION IS TO DRAW THEN PACK OBJECT IN JSON AND SEND TO JAVA SCRIPT MESSAGE
    //TO DRAW IT
    // Generalny Schemat [Request(Draw,Pass,Accept][WHO(Player1,Player2)]*[WHAT(X,Y)]*
    // * - Optional
    //Przechwytywanie Wiadomości na obiektach JSON
    //      function CallBack JSON
    //Wysyłanie Wiadomośdci na obiektach JSON

    public static Result index() {
        return ok(index.render());
    }

    public static Result goGame(String username) {
        if(username == null || username.trim().equals("")) {
            flash("error", "Please choose a valid username.");
            return redirect(routes.Application.index());
        }
        return ok(goGame.render(username));
    }


    public static Result goGameJs(String username) {
        return ok(views.js.goGame.render(username));
    }

    //Implement Where to put connection with websocket

    public static WebSocket<JsonNode> goSocketCreator(final String username){

        return new WebSocket<JsonNode>(){

            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
                try{
                    Server.join(username,in,out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }

}
