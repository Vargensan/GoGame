package models;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import models.messages.*;

/**
 * Created by cp24 on 2017-01-23.
 */
public class GameTable extends UntypedActor {


    int numberofPlayers;
    ActorRef player_first;
    ActorRef player_sec;


    //What GameTable should do before it starts?
    @Override
    public void preStart() throws Exception {
        numberofPlayers=0;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        //Add Actor Reference to GameTable
        if(message instanceof SenderReference){
            if(numberofPlayers==0){
                player_first=getSender();
                numberofPlayers++;
            }else{
                player_sec=getSender();
                numberofPlayers++;
            }
        }
        else if(message instanceof AskGameFull){
            if(numberofPlayers==1){
                getSender().tell(new GameNotFull(), getSelf());
            } else{
                getSender().tell(new GameFull(), getSelf());
            }
        }

        else if (message instanceof Move) {
            int position = ((Move) message).getPosition();
            String name = ((Move) message).getName();
            getSender().tell(new Ack(), getSelf());
            String text = "move on position    " + position + "  from " + name;
            notifyAll(new Info(text, "Table"));
        } else if (message instanceof Info) {
            String text = ((Info) message).getText();
            String name = ((Info) message).getName();

            notifyAll(new Info(text, name));
        } //else if (message instanceof Quit) {

           /* String name = ((Quit) message).getName();
            members.remove(name);
            String text = "has left the game";
            notifyAll(new Info(text, name));


        } */else {
            unhandled(message);
        }
    }

    private void notifyAll(Object message){
        player_first.tell(message, getSelf());
        player_sec.tell(message, getSelf());
    }

}
