/**
 * 
 */
package linker;
import java.util.*;

import message.Msg;

import java.io.*;
/**
 * @author franv
 *
 */
public class Linker {
    PrintWriter[] dataOut;
    BufferedReader[] dataIn;
    BufferedReader dIn;
    int myId, N;
    Connector connector;
    public IntLinkedList neighbors = new IntLinkedList();
    public Linker(String basename, int id, int numProc) throws Exception {
        myId = id;
        N = numProc;
        dataIn = new BufferedReader[numProc];
        dataOut = new PrintWriter[numProc];
        Topology.readNeighbors(myId, N, neighbors);
        connector = new Connector();
        connector.Connect(basename, myId, numProc, dataIn, dataOut);
    }
    public void sendMsg(int destId, String tag, String msg, String title) {     
        dataOut[destId].println(myId + " " + destId + " " + 
				      tag + " " + title + " " + msg + "#");
        dataOut[destId].flush();
    }
    public void sendMsg(int destId, String tag, String title) {
        sendMsg(destId, tag, " 0 ", title);
    }
    public void multicast(IntLinkedList destIds, String tag, String msg){
        for (int i=0; i<destIds.size(); i++) {
            sendMsg(destIds.getEntry(i), tag, msg);
        }
    }
    public Msg receiveMsg(int fromId) throws IOException  {        
        String getline = dataIn[fromId].readLine();
        Util.println(" received message " + getline);
        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String title = st.nextToken();
        String msg = st.nextToken("#");
        return new Msg(srcId, destId, tag, msg, title);        
    }
    public int getMyId() { return myId; }
    public int getNumProc() { return N; }
    public void close() {connector.closeSockets();}
}
