
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RMIServer implements RMI {

    private Connection conn = null;
    private String query;
    private PreparedStatement preparedStatement = null;

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
        return "Não encontro nada!";
    }



    public static void main(String[] args) {

        System.getProperties().put("java.security.policy", "politicas.policy");
        //System.setSecurityManager(new SecurityManager());
        int port = 1099;
        String name = "ibei";
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




    public Users login(Users user) {
        try {

            query = "SELECT nameuser, passworduser FROM user WHERE nameuser=? AND passworduser=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                Users newUser = new Users(user.getName(),user.getPassword());
                System.out.println("\nLogin de "+user.getName());
                return newUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

            query = "INSERT INTO user (nameuser,passworduser) VALUES (?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
            System.out.println("\nRegisto de "+user.getName());
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Auctions create(Auctions auction){
        System.out.println("\nCriação de leilao"); //ATENCAO AS DATAS
        try {
            query = "INSERT INTO leilao (idartigoleilao,datacriacaoleilao,dataterminoleilao,ativoleilao, tituloleilao, descricaoleilao, precomaximoleilao, user_nameuser) VALUES (?,?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            //preparedStatement.setInt(1, auction);
            preparedStatement.setLong(1, auction.getCode());
            preparedStatement.setLong(2, auction.getDatacriacao());
            preparedStatement.setLong(3, auction.getDataLimite());
            preparedStatement.setFloat(4, auction.getAtivo());
            preparedStatement.setString(5, auction.getTitle());
            preparedStatement.setString(6, auction.getDescription());
            preparedStatement.setFloat(7, auction.getAmount());
            preparedStatement.setString(8, auction.getAuctionUsername());
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
                auctions.add(new Auctions(rs.getInt("idleilao"), rs.getLong("idartigoleilao") , rs.getString("tituloleilao"), rs.getString("descricaoleilao"), rs.getFloat("precomaximoleilao"), rs.getString("user_nameuser")));
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
                auction= new Auctions(rs.getInt("idleilao"), rs.getLong("idartigoleilao") , rs.getString("tituloleilao"), rs.getString("descricaoleilao"), rs.getFloat("precomaximoleilao"), rs.getString("user_nameuser"));
            }

            return auction;


        } catch (SQLException e) {
            e.printStackTrace();
        }

    return auction;
    }



    private void BDconnect()
    {
        String dataBase = "jdbc:mysql://localhost/ibeibd";
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
