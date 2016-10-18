import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ritaalmeida on 18/10/16.
 */
public class TCPClient {

    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public static void main(String args[])
    {
        try {
            String in;
            Scanner sc = new Scanner(System.in);
            Socket conn = new Socket("localhost",6000);
            dos = new DataOutputStream(conn.getOutputStream());
            dis = new DataInputStream(conn.getInputStream());
            oos = new ObjectOutputStream(dos);
            ois = new ObjectInputStream(dis);
            while(true)
            {
                System.out.print("Funcao:");
                in = sc.nextLine();
                if (in.compareTo("login") == 0){
                    login();
                }
                else if(in.compareTo("close") == 0){
                    return;
                }
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void login() {

        String name, password;

        Scanner sc = new Scanner(System.in);
        System.out.printf("LOGIN");

        System.out.println("Nome: ");
        name = sc.nextLine();

        System.out.println("Password: ");
        password = sc.nextLine();

        Users log = new Users(name, password);

        try {
            dos.writeInt(1);
            oos.writeObject(log);
            oos.flush();

            log = (Users)ois.readObject();

            if (log == null){
                System.out.println("\n ERRO NO LOGIN!\n \n");
                login();
            }
            else{
                System.out.printf("LOGIN CERTO");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
