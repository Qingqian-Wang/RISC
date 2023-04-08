import java.io.*;
import java.net.Socket;


/*
 * This class represents a player in a networked game
 * It contains information about the player's socket, input and output streams, and ID
 */

public class PlayerInfo {
    private final Socket playerSocket;

    private ObjectInputStream in;

    private ObjectOutputStream out;
    private final int playerID;

    public PlayerInfo(Socket playerSocket, int playerID) throws IOException {
        this.playerSocket = playerSocket;
        this.playerID = playerID;
        out = new ObjectOutputStream(playerSocket.getOutputStream());
        in = new ObjectInputStream(playerSocket.getInputStream());

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
        playerSocket.close();
    }

}
