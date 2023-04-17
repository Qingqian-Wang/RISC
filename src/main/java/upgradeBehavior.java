public class upgradeBehavior extends Behavior{
    int currLevel;
    int targetLevel;
    int unitsNum;
    int upgradeCost;

    // don't use units in Behavior, use unitsNum instead, so assign a new unitStorage to units
    public upgradeBehavior(Territory origin, int playerID, String type, int currLevel, int targetLevel, int unitsNum) {
        super(origin, origin, playerID, type);
        this.currLevel = currLevel;
        this.targetLevel = targetLevel;
        this.unitsNum = unitsNum;
        this.upgradeCost = units.calCost(currLevel, targetLevel, unitsNum);
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
}
