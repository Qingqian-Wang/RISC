import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class GlobalMap implements NetworkObject, Serializable {
    private ArrayList<Territory> mapArrayList;

    public GlobalMap(ArrayList<Territory> mapArrayList) {
        this.mapArrayList = mapArrayList;
    }
    public GlobalMap() {
        this.mapArrayList = new ArrayList<>();
    }

    public ArrayList<Territory> getMapArrayList() {
        return mapArrayList;
    }

    public void sendList(Socket socket) throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(this);
    }

    public void receiveList(Socket socket) throws Exception{
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        GlobalMap globalMap = (GlobalMap) in.readObject();
        this.mapArrayList = globalMap.getMapArrayList();
    }
}
