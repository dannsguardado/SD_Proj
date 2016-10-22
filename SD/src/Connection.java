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
    Users log = null;
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
                        System.out.printf(info.toString()); // TESTE DE IMPRESSÃO de HASHMAP
                    }
                    makeThings(info);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void makeThings(HashMap<String,String> info) {
        if (log == null) {
            if ("login".compareTo(info.get("type")) == 0) {
                log = new Users(info.get("username"), info.get("password"));
                try {
                    log = rmiConnection.login(log);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (log == null)
                    outToClient.println("type: login, ok: false\n");
                else
                    outToClient.println("type: login, ok: true\n");

            } else if ("register".compareTo(info.get("type")) == 0) {
                log = new Users(info.get("username"), info.get("password"));
                try {
                    log = rmiConnection.register(log);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (log.getUsernameID() == -1)
                    outToClient.println("type: register, ok: false\n");
                else
                    outToClient.println("type: register, ok: true\n");
                log = null;
            }
        }
            /*if (log != null) {
                if("create_auction".compareTo((String)info.get("type"))== 0) {
                    auction = new Auctions( (int)info.get("code"), (String)info.get("title"), (String)info.get("description"), (int)info.get("amount"));
                    auction = rmiConnection.create(auction);
                    info = new HashMap();
                    System.out.println(auction);
                    info.put("code", (String)auction.getCode());
                    info.put("title", auction.getTitle());
                    info.put("description", auction.getDescription());
                    info.put("amount", (String)auction.getAmount());
                }

                        /*else if("search_auction".compareTo((String)info.get("type"))==0){


                        }

                }
            }*/
    }
}
