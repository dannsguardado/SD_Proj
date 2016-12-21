package model;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**

 * Created by ritaalmeida on 24/11/16.
 */
public class SessionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private RMI rmiConnection;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private Socket socket;
    private Users user;
    private int rmiPort = 1098; // porto de ligação RMI
    private String rmiName = "ibei";
    private String rmiIp = "localhost";
    private ArrayList<PrintWriter> clients = new ArrayList<PrintWriter>();
    private ArrayList<WaitNotification> waitNotifications = new ArrayList<>();


    public SessionModel() {
        createSocket();
    }

    public void createSocket()
    {
        try
        {
            rmiConnection.testRMI();

            socket = new Socket("localhost", 6005);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            objOut = new ObjectOutputStream(out);
            objIn = new ObjectInputStream(in);
            waitNotifications = (ArrayList<WaitNotification>)objIn.readObject();


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
                return false;
            } else {
                return true;
            }
        } catch (RemoteException ex) {
            System.err.println("Error on Login, Remote Exeption: " + ex);
            createSocket();
        }

        return false;
    }

    public boolean loginFacebook(String idFacebook, String tokenFacebook, String username){

        try {
            if(rmiConnection.loginFacebook(idFacebook, tokenFacebook, username) == true);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Users getIDFacebook(Users user){
        try {
            return rmiConnection.getIDFacebook(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Users getMyIDFacebook(String id){
        try {
            return rmiConnection.getMyIDFacebook(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Users removeFacebook(Users user){

        try {
            return rmiConnection.removeFacebook(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Auctions createAuction(long code, String title, String description, float amount, java.sql.Timestamp dataLimite){

        Auctions auction = new Auctions(code, title, description, amount, user.getName(), dataLimite);
        try
        {
            return rmiConnection.create(auction,user.getUsernameID(),true);
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

    public boolean sendMessage(long id, String username){
        try
        {
            if(rmiConnection.createNotification(username,id))return true;
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return false;
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
            if (auction != null){
                return rmiConnection.editAuction(auction, info);
            }
            else {
                return null;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Auctions> searchAuction(long code){
        try {
            return rmiConnection.search(code);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Message> getMessages(Auctions code){
        try {
            return rmiConnection.allMessagesBid(code);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Bid> getBids(Auctions code){
        try {
            return rmiConnection.allBidsAuction(code);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bid getBestBid(long code){
        try {
            return rmiConnection.bestBid(code);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
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
    public boolean logOut(int on){
        try {
            rmiConnection.logs(user,on);
            return true;

        } catch (RemoteException ex) {
            System.err.println("Error on Login, Remote Exeption: " + ex);
        }
        return false;
    }


    public boolean register(String username, String  password){
        user = new Users(username, password);
        try {
            if((user = rmiConnection.register(user)) == null ){
                return false;
            }
            else{
                rmiConnection.login(user);
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Auctions> myAuctions(String name){
        name = user.getName();
        try {
            return rmiConnection.myauctions(name);
        } catch (RemoteException e) {
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
