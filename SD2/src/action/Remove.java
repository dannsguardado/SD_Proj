package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 17/12/16.
 */
public class Remove extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        session.remove("user");
        if(user.getRmiConnection()!=null)
        {
            user.setUser(user.removeFacebook(user.getUser()));
            session.put("user",user);
            return "success";
        }
        else {
            return "stay";
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
