import java.io.DataOutputStream;
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
            Player p = new Player(port, i + 1, playerNum);
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
            DataOutputStream dataOut = new DataOutputStream(player.getSocket().getOutputStream());
            int totalUnit = 50;
            dataOut.writeInt(totalUnit);
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

        int[] territoryOwner;
        if (playerList.size() == 2) {
            territoryOwner = new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
        } else if (playerList.size() == 3) {
            territoryOwner = new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3};
        } else if (playerList.size() == 4) {
            territoryOwner = new int[]{1, 1, 2, 2, 3, 3, 4, 4};
        } else {
            territoryOwner = new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        }

        for (int i = 0; i < playerNum; i++) {
            map.get(i).setOwnID(territoryOwner[i]);
        }
        initialzeMapHelper();
    }

    public void initialzeMapHelper() {
        int playerNum = playerList.size();
        map.get(0).updateNeighbor(map. get(1));
        map. get(0).updateNeighbor(map. get(3));

        map. get(1).updateNeighbor(map. get(0));
        map. get(1).updateNeighbor(map. get(2));
        map. get(1).updateNeighbor(map. get(4));

        map. get(2).updateNeighbor(map. get(1));
        map. get(2).updateNeighbor(map. get(5));

        map. get(3).updateNeighbor(map. get(0));
        map. get(3).updateNeighbor(map. get(4));
        map. get(3).updateNeighbor(map. get(6));
        map. get(3).updateNeighbor(map. get(7));

        map. get(4).updateNeighbor(map. get(1));
        map. get(4).updateNeighbor(map. get(3));
        map. get(4).updateNeighbor(map. get(5));
        map. get(4).updateNeighbor(map. get(7));
        if (playerNum != 4) {
            map. get(4).updateNeighbor(map. get(8));
        }

        map. get(5).updateNeighbor(map. get(3));
        map. get(5).updateNeighbor(map. get(4));
        if (playerNum != 4) {
            map. get(5).updateNeighbor(map. get(8));
        }
        if (playerNum != 3 || playerNum != 4) {
            map. get(5).updateNeighbor(map. get(9));
        }

        map. get(6).updateNeighbor(map. get(3));
        map. get(6).updateNeighbor(map. get(7));


        map. get(7).updateNeighbor(map. get(3));
        map. get(7).updateNeighbor(map. get(4));
        map. get(7).updateNeighbor(map. get(6));
        if (playerNum != 4) {
            map. get(7).updateNeighbor(map. get(8));
        }

        if(playerNum != 4){
            map. get(8).updateNeighbor(map. get(4));
            map. get(8).updateNeighbor(map. get(5));
            map. get(8).updateNeighbor(map. get(7));
            if (playerNum != 3) {
                map. get(8).updateNeighbor(map. get(9));
            }
        }

        if (playerNum != 3 || playerNum != 4) {
            map. get(9).updateNeighbor(map. get(5));
            map. get(9).updateNeighbor(map. get(8));
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
