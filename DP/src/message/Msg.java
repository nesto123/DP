/**
 * 
 */
package message;
import java.util.*;

/**
 * @author franv
 *
 */
public class Msg {
    int srcId, destId;
    String tag;
    String msgBuf;
    public Msg(int s, int t, String msgType, String buf) {
        this.srcId = s;
        destId = t;
        tag = msgType;
        msgBuf = buf;
    }
    
    /** 
     * @return int
     */
    public int getSrcId() {
        return srcId;
    }
    
    /** 
     * @return int
     */
    public int getDestId() {
        return destId;
    }
    
    /** 
     * @return String
     */
    public String getTag() {
        return tag;
    }
    
    /** 
     * @return String
     */
    public String getMessage() {
        return msgBuf;
    }
    
    /** 
     * @return int
     */
    public int getMessageInt() {
        StringTokenizer st = new StringTokenizer(msgBuf);
        return Integer.parseInt(st.nextToken());
    }
    
    /** 
     * @param st
     * @return Msg
     */
    public static Msg parseMsg(StringTokenizer st){
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String buf = st.nextToken("#");
        return new Msg(srcId, destId, tag, buf);
    }
    
    /** 
     * @return String
     */
    public String toString(){
        String s = String.valueOf(srcId)+" " +
                    String.valueOf(destId)+ " " +
                    tag + " " + msgBuf + "#";
        return s;
    }
}
