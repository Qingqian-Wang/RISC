import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

//This class implements the NetworkObject and Serializable interfaces
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
        out.flush();
        out.writeObject(this);
        out.flush();
    }

	// Reads a GlobalMap object from the provided input stream and assigns its list
	// of territories to the current object's list
    @Override
    public void receiveList(ObjectInputStream in) throws Exception {
        GlobalMap globalMap = (GlobalMap) in.readObject();
        this.mapArrayList = globalMap.getMapArrayList();
    }
}
