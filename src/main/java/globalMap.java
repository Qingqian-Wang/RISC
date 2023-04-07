import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GlobalMap {
    private ArrayList<Territory> mapArrayList;

    public ArrayList<Territory> getMapArrayList() {
        return mapArrayList;
    }

    public void sendList(Socket socket) throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(this);
    }

    public static GlobalMap receiveList(Socket socket) throws Exception{
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        GlobalMap globalMap = (GlobalMap) in.readObject();
        return globalMap;
    }
}
