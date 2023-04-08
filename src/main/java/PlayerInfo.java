import java.io.*;
import java.net.Socket;

public class PlayerInfo {
    private final Socket playerSocket;

    private ObjectInputStream in;

    private ObjectOutputStream out;
    private final int playerID;

    public PlayerInfo(Socket playerSocket, int playerID) throws IOException {
        this.playerSocket = playerSocket;
        this.playerID = playerID;
        in = new ObjectInputStream(playerSocket.getInputStream());
        out = new ObjectOutputStream(playerSocket.getOutputStream());
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
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
