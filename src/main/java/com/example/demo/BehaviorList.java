package com.example.demo;

import java.io.*;
import java.util.ArrayList;

/*
 * This class represents a list of behaviors performed by a player during a game.
 * It implements the NetworkObject interface and Serializable interface to allow
 * for network communication and object serialization.
 * */
public class BehaviorList implements NetworkObject, Serializable {
    public ArrayList<Behavior> moveList;// A list of behaviors related to move
    public ArrayList<Behavior> attackList;// A list of behaviors related to attack

    public ArrayList<upgradeBehavior> upgradeList;// A list of behaviors related to upgrade
    public int evloveNum;

    public int playerID;// The ID of the player who performed the behaviors
    public int status; // -1 means disconnect; 0 means dead; 1 means live

    public BehaviorList() {
        this.playerID = 0;
        moveList = new ArrayList<>();
        attackList = new ArrayList<>();
        upgradeList = new ArrayList<>();
        this.status = 0;
        evloveNum = 0;
    }
    public BehaviorList(int playerID, int status) {
        this.playerID = playerID;
        moveList = new ArrayList<>();
        attackList = new ArrayList<>();
        upgradeList = new ArrayList<>();
        this.status = status;
        evloveNum = 0;
    }

    public int getEvloveNum() {
        return evloveNum;
    }
    public void addEvloveNum(){
        this.evloveNum+=1;
    }

    public ArrayList<Behavior> getMoveList() {
        return moveList;
    }
    public void addToMoveList(Behavior b){
        this.moveList.add(b);
    }
    public void addToAttackList(Behavior b){
        this.attackList.add(b);
    }

    public ArrayList<Behavior> getAttackList() {
        return attackList;
    }

    public void addToUpgradeList(upgradeBehavior b){
        this.upgradeList.add(b);
    }

    public ArrayList<upgradeBehavior> getUpgradeList() {
        return upgradeList;
    }

    public int getPlayerID() {
        return playerID;
    }

    /*
     * Sends the BehaviorList object over an ObjectOutputStream.
     *
     * @param out The ObjectOutputStream used for network communication
     * @throws Exception If there is an error with the ObjectOutputStream
     */
    public void sendList(ObjectOutputStream out) throws Exception {
        out.flush();
        out.writeObject(this);
        out.flush();
    }


    /*
     * Receives a BehaviorList object over an ObjectInputStream and updates the
     * instance variables.
     *
     * @param in The ObjectInputStream used for network communication
     *
     * @throws Exception If there is an error with the ObjectInputStream
     */

    public void receiveList(ObjectInputStream in) throws Exception{
        BehaviorList behaviorList = (BehaviorList) in.readObject();
        this.moveList = behaviorList.getMoveList();
		this.attackList = behaviorList.getAttackList();
		this.playerID = behaviorList.getPlayerID();
        this.status = behaviorList.status;
    }
}
