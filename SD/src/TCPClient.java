import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class establishes a TCP connection to a specified server, and loops
 * sending/receiving strings to/from the server.
 * <p>
 * The main() method receives two arguments specifying the server address and
 * the listening port.
 * <p>
 * The usage is similar to the 'telnet <address> <port>' command found in most
 * operating systems, to the 'netcat <host> <port>' command found in Linux,
 * and to the 'nc <hostname> <port>' found in macOS.
 *
 * @author Raul Barbosa
 * @author Alcides Fonseca
 * @version 1.1
 */
class TCPClient {
    public static void main(String[] args) {
        Socket socket;
        PrintWriter outToServer;
        BufferedReader inFromServer = null;
        try {
            // connect to the specified address:port (default is localhost:12345)
            if(args.length == 2)
                socket = new Socket(args[0], Integer.parseInt(args[1]));
            else
                socket = new Socket("localhost", 6000);

            // create streams for writing to and reading from the socket
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new PrintWriter(socket.getOutputStream(), true);

            // create a thread for reading from the keyboard and writing to the server
            new Thread() {
                public void run() {
                    Scanner keyboardScanner = new Scanner(System.in);
                    while(!socket.isClosed()) {
                        String readKeyboard = keyboardScanner.nextLine();
                        outToServer.println(readKeyboard);
                    }
                }
            }.start();

            // the main thread loops reading from the server and writing to System.out
            String messageFromServer;
            while((messageFromServer = inFromServer.readLine()) != null)
                System.out.println(messageFromServer);
        } catch (IOException e) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
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

                    }*/

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

                case "2":

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

                        HashMap newinfo;

                        newinfo = (HashMap) ois.readObject();
                        System.out.println(newinfo.get("code") + " " + newinfo.get("title") + " " + newinfo.get("description") + " " + newinfo.get("amount"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
            }
=======
>>>>>>> master
            if(inFromServer == null)
                System.out.println("\nUsage: java TCPClient <host> <port>\n");
            System.out.println(e.getMessage());
        } finally {
            try { inFromServer.close(); } catch (Exception e) {}
<<<<<<< HEAD
=======
>>>>>>> client_feature_branch
>>>>>>> master
        }
    }
}
