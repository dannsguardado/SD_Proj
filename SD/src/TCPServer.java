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

            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.getProperties().put("java.security.policy", "politicas.policy");
                String name = "rmi://" + rmiIp + ":" + rmiPort + "/" + rmiName;
                System.setProperty("java.rmi.server.hostname", rmiIp);
                rmiConnection = (RMI) Naming.lookup(name);
                System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                numeroLigacoes++;
                // Inicio de uma nova thread para tratar os clientes
                new Connection(clientSocket, numeroLigacoes, rmiConnection);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}