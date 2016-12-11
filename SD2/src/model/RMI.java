package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.rmi.*;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public interface RMI extends Remote{

    public String testRMI() throws RemoteException;
    public String printNone() throws RemoteException;
    public Users login(Users user) throws RemoteException;
    public Users register(Users user) throws RemoteException;
    public Auctions create(Auctions auction, int id, boolean idcheck) throws RemoteException;
    public Auctions detail(Long code) throws RemoteException;
    public ArrayList<Auctions> search(Long code) throws RemoteException;
    public ArrayList<Auctions> myauctions(String name) throws RemoteException;
    public Auctions editAuction(Auctions auction, HashMap<String, String> info) throws RemoteException;
    public void logs(Users user, int on) throws RemoteException;
    public ArrayList<Users> onlineUsers() throws RemoteException;
    public Auctions cancelAuction(Auctions auction) throws RemoteException;
    public String banUser(String user) throws RemoteException;
    public Bid makeBid(String username, long idLeilao, float amount) throws RemoteException;
    public String deleteBid(String username) throws RemoteException;
    public Message createMessage(String mensagem, long idleilao, String username) throws RemoteException;
    public ArrayList<Bid> allUserBids(Users user) throws  RemoteException;
    public ArrayList<Message>allMessagesBid(Auctions auction) throws RemoteException;
    public ArrayList<Users> topAuctionsCreated() throws RemoteException;
    public ArrayList<Users> topSold() throws RemoteException;
    public int topLast() throws RemoteException;
    public ArrayList<Bid> allBidsAuction(Auctions auction) throws RemoteException;
    public void updateActiveAuctions() throws RemoteException;
    public void lastAcessRegister(Users user) throws  RemoteException;
    public String onlineNotification(String username) throws  RemoteException;
}
