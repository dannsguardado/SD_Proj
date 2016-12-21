package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class CancelAuction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private int id;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        session.remove("auction");
        if (auction.getRmiConnection() != null) {
            if(session.get("user")!=null){
                if (id != 0) {
                    Auctions auctions= null;
                    if ((auctions = auction.cancelAuction(id)) != null) {
                        session.put("auction", auctions);
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



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }
}
