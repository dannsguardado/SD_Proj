package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class EditAuction extends ActionSupport implements SessionAware {
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
        HashMap<String, String> info = new HashMap<>();

        if (auction.getRmiConnection() != null) {
            if (code != 0){
                if (title != null) {
                    info.put("title", title);
                    if (auction.editAuction(code, info) != null) {
                        return "success";
                    } else {
                        return "login";
                    }
                }
                if (amount != 0) {
                    info.put("amount", Float.toString(amount));
                    if (auction.editAuction(code, info) != null) {
                        return "success";
                    } else {
                        return "login";
                    }
                }
                if (datalimite != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    info.put("deadline", dateFormat.format(datalimite));
                    if (auction.editAuction(code, info) != null) {
                        return "success";
                    } else {
                        return "login";
                    }
                }
                if (description != null) {
                    info.put("description", description);
                    if (auction.editAuction(code, info) != null) {
                        return "success";
                    } else {
                        return "login";
                    }
                }
                return "noservice";
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
