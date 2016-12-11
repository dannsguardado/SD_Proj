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
            if (id != 0) {
                if ((auction.cancelAuction(id)) != null) {
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
