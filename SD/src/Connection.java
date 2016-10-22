import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by henriquecabral on 21/10/16.
 */
public class Connection extends Thread {
    BufferedReader inFromClient;
    PrintWriter outToClient;
    int numeroLigacao;
    Socket client;
    RMI rmiConnection;
    Users userLog = null;
    Auctions auction;


    public Connection(Socket client, int numeroLigacao, RMI rmiConnection) {
        try {
            this.client = client;
            this.numeroLigacao = numeroLigacao;
            this.rmiConnection = rmiConnection;
            this.inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.outToClient = new PrintWriter(client.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String messageFromClient;
            HashMap<String,String> info = new HashMap<>();

            while (!client.isClosed()) {
                while ((messageFromClient = inFromClient.readLine()) != null) {
                    String[] keyValuePairs = messageFromClient.split(", ");
                    for(String pair : keyValuePairs) {
                        String[] entry = pair.split(": ");
                        info.put(entry[0].trim(), entry[1].trim());
                    }
                    System.out.println(info);
                    makeThings(info);
                    info.clear();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void makeThings(HashMap<String,String> info) {
        if (userLog == null) {
            if ("login".compareTo(info.get("type")) == 0) {
                userLog = new Users(info.get("username"), info.get("password"));
                try {
                    userLog = rmiConnection.login(userLog);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (userLog == null)
                    outToClient.println("type: login, ok: false\n");
                else
                    outToClient.println("type: login, ok: true\n");

            } else if ("register".compareTo(info.get("type")) == 0) {
                userLog = new Users(info.get("username"), info.get("password"));
                try {
                    userLog = rmiConnection.register(userLog);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (userLog.getUsernameID() == -1)
                    outToClient.println("type: register, ok: false\n");
                else
                    outToClient.println("type: register, ok: true\n");
                userLog = null;
            }
        }
        else
        {
            switch(info.get("type")) {
                case "create_auction": {
                    auction = new Auctions(Long.parseLong(info.get("code")), info.get("title"), info.get("description"), Float.parseFloat(info.get("amount")));
                    try {
                        auction = rmiConnection.create(auction);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    outToClient.println("type: create_auction, ok: true");
                }
                case "search_auction": {


                }
                case "": {

                }
                default:
                {
                    outToClient.println("type: undefined, msg: verifique se o comando tem um type possível");
                }

            }
        }
    }
}
