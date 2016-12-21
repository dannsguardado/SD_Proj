package model;

/**
 * Created by dannsguardado on 15/12/2016.
 */

import java.io.Serializable;

public class WaitNotification implements Serializable{

    private String username;
    private long ID;

    public WaitNotification(String username, long ID){
        this.username = username;
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
