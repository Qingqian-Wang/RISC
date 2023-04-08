import java.io.Serializable;

/*
 * This class represents a player's Behavior in a game, including information about the 
 * origin territory, destination territory, number of units, player ID, and type of behavior
 */
public class Behavior implements Serializable {
	Territory origin; // The origin territory of the behavior
	Territory destination; // The destination territory of the behavior
	int units; // The number of units involved in the behavior
	int playerID; // The unique ID of the player performing the behavior
	String type; // The type of behavior being performed

    public Behavior(Territory origin, Territory destination, int units, int playerID, String type) {
        this.origin = origin;
        this.destination = destination;
        this.units = units;
        this.playerID = playerID;
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
        return units;
    }

    public int getOwnID() {
        return playerID;
    }

    public String getType() {
        return type;
    }
}
