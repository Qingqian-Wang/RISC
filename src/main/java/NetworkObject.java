import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public interface NetworkObject {
    public void sendList(ObjectOutputStream out) throws Exception;

    public void receiveList(ObjectInputStream in) throws Exception;
}
