import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RMIServer implements RMI {

    private Connection conn = null;
    private String query;
    private PreparedStatement preparedStatement = null;
    private static int number_rmi;

    public RMIServer() throws RemoteException{
        super();
        BDconnect();

    }

    /*
   * Esta função serve apenas para uma primeira impressão de teste para que se possa verificar
   * que o Servidor Primário se conectou ao RMI
   * **/
    public String printTest() throws RemoteException
    {
        System.out.println("Connected to TCP Server");
        return "Connected to RMI";
    }

    public String printNone() throws RemoteException {
        return ">>>Testing connection<<<";
    }

    public String testRMI() throws RemoteException
    {
        return "RMI Primary alive";
    }



    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if(args.length==0)
        {
            System.out.print("RMI number: ");
            number_rmi = sc.nextInt();
        }
        else
        {
            number_rmi = Integer.parseInt(args[0]);
        }
        System.getProperties().put("java.security.policy", "politicas.policy");
        //System.setSecurityManager(new SecurityManager());
        int port = 1099;
        String name = "ibei";
        if(number_rmi==1)
        {
            try
            {
                RMI rmi = new RMIServer();
                RMI stub = (RMI) UnicastRemoteObject.exportObject(rmi,0);
                Registry regis = LocateRegistry.createRegistry(port);
                regis.rebind(name, stub);
                System.out.println("RMI Server Up!");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
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

    public Auctions create(Auctions auction, int id){
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
            return auction;
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
                query = "UPDATE leilao SET precomaximoleilao = ? WHERE idleilao = ? ";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(info.get("amount")));
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
        for(int i = 0; i < auctionsUser.size(); i++){
            cancelAuction(auctionsUser.get(i));
        }
        //actualizar banUser BD
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

    public Bid makeBid(String username, long idLeilao, int amount){

        System.out.println("\nCriação de licitacao");
        if(detail(idLeilao).getAtivo()== 0){ // protecao de valor de bid ja ultrapassado e leilao activo
            Bid bid = bestBid(idLeilao);
            System.out.println("leilao ativo");
            if(bid==null){
                System.out.println("primeira licitacao");
                return createBid(username, idLeilao, amount);
            }
            else if(bid.getValor()>amount){
                return createBid(username, idLeilao, amount);
            }
        }
        return null;
    }
    private Bid createBid(String username, long idLeilao, int amount){
        try {
            query = "INSERT INTO licitacao (valorlicitacao,user_nameuser,leilao_idleilao) VALUES (?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, username);
            preparedStatement.setLong(3, idLeilao);
            preparedStatement.executeUpdate();
            Bid bid = new Bid(amount, username, idLeilao);
            return bid;
        } catch (SQLException e) {
            e.printStackTrace();
    }
    return null;
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
