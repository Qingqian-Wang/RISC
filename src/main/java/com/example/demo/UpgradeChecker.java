package com.example.demo;

import java.util.ArrayList;

public class UpgradeChecker {
    // check in game
    public String checkMyRule(int currentRestCost, upgradeBehavior my_behavior, ArrayList<Territory> t) {
        int starterID = my_behavior.getOwnID();
        int behavior_OriginID =  my_behavior.getOrigin().getOwnID();
//        int behavior_DestinationID = my_behavior.getDestination().getOwnID();
        int currentLevel = my_behavior.getCurrLevel();
        int targetLevel = my_behavior.getTargetLevel();
        unitStorage use_Unit = my_behavior.getUnits();
        unitStorage exist_Unit = my_behavior.getOrigin().getUnits();
        // check the starter is right
        if (starterID != behavior_OriginID){
            return "the origin territory is not belong to player" + starterID;
        }
        if (exist_Unit.checkContainEnoughUnits(use_Unit) == false){
            return "use Unit larger than it has! it has " + exist_Unit.printUnits() + " but use " + use_Unit.printUnits();
        }
        if(currentLevel<0||currentLevel>6){
            return "currentLevel is out of bound for a upgrade behavior";
        }
        if(targetLevel<0||targetLevel>6){
            return "targetLevel is out of bound for a upgrade behavior";
        }
        if(my_behavior.getUpgradeCost()>currentRestCost){
            return "Cost is not enough";
        }
        return null;
    }
    // check in player
    public String checkMyRule(upgradeBehavior my_behavior, ArrayList<Territory> t) {
        int starterID = my_behavior.getOwnID();
        int behavior_OriginID =  my_behavior.getOrigin().getOwnID();
//        int behavior_DestinationID = my_behavior.getDestination().getOwnID();
        int currentLevel = my_behavior.getCurrLevel();
        int targetLevel = my_behavior.getTargetLevel();
        unitStorage use_Unit = my_behavior.getUnits();
        unitStorage exist_Unit = my_behavior.getOrigin().getUnits();
        // check the starter is right
        if (starterID != behavior_OriginID){
            return "the origin territory is not belong to player" + starterID;
        }
        if (exist_Unit.checkContainEnoughUnits(use_Unit) == false){
            return "use Unit larger than it has! it has " + exist_Unit.printUnits() + " but use " + use_Unit.printUnits();
        }
        if(currentLevel<0||currentLevel>6){
            return "currentLevel is out of bound for a upgrade behavior";
        }
        if(targetLevel<0||targetLevel>6){
            return "targetLevel is out of bound for a upgrade behavior";
        }
        return null;
    }
}
