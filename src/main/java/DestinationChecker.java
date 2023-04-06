import java.util.ArrayList;
import java.util.Objects;

public class DestinationChecker extends BasicChecker {
    public DestinationChecker(BasicChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Behavior my_behavior, ArrayList<Territory> t) {
        // move or attack?
        String type = my_behavior.getType();
        int starterID = my_behavior.getOwnID();

        if (Objects.equals(type, "Attack")) {
            int behavior_DestinationID = my_behavior.getDestination().getOwnID();
            if (starterID == behavior_DestinationID){
                return "you are attacking, input a place not belong to you!";
            }
            //neighbor
        } else if (Objects.equals(type, "Move")){
            int behavior_DestinationID = my_behavior.getDestination().getOwnID();
            if (starterID != behavior_DestinationID){
                return "you are moving, input a place belong to you!";
            }
            //find path
        }
        return null;
    }
    //find path method
}


