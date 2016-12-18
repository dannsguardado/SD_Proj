package action;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import model.Auctions;
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
    private String datalimite;


    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/";


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute() {
        SessionModel auction = getModel();
        session.remove("auction");
        session.remove("messages");
        session.remove("bids");
        session.remove("bestbid");
        if (auction.getRmiConnection() != null) {

            if (code != 0 && title != null && amount != 0 && datalimite != null) {
                Timestamp dataLimite =  java.sql.Timestamp.valueOf (datalimite.concat(":00"));
                Auctions auctions;

                if ((auctions = auction.createAuction(code, title, description, amount, dataLimite)) != null) {
                    session.put("auction", auctions);

                    if(auction.getUser().getIdFacebook() != null) {
                        OAuthService service;
                        String User_id = auction.getUser().getIdFacebook();
                        System.out.println("user_id: " + auction.getUser().getIdFacebook());

                        service = (OAuthService) session.get("service");
                        try {
                            OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL + User_id + "/feed", service);

                            String token = auction.getUser().getTokenFacebook();

                            System.out.println("lol: user: " + auction.getUser().getName());
                            System.out.println("oiii: " + auction.getUser().getTokenFacebook());

                            String key = "s";
                            System.out.println("token: " + token);
                            Token accessToken = new Token(token, key);
                            service.signRequest(accessToken, request);

                            request.addBodyParameter("message", "http://localhost:8080/detailauction?id=" + auctions.getAuctionID() + "/");

                            Response response = request.send();
                            System.out.println("Got it! Lets see what we found...");
                            System.out.println("HTTP RESPONSE: =============");
                            System.out.println(response.getCode());
                            System.out.println(response.getBody());
                            System.out.println("END RESPONSE ===============");

                        } catch (OAuthException e) {
                            e.printStackTrace();
                        }
                        return "success";
                    }
                    return "success";
                } else {
                    return "login";
                }
            } else {
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


    public void setDatalimite(String datalimite) {
        this.datalimite = datalimite;
    }

    public void setSessionModel(SessionModel model) {
        this.session.put("model", model);
    }

}
