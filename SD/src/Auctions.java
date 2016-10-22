import java.io.Serializable;
import java.sql.Date;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Auctions implements Serializable {

    private Users user;
    private int auctionID;
    private long code;
    private String title;
    private String description;
    private float amount;
    //private Date dateLimit;

    public Auctions(Users user, int auctionID){
        this.user = user;
        this.auctionID = auctionID;

    }

    public Auctions(long code, String title, String description, float amount){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        //this.dateLimit = dateLimit;
    }

    public Auctions(long code){
        this.code = code;
    }


    public long getCode() { return code; }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public float getAmount() { return amount; }

}
