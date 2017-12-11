package edu.unc.thomasshealy.foodlinkfinal;

/**
 * Created by thomasshealy on 12/7/17.
 */

public class Pin {

    public double latitude;
    public double longitude;
    public int type;
    public String username;
    public String description;
    public String phoneNumber;

    public Pin(double latitude, double longitude, int type, String username, String description,
               String phoneNumber){
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.username = username;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

}
