import java.io.Serializable;


public class Behavior implements Serializable {
    Territory origin;
    Territory destination;
    int units;
    int playerID;
    String type;

    public Behavior(Territory origin, Territory destination, int units, int playerID, String type) {
        this.origin = origin;
        this.destination = destination;
        this.units = units;
        this.playerID = playerID;
        this.type = type;
    }

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
