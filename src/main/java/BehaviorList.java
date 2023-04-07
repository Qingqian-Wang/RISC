import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BehaviorList implements NetworkObject, Serializable {
    private ArrayList<Behavior> moveList;
    private ArrayList<Behavior> attackList;
    private int playerID;

    public BehaviorList(int playerID) {
        this.playerID = playerID;
        moveList = new ArrayList<>();
        attackList = new ArrayList<>();
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

    public void sendList(Socket socket) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(this);
    }

    public void receiveList(Socket socket) throws Exception{
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        BehaviorList behaviorList = (BehaviorList) in.readObject();
        this.moveList = behaviorList.getMoveList();
		this.attackList = behaviorList.getAttackList();
		this.playerID = behaviorList.getPlayerID();
    }
}
