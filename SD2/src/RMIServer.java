

import model.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;

import static java.lang.Thread.sleep;

public class RMIServer implements RMI {

    private Connection conn = null;
    private String query;
    private PreparedStatement preparedStatement = null;
    private static int number_rmi;

    public RMIServer() throws RemoteException {
        super();
        BDconnect();

    }

    public String printNone() throws RemoteException {
        return ">>>Testing connection<<<";
    }

    public String testRMI() throws RemoteException {
        return "RMI Primary alive";
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (args.length == 0) {
            System.out.print("RMI number: ");
            number_rmi = sc.nextInt();
        } else {
            number_rmi = Integer.parseInt(args[0]);
        }
        System.getProperties().put("java.security.policy", "politicas.policy");
        //System.setSecurityManager(new SecurityManager());
        int port = 1098;
        String name = "ibei";
        if (number_rmi == 1) {
            try {
                RMI rmi = new RMIServer();
                RMI stub = (RMI) UnicastRemoteObject.exportObject(rmi, 0);
                Registry regis = LocateRegistry.createRegistry(port);
                regis.rebind(name, stub);
                System.out.println("RMI Server Up!");
                new Thread() {
                    public void run() {
                        while (true) {
                            try {
                                rmi.updateActiveAuctions();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            try {
                                sleep(30000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new RMISecondaryConnection(port, name).start();
        }

    }


    static class RMISecondaryConnection extends Thread
    {
        private int port;
        private String path;

        RMISecondaryConnection(int port, String path)
        {
            this.port = port;
            this.path = path;
        }

        public void run()
        {
            Scanner sc = new Scanner(System.in);
            String rmiIp;
            int tries = 0;
            System.out.print("Ip do RMI primário: ");
            rmiIp = sc.next();
            System.getProperties().put("java.security.policy", "politicas.policy");
            String sec_name = "rmi://" + rmiIp + ":" + port + "/" + path;
            System.setProperty("java.rmi.server.hostname", rmiIp);



            while(tries<3) {
                try {
                    RMI rmiConnection = (RMI) Naming.lookup(sec_name);
                    System.out.println("RMI primário visto, e RMI secundário UP!");
                    tries = 0;
                    while(true)
                    {
                        System.out.println(rmiConnection.testRMI());

                        try
                        {
                            this.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    tries++;
                    System.err.println("Cannot Connect, try "+tries);
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            try {
                new RMIServer().main(new String[]{"1"});
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    public void updateActiveAuctions(){

            try {
                query = "UPDATE leilao SET ativoleilao = 0 WHERE dataterminoleilao < now()";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public void lastAcessRegister(Users user){
        try{
            System.out.println("actualizou");
            query = "UPDATE user SET lastacessuser = now() WHERE nameuser = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Users login(Users user) {
        try {

            query = "SELECT nameuser, passworduser, isadminuser, banuser FROM user WHERE nameuser=? AND passworduser=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                Users newUser = new Users(user.getName(), rs.getInt("banuser"),user.getPassword());
                newUser.setIsAdmin(rs.getInt("isadminuser"));
                logs(newUser, 1);
                System.out.println("\nLogin de "+user.getName());
                return newUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void logs(Users user, int on){
        try{
            query = "UPDATE user SET isliveuser= ? WHERE nameuser = ? ";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, on);
            preparedStatement.setString(2, user.getName());
            preparedStatement.executeUpdate();
            if (on == 0){
                lastAcessRegister(user);
            }


        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public Users register(Users user){
        try {
            query = "SELECT * FROM user WHERE nameuser = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                return new Users((-1));
            }

            query = "INSERT INTO user (nameuser,passworduser, isadminuser, banuser) VALUES (?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getIsAdmin());
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
            System.out.println("\nRegisto de "+user.getName());
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Auctions create(Auctions auction, int id, boolean idcheck){
        System.out.println("\nCriação de leilao"); //ATENCAO AS DATAS
        try {
            query = "INSERT INTO leilao (idartigoleilao,datacriacaoleilao,dataterminoleilao,ativoleilao, tituloleilao, descricaoleilao, precomaximoleilao, user_nameuser) VALUES (?,?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, auction.getCode());
            preparedStatement.setTimestamp(2, auction.getDatacriacao());
            preparedStatement.setTimestamp(3, auction.getDataLimite());
            preparedStatement.setFloat(4, auction.getAtivo());
            preparedStatement.setString(5, auction.getTitle());
            preparedStatement.setString(6, auction.getDescription());
            preparedStatement.setFloat(7, auction.getAmount());
            preparedStatement.setString(8, auction.getAuction_username());
            preparedStatement.executeUpdate();

            if (idcheck){
                auction = getId(auction);
            }
            return auction;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Auctions getId(Auctions auction){
        try {
            query = "SELECT * FROM leilao WHERE idartigoleilao = ? AND tituloleilao = ? ";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, auction.getCode());
            preparedStatement.setString(2, auction.getTitle());
           /* preparedStatement.setString(3, auction.getDescription());
            preparedStatement.setFloat(4, auction.getAmount());
            preparedStatement.setString(5, auction.getAuction_username());*/
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("correu bem");
            if(rs.next()){
                auction.setAuctionID(rs.getInt("idleilao"));
                return auction;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("deu mrd");
        return null;
    }

    public ArrayList<Auctions> search(Long code){
        System.out.println("Search leilao");
        ArrayList<Auctions> auctions = new ArrayList<Auctions>();
        try {
            query = "SELECT * FROM leilao WHERE idartigoleilao = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, code);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                auctions.add(new Auctions(rs.getLong("idartigoleilao"), rs.getString("tituloleilao"), rs.getString("descricaoleilao"), rs.getFloat("precomaximoleilao"), rs.getString("user_nameuser"), rs.getInt("idleilao"), new java.sql.Timestamp ( rs.getLong("dataterminoleilao"))));
            }
            if(auctions.size()!=0){
                return auctions;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Auctions detail(Long code){
        System.out.println("Detail leilao");
        Auctions auction = null;
        try {
            query = "SELECT * FROM leilao WHERE idleilao = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, code);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                auction= new Auctions(rs.getLong("idartigoleilao"), rs.getString("tituloleilao"), rs.getString("descricaoleilao"), rs.getFloat("precomaximoleilao"), rs.getString("user_nameuser"), rs.getInt("idleilao"), new java.sql.Timestamp(rs.getLong("dataterminoleilao")));
            }

            return auction;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return auction;
    }

    public ArrayList<Auctions> myauctions(String name){
        System.out.println("Search My auctions");
        ArrayList<Auctions> auctions = new ArrayList<Auctions>();
        try {
            query = "SELECT * FROM leilao WHERE user_nameuser = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                    auctions.add(new Auctions(rs.getLong("idartigoleilao"), rs.getString("tituloleilao"), rs.getString("descricaoleilao"), rs.getFloat("precomaximoleilao"), rs.getString("user_nameuser"), rs.getInt("idleilao"), new java.sql.Timestamp ( rs.getLong("dataterminoleilao"))));
            }
            System.out.println(auctions.size());
            if(auctions.size()!=0){
                return auctions;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Auctions editAuction(Auctions auction, HashMap<String, String> info){
        System.out.println("Editar leilao ");
        try {
            if(info.get("deadline")!=null){
                System.out.println("editar deadline para"+ info + " com id ");//auction.getAuctionID());
                query = "UPDATE leilao SET dataterminoleilao = ? WHERE idleilao = ? ";
                preparedStatement = conn.prepareStatement(query);
                Timestamp dataLimite =  java.sql.Timestamp.valueOf (info.get("deadline").concat(":0"));
                preparedStatement.setTimestamp(1,dataLimite);

            }
            else if(info.get("amount")!=null) {
                System.out.println("editou");
                query = "UPDATE leilao SET precomaximoleilao = ? WHERE idleilao = ? ";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setFloat(1, Float.parseFloat(info.get("amount")));
            }
            else if(info.get("description")!=null) {
                query = "UPDATE leilao SET descricaoleilao = ? WHERE idleilao = ? ";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, info.get("description"));
            }
            else if(info.get("title")!=null) {
                query = "UPDATE leilao SET tituloleilao = ? WHERE idleilao = ? ";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, info.get("title"));
            }

            preparedStatement.setInt(2, auction.getAuctionID());
            preparedStatement.executeUpdate();
            return auction;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Users> onlineUsers(){
        System.out.println("Search leilao");
        ArrayList<Users> users = new ArrayList<Users>();
        try {
            query = "SELECT * FROM user WHERE isliveuser = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, 1);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                users.add(new Users(rs.getString("nameuser"), rs.getString("passworduser")));
            }
            if(users.size()!=0){
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Auctions cancelAuction(Auctions auction){
        try{
            query = "UPDATE leilao SET ativoleilao = ? WHERE idleilao = ? ";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, 0);

            preparedStatement.setInt(2, auction.getAuctionID());
            preparedStatement.executeUpdate();
            return auction;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String banUser(String user){
        ArrayList <Auctions> auctionsUser = myauctions(user);
        if (auctionsUser != null){
            for(int i = 0; i < auctionsUser.size(); i++){
                cancelAuction(auctionsUser.get(i));
            }

        }
        try{
            query = "UPDATE user SET banuser = ? WHERE nameuser = ? ";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, 1);

            preparedStatement.setString(2, user);
            preparedStatement.executeUpdate();
            deleteBid(user);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bid makeBid(String username, long idLeilao, float amount){

        System.out.println("\nCriação de licitacao");
        if(detail(idLeilao).getAtivo()== 1){ // protecao de valor de bid ja ultrapassado e leilao activo
            Bid bid = bestBid(idLeilao);
            System.out.println("leilao ativo");
            if(bid==null){
                return createBid(username, idLeilao, amount);
            }
            else if(bid.getValor()>amount){

                return createBid(username, idLeilao, amount);
            }
        }
        return null;
    }

    public String onlineNotification(String username){
        String aux = null;
        PreparedStatement preparedStatement1 = null;
        try{
            query = "SELECT DISTINCT leilao_idleilao FROM licitacao WHERE user_nameuser = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                System.out.println("verificar leilao de id " + rs.getInt("leilao_idleilao"));
                String query1 = "(SELECT l.user_nameuser, l.valorlicitacao FROM licitacao l WHERE l.valorlicitacao = ? AND ((SELECT u.lastacessuser FROM user u WHERE u.nameuser = ? AND isliveuser = 1) < l.datalicitacao))";
                preparedStatement1 = conn.prepareStatement(query1);
                preparedStatement1.setFloat(1, bestBid(rs.getLong("leilao_idleilao")).getValor());
                preparedStatement1.setString(2,username);
                // System.out.println("A melhor lcitacao é " + bestBid(rs.getLong("leilao_idleilao")).getIdLeilao());
                ResultSet rs1 = preparedStatement1.executeQuery();
                if(rs1.next()){
                    if (aux==null)aux="";
                    aux = aux.concat("\n" + "O leilao de id ").concat(Long.toString(rs.getLong("leilao_idleilao")));
                    aux = aux.concat(" recebeu uma melhor licitação de valor ").concat(Float.toString(rs1.getFloat("valorlicitacao")));
                }
            }
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bid createBid(String username, long idLeilao, float amount){
        if (checkActive(idLeilao)){
            try {
                query = "INSERT INTO licitacao (valorlicitacao,user_nameuser,leilao_idleilao) VALUES (?,?,?)";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setFloat(1, amount);
                preparedStatement.setString(2, username);
                preparedStatement.setLong(3, idLeilao);
                preparedStatement.executeUpdate();
                Bid bid = new Bid(amount, username, idLeilao);
                onlineNotification(username);
                return bid;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private Boolean checkActive(long idleilao){
        try {
            query = "SELECT * FROM leilao WHERE idleilao = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setFloat(1, idleilao);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next() && rs.getInt("ativoleilao")== 1) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String deleteBid(String username){
        System.out.println("\nApagar licitacao"); //ATENCAO AS DATAS
        try {
            query = "DELETE FROM licitacao WHERE user_nameuser=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return username;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Bid> allBidsAuction(Auctions auction){
        ArrayList<Bid> bids = new ArrayList<Bid>();
        try {
            query = "SELECT * FROM licitacao WHERE leilao_idleilao = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, auction.getAuctionID());
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                bids.add(new Bid(rs.getInt("idlicitacao"), rs.getInt("valorlicitacao"), rs.getString("user_nameuser"), rs.getInt("leilao_idleilao")));
            }
            if(bids.size()!=0){
                return bids;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Bid> allUserBids(Users user){
        ArrayList<Bid> bids = new ArrayList<Bid>();
        try {
            query = "SELECT * FROM licitacao WHERE user_nameuser = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                bids.add(new Bid(rs.getInt("idlicitacao"), rs.getInt("valorlicitacao"), rs.getString("user_nameuser"), rs.getInt("leilao_idleilao")));
            }
            if(bids.size()!=0){
                return bids;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Message> allMessagesBid(Auctions auction){
        ArrayList<Message> messages = new ArrayList<Message>();
        try {
            query = "SELECT * FROM mensagem WHERE leilao_idleilao= ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, auction.getAuctionID());
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                messages.add(new Message(rs.getInt("idmensagem"), rs.getString("conteudomensagem"), rs.getString("user_nameuser"), rs.getInt("leilao_idleilao")));
            }
            if(messages.size()!=0){
                return messages;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bid bestBid(long id){
        System.out.println("\nMelhor licitacao");
        Bid bid = null;
        try {
            query = "SELECT * FROM licitacao WHERE leilao_idleilao=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                bid = new Bid(rs.getInt("idlicitacao"), rs.getInt("valorlicitacao"), rs.getString("user_nameuser"), id);
                if (rs.next() && bid.getValor() > rs.getInt("valorlicitacao")) {
                    bid = new Bid(rs.getInt("idlicitacao"), rs.getInt("valorlicitacao"), rs.getString("user_nameuser"), id);
                }
            }
            return bid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Message createMessage(String mensagem, long idleilao, String username){
        System.out.println("ENSAGEM");
        try {
            query = "INSERT INTO mensagem (conteudomensagem, user_nameuser,leilao_idleilao) VALUES (?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, mensagem);
            preparedStatement.setString(2, username);
            preparedStatement.setLong(3, idleilao);
            preparedStatement.executeUpdate();
            System.out.println("Messagem realizada");
            Message message = new Message(mensagem, username, idleilao);
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Users> topAuctionsCreated(){
        try{
            query = "SELECT user_nameuser, count(idleilao) FROM leilao group by user_nameuser order by count(idleilao) DESC ";
            preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            int i= 0;
            ArrayList<Users> users = new ArrayList<Users>();
            while(rs.next() && i < 10) {
                users.add(new Users(rs.getString("user_nameuser"),rs.getInt("count(idleilao)")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Users> topSold(){
        try{
            query = "select licitacao.user_nameuser,count(user_nameuser) from licitacao where licitacao.valorlicitacao in (SELECT min(valorlicitacao) FROM licitacao, leilao where leilao.ativoleilao=0 GROUP BY leilao_idleilao) GROUP BY user_nameuser ";
            preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            int i= 0;
            ArrayList<Users> users = new ArrayList<Users>();
            while(rs.next() && i < 10) {
                users.add(new Users(rs.getString("user_nameuser"), rs.getInt("count(user_nameuser)")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int topLast(){
        try{
            query = "SELECT COUNT(idleilao) FROM leilao where leilao.datacriacaoleilao > ADDDATE(now(), -10)";
            preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt("COUNT(idleilao)");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }




    private void BDconnect()
    {
        String dataBase = "jdbc:mysql://localhost/ibeic";
        String userdb = "root";
        String passdb = "";
        try
        {
            conn = DriverManager.getConnection(dataBase, userdb, passdb);
        }
        catch (SQLException e)
        {
            System.err.println("SQL Exception:"+e);
        }
    }

}
