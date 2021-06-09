/**
 * 
 */
package camera;

/**
 * Must be implemented by application for getting its  snapshot.
 */
public interface CamUser extends MsgHandler {
    /**
     * Tracks local state of one process.
     */
    void localState();
}

