import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BehaviorList implements NetworkObject, Serializable {
    private ArrayList<Behavior> moveList;
    private ArrayList<Behavior> attackList;
    private int playerID;
    public int status; // -1 means disconnect; 0 means dead; 1 means live


    public BehaviorList(int playerID, int status) {
        this.playerID = playerID;
        moveList = new ArrayList<>();
        attackList = new ArrayList<>();
        this.status = status;
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

    public int getPlayerID() {
        return playerID;
    }

    public void sendList(ObjectOutputStream out) throws Exception {
        out.writeObject(this);
    }

    public void receiveList(ObjectInputStream in) throws Exception{
        BehaviorList behaviorList = (BehaviorList) in.readObject();
        this.moveList = behaviorList.getMoveList();
		this.attackList = behaviorList.getAttackList();
		this.playerID = behaviorList.getPlayerID();
        this.status = behaviorList.status;
    }
}
