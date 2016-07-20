
package application;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description this class for the Client Game Information
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class ClientGameInformation {

    /**
     * user's Statues Game Name
     */
    private String gameName;

    /**
     * user's Game Play Times
     */
    private int playTimes;

    /**
     * user's Game Win Number
     */
    private int win;

    /**
     * user's Game Defeat Number
     */
    private int defeat;

    /**
     * init The ClientGameInformation Using Game's Name
     * 
     * @param sGameName
     */
    public ClientGameInformation(String sGameName) {
        super();

        this.gameName = sGameName;
        this.playTimes = 0;
        this.win = 0;
        this.defeat = 0;
    }

    /**
     * get the User PlayTimes Integer
     * 
     * @return
     */
    public int getPlayTimes() {
        return playTimes;
    }

    /**
     * set The Number of The playTimes
     * 
     * @param playTimes
     */
    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    /**
     * get the User win Integer
     * 
     * @return
     */
    public int getWin() {
        return win;
    }

    /**
     * this function increase the number about win; (+1)
     */
    public void increaseWin() {
        this.win++;
    }

    /**
     * set The Number of The win
     * 
     * @param win
     */
    public void setWin(int win) {
        this.win = win;
    }

    /**
     * get the User defeat Integer
     * 
     * @return
     */
    public int getDefeat() {
        return defeat;
    }

    /**
     * set The Number of The defeat
     * 
     * @param defeat
     */
    public void setDefeat(int defeat) {
        this.defeat = defeat;
    }

    /**
     * this function increase the number about defeat; (+1)
     */
    public void increaseDefeat() {
        defeat++;
    }

    /**
     * get the User game Name String
     * 
     * @return
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * set The User game Name
     * 
     * @param gameName
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

}
