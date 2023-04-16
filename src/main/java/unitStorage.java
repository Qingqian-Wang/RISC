import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class unitStorage {
    // the first integer is the level of the unit, the second integer is the number of units
    public Map<Integer, Integer> units;

    public ArrayList<Integer> cost;

    // upgrade will reduce the specified amount of units from the current level and add them to the target level
    public void upgrade(int amount, int currLevel, int targetLevel){
        if (units.get(currLevel) >= amount){
            units.put(currLevel, units.get(currLevel) - amount);
            units.put(targetLevel, units.get(targetLevel) + amount);
        }else{
            System.out.println("Not enough units to upgrade");
        }
    }

    // cal the cost of upgrading the specified amount of units from the current level to the target level
    // Cost (Total)  Tech Level Required
    //  0 (0)        Units start here
    //  3 (3)           1
    //  8 (11)          2
    //  19 (30)         3
    //  25 (55)         4
    //  35 (90)         5
    //  50 (140)        6
    public int calCost(int amount, int currLevel, int targetLevel){
        int res = 0;
        if(cost == null){
            cost = new ArrayList<>();
            cost.add(0);
            cost.add(3);
            cost.add(8);
            cost.add(19);
            cost.add(25);
            cost.add(35);
            cost.add(50);
        }
        res += amount * (cost.get(targetLevel) - cost.get(currLevel));
        return res;
    }


    // add specific amount of units to the level 0
    public void initUnit(int num){
        units.put(0, num);
    }

    // add specific amount of units to the specified level
    public void addUnits(int num, int level){
        units.put(level, units.get(level) + num);
    }

    // remove specific amount of units from the specified level
    public void removeUnits(int num, int level){
        if (units.get(level) >= num){
            units.put(level, units.get(level) - num);
        }else{
            System.out.println("Not enough units to remove");
        }
    }


    // get units
    public Map<Integer, Integer> getUnits() {
        return units;
    }
}
