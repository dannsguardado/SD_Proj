import java.io.Serializable;
import java.sql.Date;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Auctions implements Serializable {

    private int auctionID;
    private long code;
    private String title;
    private String description;
    private float amount;
    private int dataLimite;
    private int datacriacao;
    private int ativo;
    private String auction_username;

    public Auctions( long code, String title, String description, float amount, String auction_username){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.datacriacao = 11;
        this.ativo = 1;
        this.dataLimite = 15;
        this.auction_username = auction_username;
        //this.dateLimit = dateLimit;

    }

    public Auctions( long code, String title, String description, float amount, String auction_username, int auctionID){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.datacriacao = 11;
        this.ativo = 1;
        this.dataLimite = 15;
        this.auction_username = auction_username;
        this.auctionID = auctionID;
        //this.dateLimit = dateLimit;

    }

    public Auctions(long code){
        this.code = code;
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
    public int getDatacriacao() {
        return datacriacao;
    }
    public int getDataLimite() {
        return dataLimite;
    }
    public float getAtivo() { return ativo; }
    public String getAuction_username(){return auction_username;}



}
