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

    @Override
    public void sendList(ObjectOutputStream out) throws Exception {
        out.writeObject(this);
    }

    @Override
    public void receiveList(ObjectInputStream in) throws Exception {
        GlobalMap globalMap = (GlobalMap) in.readObject();
        this.mapArrayList = globalMap.getMapArrayList();
    }
}
