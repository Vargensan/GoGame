package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Server;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result gameGo(String username) {
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
