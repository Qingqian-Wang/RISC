import java.util.ArrayList;

public class OriginChecker extends BasicChecker {
    public OriginChecker(BasicChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Behavior my_behavior, ArrayList<Territory> t) {
        int starterID = my_behavior.getOwnID();
        int behavior_OriginID =  my_behavior.getOrigin().getOwnID();
//        int behavior_DestinationID = my_behavior.getDestination().getOwnID();
        int use_Unit = my_behavior.getUnit();
        int exist_Unit = my_behavior.getOrigin().getUnit();
        // check the starter is right
        if (starterID != behavior_OriginID){
            return "the origin territory is not belong to player" + starterID;
        }
        if (use_Unit > exist_Unit){
            return "use Unit larger than it has! it has" + exist_Unit + "but use" + use_Unit;
        }
        return null;
    }
}


