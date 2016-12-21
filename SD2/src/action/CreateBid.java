package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.Bid;
import model.Message;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class CreateBid extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private long code;
    private float amount;


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        session.remove("auctions");
        session.remove("messages");
        session.remove("bids");
        session.remove("bestbid");
        if (auction.getRmiConnection() != null) {
            if(session.get("user")!=null){

                if (code != 0 && amount != 0 ) {
                    if (auction.createBid(code, amount) != null) {
                        Auctions auctions;
                        if((auctions = auction.detailAuction(code)) != null) {
                            session.put("auction", auctions);
                            ArrayList<Message> message;
                            if ((message = auction.getMessages(auctions)) != null) {
                                session.put("messages", message);
                            }
                            ArrayList<Bid> bids;
                            if ((bids = auction.getBids(auctions)) != null) {
                                session.put("bids", bids);
                                Bid best;
                                if ((best = auction.getBestBid(code)) != null) {
                                    session.put("bestbid", best);
                                }
                            }
                        }
                        return "success";
                    }
                }
                return "stay";
            }else {
                return "login";
            }
        } else {
            return "noservice";
        }
    }


    public SessionModel getModel() {
        if (!session.containsKey("model")) {
            this.setSessionModel(new SessionModel());
        }
        return (SessionModel) session.get("model");
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
