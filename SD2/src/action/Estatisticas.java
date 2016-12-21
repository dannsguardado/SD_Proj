package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import model.Users;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class Estatisticas extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private int usersStasts3;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel estas = getModel();
        session.remove("usersStats1");
        session.remove("usersStats2");
        session.remove("usersStats3");
        if (estas.getRmiConnection() != null) {
            if(session.get("user")!=null) {
                ArrayList<Users> users1 = null;
                ArrayList<Users> users2 = null;
                int users3 = 0;
                System.out.println("ola");
                if (((users1 = estas.estatisticas1()) != null) && ((users2 = estas.estatisticas2()) != null) && ((users3 = estas.estatisticas3()) != 0)) {
                    session.put("usersStats1", users1);
                    session.put("usersStats2", users2);
                    session.put("usersStats3", users3);
                    return "estatisticas";
                } else {
                    return "stay";
                }
            }else {
                return "login";
            }
        } else {
            return "noservice";
        }
    }
    public void setUsersStasts3(int usersStasts3) { this.usersStasts3 = usersStasts3; }


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
