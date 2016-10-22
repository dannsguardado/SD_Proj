import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Scanner;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class TCPServer {

    private static RMI rmiConnection;

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);
        try {
<<<<<<< HEAD
=======
<<<<<<< HEAD
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

=======
>>>>>>> master
            int serverPort = 6000; //porto de recepção de ligações
            int rmiPort = 1099; // porto de ligação RMI
            String rmiName = "ibei";
            String rmiIp;
            int numeroLigacoes = 0;
            String path;

            // Mensagem de Inicio, Input de IP do RMI
            System.out.println("Estou à escuta no Porto " + serverPort);
            ServerSocket listenSocket = new ServerSocket(serverPort); //Cria um ServerSocket
            System.out.println("LISTEN SOCKET=" + listenSocket);
            System.out.print("IP do RMI:");
            rmiIp = sc.nextLine();
<<<<<<< HEAD

            while (true) {
=======
>>>>>>> client_feature_branch

            while (true) {
<<<<<<< HEAD
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

                        if("create_auction".compareTo((String)info.get("type"))== 0){
                            auction = new Auctions( (int)info.get("code"), (String)info.get("title"), (String)info.get("description"), (int)info.get("amount"));
                            auction = rmiConnection.create(auction);
                            info = new HashMap();
                            info.put("code", auction.getCode());
                            info.put("title", auction.getTitle());
                            info.put("description", auction.getDescription());
                            info.put("amount", auction.getAmount());
                            oos.writeObject(info);
                            oos.flush();

                        }

                        else if("search_auction".compareTo((String)info.get("type"))==0){
                            auction = new Auctions( (int)info.get("code") );
                            auction = rmiConnection.search(auction);
                            info = new HashMap();
                            info.put("code", auction.getCode());

                            oos.writeObject(info);

                           HashMap newinfo = new HashMap();
                            newinfo.put("code", auction.getCode());
                            newinfo.put("title", auction.getTitle());
                            newinfo.put("description", auction.getDescription());
                            newinfo.put("amount", auction.getAmount());
                            oos.writeObject(newinfo);
                            oos.flush();
                        }
                }
=======
>>>>>>> master
                Socket clientSocket = listenSocket.accept();
                System.getProperties().put("java.security.policy", "politicas.policy");
                String name = "rmi://" + rmiIp + ":" + rmiPort + "/" + rmiName;
                System.setProperty("java.rmi.server.hostname", rmiIp);
                rmiConnection = (RMI) Naming.lookup(name);
                System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                numeroLigacoes++;
                // Inicio de uma nova thread para tratar os clientes
                new Connection(clientSocket, numeroLigacoes, rmiConnection);
<<<<<<< HEAD
=======
>>>>>>> client_feature_branch
>>>>>>> master
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}