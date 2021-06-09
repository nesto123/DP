/**
 * 
 */
package linker;

import java.util.*; 
import java.net.*; 
import java.io.*;

import message.*;

/**
 * Algoritam za kauzalno ureðivanje implementiran je pomoæu ove klase.
 * Ova klasa proširuje klasu Linker da bi mogla raditi s porukama koje su 
 * proširene matricama.
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
     * Poveæava M[myId][destId] da bi uzela u obzir dotiènu poruku,
     * te prilaže ažuriranu M[][]uz poruku.
     * @param destId odredište poruke
     * @param tag tag poruke
     * @param msg sama poruka koja se salje
     */
    public synchronized void sendMsg(int destId, String tag, String msg)
    {
        M[myId][destId]++;
        super.sendMsg(destId, "matrix", Matrix.write(M));
        super.sendMsg(destId, tag, msg);
    }
    
    /**
     * Služi za slanje iste poruke veæem broju primatelja. Najprije se
     * poveæava M[myId][destId] za sve destId iz zadane liste primatelja. 
     * Zatim se tako ažurirana M[][]šalje kao prilog uz svaku kopiju poruke.
     * @param destIds odredišta poruke
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
     * Odreðuje je li poruka “kvalificirana” za isporuku aplikaciji.
     * @param W primljena matrica
     * @param srcId id pošiljatelja
     * @return true ako je kvalificirana inaèe false
     */
    boolean okayToRecv(int W[][], int srcId) {
        if (W[srcId][myId] > M[srcId][myId]+1) return false;
        for (int k = 0; k < N; k++)
            if ((k!=srcId) && (W[k][myId] > M[k][myId])) return false;
        return true;
    }
    /**
     * Prolazi listom pendingQ da bi provjerila je li neka od poruka
     * u njoj postala kvalificirana. Pronaðena kvalificirana poruka se iz
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
     * Lista deliveryQ sadrži sve poruke koje su kvalificirane i mogu se isporuèiti.
     * ­ Lista pendingQ sprema sve poruke koje su primljene no nisu još kvalificirane za
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
