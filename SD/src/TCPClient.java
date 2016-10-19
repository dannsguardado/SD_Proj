import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.Date;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class TCPClient {

    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public static void main(String args[]) {
        try {
            String in;
            Scanner sc = new Scanner(System.in);
            Socket conn = new Socket("localhost", 6000);
            dos = new DataOutputStream(conn.getOutputStream());
            dis = new DataInputStream(conn.getInputStream());
            oos = new ObjectOutputStream(dos);
            ois = new ObjectInputStream(dis);
            while (true) {
                System.out.print("Funcao:");
                in = sc.nextLine();
                if (in.compareTo("login") == 0) {
                    login();
                } else if (in.compareTo("register") == 0) {
                    register();
                } else if (in.compareTo("close") == 0) {
                    return;
                }
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void login() {

        HashMap info = new HashMap();
        String name, password;

        Scanner sc = new Scanner(System.in);
        System.out.printf("\nLOGIN\n");

        System.out.println("Nome: ");
        name = sc.nextLine();

        System.out.println("Password: ");
        password = sc.nextLine();


        try {
            info.put("type","login");
            info.put("username", name);
            info.put("password", password);
            oos.writeObject(info);
            oos.flush();

            info = (HashMap) ois.readObject();


            if (info.get("username") == null && info.get("password") == null) {
                System.out.println("\n ERRO NO LOGIN!\n \n");
                login();
            } else {
                System.out.printf("\nLOGIN CERTO\n");
            }


        } catch (IOException e) {
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

            System.out.printf("\nOpcao: ");
            String opcao = sc.nextLine();

            switch (opcao) {
                case "1":

                    HashMap info = new HashMap();
                    //Auctions auction = new Auctions();

                    int code, amount;
                    String title, description, code_aux;
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
                    amount = sc.nextInt();
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
            }
        }
    }
}

