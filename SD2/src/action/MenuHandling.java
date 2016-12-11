package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class MenuHandling extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;

    @Override
    public void setSession(Map<String, Object> session){
        this.session= session;
    }


    public String execute(){
        SessionModel handle = getModel();
        if(handle.getUser()!=null)
        {
            return "success";
        }
        else
        {
            return "login";
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
