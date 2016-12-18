package action;

import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import model.Users;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 24/11/16.
 */
public class SocialLogin extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private String code;

    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel user = getModel();
        session.remove("user");
        if (user.getRmiConnection() != null) {
            OAuthService service = (OAuthService) session.get("service");
            Verifier verifier = new Verifier(code);
            System.out.println();
            // Trade the Request Token and Verfier for the Access Token
            System.out.println("Trading the Request Token for an Access Token...");
            Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
            System.out.println("Got the Access Token!");
            System.out.println("(if your curious it looks like this: " + accessToken + " )");
            System.out.println();
            // Now let's go and ask for a protected resource!
            System.out.println("Now we're going to access a protected resource...");
            OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
            service.signRequest(accessToken, request);

            Response response = request.send();
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            String social = response.getBody();
            String split[] = social.split(",|:");

            session.put("service", service);
            String username = split[1].replace("\"",""), id = split[3].replace("\"","").replace("}","");




            String tokenFacebook = accessToken.toString();
            tokenFacebook = tokenFacebook.replace("Token[", "").replace(" , ]","");

            Users new_user =null;
            if(user.getUser()!=null) {
                if (user.loginFacebook(id, tokenFacebook, user.getUser().getName()) != false) {
                    System.out.println("lol");
                    user.setUser(user.getIDFacebook(user.getUser()));
                    session.put("user", user);
                    return "success";
                }
            }
            else
            {
                if ((new_user=user.getMyIDFacebook(id)) != null) {
                    System.out.println("lel");

                    user.setUser(new_user);
                    session.put("user", user);
                    return "success";
                }
            }
            session.put("user", user);
            return "stay";
        }
        return "noservice";
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}