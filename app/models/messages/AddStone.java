package models.messages;

/**
 * Created by cp24 on 2017-01-21.
 */
public class AddStone {
    private final int X;
    private final int Y;
    public AddStone(int X, int Y){
        this.X=X;
        this.Y=Y;
    }

    public int getX(){
        return X;
    }
    public int getY(){
        return Y;
    }
}
