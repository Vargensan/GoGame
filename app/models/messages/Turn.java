package models.messages;

public class Turn {

    final String status;

    public Turn(String status){
        this.status=status;
    }

    public String getTurn(){
        return status;
    }


}
