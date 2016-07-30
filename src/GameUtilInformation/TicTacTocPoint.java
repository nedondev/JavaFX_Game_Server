
package GameUtilInformation;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description class for the TicTacToc Game, this class's Function is that control the
 *      mouse point Event
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class TicTacTocPoint {

    /**
     * TicTacToc Mouse Click X position (board x point)
     */
    private int x;

    /**
     * TicTacToc Mouse Click Y position (board y point)
     */
    private int y;

    /**
     * init The TicTacTocPoint Using Click Mouse Event x,y (or another pointer)
     * 
     * @param x
     * @param y
     */
    public TicTacTocPoint(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * get Point x Integer
     * 
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * set Point x
     * 
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * get Point y Integer
     * 
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * set Point y
     * 
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

}
