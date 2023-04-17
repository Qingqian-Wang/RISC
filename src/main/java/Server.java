import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    public ArrayList<PlayerInfo> playerInfoList;

    public ArrayList<Territory> map;
    public int port;
    public ServerSocket serverSocket;
    private BasicChecker ruleChecker;
    /*
     * Constructor to create a Server object
     *
     * @param port the port number on which the server will listen for incoming
     * connections
     *
     * @throws IOException if an I/O error occurs when opening the server socket
     */
    public Server(int port) throws IOException {
        playerInfoList = new ArrayList<>();
        map = new ArrayList<>();
        this.port = port;
        serverSocket = new ServerSocket(this.port);
        ruleChecker = new OriginChecker(new DestinationChecker(null));
    }
    /*
     * Accepts players to the game
     *
     * @param playerNum the number of players that will be accepted
     *
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    public void acceptPlayer(int playerNum) throws IOException {
        for (int i = 0; i < playerNum; i++) {
            Socket playerSocket = serverSocket.accept();
            PlayerInfo p = new PlayerInfo(playerSocket, i + 1);
            DataOutputStream dataOut = new DataOutputStream(p.getPlayerSocket().getOutputStream());
            dataOut.writeInt(i + 1);
            dataOut.writeInt(playerNum);
            System.out.println("Accept new connection from " + playerSocket.getInetAddress());
            playerInfoList.add(p);
            System.out.println("Added player" + p.getPlayerID() + " to list");
        }
    }
    /*
     * Starts the game
     *
     * @throws Exception if any error occurs during game execution
     */

    public void gameStart() throws Exception {
        initializeMap();
        int[][] territoryOwner;
        if (playerInfoList.size() == 2) {
            territoryOwner = new int[][]{{0, 1, 2, 3, 4}, {5, 6, 7, 8, 9}};
        } else if (playerInfoList.size() == 3) {
            territoryOwner = new int[][]{{1, 2, 5}, {0, 3, 4}, {6, 7, 8}};
        } else if (playerInfoList.size() == 4) {
            territoryOwner = new int[][]{{0, 1}, {2, 5}, {3, 4}, {6, 7}};
        } else {
            territoryOwner = new int[][]{{0, 1}, {2, 5}, {3, 4}, {6, 7}, {8, 9}};
        }
        // Send game start message to all players and initialize their units
        for (PlayerInfo playerInfo : playerInfoList) {
            playerInfo.getOut().println("Game Start");
            playerInfo.getOut().println("50");
//            DataOutputStream dataOut = new DataOutputStream(playerInfo.getPlayerSocket().getOutputStream());
//            int totalUnit = 50;
//            dataOut.writeInt(totalUnit);
            String unitInfo = playerInfo.getIn().readLine();
            String[] tokens = unitInfo.split(" ");
            for (int i = 0; i < tokens.length; i++) {
                Territory temp = map.get(territoryOwner[playerInfo.getPlayerID() - 1][i]);
                temp.setUnit(Integer.parseInt(tokens[i]));
                map.set(territoryOwner[playerInfo.getPlayerID() - 1][i], temp);
            }
//            dataOut.close();
        }
        while (!gameOver()) {
            playTurn();
        }
        // Send game over message to all players and disconnect them
        for(PlayerInfo playerInfo:playerInfoList){
            playerInfo.getOut().println("Game Over");
            playerInfo.disconnect();
        }
        serverSocket.close();
    }
    /*
     *
     * Increases the unit count of each territory by one.
     */
    private void addOneUnit(){
        for (int i = 0; i < map.size(); i++) {
//            int unit = map.get(i).getUnits().addUnits(1, 0);
            map.get(i).getUnits().addUnits(1, 0);
        }
    }


    /*
     * Starts the game by initializing the map and territory ownership, and then
     * begins the game loop of player turns until the game is over.
     *
     * @throws Exception Throws an exception if there is an error in the game.
     */
    public void initializeMap() {
        int playerNum = playerInfoList.size();
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
        if (playerInfoList.size() == 2) {
            territoryOwner = new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
        } else if (playerInfoList.size() == 3) {
            territoryOwner = new int[]{2, 1, 1, 2, 2, 1, 3, 3, 3};
        } else if (playerInfoList.size() == 4) {
            territoryOwner = new int[]{1, 1, 2, 2, 3, 3, 4, 4};
        } else {
            territoryOwner = new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        }

        for (int i = 0; i < map.size(); i++) {
            map.get(i).setOwnID(territoryOwner[i]);
        }
        initializeMapHelper();
    }

    public void initializeMapHelper() {
        int playerNum = playerInfoList.size();
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


        map.get(5).updateNeighbor(map.get(2));
        map.get(5).updateNeighbor(map.get(4));
        if (playerNum != 4) {
            map.get(5).updateNeighbor(map.get(8));
        }

        if (playerNum != 3 && playerNum != 4) {
            map.get(5).updateNeighbor(map.get(9));

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
            map.get(9).updateNeighbor(map.get(5));
            map.get(9).updateNeighbor(map.get(8));
        }

    }

