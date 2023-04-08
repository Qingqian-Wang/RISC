import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DestinationChecker extends BasicChecker {
    public DestinationChecker(BasicChecker next) {
        super(next);
    }

    Territory findTerritory(String str, ArrayList<Territory> t) {
        for (Territory territory : t) {
            if (territory.getName().equals(str)) {
                return territory;
            }
        }
        return null;
    }


    public boolean findPath(Territory A, String destination, ArrayList<Territory> t, ArrayList<String> visited) {
        String origin = A.getName();
        visited.add(origin);
        for (Map.Entry<Integer, ArrayList<String>> e : A.getNeighbor().entrySet()) {
            if (e.getKey() == A.getOwnID()) {
                ArrayList<String> temp = e.getValue();
                for (String str : temp) {
                    if(visited.contains(str)){
                        continue;
                    }
                    if (str.equals(destination)) {
                        return true;
                    } else {
                        Territory newTerritory = findTerritory(str, t);
                        if (findPath(newTerritory, destination, t, visited)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected String checkMyRule(Behavior my_behavior, ArrayList<Territory> t) {
        // move or attack?
        String type = my_behavior.getType();
        int starterID = my_behavior.getOwnID();

        if (Objects.equals(type, "Attack")) {
            int enemyID = my_behavior.getDestination().getOwnID();
            if (starterID == enemyID) {
                return "you are attacking you own place!";
            }
            //check if neighbor adjacent to it
            boolean adjacent = false;
            String enemyName = my_behavior.getDestination().getName();
            if (my_behavior.getOrigin().getNeighbor().containsKey(enemyID)) {
                for (int i = 0; i < my_behavior.getOrigin().getNeighbor().get(enemyID).size(); i++) {
                    if (enemyName.equals(my_behavior.getOrigin().getNeighbor().get(enemyID).get(i))) {
                        adjacent = true;
                        break;
                    }
                }
            }
            if (!adjacent) {
                return "Enemy not adjacent to you";
            }

        } else if (Objects.equals(type, "Move")) {
            int behavior_DestinationID = my_behavior.getDestination().getOwnID();
            if (starterID != behavior_DestinationID) {
                return "you are moving, input a place belong to you!";
            }
            //find path
            ArrayList<String> visited = new ArrayList<>();
            boolean res = findPath(my_behavior.getOrigin(), my_behavior.getDestination().getName(), t, visited);
            if (!res) {
                return "connect to the places not connected";
            }
        }


        return null;
    }
    //find path method


}



