/**
 * 
 */
package app;
import java.util.*;

import linker.IntLinkedList;
import linker.Linker;
import linker.Util;
import linker.Process;
import message.Msg;

/**
 * @author franv
 *
 */
public class GlobalFunc extends Process implements GlobalService{
    FuncUser prog;
    SpanTree tree = null;
    IntLinkedList pending = new IntLinkedList();
    String myValue;
    String answer;
    boolean answerRecvd;
    boolean pendingSet = false;
    public GlobalFunc(Linker initComm, boolean isRoot) {
        super(initComm);
        tree = new SpanTree(initComm, isRoot);
    }
    public void makeTree(){
        tree.makeTree();
    }
    public void initialize(String myValue, FuncUser prog) {
        this.myValue = myValue;
        this.prog = prog;
        tree.waitForDone();
        Util.println(myId + ":" + tree.children.toString());
    }
    public synchronized String computeGlobal() {
        pending.addAll(tree.children);
        pendingSet = true;
        notifyAll();
        while (!pending.isEmpty()) myWait();
        if (tree.parent == myId) { // root node
            answer = myValue;
        } else { //non-root node
            sendMsg(tree.parent, "subTreeVal", myValue);
            answerRecvd = false;
            while (!answerRecvd) myWait();
        }
        sendChildren(answer);
        return answer;
    }
    void sendChildren(String value) {
        ListIterator t = tree.children.listIterator(0);
        while (t.hasNext()) {
            Integer child = (Integer) t.next();
            sendMsg(child.intValue(), "globalFunc", value);
        }
    }
    public synchronized void handleMsg(Msg m, int src, String tag) {
        tree.handleMsg(m, src, tag);
        if (tag.equals("subTreeVal")) {
            while (!pendingSet) myWait();
            pending.remove(src);
            myValue = prog.func(myValue, m.getMessage());
            if (pending.isEmpty()) notifyAll();
        } else if (tag.equals("globalFunc")) {
            answer = m.getMessage();
            answerRecvd = true;
            notifyAll();
        }
        else if( tag.equals("invite") || tag.equals("accept") || tag.equals("reject"))
            tree.handleMsg(m, src, tag);
    }
}
