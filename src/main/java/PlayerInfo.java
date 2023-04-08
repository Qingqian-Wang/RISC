import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * This class represents a player in a networked game
 * It contains information about the player's socket, input and output streams, and ID
 */
public class PlayerInfo {
	private final Socket playerSocket; // The socket connected to the player

	private ObjectInputStream in; // The input stream for receiving data from the player

	private ObjectOutputStream out; // The output stream for sending data to the player
	private final int playerID; // The ID assigned to the player

	// Constructor for the PlayerInfo class
    public PlayerInfo(Socket playerSocket, int playerID) throws IOException {
        this.playerSocket = playerSocket;
        this.playerID = playerID;
		out = new ObjectOutputStream(playerSocket.getOutputStream());
		in = new ObjectInputStream(playerSocket.getInputStream());

    }

	// Getter methods for the instance variables
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

	// Method for disconnecting the player by closing the input and output streams
    public void disconnect() throws IOException {
        in.close();
        out.close();
    }
}
