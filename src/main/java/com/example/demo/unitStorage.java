package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class unitStorage {
    // the first integer is the level of the unit, the second integer is the number of units
    public Map<Integer, Integer> units;

    public ArrayList<Integer> cost;

    private int remainUnits;

    private int highestLevel;

    private int lowestLevel;

    // upgrade will reduce the specified amount of units from the current level and add them to the target level
    public void upgrade(int amount, int currLevel, int targetLevel) {
        if (units.get(currLevel) >= amount) {
            units.put(currLevel, units.get(currLevel) - amount);
            units.put(targetLevel, units.get(targetLevel) + amount);
        } else {
            System.out.println("Not enough units to upgrade");
        }
    }

    // init all level to 0
    public unitStorage() {// i = 7: spy
        units = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            units.put(i, 0);
        }
    }

    public unitStorage(int x) {
        units = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            units.put(i, x);
        }
    }

    // constructor units by an arraylist
    public unitStorage(ArrayList<Integer> unitsTemp) {
        units = new HashMap<>();
        for (int i = 0; i < unitsTemp.size(); i++) {
            units.put(i, unitsTemp.get(i));
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
    public int calCost(int amount, int currLevel, int targetLevel) {
        int res = 0;
        if (cost == null) {
            cost = new ArrayList<>();
            cost.add(0);
            cost.add(3);
            cost.add(8);
            cost.add(19);
            cost.add(25);
            cost.add(35);
            cost.add(50);
        }
        if (targetLevel == 7) {
            res = amount * 20;
        } else {
            res += amount * (cost.get(targetLevel) - cost.get(currLevel));
        }
        return res;
    }


    // add specific amount of units to the level 0, and initialize all other levels to 0
    public void initUnit(int num) {
        units.put(0, num);
        for (int i = 1; i < 8; i++) {
            units.put(i, 0);
        }
    }

    // add specific amount of units to the specified level
    public void addUnits(int num, int level) {
        units.put(level, units.get(level) + num);
    }

    // remove specific amount of units from the specified level
    public void removeUnits(int num, int level) {
        if (units.get(level) >= num) {
            units.put(level, units.get(level) - num);
        } else {
            System.out.println("Not enough units to remove");
        }
    }

    public void addUnitStorage(unitStorage u) {
        for (int i = 0; i < 8; i++) {
            units.put(i, units.get(i) + u.getUnits().get(i));
        }
    }

    public void setUnitsStorage(unitStorage u) {
        for (int i = 0; i < 8; i++) {
            units.put(i, u.getUnits().get(i));
        }
    }

    public void removeUnitStorage(unitStorage u) {
        for (int i = 0; i < 8; i++) {
            units.put(i, units.get(i) - u.getUnits().get(i));
        }
    }


    // get units
    public Map<Integer, Integer> getUnits() {
        return units;
    }

    // print the units in the format as 0|1|2|3|4|5|6
    public String printUnits() {
        String res = "";
        for (int i = 0; i < 8; i++) {
            if(units.get(i)==-1){
                res += "?|";
            } else {
                res += units.get(i) + "|";
            }
        }
        return res;
    }

    public int getRemainUnits() {
        int res = 0;
        for (int i = 0; i < 8; i++) {
            res += units.get(i);
        }
        remainUnits = res;
        return remainUnits;
    }

    public int getHighestLevel() {
        int res = 0;
        for (int i = 0; i < 8; i++) {
            if (units.get(i) > 0) {
                res = i;
            }
        }
        highestLevel = res;
        return highestLevel;
    }

    public int getLowestLevel() {
        int res = 7;
        for (int i = 7; i >= 0; i--) {
            if (units.get(i) > 0) {
                res = i;
            }
        }
        lowestLevel = res;
        return lowestLevel;
    }


    // check weather the unitStorage contains enough units
    public boolean checkContainEnoughUnits(unitStorage u) {
        for (int i = 0; i < 8; i++) {
            if (units.get(i) < u.getUnits().get(i)) {
                return false;
            }
        }
        return true;
    }
}
