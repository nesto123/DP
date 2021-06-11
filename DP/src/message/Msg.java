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
    String title;
    public Msg(int s, int t, String msgType, String buf, String title) {
        this.srcId = s;
        destId = t;
        tag = msgType;
        msgBuf = buf;
        this.title = title;
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
    
    public String getTitle() {
    	return this.title;
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
        String title = st.nextToken();
        String buf = st.nextToken("#");
        return new Msg(srcId, destId, tag, buf, title);
    }
    
    /** 
     * @return String
     */
    public String toString(){
        String s = String.valueOf(srcId)+" " +
                    String.valueOf(destId)+ " " +
                    tag + " " + title + " " + msgBuf + "#";
        return s;
    }
}
