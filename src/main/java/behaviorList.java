import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BehaviorList implements Serializable {
	private ArrayList<Behavior> moveList;
	private ArrayList<Behavior> attackList;
	private int playerID;

	public ArrayList<Behavior> getMoveList() {
		return moveList;
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

	public static BehaviorList receiveList(Socket socket) throws Exception{
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		BehaviorList behaviorList = (BehaviorList) in.readObject();
		return behaviorList;
	}
}
