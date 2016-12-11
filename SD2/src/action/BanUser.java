package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.Message;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class BanUser extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private String username;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        System.out.println("adios migo");
        session.remove("auction");
        if (auction.getRmiConnection() != null) {
            if (username != null) {
                System.out.println("ban user ok");
                if ((auction.banUser(username)) != null) {
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



    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
         this.username = username;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }
}
