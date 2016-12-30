package com.GO;

import java.util.Random;

/**
 * Created by cp24 on 2016-12-23.
 */
public class BotClass {
    public static final Random generator = new Random();
    int x,y;
    PLACE i_want_add;
    Play play;
    boolean canAddHere;

    public BotClass(Play play){
        this.play = play;
        canAddHere = false;
    }
    //Reagowanie na sygnał pass -> automatyczny pass
    //...
    //Reagowanie na sygnał confirm -> auto confirm
    //...
    //Reagowanie na terytorium -> brak terrytorium
    //...
    //AUTO WIN :D
    public void sendPass(){
        play.passGame();
    }

    public void sendConfirm(){

    }

    public void sendNoTerritory(){

    }

    private int setSize(){
        return play.getPlayBoard().getSize();
    }

    public void checkVaildMove(){
        do {
            x = generator.nextInt(setSize());
            y = generator.nextInt(setSize());
            canAddHere = play.getPlayBoard().canAddHere(play.get_player_color(), x, y);
        } while (!canAddHere);
        play.getPlayBoard().addStone(play.get_player_color(),x,y);
    }

}
