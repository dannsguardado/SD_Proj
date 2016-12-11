package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
import model.Message;
import model.SessionModel;
import model.Users;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

/**
 * Created by ritaalmeida on 28/11/16.
 */
public class Estatisticas extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        session.remove("usersStats1");
        session.remove("usersStats2");
        session.remove("usersStats3");
        if (auction.getRmiConnection() != null) {
            ArrayList<Users> users1 = null;
            ArrayList<Users> users2 = null;
            int users3 = 0;
            System.out.println("ola");
            if (((users3 = auction.estatisticas3()) != 0) ) {
                //&& ((users2 = auction.estatisticas2()) != null) && ((users3 = auction.estatisticas3()) != 0)
                //session.put("usersStats1", users1.get(0)); // retirar o get(0) e acrescentar no jsp o foreach
               // session.put("usersStats2", users2.get(0));
                session.put("usersStats1", users3);
                return "estatisticas";
            } else {
                return "stay";
            }

            // login aqui ???
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

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }
}
