/**
 * 
 */
package linker;
import java.util.LinkedList;

/**
 * @author franv
 *
 */
public class IntLinkedList extends LinkedList {
    public void add(int i) {
        super.add(new Integer(i));
    }
    public boolean contains(int i) {
        return super.contains(new Integer(i));
    }
    public int removeHead() {
        Integer j = (Integer) super.removeFirst();
        return j.intValue();
    }
    public boolean removeObject(int i) {
        return super.remove(new Integer(i));
    }
    public int getEntry(int index) {
        Integer j = (Integer) super.get(index);
        return j.intValue();
    }
}
