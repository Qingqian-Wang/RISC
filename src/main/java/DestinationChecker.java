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
            int enemyID = my_behavior.getDestination().getOwnID();
            if (starterID == enemyID){
                return "you are attacking you own place!";
            }
            //check if neighbor adjacent to it
            boolean adjacent = false;
            String enemyName = my_behavior.getDestination().getName();
            if(my_behavior.getOrigin().getNeighbor().containsKey(enemyID)) {
                for(int i = 0; i < my_behavior.getOrigin().getNeighbor().get(enemyID).size(); i++){
                    if(enemyName.equals(my_behavior.getOrigin().getNeighbor().get(enemyID).get(i))){
                        adjacent = true;
                        break;
                    }
                }
            }
            if(!adjacent){
                return "Enemy not adjacent to you";
            }

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


