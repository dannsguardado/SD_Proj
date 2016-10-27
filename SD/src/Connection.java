import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by henriquecabral on 21/10/16.
 */
public class Connection extends Thread {
    BufferedReader inFromClient;
    PrintWriter outToClient;
    int numeroLigacao;
    Socket client;
    RMI rmiConnection;
    RMIConnection rmi_conn;
    Users userLog = null;
    Auctions auction;
    ArrayList<PrintWriter> clients;


    public Connection(Socket client, int numeroLigacao, RMIConnection rmi_conn, ArrayList<PrintWriter> clients) {
        try {
            this.client = client;
            this.numeroLigacao = numeroLigacao;
            this.rmi_conn = rmi_conn;
            this.inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.outToClient = new PrintWriter(client.getOutputStream(), true);
            clients.add(this.outToClient);
            this.clients = clients;
            this.rmiConnection = rmi_conn.getRmiConnection();
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String messageFromClient;
            HashMap<String, String> info = new HashMap<>();
            while (!client.isClosed()) {
                while ((messageFromClient = inFromClient.readLine()) != null) {
                    System.out.println(messageFromClient);
                    String[] keyValuePairs = messageFromClient.split(", ");
                    for (String pair : keyValuePairs) {
                        String[] entry = pair.split(": ");
                        info.put(entry[0].trim(), entry[1].trim());
                    }
                    System.out.println(info);
                    makeThings(info);
                    info.clear();
                }
            }
            //rmiConnection.logs(userLog, 0);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeThings(HashMap<String, String> info) {
        boolean made_request;
        if (userLog == null) {
            if ("login".compareTo(info.get("type")) == 0) {
                made_request = false;
                userLog = new Users(info.get("username"), info.get("password"));
                while(made_request==false) {
                    try {
                        userLog = rmiConnection.login(userLog);
                        made_request = true;
                    } catch (RemoteException e) {
                        rmiConnection = rmi_conn.getRmiConnection();
                    }
                }
                if (userLog == null)
                    outToClient.println("type: login, ok: false\n");
                else if(userLog.getIsBan()==1){
                    userLog = null;
                    outToClient.println("type: login, ok: You have been banned\n");
                }
                else {
                    outToClient.println("type: login, ok: true\n");
                }


            } else if ("register".compareTo(info.get("type")) == 0) {
                made_request = false;
                userLog = new Users(info.get("username"), info.get("password"));
                while (made_request==false)
                {
                    try {
                        userLog = rmiConnection.register(userLog);
                        made_request = true;
                    } catch (RemoteException e) {
                        rmiConnection = rmi_conn.getRmiConnection();
                    }
                }
                //FALTA VERIFICAR SE OO USERNAME JA EXISTE
                outToClient.println("type: register, ok: true\n");
                userLog = null;
            }
             else if ("register_admin".compareTo(info.get("type")) == 0) {
                made_request = false;
                userLog = new Users(info.get("username"), info.get("password"));
                userLog.setIsAdmin(1);
                while (made_request==false)
                {
                    try {
                        userLog = rmiConnection.register(userLog);
                        made_request = true;
                    } catch (RemoteException e) {
                        rmiConnection = rmi_conn.getRmiConnection();
                    }
                }
                outToClient.println("type: register, ok: true\n");
                userLog = null;
            }
<<<<<<< HEAD
        }else if (userLog != null ) { //&& userLog.getUsernameID() != -1
            switch (info.get("type")) {
                case "create_auction": {
                    made_request = false;
                    Timestamp dataLimite =  java.sql.Timestamp.valueOf (info.get("deadline").concat(":0"));
                    System.out.println("o que o mano criou foi "+ dataLimite);
                    auction = new Auctions(Long.parseLong(info.get("code")), info.get("title"), info.get("description"), Float.parseFloat(info.get("amount")), userLog.getName(), dataLimite);
                    while(made_request==false)
                    {
                        try {
                            auction = rmiConnection.create(auction, userLog.getUsernameID());
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
=======
        }else if (userLog != null&& userLog.getUsernameID() != -1 ) {
            switch (info.get("type")) {
                case "create_auction": {
                    Timestamp dataLimite =  java.sql.Timestamp.valueOf (info.get("deadline").concat(":0"));
                    System.out.println("o que o mano criou foi "+ dataLimite);
                    auction = new Auctions(Long.parseLong(info.get("code")), info.get("title"), info.get("description"), Float.parseFloat(info.get("amount")), userLog.getName(), dataLimite);
                    try {
                        System.out.println(userLog.getUsernameID());
                        auction = rmiConnection.create(auction, userLog.getUsernameID());
                    } catch (RemoteException e) {
                        e.printStackTrace();
>>>>>>> origin/master
                    }
                    outToClient.println("type: create_auction, ok: true");
                    break;
                }
                case "search_auction": {
                    made_request = false;
                    ArrayList<Auctions> auctions = new ArrayList<Auctions>();
                    while(made_request==false)
                    {
                        try {
                            auctions = rmiConnection.search(Long.parseLong(info.get("code")));
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
                    }
                    if (auctions == null) {
                        outToClient.println("type: search_auction, items_count: 0");
                    } else {
                        int items_count = auctions.size();
                        String out = "type: search_auction, items_count: " + items_count;
                        for (int i = 0; i < items_count; i++) {//falta dar get do id
                            out = out.concat(", items_");
                            out = out.concat(Integer.toString(i));
                            out = out.concat("_id: ");
                            out = out.concat(Integer.toString(auctions.get(i).getAuctionID()));
                            out = out.concat(", items_");
                            out = out.concat(Integer.toString(i));
                            out = out.concat("_code: ");
                            out = out.concat(Long.toString(auctions.get(i).getCode()));
                            out = out.concat(", items_");
                            out = out.concat(Integer.toString(i));
                            out = out.concat("_title: ");
                            out = out.concat(auctions.get(i).getTitle());
                        }
                        outToClient.println(out);
                    }
                    break;
                }
                case "detail_auction": {
                    auction = findAuctionByID(info);
                    if (auction == null) {
                        outToClient.println("type: detail_auction, items_count: 0");
                    } else { //FALTA AQUI ADD INFO DE BID E DATAS
                        outToClient.println("type: detail_auction, title: " + auction.getTitle() + " description: " + auction.getDescription());
                    }
                    break;
                }
                case "my_auctions": {
                    made_request = false;
                    ArrayList<Auctions> auctions = new ArrayList<Auctions>();
                    while(made_request==false)
                    {
                        try {
                            auctions = rmiConnection.myauctions(userLog.getName());
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
                    }
                    if (auctions == null) {
                        outToClient.println("type: search_auction, items_count: 0");
                    } else {
                        int items_count = auctions.size();
                        String out = "type: search_auction, items_count: " + items_count;
                        for (int i = 0; i < items_count; i++) {//falta dar get do id
                            out = out.concat(", items_");
                            out = out.concat(Integer.toString(i));
                            out = out.concat("_id: ");
                            out = out.concat(Integer.toString(auctions.get(i).getAuctionID()));
                            out = out.concat(", items_");
                            out = out.concat(Integer.toString(i));
                            out = out.concat("_code: ");
                            out = out.concat(Long.toString(auctions.get(i).getCode()));
                            out = out.concat(", items_");
                            out = out.concat(Integer.toString(i));
                            out = out.concat("_title: ");
                            out = out.concat(auctions.get(i).getTitle());
                        }
                        outToClient.println(out);
                    }
                    break;
                }
                case "bid": {
<<<<<<< HEAD
                    made_request = false;
                    Bid bid = null;
                    while (made_request == false)
                    {
                        try {
                            bid = rmiConnection.makeBid(userLog.getName(), Integer.parseInt(info.get("id")), Integer.parseInt(info.get("amount")));
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }

=======
                    Bid bid = null;
                    try {
                        bid = rmiConnection.makeBid(userLog.getName(), Integer.parseInt(info.get("id")), Integer.parseInt(info.get("amount")));
                    } catch (RemoteException e) {
                        e.printStackTrace();
>>>>>>> origin/master
                    }
                    if (bid == null){
                        outToClient.println("type: bid, ok: false");
                    }
                    else {
                        outToClient.println("type: bid, ok: true");
                    }

                    break;
                }
                case "edit_auction": {
                    made_request = false;
                    auction = findAuctionByID(info);
                    while(made_request==false)
                    {
                        try {
                            String aux = null;

                            auction = rmiConnection.editAuction(auction,info);
                            made_request = true;
                            if (auction == null) {
                                outToClient.println("type: edit_auction, ok: false");
                            } else {
                                outToClient.println("type: edit_auction, ok: true");
                            }

                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }

                    }
                    break;
                }
                case "message": {
                    break;
                }
                case "online_users": {
                    made_request = false;
                    ArrayList<Users> users = new ArrayList<Users>();
                    String aux = "type: online_users, items_count: ";
                    while(made_request == false)
                    {
                        try {
                            users = rmiConnection.onlineUsers();
                            made_request=true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
                    }
                    if (users == null) {
                        outToClient.println("type: online_users, items_count: 0");
                    } else {
                        aux.concat(Integer.toString(users.size()));
                        aux.concat(",");
                        for (int i = 0; i <users.size(); i++){
                            aux = printUser(aux, users.get(i), i);
                        }
                    }
                    outToClient.println(aux);

                    break;
                }

                // ESTE SÂO APENAS PARA OS ADMIN'S!!!!
                case "cancel_auction": {
<<<<<<< HEAD
                    made_request = false;
=======
>>>>>>> origin/master
                    if(userLog.getIsAdmin() == 1){
                        auction = findAuctionByID(info);
                        while(made_request==false)
                        {
                            try {
                                auction = rmiConnection.cancelAuction(auction);
                                made_request = true;
                                if (auction == null) {
                                    outToClient.println("type: edit_auction, ok: false");
                                } else {
                                    outToClient.println("type: cancel_auction, ok: true");
                                }
                            } catch (RemoteException e) {
                                rmiConnection = rmi_conn.getRmiConnection();
                            }

                        }
                    }
                    else {
                        outToClient.println("type: cancel_auctions, ok: No premission");
                    }
                    break;
                }
                case "ban_user": {
<<<<<<< HEAD
                    made_request = false;
                    System.out.println("bora banir maninhos");
                    if(userLog.getIsAdmin() == 1){
                        String userBan = info.get("username");
                        while(made_request==false)
                        {
                            try {
                                userBan = rmiConnection.banUser(userBan);
                                made_request = true;
                                if (userBan == null) {
                                    outToClient.println("type: ban_user, ok: false");
                                } else {
                                    outToClient.println("type: ban_user, ok: true");
                                }
                            } catch (RemoteException e) {
                                rmiConnection = rmi_conn.getRmiConnection();
                            }

=======
                    System.out.println("bora banir maninhos");
                    if(userLog.getIsAdmin() == 1){
                        String userBan = info.get("username");
                        try {
                            userBan = rmiConnection.banUser(userBan);
                            if (userBan == null) {
                                outToClient.println("type: ban_user, ok: false");
                            } else {
                                outToClient.println("type: ban_user, ok: true");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
>>>>>>> origin/master
                        }
                    }
                    else {
                        outToClient.println("type: ban_user, ok: No premission");
                    }
                    break;
                }
                case "server_stats": {
                    break;
                }
                case "server_test": {
                    break;
                }
                case "exit":{
<<<<<<< HEAD
                    made_request = false;
                    while(made_request==false)
                    {
                        try{
                            rmiConnection.logs(userLog, 0);
                            made_request = true;
                            userLog = null;
                        }catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }

=======
                    try{
                        rmiConnection.logs(userLog, 0);
                        userLog = null;
                    }catch (RemoteException e) {
                        e.printStackTrace();
>>>>>>> origin/master
                    }
                    break;
                }
                default: {
                    outToClient.println("type: undefined, msg: verifique se o comando tem um type possível");
                    break;
                }

            }
        }
    }
    private String printUser(String out, Users user, int i){
        out = out.concat(" users_");
        out = out.concat(Integer.toString(i));
        out = out.concat("_username: ");
        out = out.concat(user.getName());
        return out;
    }

    private Auctions findAuctionByID(HashMap<String, String> info){
<<<<<<< HEAD
        boolean made_request = false;
        while(made_request==false)
        {

            try {
                Auctions aux = rmiConnection.detail(Long.parseLong(info.get("id")));
                made_request = true;
                if(aux !=null){
                    System.out.println("Encontrou correctamente a accao");
                }
                return aux;
            } catch (RemoteException e) {
                rmiConnection = rmi_conn.getRmiConnection();
            }
=======
        try {
            Auctions aux = rmiConnection.detail(Long.parseLong(info.get("id")));
           if(aux !=null){
               System.out.println("Encontrou correctamente a accao");
           }
           return aux;
        } catch (RemoteException e) {
            e.printStackTrace();
>>>>>>> origin/master
        }
        return null;
    }
}

