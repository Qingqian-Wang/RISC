import edu.duke.zj82.Territory.Territory;

import java.io.Serializable;


public class Behavior implements Serializable {
    Territory origin;
    Territory destination;
    int units;
    int playerID;
    String type;

    public Territory getOrigin() {
        return origin;
    }

    public Territory getDestination() {
        return destination;
    }

    public int getUnit() {
        return units;
    }

    public int getOwnID() {
        return playerID;
    }

    public String getType() {
        return type;
    }
}
