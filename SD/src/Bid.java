import java.io.Serializable;
import java.util.Date;
import java.lang.String;


/**
 * Created by dannsguardado on 25/10/2016.
 */
public class Bid implements Serializable{

    private int id;
    private int valor;
    private String username;
    private  long idLeilao;

    Bid(int id, int valor, String username, long idLeilao){
        this.id = id;
        this.valor = valor;
        this.username = username;
        this.idLeilao = idLeilao;
    }

    Bid(int valor, String username, long idLeilao){
        this.valor = valor;
        this.username = username;
        this.idLeilao = idLeilao;
    }


    public String getUsername() {
        return username;
    }
    public int getValor() {
        return valor;
    }
}
