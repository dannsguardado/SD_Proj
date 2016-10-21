import java.io.*;
import java.net.Socket;
import java.util.*;
import java.rmi.RemoteException;

/**
 * Created by henriquecabral on 21/10/16.
 */
public class Connection extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;

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
            this.dis = new DataInputStream(client.getInputStream());
            this.dos = new DataOutputStream(client.getOutputStream());
            this.oos = new ObjectOutputStream(dos);
            this.ois = new ObjectInputStream(dis);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                HashMap info;

                int choose;

                while (log == null ) {
                    info = (HashMap) ois.readObject();
                    if ("login".compareTo((String)info.get("type"))==0) {
                        log = new Users((String) info.get("username"),(String)info.get("password"));
                        log = rmiConnection.login(log);
                        if (log == null){
                            info.put("username",null);
                            info.put("password",null);
                        }
                        else {
                            info = new HashMap();
                            info.put("username", log.getName());
                            info.put("password", log.getPassword());
                        }
                        oos.writeObject(info);
                    }

                    else if ("register".compareTo((String) info.get("type")) == 0){
                        log = new Users ((String) info.get("username"), (String)info.get("password"));
                        log = rmiConnection.register(log);
                        info = new HashMap();

                        info.put("username", log.getName());
                        info.put("password", log.getPassword());
                        oos.writeObject(info);
                        oos.flush();

                        if(log.getUsernameID()==-1){
                            log = null;
                        }
                        if (log!= null){
                            dis.readInt();
                            log = new Users ((String) info.get("username"), (String)info.get("password"));
                            log = rmiConnection.login(log);
                            info.put("username", log.getName());
                            info.put("password", log.getPassword());
                            oos.writeObject(info);
                            oos.flush();
                        }
                    }
                }
                if (log != null){
                    info = (HashMap) ois.readObject();
                    while(true){
                        if("create_auction".compareTo((String)info.get("type"))== 0){
                            auction = new Auctions( (int)info.get("code"), (String)info.get("title"), (String)info.get("description"), (int)info.get("amount"));
                            auction = rmiConnection.create(auction);
                            info = new HashMap();
                            System.out.println(auction);
                            info.put("code", auction.getCode());
                            info.put("title", auction.getTitle());
                            info.put("description", auction.getDescription());
                            info.put("amount", auction.getAmount());
                            oos.writeObject(info);
                            oos.flush();

                        }

                        /*else if("search_auction".compareTo((String)info.get("type"))==0){


                        }*/
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
