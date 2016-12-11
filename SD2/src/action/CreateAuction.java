package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class CreateAuction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private long code;
    private String title, description;
    private float amount;
    private Timestamp datalimite;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        if (auction.getRmiConnection() != null) {
            if (code != 0 && title != null && amount != 0 && datalimite!=null) {
                if (auction.createAuction(code, title, description, amount, datalimite) != null) {
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

    public void setCode(long code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


    public void setDatalimite(Timestamp datalimite) {
        this.datalimite = datalimite;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
