import java.net.Socket;

public interface NetworkObject {
    public void sendList(Socket socket);

    public void receiveList(Socket socket);
}
