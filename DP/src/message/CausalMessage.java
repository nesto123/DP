/**
 * 
 */
package message;

/**
 * Struktura kauzalne poruke.
 * @author franv
 *
 */
public class CausalMessage {
    Msg m;
    int N;
    int W[][];
    public CausalMessage(Msg m, int N, int matrix[][]) {
        this.m = m;
        this.N = N;
        W = matrix;
    }
    public int[][] getMatrix() {
        return W;
    }
    public Msg getMessage() {
        return m;
    }
}
