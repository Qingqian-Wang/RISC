import java.net.Socket;

public interface NetworkObject {
    public void sendList(Socket socket) throws Exception;

    public void receiveList(Socket socket) throws Exception;
}
