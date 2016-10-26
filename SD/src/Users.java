import java.io.Serializable;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class Users implements Serializable {
    private int usernameID;
    private String name;
    private String password;
    private int isAdmin;
    private int isBan;

    public Users (int usernameID){
        this.usernameID = usernameID;
        isBan = 0;
    }

    public Users (String name, String password,int usernameID){
        this.name = name;
        this.password = password;
        this.usernameID = usernameID;
        isBan = 0;
    }
    public Users (String name,int isBan, String password){
        this.name = name;
        this.password = password;
        this.isBan = isBan;
    }

    public Users (String name, String password){
        this.name = name;
        this.password = password;
        isBan = 0;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getUsernameID() { return usernameID; }
    public int getIsBan() { return isBan; }
    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int i) { isAdmin= i; }


}
