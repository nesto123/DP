/**
 * 
 */
package camera;
import java.io.*;

import message.Msg;

/**
 * @author franv
 *
 */
public interface MsgHandler {
    public void handleMsg(Msg m, int srcId, String tag);
    public Msg receiveMsg(int fromId) throws IOException;
}
