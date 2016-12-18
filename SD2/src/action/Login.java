package action;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by ritaalmeida on 24/11/16.
 */
public class Login extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> session;
    private String username = null, password = null, loginType = null, oauth_verifier = null;

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;

    String apiKey = "1427992833880237";
    String apiSecret = "0a9087953c46c85665c5d061d4af05a5";

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel user = getModel();
        session.remove("user");
        if (user.getRmiConnection() != null) {

            if (username != null && password != null) {
                if (user.login(username, password)) {

                    user.setUser(user.getIDFacebook(user.getUser()));
                    session.put("user", user);

                    if(user.getUser().getIsAdmin()==1){
                        return "admin";
                    }

                    return "success";
                } else {
                    return "login";
                }
            }
            System.out.println("tipo: " + loginType);
            if(loginType != null && loginType.matches("facebook")){

                OAuthService service = new ServiceBuilder()
                        .provider(FacebookApi.class)
                        .apiKey(apiKey)
                        .apiSecret(apiSecret)
                        .callback("http://localhost:8080/socialLogin") // Do not change this.
                        .scope("publish_actions")
                        .build();

                System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
                System.out.println();

                // Obtain the Authorization URL
                System.out.println("Fetching the Authorization URL...");
                String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
                System.out.println("Got the Authorization URL!");
                System.out.println("Now go and authorize Scribe here:");
                System.out.println(authorizationUrl);
                System.out.println("And paste the authorization code here");
                session.put("service",service);
                session.put("authorization",authorizationUrl);

                return "authorization";
            }
        }
        else {
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

    public String getOauth_verifier() { return oauth_verifier; }

    public void setOauth_verifier(String oauth_verifier) { this.oauth_verifier = oauth_verifier; }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}