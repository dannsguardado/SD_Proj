import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public interface RMI extends Remote{

    public String printTest() throws RemoteException;
    public String printNone() throws RemoteException;
    public Users login(Users user) throws RemoteException;

}
