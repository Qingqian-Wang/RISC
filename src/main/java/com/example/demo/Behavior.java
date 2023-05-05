package com.example.demo;

import java.io.Serializable;
import java.util.ArrayList;
/*
 * This class represents a player's Behavior in a game, including information about the
 * origin territory, destination territory, number of units, player ID, and type of behavior
 */

public class Behavior implements Serializable {
    Territory origin;// The origin territory of the behavior
    Territory destination;// The destination territory of the behavior

    unitStorage units;
    int ownID;// The unique ID of the player performing the behavior
    String type; // The type of behavior being performed




    public Behavior(Territory origin, Territory destination, ArrayList<Integer> unitsTemp, int ownID, String type) {
        this.origin = origin;
        this.destination = destination;
        this.units = new unitStorage(unitsTemp);
        this.ownID = ownID;
        this.type = type;
    }

    public Behavior(Territory origin, Territory destination, int ownID, String type) {
        this.origin = origin;
        this.destination = destination;
        this.units = new unitStorage();
        this.ownID = ownID;
        this.type = type;
    }

    // constructor only initialize ownid and origin, use for cloak
    public Behavior(int ownID, Territory origin) {
        this.origin = origin;
        this.destination = null;
        this.units = null;
        this.ownID = ownID;
        this.type = null;
    }

    // default constructor
    public Behavior() {
        this.origin = null;
        this.destination = null;
        this.units = new unitStorage();
        this.ownID = 0;
        this.type = null;
    }

    // Getter methods for the instance variables
    public Territory getOrigin() {
        return origin;
    }

    public Territory getDestination() {
        return destination;
    }


    public unitStorage getUnits() {
        return units;

    }

    public int getOwnID() {
        return ownID;
    }

    public String getType() {
        return type;
    }
}
