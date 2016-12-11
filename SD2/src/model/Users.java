package model;
import java.io.Serializable;
import java.security.Timestamp;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Users implements Serializable {
    private int usernameID;
    private String name;
    private String password;
    private int isAdmin;
    private int isBan;
    private int auctionsCount;
    private int isLive;
    private java.sql.Timestamp lastAccess;
    //ver mrd de id

    public Users (int usernameID){
        this.usernameID = usernameID;
        isBan = 0;
    }
    public Users (String name, int isBan, int isAdmin,int isLive, java.sql.Timestamp lastAccess, int auctionsCount){
        this.name = name;
        this.isBan = isBan;
        this.isAdmin = isAdmin;
        this.isLive = isLive;
        this.auctionsCount = auctionsCount;
        this.lastAccess = lastAccess;
    }

    public Users (String name, int auctionsCount){
        this.name = name;
        this.auctionsCount = auctionsCount;
    }

    public Users (String name, String password,int usernameID){
        this.name = name;
        this.password = password;
        this.usernameID = usernameID;
        isBan = 0;
    }
    public Users (String name,int isBan, String password){
        this.name = name;
        this.password = password;
        this.isBan = isBan;
    }

    public Users (String name, String password){
        this.name = name;
        this.password = password;
        isBan = 0;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getUsernameID() { return usernameID; }
    public int getIsBan() { return isBan; }
    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int i) { isAdmin= i; }

    public int getAuctionsCount(){return auctionsCount;}
    public void setAuctionsCount(int auctionsCount){this.auctionsCount = auctionsCount;}


    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    public java.sql.Timestamp getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(java.sql.Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }
}
