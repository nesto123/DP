package camera;

import app.GlobalFunc;
import detector.TermDetector;
import linker.Linker;
import message.Msg;

import java.util.LinkedList;

import app.CentMutex;
import app.FileSystem;

public class TermRecvCamera extends RecvCamera{
    TermDetector td = null;
    boolean finished = false;
    CentMutex lock;
    int D, temp;
    int version = 0;
    public TermRecvCamera( Linker initComm, CamUser app, GlobalFunc func, TermDetector td, int num ){
        super( initComm, app, func);
        this.td = td;
        lock = new CentMutex( initComm );
        D = num;
        temp = num;
    }
    public synchronized void globalState(){
        super.globalState();
        td.initiate();
    }
    boolean isDone(){
        return super.isDone();
    }
    public synchronized void handleMsg( Msg m, int src, String tag ){
        super.handleMsg( m, src, tag );
        if( tag.equals( "marker" ) || tag.equals( "signal" ) ){
            td.handleMsg( m, src, tag );
            td.turnPassive();
            if( td.isDone() == true && myId == 0 )
                broadcastMsg( "done", 0 );
        }
        else if( tag.equals( "done" ) ){
            td.setFinished();
            //lock.requestCS();
            sendMsg( 0, "local", myValue );
            //lock.releaseCS();
            finish();
        }
        else if( tag.equals( "local" ) ){
            myValue = myValue.concat( "||" + m.getTitle() + m.getMessage().substring( 0, m.getMessage().length() - 2 ) );
            D = D - 1;
            if( D == 1 ){
                String newLine = System.getProperty("line.separator");
                myValue = myValue.replace( "||", newLine );
                System.out.println( myValue );
                FileSystem files = new FileSystem( "Snapshots" );
                version++;
                files.createFile( "snapshot " + version , myValue );
                finish();
            }
        }
        else if( tag.equals( "request" ) || tag.equals( "release" ) || tag.equals( "okay" ) )
            lock.handleMsg( m, src, tag );
    }
    public synchronized void sendMsg( int dest, String tag, int msg ){
        super.sendMsg( dest, tag, msg );
        if( tag.equals( "marker" ) || tag.equals( "signal" ) )
            td.sendAction();
    }
    public synchronized boolean isFinished(){
        return td.isDone();
    }
    public synchronized void waitForDone(){
        while( !finished ) myWait();
    }
    public void finish(){
        myValue = "";
        myColor = white;
        D = temp;
        for (int i = 0; i < N; i++)
            if (isNeighbor(i)) {
                closed[i] = false;
                chan[i] = new LinkedList();
            } else closed[i] = true;
    }
}
