package ServerPattern;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <h1>ServerTCP</h1>
 * Represents a TCP Server that listens a certain port number.
 *
 */
public class ServerTCP implements Serializable{

    private int nbConnections = 0;
    private final int nbConnectionsMax;

    private transient Socket clientSocket;

    private transient IContext context;
    private transient IProtocol protocol;

    private final int numberPort;

    public ServerTCP(int numberPort) {
        this.numberPort = numberPort;
        this.nbConnectionsMax = 2;
    }

    public ServerTCP(IContext context, IProtocol protocol, int numberPort) {
        this(numberPort);
        this.context = context;
        this.protocol = protocol;
    }

    /**
     * Creates a Server Socket on a certain port and continues to accept Clients as long as the maximum number of Clients is not surpassed.
     */
    public void go() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(numberPort);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + numberPort + ", " + e);
            System.exit(1);
        }

        while (nbConnections <= nbConnectionsMax) {
            try {
                System.out.println("Server is waiting for a client");
                System.out.println("Current number of clients: " + nbConnections);
                clientSocket = serverSocket.accept();
                nbConnections++;
            } catch (IOException e) {
                System.out.println("Accept failed: " + numberPort + ", " + e);
                System.exit(1);
            }
            Process process = new Process(clientSocket,this);
            process.start();
        }

        try {
            serverSocket.close();
            this.nbConnections--;
        } catch (IOException e) {
            System.out.println("Could not close");
        }
    }

    public IProtocol getProtocol() {
        return protocol;
    }

    public IContext getContext() {
        return context;
    }
}


