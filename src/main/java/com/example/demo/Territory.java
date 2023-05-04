package com.example.demo;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Territory implements Serializable {
    public String name;
    public int ownID;
    public unitStorage units;
    public HashMap<Integer, ArrayList<String>> neighbor;
    public Map<Integer, Territory> viewMap;
    public Map<Integer, Integer> spiesCollection;
    public int hideTurnCount;
    public int size;
    public boolean ableToSee;

    public Territory() {
        this.name = "Temp";
        this.ownID = 0;
        this.units = new unitStorage();
        this.neighbor = new HashMap<>();
        this.viewMap = new HashMap<>();
        this.spiesCollection = new HashMap<>();
        this.hideTurnCount = 0;
        this.size = 10;
        this.ableToSee = true;
    }

    public Territory(Territory t, int x) {
        this.name = t.getName();
        this.ownID = x;
        this.units = new unitStorage(x);
        this.neighbor = t.getNeighbor();
        this.viewMap = t.getViewMap();
        this.spiesCollection = new HashMap<>();
        this.hideTurnCount = t.getHideTurnCount();
        this.size = t.getSize();
        this.ableToSee = true;
    }
    public Territory(Territory t) {
        this.name = t.getName();
        this.ownID = t.getOwnID();
        this.units = new unitStorage();
        setUnits((t.getUnits()));
        this.neighbor = t.getNeighbor();
        this.viewMap = t.getViewMap();
        this.spiesCollection = t.getSpiesCollection();
        this.hideTurnCount = t.getHideTurnCount();
        this.size = t.getSize();
        this.ableToSee = t.isAbleToSee();
    }

    public Territory(String name, int ownID) {
        this.name = name;
        this.ownID = ownID;
        this.units = new unitStorage();
        this.neighbor = new HashMap<>();
        this.viewMap = new HashMap<>();
        this.spiesCollection = new HashMap<>();
        this.hideTurnCount = 0;
        this.size = 10;
    }


    public Map<Integer, Territory> getViewMap() {
        return viewMap;
    }

    public void setViewMap(Map<Integer, Territory> viewMap) {
        this.viewMap = viewMap;
    }

    public boolean isAbleToSee() {
        return ableToSee;
    }

    public void setAbleToSee(boolean ableToSee) {
        this.ableToSee = ableToSee;
    }

    public Map<Integer, Integer> getSpiesCollection() {
        return spiesCollection;
    }

    public int getHideTurnCount() {
        return hideTurnCount;
    }

    public String getName() {
        return this.name;
    }

    public int getOwnID() {
        return this.ownID;
    }

    public unitStorage getUnits() {
        return this.units;
    }

    public HashMap<Integer, ArrayList<String>> getNeighbor() {
        return this.neighbor;
    }

    public void setOwnID(int id) {
        this.ownID = id;
    }

    public void addUnit(int num, int level) {
        this.units.addUnits(num, level);
    }

    public void removeUnit(int num, int level) {
        this.units.removeUnits(num, level);
    }

    public void addSpy(int playerID, int num) {
        if (spiesCollection.containsKey(playerID)) {
            spiesCollection.put(playerID, spiesCollection.get(playerID) + num);
        } else {
            spiesCollection.put(playerID, num);
        }
    }

    public void removeSpy(int playerID, int num) {
        spiesCollection.put(playerID, spiesCollection.get(playerID) - num);
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setUnits(unitStorage units) {
        if(units!=null){
            for(int i = 0;i < 8; i++){
                this.units.addUnits(units.getUnits().get(i),i);
            }
        }
    }

    public void setNeighbor(HashMap<Integer, ArrayList<String>> neighbor) {
        this.neighbor = neighbor;
    }

    public void setSpiesCollection(Map<Integer, Integer> spiesCollection) {
        this.spiesCollection = spiesCollection;
    }

    public void setHideTurnCount(int hideTurnCount) {
        this.hideTurnCount = hideTurnCount;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // based on territory id, find its name in hashmap
    public int getIDByName(Territory t) {
        for (Map.Entry<Integer, ArrayList<String>> e : neighbor.entrySet()) {
            if (e.getValue().contains(t.getName())) {
                return e.getKey();
            }
        }
        return -1;
    }

    public int getSize() {
        return size;
    }

    public void updateNeighbor(Territory t) {
        // check the reason of update: if exist a key that contains it, that means this territory's own is changed
        // else: initializing the map
        int existKey = getIDByName(t);
        if (existKey != -1) {
            ArrayList<String> modifiedList = this.neighbor.get(existKey);
            modifiedList.remove(t.getName());
            this.neighbor.put(existKey, modifiedList);
        }

        // add the name to the hashmap
        ArrayList<String> tempList;
        if (this.neighbor.containsKey(t.getOwnID())) {
            tempList = this.neighbor.get(t.getOwnID());
        } else {
            tempList = new ArrayList<>();
        }
        tempList.add(t.getName());
        this.neighbor.put(t.getOwnID(), tempList);
    }
}
