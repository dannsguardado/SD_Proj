import java.io.Serializable;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Users implements Serializable {
    private int usernameID;
    private String name;
    private String password;
    int isAdmin;

    public Users (int usernameID){
        this.usernameID = usernameID;
    }

    public Users (String name, String password,int usernameID){
        this.name = name;
        this.password = password;
        this.usernameID = usernameID;
    }

    public Users (String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getUsernameID() { return usernameID; }

    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int i) { isAdmin= i; }


}
