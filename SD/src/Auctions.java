import java.io.Serializable;
import java.sql.Date;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Auctions implements Serializable {

    private String username;
    private int auctionID;
    private long code;
    private String title;
    private String description;
    private float amount;
    private int dataLimite;
    private int datacriacao;
    private int ativo;

    public Auctions( long code, String title, String description, float amount, String user){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.username = user;
        this.datacriacao = 11;
        this.ativo = 1;
        this.dataLimite = 15;
        //this.dateLimit = dateLimit;
    }

    public Auctions(int auctionID, long code, String title, String description, float amount, String user){
        this.auctionID = auctionID;
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.username = user;
        this.datacriacao = 11;
        this.ativo = 1;
        this.dataLimite = 15;
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
    public String getAuctionUsername() {
        return username;
    }


}
