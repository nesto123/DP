/**
 * 
 */
package linker;

import java.util.*; 
import java.net.*; 
import java.io.*;

import message.*;

/**
 * Algoritam za kauzalno ure�ivanje implementiran je pomo�u ove klase.
 * Ova klasa pro�iruje klasu Linker da bi mogla raditi s porukama koje su 
 * pro�irene matricama.
 * @author franv
 *
 */
public class CausalLinker extends Linker {
    int M[][];
    LinkedList deliveryQ = new LinkedList(); // deliverable messages
    LinkedList pendingQ = new LinkedList(); // messages with matrix
    
    public CausalLinker(String basename, int id, int numProc)
                                            throws Exception 
    {
        super(basename, id, numProc);
        M = new int[N][N];
        Matrix.setZero(M);
    }
    
    /**
     * Pove�ava M[myId][destId] da bi uzela u obzir doti�nu poruku,
     * te prila�e a�uriranu M[][]uz poruku.
     * @param destId odredi�te poruke
     * @param tag tag poruke
     * @param msg sama poruka koja se salje
     */
    public synchronized void sendMsg(int destId, String tag, String msg)
    {
        M[myId][destId]++;
        super.sendMsg(destId, "matrix", Matrix.write(M));
        super.sendMsg(destId, tag, msg);
    }

    public synchronized void sendMsg(int destId, String tag, String msg, String title)
    {
        M[myId][destId]++;
        super.sendMsg(destId, "matrix", " 0 ", Matrix.write(M));
        super.sendMsg(destId, tag, msg, title);
    }
    
    /**
     * Slu�i za slanje iste poruke ve�em broju primatelja. Najprije se
     * pove�ava M[myId][destId] za sve destId iz zadane liste primatelja. 
     * Zatim se tako a�urirana M[][]�alje kao prilog uz svaku kopiju poruke.
     * @param destIds odredi�ta poruke
     * @param tag tag poruke
     * @param msg sama poruka koja se salje
     */
    public synchronized void multicast(IntLinkedList destIds, 
                                            String tag, String msg) {
        for (int i=0; i<destIds.size(); i++)
            M[myId][destIds.getEntry(i)]++;
        for (int i=0; i<destIds.size(); i++) {
            int destId = destIds.getEntry(i);
            super.sendMsg(destId, "matrix", Matrix.write(M));	// sending matrix
            super.sendMsg(destId, tag, msg);           // Sending application message
        }
    }
    
    /**
     * Odre�uje je li poruka �kvalificirana� za isporuku aplikaciji.
     * @param W primljena matrica
     * @param srcId id po�iljatelja
     * @return true ako je kvalificirana ina�e false
     */
    boolean okayToRecv(int W[][], int srcId) {
        if (W[srcId][myId] > M[srcId][myId]+1) return false;
        for (int k = 0; k < N; k++)
            if ((k!=srcId) && (W[k][myId] > M[k][myId])) return false;
        return true;
    }
    /**
     * Prolazi listom pendingQ da bi provjerila je li neka od poruka
     * u njoj postala kvalificirana. Prona�ena kvalificirana poruka se iz
     * pendingQ prebacuje u deliveryQ.
     */
    synchronized void checkPendingQ() {
        ListIterator iter = pendingQ.listIterator(0);
        while (iter.hasNext()) {
            CausalMessage cm = (CausalMessage) iter.next();
            if (okayToRecv(cm.getMatrix(), cm.getMessage().getSrcId())){
                iter.remove(); deliveryQ.add(cm);
            }
        }
    }    
    
    // polls the channel given by fromId to add to the pendingQ
    /**
     * Koristi dvije vezane liste (reda) za spremanje poruka.
     * Lista deliveryQ sadr�i sve poruke koje su kvalificirane i mogu se isporu�iti.
     * � Lista pendingQ sprema sve poruke koje su primljene no nisu jo� kvalificirane za
     * isporuku
     */
    public Msg receiveMsg(int fromId) throws IOException {
        checkPendingQ();
        while (deliveryQ.isEmpty()) {
            Msg matrix = super.receiveMsg(fromId);// matrix
            int [][]W = new int[N][N];
            Matrix.read(matrix.getMessage(), W);
            Msg m1 = super.receiveMsg(fromId);//app message
            pendingQ.add(new CausalMessage(m1, N, W));
            checkPendingQ();
        }
        CausalMessage cm = (CausalMessage) deliveryQ.removeFirst();
        Matrix.setMax(M, cm.getMatrix());
        return cm.getMessage();
    }
}
