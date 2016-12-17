package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class SearchAuction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private long code;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        session.remove("auctions");
        if (auction.getRmiConnection() != null) {
            if(session.get("user")!=null) {
                ArrayList<Auctions> auctions;
                if (code != 0) {
                    if ((auctions = auction.searchAuction(code)) != null) {
                        session.put("auctions", auctions);
                        return "listauctions";
                    }
                }else
                {
                    return "stay";
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



    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }
}
