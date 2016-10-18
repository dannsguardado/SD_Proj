import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String name = "ola";
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
        System.out.println("Login de "+user.getName());

        try {

            query = "SELECT id, username, password FROM users WHERE username=? AND password=?";
        preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next())
        {
            Users newUser = new Users(user.getName(),user.getPassword());
            return newUser;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }



    private void BDconnect()
    {
        String dataBase = "jdbc:mysql://localhost/bd";
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
