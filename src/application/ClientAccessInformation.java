
package application;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description this class for the Client Information
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class ClientAccessInformation {

    /**
     * user IpAddress Information
     */
    private String ipAddress;

    /**
     * user Access or Terminate Time
     */
    private String AccessAndTerminateTime;

    /**
     * user Access or Terminate Type
     */
    private String type;

    /**
     * init The ClientAccessInformation Using User's IpAddress and Access or
     * terminate Time and Access Type
     * 
     * @param ipAddress
     * @param accessAndTerminateTime
     * @param type
     */
    public ClientAccessInformation(String ipAddress, String accessAndTerminateTime, String type) {
        super();
        this.ipAddress = ipAddress;
        AccessAndTerminateTime = accessAndTerminateTime;
        this.type = type;
    }

    /**
     * get the User IP Address String
     * 
     * @return
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * set The User's IP Address
     * 
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * get the User Access or Terminate Time String
     * 
     * @return
     */
    public String getAccessAndTerminateTime() {
        return AccessAndTerminateTime;
    }

    /**
     * set The User's IP Access or Terminate Time
     * 
     * @param accessAndTerminateTime
     */
    public void setAccessAndTerminateTime(String accessAndTerminateTime) {
        AccessAndTerminateTime = accessAndTerminateTime;
    }

    /**
     * get the User Access or Terminate Type String
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * set The User's IP Access or Terminate Type
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

}