//    public ArrayList<Integer> getOwnIDList(){
//        ArrayList<Integer> res = new ArrayList<>();
//        for(Territory t:map){
//            res.add(t.getOwnID());
//        }
//        return res;
//    }
//    public ArrayList<Integer> getUnitsList(){
//        ArrayList<Integer> res = new ArrayList<>();
//        for(Territory t:map){
//            res.add(t.getUnit());
//        }
//        return res;
//    }
    public void playTurn() throws Exception {
        List<Integer> orders = new ArrayList<>();
        for (int i = 1; i <= playerInfoList.size(); i++) {
            orders.add(i);
        }
        Random rand = new Random();
        HashMap<Integer, BehaviorList> orderMap = new HashMap<>();
        ArrayList<Behavior> attackList = new ArrayList<>();
        ArrayList<Behavior> moveList = new ArrayList<>();
        for (PlayerInfo playerInfo : playerInfoList) {
            playerInfo.getOut().println("Turn Start");
            sendPlayerStatus(playerInfo);
            ObjectMapper objectMapper = new ObjectMapper();
            String mapInfo = objectMapper.writeValueAsString(map);
            playerInfo.getOut().println(mapInfo);
            String behaviorListInfo = playerInfo.getIn().readLine();
            BehaviorList behaviorList = objectMapper.readValue(behaviorListInfo, BehaviorList.class);
            if(behaviorList.status==-1){
                playerInfo.disconnect();
                playerInfoList.remove(playerInfo);
            }
            int index = rand.nextInt(orders.size());
            orderMap.put(orders.get(index), behaviorList);
            orders.remove(index);
        }
        for (int i = 1; i <= playerInfoList.size(); i++) {
            attackList.addAll(orderMap.get(i).getAttackList());
            moveList.addAll(orderMap.get(i).getMoveList());
        }
        checkAndExecuteMoveBehavior(moveList);
        checkAndExecuteAttackBehavior(attackList);
        addOneUnit();
    }

    private void executeMoveBehavior(Behavior b) {
        String sourceName = b.getOrigin().getName();
        String destName = b.getDestination().getName();
        unitStorage units = b.getUnits();
        for (int i = 0; i < map.size(); i++) {
            if (sourceName.equals(map.get(i).getName())) {
                map.get(i).getUnits().removeUnitStorage(units);
                break;
            }
        }
        for (int i = 0; i < map.size(); i++) {
            if (destName.equals(map.get(i).getName())) {
                map.get(i).getUnits().addUnitStorage(units);
                break;
            }
        }
        System.out.println("Move "+units.printUnits()+" from "+sourceName+" to " +destName);
    }

    private void checkAndExecuteMoveBehavior(ArrayList<Behavior> behaviorArrayList) {  // checkandupdate
        for (Behavior b : behaviorArrayList) {
            if (ruleChecker.checkBehavior(b, map) == null) {
                executeMoveBehavior(b);
            }
        }
    }

    private void moveUnitCuzAttack(Behavior b) {
        String sourceName = b.getOrigin().getName();
        unitStorage units = b.getUnits();
        for (int i = 0; i < map.size(); i++) {
            if (sourceName.equals(map.get(i).getName())) {
                map.get(i).getUnits().;
                break;
            }
        }
    }

    private boolean getAttackResult(int attackerBonus, int defenderBonus) {
        Random rand = new Random();
        int attacker = rand.nextInt(20) + 1 + attackerBonus;
        int defender = rand.nextInt(20) + 1 + defenderBonus;
        return attacker > defender;
    }

    private void updateAllNeighbor(Territory t) {
        for (int i = 0; i < map.size(); i++) {
            for (Map.Entry<Integer, ArrayList<String>> e : map.get(i).getNeighbor().entrySet()) {
                if (e.getValue().contains(t.getName())) {
                    map.get(i).updateNeighbor(t);
                    break;
                }
            }
        }
    }

    private void executeAttackBehavior(Behavior b) {
        String destName = b.getDestination().getName();
        unitStorage units = b.getUnits();
        System.out.println("Attack from "+b.getOrigin().getName()+" to "+ destName+" using "+units.printUnits()+" units.");
        for (int i = 0; i < map.size(); i++) {
            if (destName.equals(map.get(i).getName())) {
                int sig = 0;
                while (units.getRemainUnits() != 0 && map.get(i).getUnits().getRemainUnits() != 0) {
                    sig++;
                    int attackerBonus = 0;
                    int defenderBonus = 0;
                    if(sig % 2 == 1){
                        attackerBonus = units.getHighestLevel();
                        defenderBonus = map.get(i).getUnits().getLowestLevel();
                    }else {
                        attackerBonus = units.getLowestLevel();
                        defenderBonus = map.get(i).getUnits().getHighestLevel();
                    }

                    if (getAttackResult(attackerBonus, defenderBonus)) {
                        map.get(i).getUnits().removeUnits(1, defenderBonus);
                    } else {
                        units.removeUnits(1, attackerBonus);
                    }
                }
                if (units.getRemainUnits() != 0) {
                    System.out.println("Attack Success, remain "+units.printUnits()+" units");
                    map.get(i).setOwnID(b.getOwnID());
                    map.get(i).getUnits().setUnitsStorage(units);
                    updateAllNeighbor(map.get(i));
                } else {
                    System.out.println("Attack fail, remain "+map.get(i).getUnits().printUnits()+" units");
                }
                break;
            }
        }
    }

    private void checkAndExecuteAttackBehavior(ArrayList<Behavior> behaviorArrayList) {
        for (Behavior b : behaviorArrayList) {
            if (ruleChecker.checkBehavior(b, map) == null) { // checkandupdate
                moveUnitCuzAttack(b);
            } else {
                behaviorArrayList.remove(b);
                if(behaviorArrayList.isEmpty()){
                    break;
                }
            }
        }
        for (Behavior b : behaviorArrayList) {
            executeAttackBehavior(b);
        }
    }

    public void sendPlayerStatus(PlayerInfo playerInfo) throws IOException {
        int count = 0;
        for(Territory t:map){
            if(t.getOwnID()==playerInfo.getPlayerID()){
                count++;
                break;
            }
        }
        playerInfo.getOut().println(Integer.toString(count));
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

    public static void main(String[] args) throws Exception {
        if(args.length!=2){
            System.out.println("Invalid input");
        } else {
            int serverPort = Integer.parseInt(args[0]);
            Server s = new Server(serverPort);
            int totalPlayerNum = Integer.parseInt(args[1]);
            if(totalPlayerNum<2||totalPlayerNum>5){
                System.out.println("The total number of players should be between 2 and 5, but it is "+args[1]);
            } else {
                s.acceptPlayer(totalPlayerNum);
                s.gameStart();
            }

        }
    }

}
