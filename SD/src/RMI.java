import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public interface RMI extends Remote{

    String testRMI() throws RemoteException;
    String printTest() throws RemoteException;
    String printNone() throws RemoteException;
    Users login(Users user) throws RemoteException;
    Users register(Users user) throws RemoteException;
    Auctions create(Auctions auction, int id) throws RemoteException;
    Auctions detail(Long code) throws RemoteException;
    ArrayList<Auctions> search(Long code) throws RemoteException;
    ArrayList<Auctions> myauctions(String name) throws RemoteException;
    Auctions editAuction(Auctions auction, HashMap<String, String> info) throws RemoteException;
    void logs(Users user, int on) throws RemoteException;
    ArrayList<Users> onlineUsers() throws RemoteException;
    Auctions cancelAuction(Auctions auction) throws RemoteException;
    String banUser(String user) throws RemoteException;
    Bid makeBid(String username, long idLeilao, float amount) throws RemoteException;
    String deleteBid(String username) throws RemoteException;
    Message createMessage(String mensagem, long idleilao, String username) throws RemoteException;
    ArrayList<Bid> allUserBids(Users user) throws  RemoteException;
    ArrayList<Message>allMessagesBid(Auctions auction) throws RemoteException;
    String topAuctionsCreated() throws RemoteException;
    String topSold() throws RemoteException;
    String topLast() throws RemoteException;

}
