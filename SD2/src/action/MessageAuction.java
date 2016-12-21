package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.Message;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class MessageAuction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private long code;
    private String message;


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        if (auction.getRmiConnection() != null) {
            if(session.get("user")!=null) {
                if (code != 0 && message != null ) {
                    if (auction.messageAuction(code, message) != null) {//faz sms
                        Auctions auctions;
                        if((auctions = auction.detailAuction(code)) != null){//apresenta
                            ArrayList<Message> message;
                            if((message = auction.getMessages(auctions)) != null) {
                                session.put("auction", auctions);
                                session.put("messages", message);
                            }
                            return "detailauction";
                        }
                    }
                }else {
                    return "stay";
                }
            }else {
                return "login";
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

    public void setCode(long code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
