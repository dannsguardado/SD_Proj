
import java.io.*;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class TCPAdmin{

    static RMI rmiConnection;

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String rmiName = "ibei";
        System.out.print("Admin mode\nIP do RMI:");
        String rmiIp = sc.nextLine();
        int rmiPort = 1099; // porto de ligação RMI
        try {
            String name = "rmi://" + rmiIp + ":" + rmiPort + "/" + rmiName;
            rmiConnection = (RMI) Naming.lookup(name);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e1) {
            e1.printStackTrace();
        }
        login();
    }

    private static void login() {

        String name, password;

        Scanner sc = new Scanner(System.in);
        System.out.printf("\nLOGIN\n");

        System.out.println("Nome: ");
        name = sc.nextLine();

        System.out.println("Password: ");
        password = sc.nextLine();


        Users user = new Users(name, password);
        try {
            rmiConnection.login(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        menu();
    }

    public static void register() {

        String name, password;

        Scanner sc = new Scanner(System.in);
        System.out.printf("\nREGISTER\n");

        System.out.println("Nome: ");
        name = sc.nextLine();

        System.out.println("Password: ");
        password = sc.nextLine();


        Users user = new Users(name, password);
        try {
            rmiConnection.register(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        menu();
    }

    public static void menu() {

        while (true) {
            System.out.println("\nMenu!!");
            System.out.println("1 - Cancelar leilao");
            System.out.println("2 - Banir user");
            System.out.println("3 - Estatísticas");
            System.out.println("4 - Testar servidor");

            System.out.printf("\nOpcao: ");
            Scanner sc = new Scanner(System.in);
            String opcao = sc.nextLine();

            switch (opcao) {
                case "1"://cancelar leilao
                    System.out.println("Introduza id do leilao:");
                    long id = sc.nextLong();
                    Auctions auction = null;
                    try{
                        auction = rmiConnection.detail(id);
                        try{
                            auction = rmiConnection.cancelAuction(auction);
                        }catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if(auction == null){
                        System.out.println("Leilão não cancelado");
                    }
                    else {
                        System.out.println("Leilão cancelado");
                    }
                    break;
                case "2": //banir user
                    System.out.println("Introduza nome do user:");
                    String userBan = sc.nextLine();
                    try{
                        userBan = rmiConnection.banUser(userBan);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if(userBan == null){
                        System.out.println("User não banido");
                    }
                    else {
                        System.out.println("User banido");
                    }
                    break;
                case "3"://estatisticas
                    System.out.println("Estatísticas de utilização");
                    String auctionsCreated = null;
                    String auctionsSold = null;
                    String auctionsLast = null;
                    try{
                        auctionsCreated = rmiConnection.topAuctionsCreated();

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try{
                        auctionsSold = rmiConnection.topSold();

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try{
                        auctionsLast = rmiConnection.topLast();

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if(auctionsCreated == null){
                        System.out.println("Top 10 utilizadores com mais leilões criados: Erro");
                    }
                    else if(auctionsSold == null){
                        System.out.println("Top 10 utilizadores que mais leilões venceram: Erro");
                    }
                    else if(auctionsLast == null){
                        System.out.println("Número total de leilões nos últimos 10 dias: Erro");
                    }
                    else {
                        System.out.println("Top 10 utilizadores com mais leilões criados: ");
                        System.out.println(auctionsCreated);
                        System.out.println("Top 10 utilizadores que mais leilões venceram: ");
                        System.out.println(auctionsSold);
                        System.out.println("Número total de leilões nos últimos 10 dias");
                        System.out.println(auctionsLast);
                    }
                    break;
                case"4"://testes servidor
                   /* try{

                       // auction = rmiConnection.create();
                        try{
                           // auction = rmiConnection.cancelAuction(auction);
                        }catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }*/

                    break;
                default:
                    System.out.println("Escolha errada. Introduza um inteiro entre 1 e 4");
            }
        }
    }
}

