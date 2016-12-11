package model;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ritaalmeida on 24/11/16.
 */
public class SessionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private RMI rmiConnection;
    private Users user;
    private int rmiPort = 1098; // porto de ligação RMI
    private String rmiName = "ibei";
    private String rmiIp = "localhost";
    private ArrayList<PrintWriter> clients = new ArrayList<PrintWriter>();

    public SessionModel() {
        createSocket();
    }

    public void createSocket()
    {
        try
        {
            rmiConnection.testRMI();
        }
        catch(Exception e)
        {
            RMIConnection rmi_conn = new RMIConnection(rmiIp, rmiPort, rmiName, clients);
            rmiConnection = rmi_conn.getRmiConnection();
        }
    }

    public RMI getRmiConnection()
    {
        return rmiConnection;
    }

    public boolean login(String username, String password) {

            try {
                user = new Users(username, password);
                if ((user = rmiConnection.login(user)) == null) {
                    System.out.println("Login: false");
                    return false;
                } else {
                    System.out.println("Login: true");
                    return true;
                }
            } catch (RemoteException ex) {
                System.err.println("Error on Login, Remote Exeption: " + ex);
                createSocket();
            }

        return false;
    }

    public Auctions createAuction(long code, String title, String description, float amount, java.sql.Timestamp dataLimite){

        Auctions auction = new Auctions(code, title, description, amount, user.getName(), dataLimite);
        try
        {
            return rmiConnection.create(auction,user.getUsernameID(),false);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Bid createBid(long code, float amount){
        try
        {
            return rmiConnection.makeBid(user.getName(),code, amount);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Message messageAuction(long code, String message){
        try
        {
            return rmiConnection.createMessage(message ,code, user.getName());
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String banUser(String username){
        try
        {
            return rmiConnection.banUser(username);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Users> estatisticas1(){
        try
        {
            return rmiConnection.topAuctionsCreated();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Users> estatisticas2(){
        try
        {
            return rmiConnection.topSold();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public int estatisticas3(){
        try
        {
            return rmiConnection.topLast();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public Auctions cancelAuction(int id){
        try
        {
            return rmiConnection.cancelAuction(findAuctionByID(id));
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Auctions editAuction(long code, HashMap<String, String> info){


        Auctions auction= findAuctionByID(code);

        try
        {
            return rmiConnection.editAuction(auction, info);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Auctions> searchAuction(long code){
        System.out.println("Search Auction");
        try {
            System.out.println("Login: true");
            return rmiConnection.search(code);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Login: false");
        return null;
    }

    public Auctions detailAuction(Long ID){
        try {

            return rmiConnection.detail(ID);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Auctions findAuctionByID(long code){
        try {
            return rmiConnection.detail(code);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
