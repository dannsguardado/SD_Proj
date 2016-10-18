import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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


                int choose;
                while (log == null) {
                    choose = dis.readInt();
                    if (choose == 1) {
                        log = (Users) ois.readObject();
                        log = rmiConnection.login(log);
                        oos.writeObject(log);
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
