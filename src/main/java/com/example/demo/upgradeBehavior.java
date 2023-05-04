package com.example.demo;

public class upgradeBehavior{
    Territory origin;// The origin territory of the behavior
    Territory destination;// The destination territory of the behavior

    int techLevel;
    unitStorage units;
    int ownID;// The unique ID of the player performing the behavior
    String type; // The type of behavior being performed
    int currLevel;
    int targetLevel;
    int unitsNum;
    int upgradeCost;

    public upgradeBehavior() {
        this.origin = null;
        this.destination = null;
        this.techLevel = 0;
        this.units = null;
        this.ownID = 0;
        this.type = null;
        this.currLevel = 0;
        this.targetLevel = 0;
        this.unitsNum = 0;
        this.upgradeCost = 0;
    }

    // don't use units in Behavior, use unitsNum instead, so assign a new unitStorage to units
    public upgradeBehavior(Territory origin, int playerID, String type, int currLevel, int targetLevel, int unitsNum, int techLevel) {
        this.origin = origin;
        this.destination = origin;
        this.techLevel = techLevel;
        this.units = new unitStorage();
        this.ownID = playerID;
        this.type = type;
        this.currLevel = currLevel;
        this.targetLevel = targetLevel;
        this.unitsNum = unitsNum;
        this.upgradeCost = units.calCost(unitsNum ,currLevel, targetLevel);
    }

    public void setOrigin(Territory origin) {
        this.origin = origin;
    }

    public void setDestination(Territory destination) {
        this.destination = destination;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(int techLevel) {
        this.techLevel = techLevel;
    }

    public void setUnits(unitStorage units) {
        this.units = units;
    }

    public void setOwnID(int ownID) {
        this.ownID = ownID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCurrLevel(int currLevel) {
        this.currLevel = currLevel;
    }

    public void setTargetLevel(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    public void setUnitsNum(int unitsNum) {
        this.unitsNum = unitsNum;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public int getUnitsNum() {
        return unitsNum;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

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
