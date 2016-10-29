import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Integer;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class TCPServer {

    static int numeroUserON1;
    static int numeroUserON2;
    static int numeorUserON3;

    public static void main(String args[]) {
        String[] addresses = getConfigurations();
        UDPSocket udpSocket;
        Scanner sc = new Scanner(System.in);
        int numeroLigacoes = 0;
        int server_num = 0;
        int rmiPort = 1099; // porto de ligação RMI
        String rmiName = "ibei";
        String rmiIp;
        int serverPort = 0;
        String path;
        ServerSocket listenSocket = null;
        ArrayList<PrintWriter> clients = new ArrayList<PrintWriter>();
        // Mensagem de Inicio, Input de IP do RMI
        while (server_num < 3) {
            String[] split = addresses[server_num].split("-");
            serverPort = Integer.parseInt(split[1]);
            try {
                listenSocket = new ServerSocket(serverPort); //Cria um ServerSocket
                System.out.println("Estou à escuta no Porto " + serverPort);
                System.out.println("LISTEN SOCKET=" + listenSocket);
                if (server_num == 0) {
                    udpSocket = new UDPSocket(addresses[0], addresses[1], addresses[2], clients);
                    numeroUserON1++;
                } else if (server_num == 1) {
                    udpSocket = new UDPSocket(addresses[1], addresses[0], addresses[2], clients);
                    numeroUserON2++;
                } else {
                    udpSocket = new UDPSocket(addresses[2], addresses[0], addresses[1], clients);
                    numeorUserON3++;
                }
                break;
            } catch (IOException e) {

            }
            ++server_num;
        }

        System.out.print("IP do RMI:");
        rmiIp = sc.nextLine();
        RMIConnection rmi_conn = new RMIConnection(rmiIp, rmiPort, rmiName, clients);

        while (true) {
            Socket clientSocket = null;

            try {
                clientSocket = listenSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);


            // Inicio de uma nova thread para tratar os clientes
            new Connection(clientSocket, numeroLigacoes, rmi_conn, clients);

        }

    }


    public static String[] getConfigurations() {
        File file = new File("servers.conf");
        Scanner sc = new Scanner(System.in);
        String[] addresses = new String[3];

        BufferedReader br = null;
        BufferedWriter bw = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            try {
                int i = 0;
                while ((line = br.readLine()) != null) {
                    addresses[i] = line;
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            try {
                bw = new BufferedWriter(new FileWriter(file));
                System.out.println("Address of server");
                for (int i = 0; i < 3; i++) {
                    System.out.print("nº" + (i + 1) + ":");
                    addresses[i] = sc.next();
                    bw.write(addresses[i] + "\n");
                }
                bw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        return addresses;
    }

    public static class UDPSocket extends Thread
    {
        String address_1;
        String address_2;
        String my_addresss;
        ArrayList<PrintWriter> clients;
        DatagramSocket dataSocket;

        UDPSocket(String my_addresss, String address_1, String address_2, ArrayList<PrintWriter> clients )
        {
            this.address_1 = address_1;
            this.address_2 = address_2;
            this.clients = clients;
            this.my_addresss = my_addresss;
            this.start();
        }

       public void run()
        {
            String []split = my_addresss.split("-"), split_1 = address_1.split("-"), split_2 = address_2.split("-");
            int my_port = Integer.parseInt(split[2]), port_1, port_2;
            try
            {
                byte []buffer;
                InetAddress aHost_1 = null, aHost_2 = null;
                dataSocket = new DatagramSocket(my_port);
                try
                {
                    aHost_1 = InetAddress.getByName(split_1[0]);
                }
                catch (UnknownHostException e)
                {
                    System.err.println("UnknownHostException");
                }
                try
                {
                    aHost_2 = InetAddress.getByName(split_2[0]);
                } catch (UnknownHostException e)
                {
                    System.err.println("UnknownHostException");
                }
                port_1 = Integer.parseInt(split_1[2]);
                port_2 = Integer.parseInt(split_2[2]);
                DatagramPacket request_1,request_2,reply_1,reply_2;
                while(true)
                {
                    buffer = Integer.toString(clients.size()).getBytes();
                    System.out.println(Integer.toString(clients.size()));
                    request_1=new DatagramPacket(buffer,buffer.length,aHost_1,port_1);
                    try
                    {
                        dataSocket.send(request_1);
                        System.out.println("Server 1 sends: " );

                    }
                    catch (IOException e)
                    {
                        System.err.println("Can't send ping...");
                    }
                    request_2=new DatagramPacket(buffer,buffer.length,aHost_2,port_2);
                    try
                    {
                        dataSocket.send(request_2);
                        System.out.println("Server 2 sends: "  );
                    }
                    catch (IOException e)
                    {
                        System.err.println("Can't send ping...");
                    }
                    buffer = new byte [
                    reply_1 = new DatagramPacket(buffer,buffer.length);
                    try
                    {
                        dataSocket.receive(reply_1);
                        System.out.println("Recebeu: " +  new String(reply_1.getData()));
                    }
                    catch (IOException e)
                    {
                        System.err.println("Can't receive ping...");
                    }
                    reply_2 = new DatagramPacket(buffer,buffer.length);
                    try
                    {
                        dataSocket.receive(reply_2);
                        System.out.println("Recebeu: " + new String(reply_2.getData()));

                    }
                    catch (IOException e)
                    {
                        System.err.println("Can't receive ping...");
                    }
                    try
                    {
                        this.sleep(5000);
                    }
                    catch (InterruptedException e)
                    {

                        e.printStackTrace();
                    }
                }
            }
            catch (SocketException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(dataSocket!=null)
                {
                    dataSocket.close();

                }
            }
        }
    }
}

