
package GameUtilInformation;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description this class for the CatchMe Game, this class manage the state of
 *              the catchMe board.
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class CatchmeBoardStatues {

    /**
     * is positioned board user's number
     */
    int tagNumber;

    /**
     * is positioned board user's item set number
     */
    int itemset;

    /**
     * init The CatchmeBoardStatues Using tagNumber and ItemSetNumber
     * 
     * @param tagNumber
     * @param itemset
     */
    public CatchmeBoardStatues(int tagNumber, int itemset) {
        super();
        this.tagNumber = tagNumber;
        this.itemset = itemset;
    }

    /**
     * get the User tagNumber Integer
     * 
     * @return
     */
    public int getTagNumber() {
        return tagNumber;
    }

    /**
     * set The Number of The User tag
     * 
     * @param tagNumber
     */
    public void setTagNumber(int tagNumber) {
        this.tagNumber = tagNumber;
    }

    /**
     * get the Board Position Item set Number Integer
     * 
     * @return
     */
    public int getItemset() {
        return itemset;
    }

    /**
     * set The Number of the Item set Number
     * 
     * @param itemset
     */
    public void setItemset(int itemset) {
        this.itemset = itemset;
    }

}
