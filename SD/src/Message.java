import java.io.Serializable;
import java.lang.String;


/**
 * Created by dannsguardado on 25/10/2016.
 */
public class Message implements Serializable{

    private int id;
    private String mensagem;
    private String username;
    private  long idLeilao;

    Message(int id, String mensagem, String username, long idLeilao){
        this.id = id;
        this.mensagem = mensagem;
        this.username = username;
        this.idLeilao = idLeilao;
    }

    Message (String mensagem, String username, long idLeilao){
        this.mensagem = mensagem;
        this.username = username;
        this.idLeilao = idLeilao;
    }


    public String getUsername() {
        return username;
    }
    public String getMessagem() {
        return mensagem;
    }
}
