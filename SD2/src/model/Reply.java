package model;

import java.io.Serializable;

/**
 * Created by ritaalmeida on 29/11/16.
 */
public class Reply implements Serializable{

    private static final long serialVersionUID = 1L;
    private int replyID;
    private String message;
    private Users user;
    public Reply() { }

    public Reply(String message, Users user) {
        this.message = message;
        this.user = user;
    }

    public Reply(int replyID, String message, Users user)
    {
        this.replyID = replyID;
        this.message = message;
        this.user = user;
    }

    public int getReplyID() { return replyID; }

    public void setReplyID(int replyID) { this.replyID = replyID; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public Users getUser() { return user; }

    public void setUser(Users user) { this.user = user; }

    public String toString() { return  user.getName() + ": " + message; }

}
