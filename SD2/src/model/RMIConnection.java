package model;

import model.RMI;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by ritaalmeida on 26/10/16.
 */
class RMIConnection
{
    private String rmiIp;
    private int rmiPort;
    private String rmiName;
    private RMI rmiConnection;
    private HandleConnection handle;
    private ArrayList<PrintWriter> clients;

    RMIConnection(String rmiIp, int rmiPort, String rmiName, ArrayList<PrintWriter> clients)
    {
        this.rmiIp = rmiIp;
        this.rmiPort = rmiPort;
        this.rmiName = rmiName;
        this.clients = clients;
    }

    public RMI getRmiConnection()
    {
        try
        {
            rmiConnection.printNone();
        }
        catch (RemoteException e)
        {
            rmiConnection=null;
        }
        catch(NullPointerException e)
        {}
        if(rmiConnection==null)
        {
            handle = new HandleConnection(rmiConnection, clients);
            handle.start();
            try {
                handle.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            rmiConnection = handle.getRmiConnection();
        }
        return rmiConnection;
    }


    public class HandleConnection extends Thread
    {
        public RMI rmiConnection;
        public int time=0;
        ArrayList<PrintWriter> clients;

        HandleConnection(RMI rmiConnection,ArrayList<PrintWriter> clients)
        {
            this.rmiConnection = rmiConnection;
            this.clients = clients;
        }

        public RMI getRmiConnection() {
            return rmiConnection;
        }

        public int getTime() {
            return time;
        }

        public void run()
        {
            time = 0;
            boolean get_conn = false;
            System.getProperties().put("java.security.policy", "politicas.policy");
            String name = "rmi://" + rmiIp + ":" + rmiPort + "/" + rmiName;
            System.setProperty("java.rmi.server.hostname", rmiIp);
            while(get_conn == false)
            {
                try
                {
                    rmiConnection = (RMI) Naming.lookup(name);
                    get_conn = true;
                }
                catch (NotBoundException e)
                {
                    e.printStackTrace();
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (RemoteException e)
                {
                    System.err.println("RMI NOT FOUND!");
                    time++;
                    if(time>30)
                    {
                        for (int i=0;i<clients.size();i++) {
                            clients.get(i).println("RMI is Down!");
                        }
                    }
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

}
