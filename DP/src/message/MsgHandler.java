/**
 * 
 */
package message;
import java.io.*;

/**
 * @author franv
 *
 */
public interface MsgHandler {
    public void handleMsg(Msg m, int srcId, String tag);
    public Msg receiveMsg(int fromId) throws IOException;
}
