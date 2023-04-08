import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {

    public ArrayList<Socket> playerList;
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

    public void gameStart() throws Exception {
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


    //private void checkBehavior(ArrayList<Behavior>){}

    ArrayList<Map.Entry<String, Integer>> mapInfo;
    private void addoneUnit(){
        for (Territory t : map) {
            int unit = t.getUnit() + 1;
            t.setUnit(unit);
         }
    }


    public void initialzeMap() {
        int playerNum = playerList.size();
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Midkemia", -1));
        map.add(new Territory("Oz", -1));
        map.add(new Territory("Gondor", -1));
        map.add(new Territory("Elantris", -1));
        map.add(new Territory("Scadrial", -1));
        map.add(new Territory("Roshar", -1));
        map.add(new Territory("Hogwarts", -1));
        if (playerNum != 3 && playerNum != 4) {
            map.add(new Territory("Mordor", -1));
        }
        if (playerNum != 4) {
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

        for (int i = 0; i < map.size(); i++) {
            map.get(i).setOwnID(territoryOwner[i]);
        }
        initialzeMapHelper();
    }

    public void initialzeMapHelper() {
        int playerNum = playerList.size();
        map.get(0).updateNeighbor(map.get(1));
        map.get(0).updateNeighbor(map.get(3));

        map.get(1).updateNeighbor(map.get(0));
        map.get(1).updateNeighbor(map.get(2));
        map.get(1).updateNeighbor(map.get(4));

        map.get(2).updateNeighbor(map.get(1));
        map.get(2).updateNeighbor(map.get(5));

        map.get(3).updateNeighbor(map.get(0));
        map.get(3).updateNeighbor(map.get(4));
        map.get(3).updateNeighbor(map.get(6));
        map.get(3).updateNeighbor(map.get(7));

        map.get(4).updateNeighbor(map.get(1));
        map.get(4).updateNeighbor(map.get(3));
        map.get(4).updateNeighbor(map.get(5));
        map.get(4).updateNeighbor(map.get(7));
        if (playerNum != 4) {
            map.get(4).updateNeighbor(map.get(8));
        }

        map.get(5).updateNeighbor(map.get(3));
        map.get(5).updateNeighbor(map.get(4));
        if (playerNum != 4) {
            map.get(5).updateNeighbor(map.get(8));
        }

        if (playerNum != 3 && playerNum != 4) {
            map. get(5).updateNeighbor(map. get(9));

        }

        map.get(6).updateNeighbor(map.get(3));
        map.get(6).updateNeighbor(map.get(7));


        map.get(7).updateNeighbor(map.get(3));
        map.get(7).updateNeighbor(map.get(4));
        map.get(7).updateNeighbor(map.get(6));
        if (playerNum != 4) {
            map.get(7).updateNeighbor(map.get(8));
        }

        if (playerNum != 4) {
            map.get(8).updateNeighbor(map.get(4));
            map.get(8).updateNeighbor(map.get(5));
            map.get(8).updateNeighbor(map.get(7));
            if (playerNum != 3) {
                map.get(8).updateNeighbor(map.get(9));
            }
        }


        if (playerNum != 3 && playerNum != 4) {
            map. get(9).updateNeighbor(map. get(5));
            map. get(9).updateNeighbor(map. get(8));
        }

    }

    public void playTurn() throws Exception {
        List<Integer> orders = new ArrayList<>();
        for(int i = 1; i <= playerList.size(); i++){
            orders.add(i);
        }
        Random rand = new Random();
        HashMap<Integer, BehaviorList> orderMap = new HashMap<>();
        ArrayList<Behavior> attackList = new ArrayList<>();
        ArrayList<Behavior> moveList = new ArrayList<>();
        for (Player player : playerList) {
            player.getOut().println("Turn Start");
            GlobalMap current = new GlobalMap(map);
            current.sendList(player.getSocket());
            BehaviorList behaviorList = new BehaviorList(player.getID());
            behaviorList.receiveList(player.getSocket());
            int index = rand.nextInt(orders.size());
            orderMap.put(orders.get(index),behaviorList);
            orders.remove(index);
        }
        for(int i = 1; i <= playerList.size();i++){
            attackList.addAll(orderMap.get(i).getAttackList());
            moveList.addAll(orderMap.get(i).getMoveList());
        }
        checkAndExecuteMoveBehavior(moveList);
        checkAndExecuteAttackBehavior(attackList);
        //need check all player status by using map
        //need increase unit of each territory
    }
    private void executeMoveBehavior(Behavior b){
        String sourceName = b.getOrigin().getName();
        String destName = b.getDestination().getName();
        int unit = b.getUnit();
        for(int i = 0; i < map.size(); i++){
            if(sourceName.equals(map.get(i).getName())){
                map.get(i).setUnit(map.get(i).getUnit()-unit);
                break;
            }
        }
        for(int i = 0; i < map.size(); i++){
            if(destName.equals(map.get(i).getName())){
                map.get(i).setUnit(map.get(i).getUnit()+unit);
                break;
            }
        }
    }

    private void checkAndExecuteMoveBehavior(ArrayList<Behavior> behaviorArrayList) {
        for(Behavior b:behaviorArrayList){
            if(ruleChecker.checkBehavior(b,map)==null){
                executeMoveBehavior(b);
            }
        }
    }

    private void moveUnitCuzAttack(Behavior b){
        String sourceName = b.getOrigin().getName();
        int unit = b.getUnit();
        for(int i = 0; i < map.size(); i++){
            if(sourceName.equals(map.get(i).getName())){
                map.get(i).setUnit(map.get(i).getUnit()-unit);
                break;
            }
        }
    }

    private boolean getAttackResult(){
        Random rand = new Random();
        int attacker = rand.nextInt(20)+1;
        int defender = rand.nextInt(20)+1;
        return attacker > defender;
    }

    private void updateAllNeighbor(Territory t){
        for(int i = 0; i < map.size(); i++){
            for(Map.Entry<Integer,ArrayList<String>> e:map.get(i).getNeighbor().entrySet()){
                if(e.getValue().contains(t.getName())){
                    map.get(i).updateNeighbor(t);
                    break;
                }
            }
        }
    }

    private void executeAttackBehavior(Behavior b){
        String destName = b.getDestination().getName();
        int unit = b.getUnit();
        for(int i = 0; i < map.size(); i++){
            if(destName.equals(map.get(i).getName())){
                while(unit!=0&&map.get(i).getUnit()!=0){
                    if(getAttackResult()){
                        map.get(i).setUnit(map.get(i).getUnit()-1);
                    } else {
                        unit--;
                    }
                }
                if(unit!=0){
                    map.get(i).setOwnID(b.getOwnID());
                    map.get(i).setUnit(unit);
                    updateAllNeighbor(map.get(i));
                }
                break;
            }
        }
    }

    private void checkAndExecuteAttackBehavior(ArrayList<Behavior> behaviorArrayList){
        for(Behavior b:behaviorArrayList){
            if(ruleChecker.checkBehavior(b,map)==null){
                moveUnitCuzAttack(b);
            }
        }
        for(Behavior b:behaviorArrayList){
            if(ruleChecker.checkBehavior(b,map)==null){
                executeAttackBehavior(b);
            }
        }
    }

    public void sendPlayerStatus() {
    }

    public boolean gameOver() {
        int ownID = 0;
        for (Territory t : map) {
            if (ownID == 0) {
                ownID = t.getOwnID();
            } else if (ownID != t.getOwnID() && t.getOwnID() != -1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }

}
