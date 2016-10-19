import java.io.Serializable;
import java.sql.Date;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Auctions implements Serializable {

    private Users user;
    private int auctionID;
    private int code;
    private String title;
    private String description;
    private int amount;
    //private Date dateLimit;

    public Auctions(Users user, int auctionID){
        this.user = user;
        this.auctionID = auctionID;

    }

    public Auctions(int code, String title, String description, int amount){
        this.code = code;
        this.title = title;
        this.description = description;
        this.amount = amount;
        //this.dateLimit = dateLimit;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCode() { return code; }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getAmount() { return amount; }

}
