package GUIGo;

import com.GO.PLACE;

/**
 * Created by cp24 on 2016-12-20.
 */
public class Neighboor {

    private PLACE position;
    private boolean status;
    private boolean needtodojobhere;
    private int posX;
    private int posY;

    public Neighboor(){

    }
    public boolean getStatus(){
        return status;
    }

    public int getPosY(){
        return posY;
    }
    public int getPosX(){
        return posX;
    }
    public PLACE getPlaceStatus(){
        return position;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
    public void setPosX(int posX){
        this.posX = posX;
    }
    public void setStatus(boolean done){
        this.status = done;
    }
    public void setPosition(PLACE pos){
        this.position = pos;
    }
}
