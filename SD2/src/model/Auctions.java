package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.lang.String;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Auctions implements Serializable {

    private int auctionID;
    private long code;
    private String title;
    private String description;
    private float amount;
    private java.sql.Timestamp dataLimite;
    private java.sql.Timestamp datacriacao;
    private int ativo;
    private String auction_username;
    private ArrayList<Message> messages;

    public Auctions( long code, String title, String description, float amount, String auction_username, java.sql.Timestamp dataLimite){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.ativo = 1;
        this.dataLimite = dataLimite;
        this.auction_username = auction_username;
        //this.dateLimit = dateLimit;
        Date date = new Date();
        datacriacao = new java.sql.Timestamp(date.getTime());
        System.out.println("create data limite: "+ dataLimite);

    }

    public Auctions( long code, String title, String description, float amount, String auction_username, int auctionID, java.sql.Timestamp dataLimite){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        Date date = new Date();
        datacriacao = new java.sql.Timestamp(date.getTime());
        this.ativo = 1;
        this.auction_username = auction_username;
        this.auctionID = auctionID;
        this.dataLimite = dataLimite;
    }

    public Auctions(long code){
        this.code = code;
    }

    public ArrayList<Message> getMessagesAuction() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }


    public int getAuctionID(){ return auctionID; }
    public long getCode() { return code; }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public float getAmount() { return amount; }
    public java.sql.Timestamp getDatacriacao() {
        return datacriacao;
    }
    public java.sql.Timestamp getDataLimite() {
        return dataLimite;
    }
    public float getAtivo() { return ativo; }
    public String getAuction_username(){return auction_username;}


    public void setAuctionID(int auctionID) {
        this.auctionID = auctionID;
    }
}
