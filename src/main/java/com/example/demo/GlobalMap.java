package com.example.demo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
//This class implements the NetworkObject and Serializable interfaces
public class GlobalMap implements NetworkObject, Serializable {
    public ArrayList<Territory> mapArrayList;

    public GlobalMap(ArrayList<Territory> mapArrayList) {
        this.mapArrayList = mapArrayList;
    }
    public GlobalMap() {
        this.mapArrayList = new ArrayList<>();
    }

    public ArrayList<Territory> getMapArrayList() {
        return mapArrayList;
    }

    @Override
    public void sendList(ObjectOutputStream out) throws Exception {
        out.flush();
        out.writeObject(this);
        out.flush();
    }

    @Override
    public void receiveList(ObjectInputStream in) throws Exception {
        GlobalMap globalMap = (GlobalMap) in.readObject();
        this.mapArrayList = globalMap.getMapArrayList();
    }
}
