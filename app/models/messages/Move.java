package models.messages;

public class Move {
	final int X;
	//final int Y;
	//final int[] position;
	final String name;


	public int getPosition() {
		return X;
	}

	public int getX(){
		return X;
	}
	//public int getY(){
		//return Y;
	//}
	
	public String getName() {
		return name;
	}
	
	public Move(int X/*,int Y*/, String name) {
	    this.X = X;
	    //this.Y = Y;
	    //this.position= new int[2];
	    //this.position[1] = X;
	    //this.position[2] = Y;
	    this.name = name;
	
	}
}
