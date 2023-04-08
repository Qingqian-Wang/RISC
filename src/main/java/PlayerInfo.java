import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerInfo {
    private final Socket playerSocket;
    private BufferedReader in;
    private PrintWriter out;
    private final int playerID;

    public PlayerInfo(Socket playerSocket, int playerID) throws IOException {
        this.playerSocket = playerSocket;
        this.playerID = playerID;
        this.in = new BufferedReader(new InputStreamReader(this.playerSocket.getInputStream()));
        this.out = new PrintWriter(this.playerSocket.getOutputStream(), true);
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
    }
}
