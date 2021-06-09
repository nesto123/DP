/**
 * 
 */
package camera;

/**
 * @author franv
 *
 */
public interface Camera extends MsgHandler {
    /**
     * Gets global snapshot.
     */
    void globalState();
}
