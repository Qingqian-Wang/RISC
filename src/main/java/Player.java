import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Player implements Runnable{
    public Socket clientSocket;

    public int playerID;

    public int status;

    public int serverPort;

    private BufferedReader in;

    private PrintWriter out;

    private BasicChecker ruleChecker;

    public Player(int serverPort, int id) {
        this.serverPort = serverPort;
        this.playerID = id;
        ruleChecker = new OriginChecker(null);
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public int getID() {
        return playerID;
    }

    public int getStatus() {
        return status;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setID(int playerID) {
        this.playerID = playerID;
    }

    private void playOneTurn(){}

    private void checkBehavior(ArrayList<Behavior> list){}


    public void connectToServer() throws IOException {
        this.clientSocket = new Socket("localhost",serverPort);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {

    }
}
