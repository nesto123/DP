/**
 * 
 */
package camera;

import message.MsgHandler;
import app.FuncUser;

/**
 * Must be implemented by application for getting its  snapshot.
 */
public interface CamUser extends MsgHandler, FuncUser {
    /**
     * Tracks local state of one process.
     */
    String localState();
}

