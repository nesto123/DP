/**
 * 
 */
package app;


import java.io.*;
import java.util.*;

import linker.*;
import linker.Process;
import message.*;
import camera.*;
import detector.DSTerm;
/**
 * 
 * 		---	!	1. Aplikacija	!	----
 * @author franv
 *
 */
public class App1 extends Process implements CamUser{
	static int myId;
	
    public App1(Linker initComm) {
        super(initComm);
    }
    public synchronized void handleMsg(Msg m, int src, String tag){
        if (tag.equals("chat")) {
            //System.out.println(m.getMessage());
            FileSystem files = new FileSystem(String.valueOf(myId));
            //BufferedReader din = new BufferedReader(
    			//	new InputStreamReader(System.in));
            String fileName=m.getTitle();
			try {
				//fileName = App1.getUserInput(din, "Enter new file name:");
				if(files.createFile(fileName,m.getMessage()) ){
	        		System.out.println("File " + fileName + " recived from " + src + ".");
                    System.out.println( "Enter command (create,send,list,delete):" );
                }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }
    public static String getUserInput(BufferedReader din, String  Message) throws Exception {
        System.out.println(Message);
        String chatMsg = din.readLine();
        return chatMsg;
    }
    public IntLinkedList getDest(BufferedReader din) throws Exception {
//        System.out.println("Type in destination pids with -1 at end:");
//        System.out.println("Only one pid for synch order:");
//        IntLinkedList destIds = new IntLinkedList(); //dest for msg
//        StringTokenizer st = new StringTokenizer(din.readLine());
//        while (st.hasMoreTokens()) {
//            int pid = Integer.parseInt(st.nextToken());
//            if (pid == -1) break;
//            else destIds.add(pid);
//        }
//        return destIds;
//        
        ////
        System.out.println("Type in destination: ");
        IntLinkedList destIds = new IntLinkedList(); //dest for msg
        StringTokenizer st = new StringTokenizer(din.readLine());
        while (st.hasMoreTokens()) {
            int pid = Integer.parseInt(st.nextToken());
            destIds.add(pid);
        }
        return destIds;        
    }
    public synchronized String localState() {
        FileSystem files = new FileSystem(String.valueOf(myId));
        String newLine = System.getProperty("line.separator");
        return "----------||List of all files at " + String.valueOf(myId) + ":" + "||" + files.getDocumentListInLine();
    }
    public String func( String x, String y ){
        x = x.concat( y );
        return x;
    }
    
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	
    	
        BufferedReader din = new BufferedReader(
				new InputStreamReader(System.in));
        
        String[] args1 =  App1.getUserInput(din, "Enter arguments: ").split(" ");
        
    	// Mo�da prebacit sve da se �ita sa ulaza, a ne sa komandne linije.
        String baseName = args1[0];								//	Ime aplikacije
        myId = Integer.parseInt(args1[1]);					//	Broj tog processa
        int numProc = Integer.parseInt(args1[2]);				//	Ukupan broj procesa
        Linker comm = null;      
        
        
        if (args1[3].equals("simple"))							//	FIFO -- ne koristiri 
            comm = new Linker(baseName, myId, numProc);
        else if (args1[3].equals("causal"))						//	Kauzalni -- bolje
            comm = new CausalLinker(baseName, myId, numProc);
        else
        	throw new Exception("Invalid comandline arguments!");
        
        App1 c = new App1(comm);
        GlobalFunc g = new GlobalFunc(comm, (myId==0));
        Camera camera = new TermRecvCamera( comm, c, g, new DSTerm( comm ), numProc );

        /*for (int i = 0; i < numProc; i++)
            if (i != myId) 
            	(new ListenerThread(i, c)).start();*/

        for (int i = 0; i < numProc; i++)
            if (i != myId) 
                (new ListenerThread(i, camera)).start();

        //g.makeTree();
        
        FileSystem files = new FileSystem(String.valueOf(myId));
        while (true) {
            String chatMsg = App1.getUserInput(din, "Enter command (create,send,list,delete):");
            
            if (chatMsg.equals("quit")) 
        	{
        		return;
        	}
            else if(chatMsg.equals("create"))
            {
            	String fileName = App1.getUserInput(din, "Enter new file name:");
            	if(files.createFile(fileName,"") )
            		System.out.println("File " + fileName + " created.");
            	else
            		System.out.println("File " + fileName + " NOT created.");
            }
            else if( chatMsg.equals("send"))
            {
            	String fileName = App1.getUserInput(din, "Enter file name:");
            	chatMsg = files.getFile(fileName);
                IntLinkedList destIds =  c.getDest(din);
                if(!files.deleteFile(fileName))
            		throw new IOException("File coud not be deleted.");
                
                comm.sendMsg(destIds.getEntry(0), "chat", chatMsg, fileName);
            }
            else if( chatMsg.equals("list"))
            {
            	System.out.println("List of all files at " + String.valueOf(myId) + ":");
            	System.out.print(files.getDocumentList());
            }
            else if( chatMsg.equals("delete"))
            {
            	String fileName = App1.getUserInput(din, "Enter new file name:");
            	if(files.deleteFile(fileName))
            		System.out.println("File " + fileName + " deleted.");
            	else
            		System.out.println("File " + fileName + " not found.");            	
            }
            else if( chatMsg.equals( "edit" ) )
            {
                String fileName = App1.getUserInput(din, "Enter file name:");
                String contents = files.getFile( fileName );
                System.out.println( contents );
                contents = App1.getUserInput(din, "Enter file contents:");
                files.writeToFile(fileName, contents);
            }
            else if( chatMsg.equals( "snapshot" ) ){
                camera.globalState();
            }
            else
            {
            	System.out.println("ERROR: Invalid input!");
            }
        }
    }
}