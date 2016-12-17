
import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TCPAdmin{

    static RMI rmiConnection;
    static Users userLog = null;

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
        while (userLog == null){

            System.out.println("Nome: ");
            name = sc.nextLine();

            System.out.println("Password: ");
            password = sc.nextLine();

            try {
                userLog = rmiConnection.login(new Users(name, password));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if(userLog == null){
                System.out.println("Login incorreto");
            }
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
                    }else {
                        System.out.println(auctionsCreated);
                    }
                    if(auctionsSold == null){
                        System.out.println("Top 10 utilizadores que mais leilões venceram: Erro");
                    }
                    else {
                        System.out.println(auctionsSold);
                    }
                    if(auctionsLast == null){
                        System.out.println("Número total de leilões nos últimos 10 dias: Erro");
                    }
                    else {
                        System.out.println(auctionsLast);
                    }

                    break;
                case"4"://testes servidor
                    auction = null;//testar criar leilao
                    Random rnd = new Random();
                    java.util.Date date = new java.util.Date();
                    java.sql.Timestamp dataLimite = new java.sql.Timestamp(date.getTime());;
                    dataLimite.setTime(date.getTime() + rnd.nextInt());
                    String title = generateString(20);
                    String description = generateString(30);
                    auction = new Auctions(rnd.nextInt(), title, description, rnd.nextFloat(), userLog.getName(), dataLimite);

                    try {
                        auction = rmiConnection.create(auction, userLog.getUsernameID(), true);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    if (auction == null){
                        System.out.println("type: create_auction, ok: false");
                        break;
                    }
                    else {
                        System.out.println("type: create_auction, ok: true");
                    }
                    //Testar fazer licitacao
                    Bid bid = null;
                    try {
                        bid = rmiConnection.makeBid(userLog.getName(), auction.getAuctionID(), auction.getAmount()-1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (bid == null){
                        System.out.println("type: bid, ok: false");
                    }
                    else {
                        System.out.println("type: bid, ok: true");
                    }


                    // testar consultar detalhes
                    ArrayList<Bid> bids = new ArrayList<Bid>();
                    ArrayList<Message> messages = new ArrayList<Message>();
/*
                    try {
                        auction = rmiConnection.detail();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
*/
                    if (auction == null){
                        System.out.println("type: detail_auction, ok: false");
                    }
                    try{
                        bids = rmiConnection.allBidsAuction(auction);
                    } catch (RemoteException e) {
                        e.printStackTrace();

                    }
                    try{
                        messages = rmiConnection.allMessagesBid(auction);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    String auxDeadline = auction.getDataLimite().toString();
                    String auxClient = "type: detail_auction, title: " + auction.getTitle() + ", description: " + auction.getDescription()+", deadline: " + auxDeadline.substring(0, auxDeadline.length()-7);

                    if(messages == null && bids == null){
                        auxClient = auxClient.concat(", messages_count: 0");
                        auxClient = auxClient.concat(", code: ").concat(Long.toString(auction.getCode()));
                        auxClient = auxClient.concat(", bids_count: 0");
                        System.out.println(auxClient);
                        break;
                    }
                    else if(messages == null ){
                        auxClient = auxClient.concat(", messages_count: 0");
                        auxClient = auxClient.concat(", code: ").concat(Long.toString(auction.getCode()));
                        System.out.println(auxClient.concat(printBids(bids)));
                        break;
                    }
                    auxClient = auxClient.concat(", messages_count: ").concat(Integer.toString(messages.size()));
                    auxClient = auxClient.concat(", code: ").concat(Long.toString(auction.getCode()));
                    auxClient = auxClient.concat(printMessage(messages));
                    if(bids == null){
                        System.out.println(auxClient.concat(", bids_count: 0"));
                        break;
                    }

                    System.out.println(auxClient.concat(printBids(bids)));

                    break;
                default:
                    System.out.println("Escolha errada. Introduza um inteiro entre 1 e 4");
            }
        }
    }
    public static String generateString(int length) {
        String characters = "abcdefghijlmnopqrstuvxzwy";
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private static String printBids(ArrayList<Bid> bids){
        String out = ", bids_count: " + bids.size();
        for(int i = 0; i < bids.size(); i++){
            out = out.concat(", bid_").concat(Integer.toString(i)).concat("_username: ").concat(bids.get(i).getUsername());
            out = out.concat(", bid_").concat(Integer.toString(i)).concat("_amount: ").concat(Float.toString(bids.get(i).getValor()));
        }
        return out;
    }

    private static String printMessage(ArrayList<Message> messagens){
        String out = null;
        for(int i = 0; i < messagens.size(); i++){
            out = ", messages_";
            out = out.concat(Integer.toString(i)).concat("_username: ").concat(messagens.get(i).getUsername());
            out = out.concat(", messages_").concat(Integer.toString(i)).concat("_text: ").concat(messagens.get(i).getMessagem());
        }
        return out;
    }

}

