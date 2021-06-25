package app;

import linker.IntLinkedList;
import linker.Linker;
import linker.Process;
import message.Msg;

public class CentMutex extends Process {
    boolean haveToken;
    final int leader = 0;
    IntLinkedList pendingQ = new IntLinkedList();
    public CentMutex( Linker initComm ){
        super( initComm );
        haveToken = ( myId == leader );
    }
    public synchronized void requestCS(){
        sendMsg( leader, "request" );
        while( !haveToken ) myWait();
    }
    public synchronized void releaseCS(){
        sendMsg( leader, "release" );
        haveToken = false;
    }
    public synchronized void handleMsg( Msg m, int src, String tag ){
        if( tag.equals( "request" ) ){
            if( haveToken ){
                sendMsg( src, "okay" );
                haveToken = false;
            }
            else pendingQ.add( src );
        }
        else if( tag.equals( "release" ) ){
            if( !pendingQ.isEmpty() ){
                int pid = pendingQ.removeHead();
                sendMsg( pid, "okay" );
            }
            else haveToken = true;
        }
        else if( tag.equals( "okay" ) ){
            haveToken = true;
            notify();
        }
    }
}
