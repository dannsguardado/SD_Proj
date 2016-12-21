package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ritaalmeida on 06/12/16.
 */
public class MyAuctions extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private String name = null;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        session.remove("auctions");
        if (auction.getRmiConnection() != null) {
            if (session.get("user") != null) {
                ArrayList<Auctions> auctions = null;
                if (getModel().getUser().getName() != null) {
                    if ((auctions = auction.myAuctions(getModel().getUser().getName())) != null) {
                        session.put("auctions", auctions); // retirar o get(0) e acrescentar no jsp o foreach
                        return "listauctions";
                    }
                }else {
                    return "stay";
                }
            }else{
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




    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }
}
