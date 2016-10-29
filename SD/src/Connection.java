import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;


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
            if (!client.isClosed()) {


                while ((messageFromClient = inFromClient.readLine()) != null) {
                    System.out.println(messageFromClient);
                    String[] keyValuePairs = messageFromClient.split(", ");
                    for (String pair : keyValuePairs) {
                        String[] entry = pair.split(": ");
                        info.put(entry[0].trim(), entry[1].trim());
                    }
                    System.out.println(info);

                    numeroLigacao++;
                    makeThings(info);
                    info.clear();
                }
                /*try{
                    outToClient.println("Clientes ligados: "+clients.size());
                    sleep(60000);
                    //outToClient.println("Clientes ligados: "+clients.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

            }

            rmiConnection.logs(userLog, 0);
            clients.remove(this.outToClient);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(2000);
        } catch (InterruptedException e) {
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
        }else if (userLog != null ) {
            switch (info.get("type")) {
                case "create_auction": {
                    made_request = false;
                    Timestamp dataLimite =  java.sql.Timestamp.valueOf (info.get("deadline").concat(":00"));
                    auction = new Auctions(Long.parseLong(info.get("code")), info.get("title"), info.get("description"), Float.parseFloat(info.get("amount")), userLog.getName(), dataLimite);
                    while(made_request==false)
                    {
                        try {
                            auction = rmiConnection.create(auction, userLog.getUsernameID());
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
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
                    ArrayList<Bid> bids = new ArrayList<Bid>();
                    ArrayList<Message> messages = new ArrayList<Message>();
                    if (auction == null) {
                        outToClient.println("type: detail_auction, items_count: 0");
                    } else {
                        String auxDeadline = auction.getDataLimite().toString();

                        try{
                            bids = rmiConnection.allUserBids(userLog);
                        } catch (RemoteException e) {
                            e.printStackTrace();

                        }
                        try{
                            messages = rmiConnection.allMessagesBid(auction);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        String auxClient = "type: detail_auction, title: " + auction.getTitle() + ", description: " + auction.getDescription()+", deadline: " + auxDeadline.substring(0, auxDeadline.length()-7);

                        if(messages == null && bids == null){
                            auxClient = auxClient.concat(", messages_count: 0");
                            auxClient = auxClient.concat(", code: ").concat(Long.toString(auction.getCode()));
                            auxClient = auxClient.concat(", bids_count: 0");
                            outToClient.println(auxClient);
                            break;
                        }
                        else if(messages == null ){
                            auxClient = auxClient.concat(", messages_count: 0");
                            auxClient = auxClient.concat(", code: ").concat(Long.toString(auction.getCode()));
                            outToClient.println(auxClient.concat(printBids(bids)));
                            break;
                        }
                        auxClient = auxClient.concat(", messages_count: ").concat(Integer.toString(messages.size()));
                        auxClient = auxClient.concat(", code: ").concat(Long.toString(auction.getCode()));
                        auxClient = auxClient.concat(printMessage(messages));
                        if(bids == null){
                            outToClient.println(auxClient.concat(", bids_count: 0"));
                            break;
                        }

                        outToClient.println(auxClient.concat(printBids(bids)));
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
                        ArrayList<Bid> bids = new ArrayList<Bid>();
                        try{
                            bids = rmiConnection.allUserBids(userLog);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (bids == null){
                            outToClient.println(printAuctions(auctions));
                            break;
                        }
                        outToClient.println(printAuctions(auctions).concat(printBids(bids)));
                    }
                    break;
                }
                case "bid": {
                    made_request = false;
                    Bid bid = null;
                    while (made_request == false)
                    {
                        try {
                            bid = rmiConnection.makeBid(userLog.getName(), Integer.parseInt(info.get("id")), Float.parseFloat(info.get("amount")));
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
                        if (bid == null){
                            outToClient.println("type: bid, ok: false");
                        }
                        else {
                            outToClient.println("type: bid, ok: true");
                        }
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

                            auction = rmiConnection.editAuction(auction, info);
                            made_request = true;

                        }catch (RemoteException e) {
                                rmiConnection = rmi_conn.getRmiConnection();
                            }

                            if (auction == null) {
                                outToClient.println("type: edit_auction, ok: false");
                            } else {
                                outToClient.println("type: edit_auction, ok: true");
                            }

                    }
                    break;
                }
                case "message": {
                    Message message = null;
                    made_request = false;
                    while(made_request==false) {
                        try {
                            message = rmiConnection.createMessage(info.get("text"), Integer.parseInt(info.get("id")), userLog.getName());
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
                        if (message == null) {
                            outToClient.println("type: message, ok: false");
                        } else {
                            outToClient.println("type: message, ok: true");
                        }
                    }
                    break;
                }
                case "online_users": {
                    made_request = false;
                    ArrayList<Users> users = null;
                    String aux = null;
                    while (made_request == false) {
                        try {
                            users = rmiConnection.onlineUsers();
                            made_request = true;
                        } catch (RemoteException e) {
                            rmiConnection = rmi_conn.getRmiConnection();
                        }
                    }
                    if (users == null) {
                        outToClient.println("type: online_users, users_count: 0");
                        break;
                    } else {
                        aux = "type: online_users, users_count: " + Integer.toString(users.size());
                        for (int i = 0; i < users.size(); i++) {
                            aux = printUser(aux, users.get(i), i);
                        }
                    }
                    outToClient.println(aux);

                    break;
                }

                // ESTE SÂO APENAS PARA OS ADMIN'S!!!!
                case "cancel_auction": {
                    made_request = false;
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
                    }
                    break;
                }

                default: {
                    outToClient.println("type: undefined, msg: verifique se o comando tem um type possível");
                    break;
                }

            }
        }
        /*try {
            rmiConnection.updateActiveAuctions();
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }

    private String printBids(ArrayList<Bid> bids){
        String out = ", bids_count: " + bids.size();
        for(int i = 0; i < bids.size(); i++){
            out = out.concat(", bid_").concat(Integer.toString(i)).concat("_username: ").concat(bids.get(i).getUsername());
            out = out.concat(", bid_").concat(Integer.toString(i)).concat("_amount: ").concat(Float.toString(bids.get(i).getValor()));
        }
        return out;
    }

    private String printMessage(ArrayList<Message> messagens){
        String out = null;
        for(int i = 0; i < messagens.size(); i++){
            out = ", messages_";
            out = out.concat(Integer.toString(i)).concat("_username: ").concat(messagens.get(i).getUsername());
            out = out.concat(", messages_").concat(Integer.toString(i)).concat("_text: ").concat(messagens.get(i).getMessagem());
        }
        return out;
    }

    private String printUser(String out, Users user, int i){
        out = out.concat(", users_");
        out = out.concat(Integer.toString(i));
        out = out.concat("_username: ");
        out = out.concat(user.getName());
        return out;
    }
    private String printAuctions(ArrayList<Auctions> auctions){
        String out = "type: search_auction, items_count: " + auctions.size();
        for (int i = 0; i < auctions.size(); i++) {//falta dar get do id
            out = out.concat(", items_").concat(Integer.toString(i)).concat("_id: ").concat(Integer.toString(auctions.get(i).getAuctionID()));
            out = out.concat(", items_").concat(Integer.toString(i)).concat("_code: ").concat(Long.toString(auctions.get(i).getCode()));
            out = out.concat(", items_").concat(Integer.toString(i)).concat("_title: ").concat(auctions.get(i).getTitle());
        }
        return out;
    }

    private Auctions findAuctionByID(HashMap<String, String> info){
        boolean made_request = false;
        while(made_request==false)
        {

            try {
                Auctions aux = rmiConnection.detail(Long.parseLong(info.get("id")));
                made_request = true;
                return aux;
            } catch (RemoteException e) {
                rmiConnection = rmi_conn.getRmiConnection();
            }

        }
        return null;
    }
}

