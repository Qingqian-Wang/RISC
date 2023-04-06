import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Territory implements Serializable {
    private final String name;
    private int ownID;
    private int units;
    private HashMap<Integer, ArrayList<String>> neighbor;

    public Territory(String name, int ownID) {
        this.name = name;
        this.ownID = ownID;
        this.units = 0;
        this.neighbor = new HashMap<Integer, ArrayList<String>>();
    }

    public String getName() {
        return this.name;
    }

    public int getOwnID() {
        return this.ownID;
    }

    public int getUnits() {
        return this.units;
    }

    public HashMap<Integer, ArrayList<String>> getNeighbor() {
        return this.neighbor;
    }

    public void setOwnID(int id) {
        this.ownID = id;
    }

    public void setUnits(int num) {
        this.units = num;
    }

    // based on territory id, find its name in hashmap
    private int getIDByName(Territory t){
        for(Map.Entry<Integer,ArrayList<String>> e: neighbor.entrySet()){
            if(e.getValue().contains(t.getName())){
                return e.getKey();
            }
        }
        return -1;
    }
    public void updateNeighbor(Territory t){
        // check the reason of update: if exist a key that contains it, that means this territory's own is changed
        // else: initializing the map
        int existKey = getIDByName(t);
        if(existKey != -1){
            ArrayList<String> modifiedList = this.neighbor.get(existKey);
            modifiedList.remove(t.getName());
            this.neighbor.put(existKey,modifiedList);
        }

        // add the name to the hashmap
        ArrayList<String> tempList;
        if(this.neighbor.containsKey(t.getOwnID())){
            tempList = this.neighbor.get(t.getOwnID());
        } else {
            tempList = new ArrayList<>();
        }
        tempList.add(t.getName());
        this.neighbor.put(t.getOwnID(),tempList);
    }
}
