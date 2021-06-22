package app;
import java.util.*;

import linker.Linker;
import linker.Process;
import linker.IntLinkedList;
import message.Msg;

/**
 * 
 * @author franv
 *
 */
public class SpanTree extends Process{
    public int parent = -1; // no parent yet
    public IntLinkedList children = new IntLinkedList();
    int numReports = 0;
    boolean done = false;
    Linker inComm;
    public SpanTree(Linker initComm, boolean isRoot) {
        super(initComm);
        inComm = initComm;
        if (isRoot) {
            parent = myId;
	    /*if (initComm.neighbors.size() == 0) 
		done = true;
        else 
		    sendToNeighbors( "invite", myId);*/
        }
    }
    public void makeTree(){
	    if (inComm.neighbors.size() == 0) 
		    done = true;
        else 
		    sendToNeighbors( "invite", myId);
    }
    public synchronized void waitForDone() { // block till children known
	while (!done) myWait();
    }
    public synchronized void handleMsg(Msg m, int src, String tag) {
        if (tag.equals("invite")) {
            System.out.println( "::Jesmo tu?::" );
            if (parent == -1) {
            	numReports++;
                parent = src;
                sendMsg(src, "accept");
                for (int i = 0; i < N; i++)
                    if ((i != myId) && (i != src) && isNeighbor(i))
                        sendMsg(i, "invite");
            } else
                sendMsg(src, "reject");
        } else if ((tag.equals("accept")) || (tag.equals("reject"))) {
            if (tag.equals("accept")) children.add(src);
            numReports++;
            if (numReports == inComm.neighbors.size()) {
		done = true;
		notify();
	    }
        }
    }
}
