package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 05/12/16.
 */
public class Register extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private String username = null, password = null;

    @Override
    public void setSession(Map<String, Object> session){
        this.session= session;
    }
    public String execute() {
        SessionModel user = getModel();
        session.remove("user");
        if (user.getRmiConnection() != null) {

            if (username != null && password != null) {
                if (user.register(username, password) == false) {

                    session.put("user", user);
                    return "success";
                }
            }
            else
            {
                return "stay";
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
