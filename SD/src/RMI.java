import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public interface RMI extends Remote{

    String printTest() throws RemoteException;
    String printNone() throws RemoteException;
    Users login(Users user) throws RemoteException;
    Users register(Users user) throws RemoteException;
    Auctions create(Auctions auction) throws RemoteException;
    Auctions detail(Long code) throws RemoteException;
    ArrayList<Auctions> search(Long code) throws RemoteException;
}
