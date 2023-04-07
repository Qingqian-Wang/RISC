import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;


public class Server {

    public ArrayList<Player> playerList;
    public ArrayList<Territory> map;
    public int port;
    public ServerSocket serverSocket;
    private BasicChecker ruleChecker;

    public Server(int port) throws IOException {
        playerList = new ArrayList<>();
        map = new ArrayList<>();
        this.port = port;
        serverSocket = new ServerSocket(this.port);
        ruleChecker = new OriginChecker(new DestinationChecker(null));
    }

    public void acceptPlayer(int playerNum) throws IOException {
        for (int i = 0; i < playerNum; i++) {
            Player p = new Player(port, i + 1);
            p.connectToServer();
            Socket playerSocket = serverSocket.accept();
            System.out.println("Accept new connection from " + playerSocket.getInetAddress());
            playerList.add(p);
            System.out.println("Added player" + p.getID() + " to list");
            Thread playerThread = new Thread(p);
            playerThread.start();
        }
    }

    public void gameStart() throws IOException {
        initialzeMap();
        int[][] territoryOwner;
        if (playerList.size() == 2) {
            territoryOwner = new int[][]{{0, 1, 2, 3, 4}, {5, 6, 7, 8, 9}};
        } else if (playerList.size() == 3) {
            territoryOwner = new int[][]{{1, 2, 5}, {0, 3, 4}, {6, 7, 8}};
        } else if (playerList.size() == 4) {
            territoryOwner = new int[][]{{0, 1}, {2, 5}, {3, 4}, {6, 7}};
        } else {
            territoryOwner = new int[][]{{0, 1}, {2, 5}, {3, 4}, {6, 7}, {8, 9}};
        }
        for (Player player : playerList) {
            player.getOut().println("Game Start");
            String unitInfo = player.getIn().readLine();
            String[] tokens = unitInfo.split(" ");
            for (int i = 0; i < tokens.length; i++) {
                Territory temp = map.get(territoryOwner[player.getID() - 1][i]);
                temp.setUnit(Integer.parseInt(tokens[i]));
                map.set(territoryOwner[player.getID() - 1][i], temp);
            }
        }
        while (!gameOver()) {
            playTurn();
        }
    }


    public void initialzeMapHelper() {

    }



    ArrayList<Map.Entry<String, Integer>> mapInfo;

    public void initialzeMap(){
        int playerNum = playerList.size();
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Midkemia", -1));
        map.add(new Territory("Oz", -1));
        map.add(new Territory("Gondor", -1));
        map.add(new Territory("Elantris", -1));
        map.add(new Territory("Scadrial", -1));
        map.add(new Territory("Roshar", -1));
        map.add(new Territory("Hogwarts", -1));
        if(playerNum != 3 || playerNum != 4){
            map.add(new Territory("Mordor", -1));
        }
        if(playerNum != 4){
            map.add(new Territory("Duke", -1));
        }




    }

    public void playTurn() {
    }

    private void checkBehavior(ArrayList<Behavior>) {
    }

    public void sendPlayerStatus() {
    }

    public boolean gameOver() {
        int ownID = -1;
        for (Territory t : map) {
            if (ownID == -1) {
                ownID = t.getOwnID();
            } else if (ownID != t.getOwnID()) {
                return false;
            }
        }
        return true;
    }


}
