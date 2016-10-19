import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class TCPServer {

    private static RMI rmiConnection;


    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);
        try {
            int serverPort = 6000; //porto=6000
            int serverNumber;

                int numero = 0;

                String path;



                System.out.println("A Escuta no Porto 6000");
                ServerSocket listenSocket = new ServerSocket(serverPort);
                System.out.println("LISTEN SOCKET=" + listenSocket);
                System.out.print("IP do RMI:");
                String ip = sc.nextLine();

                while (true) {
                    Socket clientSocket = listenSocket.accept();
                    System.getProperties().put("java.security.policy", "politicas.policy");
                    int rmiport = 1099;
                    String name  = "rmi://"+ip+":"+rmiport+"/ola";

                    System.setProperty("java.rmi.server.hostname", ip);
                    rmiConnection = (RMI) Naming.lookup(name);
                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    new Connection(clientSocket, numero, rmiConnection);
                }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


}

class Connection extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    int numero;
    Socket client;
    RMI rmiConnection;
    Users log = null;
    Auctions auction;


    public Connection(Socket client, int numero, RMI rmiConnection) {
        try {
            this.client = client;
            this.numero = numero;
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
