import java.util.ArrayList;

public class UpgradeChecker {
    public String checkMyRule(upgradeBehavior my_behavior, ArrayList<Territory> t) {
        int starterID = my_behavior.getOwnID();
        int behavior_OriginID =  my_behavior.getOrigin().getOwnID();
//        int behavior_DestinationID = my_behavior.getDestination().getOwnID();
        unitStorage use_Unit = my_behavior.getUnits();
        unitStorage exist_Unit = my_behavior.getOrigin().getUnits();
        // check the starter is right
        if (starterID != behavior_OriginID){
            return "the origin territory is not belong to player" + starterID;
        }
        if (exist_Unit.checkContainEnoughUnits(use_Unit) == false){
            return "use Unit larger than it has! it has " + exist_Unit.printUnits() + " but use " + use_Unit.printUnits();
        }
        return null;
    }
}
