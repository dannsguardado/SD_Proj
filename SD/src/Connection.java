import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
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
    Users userLog = null;
    Auctions auction;


    public Connection(Socket client, int numeroLigacao, RMI rmiConnection) {
        try {
            this.client = client;
            this.numeroLigacao = numeroLigacao;
            this.rmiConnection = rmiConnection;
            this.inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.outToClient = new PrintWriter(client.getOutputStream(), true);
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
        if (userLog == null) {
            if ("login".compareTo(info.get("type")) == 0) {
                userLog = new Users(info.get("username"), info.get("password"));
                try {
                    userLog = rmiConnection.login(userLog);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (userLog == null)
                    outToClient.println("type: login, ok: false\n");
                else {
                    System.out.println("és admin mano ?? "+ userLog.getIsAdmin());
                    outToClient.println("type: login, ok: true\n");
                }


            } else if ("register".compareTo(info.get("type")) == 0) {
                userLog = new Users(info.get("username"), info.get("password"));
                try {
                    userLog = rmiConnection.register(userLog);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                //FALTA VERIFICAR SE OO USERNAME JA EXISTE
                outToClient.println("type: register, ok: true\n");
                userLog = null;
            }
             else if ("register_admin".compareTo(info.get("type")) == 0) {
                userLog = new Users(info.get("username"), info.get("password"));
                userLog.setIsAdmin(1);
                try {
                    userLog = rmiConnection.register(userLog);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                outToClient.println("type: register, ok: true\n");
                userLog = null;
            }
        }else if (userLog != null&& userLog.getUsernameID() != -1 ) {
            System.out.println("ola");
            switch (info.get("type")) {
                case "create_auction": {
                    System.out.println("O "+ userLog.getName()+ " criou um leilao");

                    auction = new Auctions(Long.parseLong(info.get("code")), info.get("title"), info.get("description"), Float.parseFloat(info.get("amount")), userLog.getName());
                    try {
                        System.out.println(userLog.getUsernameID());
                        auction = rmiConnection.create(auction, userLog.getUsernameID());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    outToClient.println("type: create_auction, ok: true");
                    break;
                }
                case "search_auction": {
                    ArrayList<Auctions> auctions = new ArrayList<Auctions>();
                    try {
                        auctions = rmiConnection.search(Long.parseLong(info.get("code")));
                    } catch (RemoteException e) {
                        e.printStackTrace();
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
                    ArrayList<Auctions> auctions = new ArrayList<Auctions>();
                    try {
                        auctions = rmiConnection.myauctions(userLog.getName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
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
                    break;
                }
                case "edit_auction": {
                    auction = findAuctionByID(info);
                    try {
                        String aux = null;

                        auction = rmiConnection.editAuction(auction,info);
                        if (auction == null) {
                            outToClient.println("type: edit_auction, ok: false");
                        } else {
                            outToClient.println("type: edit_auction, ok: true");
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "message": {
                    break;
                }
                case "online_users": {
                    ArrayList<Users> users = new ArrayList<Users>();
                    String aux = "type: online_users, items_count: ";
                    try {
                        users = rmiConnection.onlineUsers();
                    } catch (RemoteException e) {
                        e.printStackTrace();
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
                    System.out.println("és admin mano ?? "+ userLog.getIsAdmin());
                    if(userLog.getIsAdmin() == 1){
                        auction = findAuctionByID(info);
                        try {
                            auction = rmiConnection.cancelAuction(auction);
                            if (auction == null) {
                                outToClient.println("type: edit_auction, ok: false");
                            } else {
                                outToClient.println("type: cancel_auction, ok: true");
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        outToClient.println("type: cancel_auctions, ok: No premission");
                    }
                    break;
                }
                case "ban_user": {
                    break;
                }
                case "server_stats": {
                    break;
                }
                case "server_test": {
                    break;
                }
                case "exit":{
                    try{
                        rmiConnection.logs(userLog, 0);
                    }catch (RemoteException e) {
                        e.printStackTrace();
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
        try {
           return rmiConnection.detail(Long.parseLong(info.get("id")));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;

    }
}

