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

    public void checkVaildMove(){
        do {
            x = generator.nextInt(19);
            y = generator.nextInt(19);
            canAddHere = play.getPlayBoard().canAddHere(play.get_player_color(), x, y);
        } while (!canAddHere);
        play.getPlayBoard().addStone(play.get_player_color(),x,y);
    }

}
