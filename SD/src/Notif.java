import java.io.Serializable;

/**
 * Created by ritaalmeida on 17/11/16.
 */
public class Notif implements Serializable {
    private long idLeilao;
    private String username;
    private float valor;

    Notif(float valor, String username, long idLeilao) {
        this.valor = valor;
        this.username = username;
        this.idLeilao = idLeilao;
    }
}