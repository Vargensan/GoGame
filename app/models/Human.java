package models;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.messages.*;
import play.mvc.WebSocket;
import src.com.GO.Play;
import src.com.GO.STATE;
import play.Logger;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;
import akka.actor.*;
/**
 * Created by Bartłomiej Woś on 2017-01-21.
 */
public class Human extends UntypedActor{

    private HumanState humanState;
    private STATE playState;
    //Implement play mechanics for calculate logic from sent moves from javascript
    Play play;

    public final String name;
    //Aktor obsługujący grę
    public final ActorRef GameTable;

    protected WebSocket.In<JsonNode>  in;
    protected WebSocket.Out<JsonNode> out;

    public Human(String _name, WebSocket.In<JsonNode> _in, WebSocket.Out<JsonNode> _out, ActorRef _table)
    {
        this.name=_name;
        this.GameTable=_table;
        this.in = _in;
        this.out = _out;

        IN_OUT_respond();

    }

    private void IN_OUT_respond(){
        in.onMessage(new Callback<JsonNode>()
        {
            @Override
            public void invoke(JsonNode event)
            {
                try
                {

                    int nr = event.get("nr").asInt();
                    getSelf().tell(new Move(nr,name), getSelf() );
                }
                catch (Exception e)
                {
                    Logger.error("invokeError");
                }

            }
        });
        in.onClose(new Callback0()
        {
            @Override
            public void invoke()
            {
                GameTable.tell(new Quit(name), getSelf() );
            }
        });

    }

    //As Actor is run...
    @Override
    public void preStart() throws Exception {
        //Add to Game Table Sender Refernce
        GameTable.tell(new SenderReference(),getSelf());
        //Change state of human to unactive
        humanState=HumanState.UNACTIVE;
        //Change state of playState before Game
        playState = STATE.BEFORE_GAME;
        //Create Logic for Actor
        //----TO IMPLEMENT-----
        //play = new Play();
        //---------------------
    }

    //Actions on Reciving Messages
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Move) {
            int position = ((Move) msg).getPosition();
            String text = "I've got the position " + position + " from WebSocket and send it to Table";
            GameTable.tell(new Info(text, name), getSelf());
            GameTable.tell(new Move(position, name), getSelf());
        } else if (msg instanceof Info) {
            Info info = (Info) msg;
            ObjectNode event = Json.newObject();
            event.put("message", "[ " + info.getName() + " ] : " + info.getText());
           // event.put

            out.write(event);
        } else if (msg instanceof Ack) {

            ObjectNode event = Json.newObject();
            event.put("message", "[ Ack from Table: my move has been accepted ] ");

            out.write(event);
        } else {
            unhandled(msg);
        }
    }

}
