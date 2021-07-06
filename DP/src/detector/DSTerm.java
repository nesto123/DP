package detector;

import linker.Process;
import linker.Symbols;
import message.Msg;
import linker.Linker;

public class DSTerm extends Process implements TermDetector{
    final static int passive = 0, active = 1;
    int state = passive;
    int D = 0;
    int parent = -1;
    boolean envtFlag;
    boolean finished;
    public DSTerm( Linker initComm ){
        super( initComm );
        envtFlag = ( myId == Symbols.coordinator );
        finished = false;
    }
    public synchronized void initiate(){
        finished = false;
    }
    public synchronized void handleMsg( Msg m, int src, String tag ){
        if( tag.equals( "signal" ) ){
            D = D - 1;
            if( D == 0 ){
                if( envtFlag ){
                    System.out.println( "Termination Detected" );
                    finished = true;
                }
                else if( state == passive ){
                    sendMsg( parent, "signal" );
                    parent = -1;
                }
            }
        }
        else{ // application message
            state = active;
            if( ( parent == -1 ) && !envtFlag )
                parent = src;
            else sendMsg( src, "signal" );
        }
    }
    public synchronized void sendAction(){
        D = D + 1;
    }
    public synchronized void turnPassive(){
        state = passive;
        if( ( D == 0 ) && ( parent != -1 ) ){
            sendMsg( parent, "signal" );
            parent = -1;
        }
    }
    public synchronized boolean isDone(){
        return finished;
    }
    public synchronized void setFinished(){
        finished = true;
    }
}
