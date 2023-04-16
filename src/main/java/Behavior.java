import java.io.Serializable;
/*
 * This class represents a player's Behavior in a game, including information about the
 * origin territory, destination territory, number of units, player ID, and type of behavior
 */

public class Behavior implements Serializable {
    Territory origin;// The origin territory of the behavior
    Territory destination;// The destination territory of the behavior
    int unit;// The number of units involved in the behavior
    int ownID;// The unique ID of the player performing the behavior
    String type; // The type of behavior being performed

    public Behavior(){
        this.origin = null;
        this.destination = null;
        this.unit = 0;
        this.ownID = 0;
        this.type = null;
    }
    public Behavior(Territory origin, Territory destination, int unit, int ownID, String type) {
        this.origin = origin;
        this.destination = destination;
        this.unit = unit;
        this.ownID = ownID;
        this.type = type;
    }

    // Getter methods for the instance variables
    public Territory getOrigin() {
        return origin;
    }

    public Territory getDestination() {
        return destination;
    }

    public int getUnit() {
        return unit;
    }

    public int getOwnID() {
        return ownID;
    }

    public String getType() {
        return type;
    }
}
