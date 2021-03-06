/**
 * 
 */
package camera;
import java.util.*;

import linker.Linker;
import linker.Process;
import message.Msg;

/**
 * Chandy Lamport algorithm.
 * @author franv
 *
 */
public class RecvCamera  extends Process implements Camera {
	/**
	 * State of process can be red(1) or white(0).
	 */
    static final int white = 0, red = 1;
    /**
     * Local process collor. Initially it is white.
     */
    int myColor = white;
    /**
     * Used for determining when to stop tracking message via k-th channel.
     */
    boolean closed[];
    /**
     * Object representing app for snapshot.
     */
    CamUser app;
    /**
     * Tracks  state of k-th input channel.
     */
    LinkedList chan[] = null;
    /**
     * Value for global function
     */
    String myValue;
   
    
    /**
     * Initially all channels are open and empty.
     * @param initComm
     * @param app
     */
    public RecvCamera(Linker initComm, CamUser app ) {
        super(initComm);
        closed = new boolean[N];
        chan = new LinkedList[N];
        myValue = "";
        for (int i = 0; i < N; i++)
        	//	Checks for existence of channel form process i to local process j.
            if (isNeighbor(i)) {
                closed[i] = false;
                chan[i] = new LinkedList();
            } else closed[i] = true;
        this.app = app;
    }
    /**
     * Changes  color of process to red, marks its local state and
     * sends marker message to all output channels.
     */
    public synchronized void globalState() {
        myColor = red;
        myValue = app.localState(); // record local State;
        sendToNeighbors("marker", myId);  // send Markers
    }
    /**
     * Handles received message marker.
     * If process is white it turns it red.
     * Closes required channel.
     * Prints out everything in channel if it is marked its local state and input channels.
     */
    public synchronized void handleMsg(Msg m, int src, String tag) {
        if (tag.equals("marker")) {
            if (myColor == white) globalState();
            closed[src] = true;
            String newLine = System.getProperty("line.separator");
            if (isDone()){
                myValue = myValue.concat("Channel State: Transit Messages ");
                for (int i = 0; i < N; i++)
                    if (isNeighbor(i))
                        while (!chan[i].isEmpty())
                            myValue = myValue.concat( "||" +
                            ( chan[i].removeFirst()).toString());
                //System.out.println( "---------------------" + newLine + myValue + newLine + "-------------------------");
            }
        }
        else if( tag.equals( "matrix" ) )
            app.handleMsg( m, src, tag );
        else { // application message
            if ((myColor == red) && (!closed[src]))
                chan[src].add(m);
            app.handleMsg(m, src, tag); // give it to app
        }
    }
    //public synchronized handleMsg()
    /**
     * Determines if process has marked its local state and all input channels,
     * if it has returns true else false.
     * @return true if it has marked its state and all input channels otherwise false
     */
    boolean isDone() {
        if (myColor == white) return false;
        for (int i = 0; i < N; i++)
            if (!closed[i]) return false;
        return true;
    }
    /**
     * Get final local value
     */
    public synchronized String getMyValue(){
        return myValue;
    }
    public synchronized boolean isFinished(){
        return true;
    }
}

