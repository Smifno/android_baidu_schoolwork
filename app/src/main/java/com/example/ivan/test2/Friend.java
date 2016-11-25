package com.example.ivan.test2;



/**
 * Created by Ivan on 2016/11/24.
 */

public class Friend {
    private String name;
    private String phoneNum;
    private String Latitude;
    private String Longitude;
    private boolean isFriend;

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public void setLatitude(String Latitude)
    {
        this.Latitude = Latitude;
    }

    public void setLongitude(String Longitude)
    {
        this.Longitude = Longitude;
    }

    public void setisFriend(boolean isFriend)
    {
        this.isFriend = isFriend;
    }


    public String getName()
    {
        return name;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public String getLatitude()
    {
        return Latitude;
    }

    public String getLongitude()
    {
        return Longitude;
    }

    public boolean getisFriend()
    {
        return isFriend;
    }


}
