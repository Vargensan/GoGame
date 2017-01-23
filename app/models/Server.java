package models;

import models.messages.Join;
import play.mvc.*;
import play.libs.*;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.*;
import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.*;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/**
 * Created by Bartłomiej Woś on 2017-01-21.
 */
public class Server extends UntypedActor{

    static int players=0;
    static ActorRef TempGameTable = null;

    //Server Class
    static ActorRef actorRefTable = Akka.system().actorOf(Props.create(Server.class));

    //Map of Game Tables
    static Map<String, ActorRef> members = new HashMap<String, ActorRef>();

    //Hold GameTable for player 2, when it connects with server
    public static void SaveGameTable(ActorRef PGameTable){
        TempGameTable = PGameTable;
    }


    //Pair Two Actors in One Game Table - Messages will be recived between them
    //Actually Creates one actor and set it to GameTable
    public static void join(final String name, WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out ) throws Exception{

        //Message Join to server
        String result = (String) Await.result(ask(actorRefTable, new Join(name), 1000), Duration.create(1, SECONDS));
        if("OK".equals(result)){
            if(players==0){
                ActorRef PlayersGameTable;
                PlayersGameTable = Akka.system().actorOf(Props.create(GameTable.class));
                SaveGameTable(PlayersGameTable);
                players=1;
                ActorRef player = Akka.system().actorOf(Props.create(Human.class,name,in,out,PlayersGameTable));
                //Hold Games in Server
                members.put(name,PlayersGameTable);
                System.out.println("OK");
            }else{
                players=0;
                //Add next player to same PlayersGameTable as Before
                ActorRef player = Akka.system().actorOf(Props.create(Human.class,name,in,out,TempGameTable));
                System.out.println("OK2");

            }
        }
    }

    //Actually Server will only respond for Join Message sent by it self to it self
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Join) {

            Join join = (Join) message;

            if(false){
                // if username is already taken do sth
            }
            else {
                getSender().tell("OK", getSelf());
            }
        }
    }
}
