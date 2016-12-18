package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class EditAuction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private String title, description;
    private float amount;
    private String datalimite;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        Auctions auctions = (Auctions)session.get("auction");
        session.remove("auction");
        HashMap<String, String> info = new HashMap<>();
        if (auction.getRmiConnection() != null) {
            if(session.get("user")!=null) {
                info.put("username", ((SessionModel) session.get("user")).getUser().getName());
                if (title != null) {
                    info.put("title", title);
                    if ((auctions = auction.editAuction(auctions.getAuctionID(), info)) != null) {
                        session.put("auction", auctions);
                        return "success";
                    }
                }
                if (amount != 0) {
                    info.put("amount", Float.toString(amount));
                    if ((auctions = auction.editAuction(auctions.getAuctionID(), info)) != null) {
                        session.put("auction", auctions);
                        return "success";
                    }
                }
                if (datalimite != null) {
                    info.put("deadline", datalimite);
                    if ((auctions = auction.editAuction(auctions.getAuctionID(), info)) != null) {
                        session.put("auction", auctions);
                        return "success";
                    }

                }
                if (description != null) {
                    info.put("description", description);
                    if ((auctions = auction.editAuction(auctions.getAuctionID(), info)) != null) {
                        session.put("auction", auctions);
                        return "success";
                    }
                }
            }else {
                return "login";
            }
        }
        return "noservice";
    }


    public SessionModel getModel() {
        if (!session.containsKey("model")) {
            this.setSessionModel(new SessionModel());
        }
        return (SessionModel) session.get("model");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


    public void setDatalimite(String datalimite) {
        this.datalimite = datalimite;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
