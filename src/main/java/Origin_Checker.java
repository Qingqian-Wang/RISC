import java.util.ArrayList;
//改！！！！
public class Origin_Checker extends BasicChecker {
    public Origin_Checker(BasicChecker next) {
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
            return "the origin territory is not belong to player" + toString(starterID);
        }
        if (use_Unit > exist_Unit){
            return "use Unit larger than it has! it has" + toString(exist_Unit) + "but use" + toString(use_Unit);
        }
        return null;
    }
}


