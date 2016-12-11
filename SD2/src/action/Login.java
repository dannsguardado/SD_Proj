package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import model.SessionModel;
import model.Users;
import java.util.Map;

/**
 * Created by ritaalmeida on 24/11/16.
 */
public class Login extends ActionSupport implements SessionAware {
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
                if (user.login(username, password) != false) {
                    session.put("user", user);
                    if(user.getUser().getIsAdmin()==1)return "admin";
                    return "success";
                } else {
                    return "login";
                }
            }
            else
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