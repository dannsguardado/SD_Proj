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
/*
private static void login() {

        HashMap info = new HashMap();
        String name, password;

        Scanner sc = new Scanner(System.in);
        System.out.printf("\nLOGIN\n");

        System.out.println("Nome: ");
        name = sc.nextLine();

        System.out.println("Password: ");
        password = sc.nextLine();


        try {
            info.put("type","login");
            info.put("username", name);
            info.put("password", password);
            oos.writeObject(info);
            oos.flush();

            info = (HashMap) ois.readObject();


            if (info.get("username") == null && info.get("password") == null) {
                System.out.println("\n ERRO NO LOGIN!\n \n");
                login();
            } else {
                System.out.printf("\nLOGIN CERTO\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        menu();
    }

    public static void register() {

        HashMap info = new HashMap();
        String name, password;
        Scanner sc = new Scanner(System.in);

        System.out.printf("\nREGISTER\n");

        System.out.println("Nome: ");
        name = sc.nextLine();

        System.out.println("Password: ");
        password = sc.nextLine();



        try {
            info.put("type", "register");
            info.put("username", name);
            info.put("password", password);
            oos.writeObject(info);
            oos.flush();

            info = (HashMap) ois.readObject();


            if (info.get("username") == null && info.get("password") == null) {
                System.out.println("\n WRONG SIGN UP!\n Exiting now...\n");
                register();
            }
            else {
                System.out.println("\nSIGN UP ACCEPTED!\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        menu();

    }

    public static void menu() {

        Scanner sc = new Scanner(System.in);
        HashMap type = new HashMap();

        while (true) {
            System.out.println("\nMenu!!");
            System.out.println("1 - Criar leilao");
            System.out.println("2 - Procurar leilao");

            System.out.printf("\nOpcao: ");
            String opcao = sc.nextLine();

            switch (opcao) {
                case "1":

                    HashMap info = new HashMap();
                    //Auctions auction = new Auctions();

                    int code, amount;
                    String title, description, code_aux, amount_aux;
                    //Date dateLimit;

                    System.out.printf("\nCRIAR LEILAO\n");

                    System.out.println("\nCode: ");
                    code_aux = sc.nextLine();
                    code = Integer.parseInt(code_aux);
                    //auction.setCode(sc.nextInt());


                    System.out.println("\nTitle: ");
                    title = sc.nextLine();
                    //auction.setTitle(sc.nextLine());


                    System.out.println("\nDescription: ");
                    description = sc.nextLine();
                    //auction.setDescription(sc.nextLine());


                    System.out.println("\nAmount: ");
                    amount_aux = sc.nextLine();
                    amount = Integer.parseInt(amount_aux);
                    //auction.setAmount(sc.nextInt());


                    /*while (true) {

                        System.out.println("\n Deadline: ");
                        System.out.println("Ano: ");
                        String ano = sc.nextLine();
                        System.out.println("Mes: ");
                        String mes = sc.nextLine();
                        System.out.printf("Dia: ");
                        String dia = sc.nextLine();
                        dateLimit = new Date(Integer.parseInt(ano) - 1900, Integer.parseInt(mes) - 1, Integer.parseInt(dia))
                        //auction.setDateLimit(new Date(Integer.parseInt(ano) -                                                                                         1900, Integer.parseInt(mes) - 1, Integer.parseInt(dia)));

                    }

                    try {
        info.put("type","create_auction");
        info.put("code", code);
        info.put("title", title);
        info.put("description", description);
        //FALTA A DATA
        info.put("amount", amount);

        oos.writeObject(info);
        oos.flush();

        info = (HashMap) ois.readObject();


    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

                /*case "2":

                    int searchAuction;
                    String searchAuction_aux;

                    info = new HashMap();
                    System.out.println("Code of auction: ");
                    searchAuction_aux = sc.nextLine();
                    searchAuction = Integer.parseInt(searchAuction_aux);

                    try {
                        info.put("type","search_auction");
                        info.put("code", searchAuction);
                        oos.writeObject(info);
                        oos.flush();


                        info = (HashMap) ois.readObject();


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
}
 */
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
