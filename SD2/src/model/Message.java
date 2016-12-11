package model;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;

/**
 * Created by dannsguardado on 25/10/2016.
 */
public class Message implements Serializable{

    private int id;
    private String message;
    private String username;
    private long idLeilao;
    private ArrayList<Reply> replies;
    private Auctions auction;
    private Users user;
    private int messageID;

    public Message() {}
    public Message(int messageID) { this.messageID = messageID; }

    public Message(String message, Auctions auction, Users user) {

        this.message = message;
        this.auction = auction;
        this.user = user;
        this.replies = new ArrayList<Reply>();
    }

    public Message(int id, String message, String username, long idLeilao){
        this.id = id;
        this.message = message;
        this.username = username;
        this.idLeilao = idLeilao;
    }

    public Message (String message, String username, long idLeilao){
        this.message = message;
        this.username = username;
        this.idLeilao = idLeilao;

    }


    public Message(int messageID,String message, Auctions auction, Users user, ArrayList<Reply> replies)
    {
        this.messageID = messageID;
        this.message = message;
        this.auction = auction;
        this.user = user;
        this.replies = replies;
    }

    public int getmessageID() {
        return messageID;
    }

    public void setmessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }


    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }

    public Auctions getProject() {
        return auction;
    }

    public void setAuction(Auctions auction) {
        this.auction = auction;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public String toString() {
        String text = messageID + " -> " + user.getName() +": " + message+"\n";
        for(int i=0;i<replies.size();i++)
        {
            text = text + "\n       " + replies.get(i);
        }
        return text;
    }


}
