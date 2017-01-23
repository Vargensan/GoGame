package src.com.GO;

/**
 * Created by cp24 on 2017-01-08.
 */
public class ListenerThread extends Thread{
    Play play;
    boolean done = false;
    int job;
    public synchronized void setJob(int option){
        job = option;
        setDone();
    }

    public ListenerThread(Play play){
            this.play=play;
    }

    @Override
    public void run() {
        while(true) {
            waitUntilJobToDo();
            synchronized (this) {
                done = false;
            }
            if(job==1) {
                System.out.println("Gonna recive from other!");
                play.recivefromOther();
            }
            else if(job==2) {
                System.out.println("Gonna recive a turn!");
                play.reciveTurn();
            }
            else if(job==0){
                play.inicializeGameWithServer();
            }
            else if(job==3){
                play.recivefromOther();
                play.reciveTurn();
            }
            else if(job==4){
                play.reciveTurn();
                play.recivefromOther();
            }
        }
    }
    public synchronized void waitUntilJobToDo(){
        System.out.println("waitUntilJobToDo");
        while(!done){
            try{
                this.wait();
            } catch(InterruptedException ignore) {

            }
        }
        System.out.println("End Wait Until Job to do");
    }

    public synchronized void setDone(){
        done = true;
        this.notify();
    }
}
