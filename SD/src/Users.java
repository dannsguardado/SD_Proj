import java.io.Serializable;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Users implements Serializable {
    private int usernameID;
    private String name;
    private String password;

    public Users (int usernameID){
        this.usernameID = usernameID;
    }

    public Users (String name, String password){
        this.name = name;
        this.password = password;
        this.usernameID = -1;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getUsernameID() { return usernameID; }

}
