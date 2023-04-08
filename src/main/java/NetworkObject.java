import java.net.Socket;

/*
 *Interface for objects that can be sent and received over a network connection. 
 * */
public interface NetworkObject {
	// Sends the object over the network connection represented by the given socket
    public void sendList(Socket socket) throws Exception;

	// Receives the object over the network connection represented by the given socket
    public void receiveList(Socket socket) throws Exception;
}
