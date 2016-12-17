package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import model.Users;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 06/12/16.
 */
public class Logout extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;


    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute() {
        SessionModel logout = getModel();

        if (logout.getRmiConnection() != null) {
            if (logout.logOut(0)) {
                session.remove("user");
                return "success";
            }
        }
        return "stay";
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
