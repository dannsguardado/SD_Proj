package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.sql.Timestamp;
import java.util.Map;

public class SendMessage extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private long ID;
    private String username;
    private String message;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        if (auction.getRmiConnection() != null) {
            if (ID != 0 && username != null  && message!=null) {
                if (auction.sendMessage(ID, username)) {
                    return "success";
                } else {
                    return "login";
                }
            }else
            {
                return "stay";
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

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
