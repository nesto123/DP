/**
 * 
 */
package app;

import java.io.*;
import java.util.*;

import linker.*;
import linker.Process;
import message.*;

/**
 * 
 *          ---- Samo ogledni primjer klasu brišemo na kraju ----
 * @author franv
 *
 */
public class Chat extends Process {
    public Chat(Linker initComm) {
        super(initComm);
    }
    public synchronized void handleMsg(Msg m, int src, String tag){
        if (tag.equals("chat")) {
            System.out.println("Message from " + src +":");
            System.out.println(m.getMessage());
        }
    }
    public String getUserInput(BufferedReader din) throws Exception {
        System.out.println("Type your message in a single line:");
        String chatMsg = din.readLine();
        return chatMsg;
    }
    public IntLinkedList getDest(BufferedReader din) throws Exception {
        System.out.println("Type in destination pids with -1 at end:");
        System.out.println("Only one pid for synch order:");
        IntLinkedList destIds = new IntLinkedList(); //dest for msg
        StringTokenizer st = new StringTokenizer(din.readLine());
        while (st.hasMoreTokens()) {
            int pid = Integer.parseInt(st.nextToken());
            if (pid == -1) break;
            else destIds.add(pid);
        }
        return destIds;
    }
    public static void main(String[] args) throws Exception {
        String baseName = args[0];								//	Ime aplikacije
        int myId = Integer.parseInt(args[1]);					//	Broj tog processa
        int numProc = Integer.parseInt(args[2]);				//	Ukupan broj procesa
        Linker comm = null;
        if (args[3].equals("simple"))							//	FIFO -- ne koristiri 
            comm = new Linker(baseName, myId, numProc);
        else if (args[3].equals("causal"))						//	Kauzalni -- bolje
            comm = new CausalLinker(baseName, myId, numProc);
        //else if (args[3].equals("synch"))
        //    comm = new SynchLinker(baseName, myId, numProc);
        Chat c = new Chat(comm);
        for (int i = 0; i < numProc; i++)
            if (i != myId) (new ListenerThread(i, c)).start();
        BufferedReader din = new BufferedReader(
        new InputStreamReader(System.in));
        while (true) {
            String chatMsg = c.getUserInput(din);
            if (chatMsg.equals("quit")) break;
            IntLinkedList destIds =  c.getDest(din);
            if (args[3].equals("synch"))
                comm.sendMsg(destIds.getEntry(0), "chat", chatMsg);
            else
                comm.multicast(destIds, "chat", chatMsg);
        }
    }
}

