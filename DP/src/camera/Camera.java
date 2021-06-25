/**
 * 
 */
package camera;

import message.MsgHandler;

/**
 * @author franv
 *
 */
public interface Camera extends MsgHandler {
    /**
     * Gets global snapshot.
     */
    void globalState();
    String getMyValue();
    boolean isFinished();
}
