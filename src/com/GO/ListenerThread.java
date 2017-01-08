package com.GO;

/**
 * Created by cp24 on 2017-01-08.
 */
public class ListenerThread extends Thread{
    Play play;
    boolean done = false;
    int job;
    public void setJob(int option){
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
            done = false;
            if(job==1){

            }
        }
    }
    public void waitUntilJobToDo(){
        while(!done){
            try{
                this.wait();
            } catch(InterruptedException ignore) {

            }
        }
    }

    public void setDone(){
        done = true;
        this.notifyAll();
    }
}
