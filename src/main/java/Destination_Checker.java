import java.util.ArrayList;

public class Destination_Checker extends BasicChecker {
    public Destination_Checker(BasicChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Behavior my_behavior, ArrayList<Territory> t) {
        int behavior_DestinationID = my_behavior.getDestination().getOwnID();
        // move or attack?
        String type = my_behavior.getType();
        int starterID = my_behavior.getOwnID();

        if (type == "Attack") {
            int behavior_DestinationID = my_behavior.getDestination().getOwnID();
            if (starterID == behavior_DestinationID){
                return "you are attacking, input a place not belong to you!";
            }
        } else if (type == "Move"){
            int behavior_DestinationID = my_behavior.getDestination().getOwnID();
            if (starterID != behavior_DestinationID){
                return "you are moving, input a place belong to you!";
            }
        }
        return null;
    }
}


