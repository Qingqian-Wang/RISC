package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Game implements Runnable {
    public ArrayList<PlayerInfo> playerInfoList;

    public ArrayList<Territory> map;
    public Map<Integer, ArrayList<Territory>> viewMap;
    public int port;
    public int maxPlayerNum;
    public ServerSocket serverSocket;
    public BasicChecker ruleChecker;
    public UpgradeChecker upgradeChecker;
    // store the rest cost of each player
    public ArrayList<Integer> restCost;
    public ArrayList<Integer> restFood;
    public ArrayList<Integer> techLevelList;
    public ArrayList<Integer> cloakingOpen;
    // 0 means not open, 1 means open

    /*
     * Constructor to create a Server object
     *
     * @param port the port number on which the server will listen for incoming
     * connections
     *
     * @throws IOException if an I/O error occurs when opening the server socket
     */
    public Game(int port, int maxPlayerNum) throws IOException {
        playerInfoList = new ArrayList<>();
        map = new ArrayList<>();
        this.port = port;
        this.maxPlayerNum = maxPlayerNum;
        serverSocket = new ServerSocket(this.port);
        ruleChecker = new OriginChecker(new DestinationChecker(null));
        upgradeChecker = new UpgradeChecker();
        restCost = new ArrayList<>();
        restFood = new ArrayList<>();
        techLevelList = new ArrayList<>();
        viewMap = new HashMap<>();
        cloakingOpen = new ArrayList<>();
        for (int i = 0; i < maxPlayerNum; i++) {
            restCost.add(50);
            restFood.add(500);
            techLevelList.add(1);
            cloakingOpen.add(0);
        }
    }

    /*
     * Accepts players to the game
     *
     * @param playerNum the number of players that will be accepted
     *
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    public void acceptPlayer() throws IOException {
        for (int i = 0; i < maxPlayerNum; i++) {
            Socket playerSocket = serverSocket.accept();
            PlayerInfo p = new PlayerInfo(playerSocket, i + 1);
            DataOutputStream dataOut = new DataOutputStream(p.getPlayerSocket().getOutputStream());
            dataOut.writeInt(i + 1);
            dataOut.writeInt(maxPlayerNum);
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

    @Override
    public void run() {
        try {
            acceptPlayer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            String unitInfo = null;
            try {
                while (unitInfo == null) {
                    unitInfo = playerInfo.getIn().readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] tokens = unitInfo.split(" ");
            for (int i = 0; i < tokens.length; i++) {
                Territory temp = map.get(territoryOwner[playerInfo.getPlayerID() - 1][i]);
                temp.getUnits().initUnit(Integer.parseInt(tokens[i]));
//                temp.setUnit(Integer.parseInt(tokens[i]));
                map.set(territoryOwner[playerInfo.getPlayerID() - 1][i], temp);
            }
//            dataOut.close();
        }
        initViewMap();
        while (!gameOver()) {
            try {
                playTurn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Send game over message to all players and disconnect them
        for (PlayerInfo playerInfo : playerInfoList) {
            playerInfo.getOut().println("Game Over");
            try {
                playerInfo.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     *
     * Increases the unit count of each territory by one.
     */
    public void addOneUnit() {
        for (int i = 0; i < map.size(); i++) {
//            int unit = map.get(i).getUnits().addUnits(1, 0);
            map.get(i).getUnits().addUnits(1, 0);
        }
    }

    public void addCost() {
        for (int i = 0; i < map.size(); i++) {
            restCost.set(map.get(i).getOwnID() - 1, restCost.get(map.get(i).getOwnID() - 1) + 10);
            restFood.set(map.get(i).getOwnID() - 1, restFood.get(map.get(i).getOwnID() - 1) + 100);
        }
    }

    //=======================================upgrade function =====================
    public void checkAndExecuteUpgradeBehavior(ArrayList<upgradeBehavior> behaviorArrayList) {  // checkandupdate
        for (upgradeBehavior b : behaviorArrayList) {
            if (upgradeChecker.checkMyRule(restCost.get(b.getOwnID() - 1), b, map) == null) {
                executeUpgradeBehavior(b);
            } else {
                System.out.println("Upgrade denied because "+ upgradeChecker.checkMyRule(restCost.get(b.getOwnID() - 1), b, map));
            }
        }
    }

    public void executeUpgradeBehavior(upgradeBehavior b) {
        String sourceName = b.getOrigin().getName();
        int unitsNum = b.getUnitsNum();
        int currLevel = b.getCurrLevel();
        int targetLevel = b.getTargetLevel();
        restCost.set(b.getOwnID() - 1, restCost.get(b.getOwnID() - 1) - b.getUpgradeCost());
        for (int i = 0; i < map.size(); i++) {
            if (sourceName.equals(map.get(i).getName())) {
                map.get(i).getUnits().removeUnits(unitsNum, currLevel);
                map.get(i).getUnits().addUnits(unitsNum, targetLevel);
                break;
            }
        }

        System.out.println("Upgrade " + b.getUnitsNum() + " soldiers from level" + b.getCurrLevel() + " to level " + b.getTargetLevel() + " at " + sourceName);
    }


    // ======================================the end of upgrade function================

    //========================================cloak function============================
    public void checkAndExecuteCloakBehavior(ArrayList<Behavior> behaviorArrayList) {
        for (Behavior b : behaviorArrayList) {
            if(cloakingOpen.get(b.getOwnID() - 1) == 0){
                if(restCost.get(b.getOwnID() - 1) < 100||techLevelList.get(b.getOwnID()-1)<3) {
                    System.out.println("Cloak fail because either Cost is not enough or Tech LeveL is less than 3");
                    continue;
                }
                restCost.set(b.getOwnID() - 1, restCost.get(b.getOwnID() - 1) - 100);
                cloakingOpen.set(b.getOwnID() - 1, 1);
            }
            executeCloakBehavior(b);
        }
    }
    public void executeCloakBehavior(Behavior b) {
        if(restCost.get(b.getOwnID() - 1) > 20){
            restCost.set(b.getOwnID() - 1, restCost.get(b.getOwnID() - 1) - 20);
            String terrName = b.getOrigin().getName();
            for (int i = 0; i < map.size(); i++) {
                if (terrName.equals(map.get(i).getName())) {
                    map.get(i).setHideTurnCount(map.get(i).getHideTurnCount() + 4);
                    break;
                }
            }
            System.out.println("Cloak " + b.getOrigin().getName() + "for three more turns");
        }
    }


    //========================================the end of upgrade function=======================================


    //evolve function
    public int checkEvolevelBehavior(ArrayList<Integer> totalCost, int playerID) {
        int currentTechLevel = techLevelList.get(playerID - 1);
        if (currentTechLevel == 6) {
            return -1;
        }
        int currentRestCost = restCost.get(playerID - 1);
        if (currentRestCost < (totalCost.get(currentTechLevel + 1))) {
            return -1;
        }
        return totalCost.get(currentTechLevel + 1);
    }

    public void checkAndExecuteEvolveHelper(int playerID, int count) {
        if (count == 0) {
            return;
        }
        ArrayList<Integer> totalCost = new ArrayList<>();
        totalCost.add(0);
        totalCost.add(0);
        totalCost.add(50);
        totalCost.add(75);
        totalCost.add(125);
        totalCost.add(200);
        totalCost.add(300);
        int expectedCost = checkEvolevelBehavior(totalCost, playerID);
        if (expectedCost != -1) {
            System.out.println("Player " + playerID + " evolve succeed");
            techLevelList.set(playerID - 1, techLevelList.get(playerID - 1) + 1);
            restCost.set(playerID - 1, restCost.get(playerID - 1) - expectedCost);
            checkAndExecuteEvolveHelper(playerID, count - 1);
        } else{
            System.out.println("Player "+ playerID +" evolve fail because no enough Cost");
        }
    }

    public void checkAndExecuteEvolve(Map<Integer, Integer> evolveList) {
        for (Map.Entry<Integer, Integer> e : evolveList.entrySet()) {
            checkAndExecuteEvolveHelper(e.getKey(), e.getValue());
        }
    }
    //end evolve function

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

    public void initViewMap() {
        for (int i = 1; i <= maxPlayerNum; i++) {
            ArrayList<Territory> tempViewMap = new ArrayList<>();
            for (Territory t : map) {
                if (i == t.getOwnID() || (t.getSpiesCollection().containsKey(i) && t.getSpiesCollection().get(i) > 0)) {// if this has spy or you are the owner
                    Territory tempView = new Territory(t);
                    tempView.setAbleToSee(true);
                    tempViewMap.add(tempView);
                } else if (t.getNeighbor().containsKey(i) && !t.getNeighbor().get(i).isEmpty()) {// if this is just adjusted
                    Territory tempView = new Territory(t);
                    tempView.setAbleToSee(true);
                    tempViewMap.add(tempView);
                } else {// if the player never see the territory
                    Territory tempView = new Territory(t, -1);
                    tempView.setAbleToSee(false);
                    tempViewMap.add(tempView);
                }
            }
            viewMap.put(i, tempViewMap);
        }
    }


    public void playTurn() throws Exception {
        List<Integer> orders = new ArrayList<>();
        for (int i = 1; i <= playerInfoList.size(); i++) {
            orders.add(i);
        }
        Random rand = new Random();
        HashMap<Integer, BehaviorList> orderMap = new HashMap<>();
        ArrayList<Behavior> attackList = new ArrayList<>();
        ArrayList<Behavior> moveList = new ArrayList<>();
        ArrayList<upgradeBehavior> upgradeList = new ArrayList<>();
        Map<Integer, Integer> evolveList = new HashMap<>();
        ArrayList<Behavior> cloakList = new ArrayList<>();
        for (PlayerInfo playerInfo : playerInfoList) {
            playerInfo.getOut().println("Turn Start");
            sendPlayerStatus(playerInfo);
            sendPlayerRestCost(playerInfo);
            ObjectMapper objectMapper = new ObjectMapper();
            String mapInfo = objectMapper.writeValueAsString(viewMap.get(playerInfo.getPlayerID()));
            playerInfo.getOut().println(mapInfo);
            String behaviorListInfo = playerInfo.getIn().readLine();
            BehaviorList behaviorList = objectMapper.readValue(behaviorListInfo, BehaviorList.class);
            if (behaviorList.status == -1) {
                playerInfo.disconnect();
                playerInfoList.remove(playerInfo);
            }
            int index = rand.nextInt(orders.size());
            orderMap.put(orders.get(index), behaviorList);
            orders.remove(index);
            evolveList.put(playerInfo.getPlayerID(), behaviorList.getEvloveNum());
        }
        for (int i = 1; i <= playerInfoList.size(); i++) {
            attackList.addAll(orderMap.get(i).getAttackList());
            moveList.addAll(orderMap.get(i).getMoveList());
            upgradeList.addAll(orderMap.get(i).getUpgradeList());
            cloakList.addAll(orderMap.get(i).getCloakList());
            // update restCost
//            restCost.set(orderMap.get(i).getPlayerID() - 1, orderMap.get(i).getRestCost());
        }
        checkAndExecuteEvolve(evolveList);
        checkAndExecuteMoveBehavior(moveList);
        checkAndExecuteAttackBehavior(attackList);
        checkAndExecuteUpgradeBehavior(upgradeList);
        checkAndExecuteCloakBehavior(cloakList);
        addOneUnit();
        addCost();
        updateViewMap();
    }

    public Territory getTerritoryByName(String name, ArrayList<Territory> map) {
        for (Territory t : map) {
            if (name.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    public int findShortestPath(Territory A, String destination, ArrayList<Territory> t, ArrayList<String> visited, int currentPathLength) {
        if (A.getName().equals(destination)) {
            return currentPathLength;
        }
        visited.add(A.getName());
        ArrayList<Integer> nextShortestPath = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<String>> e : A.getNeighbor().entrySet()) {
            if (e.getKey() == A.getOwnID()) {
                for (String s : e.getValue()) {
                    if (visited.contains(s)) {
                        continue;
                    }
                    Territory next = getTerritoryByName(s, t);
                    nextShortestPath.add(findShortestPath(next, destination, t, visited, currentPathLength + next.getSize()));
                }
            }
        }
        int res = Integer.MAX_VALUE;
        for (Integer i : nextShortestPath) {
            if (i < res) {
                res = i;
            }
        }
        return res;
    }

    public void executeMoveBehavior(Behavior b) {
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
        restFood.set(b.getOwnID() - 1, restFood.get(b.getOwnID() - 1) - (findShortestPath(b.getOrigin(), destName, map, new ArrayList<String>(), 0) * b.getUnits().getRemainUnits()));
        System.out.println("Move " + units.printUnits() + " from " + sourceName + " to " + destName);
    }

    public void checkAndExecuteMoveBehavior(ArrayList<Behavior> behaviorArrayList) {  // checkandupdate
        for (Behavior b : behaviorArrayList) {
            if (b.getUnits().getUnits().get(7) != 0) {
                if (checkSpyMove(b)) {
                    executeSpyMoveBehavior(b);
                } else {
                    System.out.println("Move spy fail because no enough spy or target is not adjacency");
                }
            } else if (ruleChecker.checkBehavior(restFood.get(b.getOwnID() - 1), b, map) == null) {
                executeMoveBehavior(b);
            } else {
                System.out.println("Move fail because "+ ruleChecker.checkBehavior(restFood.get(b.getOwnID() - 1), b, map));
            }
        }
    }

    public void executeSpyMoveBehavior(Behavior b) {
        String sourceName = b.getOrigin().getName();
        String destName = b.getDestination().getName();
        unitStorage units = b.getUnits();
        for (int i = 0; i < map.size(); i++) {
            if (sourceName.equals(map.get(i).getName())) {
                if (map.get(i).getOwnID() == b.getOwnID()) {
                    map.get(i).getUnits().removeUnitStorage(units);
                } else {
                    map.get(i).removeSpy(b.getOwnID(), units.getUnits().get(7));
                }
                break;
            }
        }
        for (int i = 0; i < map.size(); i++) {
            if (destName.equals(map.get(i).getName())) {
                if (map.get(i).getOwnID() == b.getOwnID()) {
                    map.get(i).getUnits().addUnitStorage(units);
                } else {
                    map.get(i).addSpy(b.getOwnID(), units.getUnits().get(7));
                }
                break;
            }
        }
        System.out.println("Move " + units.printUnits() + " from " + sourceName + " to " + destName);
    }

    public ArrayList<String> getFrontierAndTerritory(int playerID) {
        ArrayList<String> res = new ArrayList<>();
        for (Territory t : map) {
            if (t.getOwnID() == playerID) {
                if (!res.contains(t.getName())) {
                    res.add(t.getName());
                }
                for (Map.Entry<Integer, ArrayList<String>> e : t.getNeighbor().entrySet()) {
                    for (String s : e.getValue()) {
                        if (!res.contains(s)) {
                            res.add(s);
                        }
                    }
                }
            }
        }
        return res;
    }

    public boolean checkSpyMove(Behavior b) {
        if (b.getOrigin().getOwnID() == b.getOwnID()) {
            BasicChecker tempChecker = new OriginChecker(null);
            if (tempChecker.checkBehavior(restFood.get(b.getOwnID() - 1), b, map) == null) {
                return getFrontierAndTerritory(b.getOwnID()).contains(b.getDestination().getName());
            }
            return false;
        } else {
            if (b.getOrigin().getSpiesCollection().get(b.getOwnID()) >= b.getUnits().getUnits().get(7)) {
                for (Map.Entry<Integer, ArrayList<String>> e : b.getOrigin().getNeighbor().entrySet()) {
                    if (e.getValue().contains(b.getDestination().getName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public Territory getTerritoryByNameInView(int playerID, String s){
        for(Territory t: viewMap.get(playerID)){
            if(t.getName().equalsIgnoreCase(s)){
                return t;
            }
        }
        return null;
    }
    public void updateViewMap() {
        ArrayList<Integer> playerList = new ArrayList<>();
        for(Territory t : map){
            if(t.getHideTurnCount() > 0){
                t.setHideTurnCount(t.getHideTurnCount() - 1);
            }
        }
        for(PlayerInfo p : playerInfoList){
            playerList.add(p.getPlayerID());
        }
        for (int i : playerList) {
            ArrayList<Territory> tempViewMap = new ArrayList<>();
            for (Territory t : map) {
                if (i == t.getOwnID() || (t.getSpiesCollection().containsKey(i) && t.getSpiesCollection().get(i) > 0)) {// if this has spy or you are the owner
                    Territory tempView = new Territory(t);
                    tempView.setAbleToSee(true);
                    tempViewMap.add(tempView);
                } else if (t.getNeighbor().containsKey(i) && !t.getNeighbor().get(i).isEmpty() && t.getHideTurnCount() == 0) {// if this is just adjusted and it is not hided
                    Territory tempView = new Territory(t);
                    tempView.setAbleToSee(true);
                    tempViewMap.add(tempView);
                } else if (t.getNeighbor().containsKey(i) && !t.getNeighbor().get(i).isEmpty() && t.getHideTurnCount() != 0) {// if this is adjusted and it is hided
                    Territory tempView = new Territory(getTerritoryByNameInView(i,t.getName()));
                    tempView.setAbleToSee(false);
                    tempViewMap.add(tempView);
                } else if (!getTerritoryByNameInView(i,t.getName()).getUnits().getUnits().containsValue(-1)) {// if the player had the view of this territory but now lose the view
                    Territory tempView = new Territory(getTerritoryByNameInView(i,t.getName()));
                    tempView.setOwnID(t.getOwnID());
                    tempView.setNeighbor(t.getNeighbor());
                    tempView.setAbleToSee(false);
                    tempViewMap.add(tempView);
                } else {// if the player never see the territory
                    Territory tempView = new Territory(t, -1);
                    tempView.setAbleToSee(false);
                    tempViewMap.add(tempView);
                }
            }
            viewMap.put(i,tempViewMap);
        }
    }

    public void moveUnitCuzAttack(Behavior b) {
        String sourceName = b.getOrigin().getName();
        unitStorage units = b.getUnits();
        restFood.set(b.getOwnID() - 1, restFood.get(b.getOwnID() - 1) - units.getRemainUnits());
        System.out.println("Player" + b.getOwnID() + " uses " + units.getRemainUnits() + " foods to attack");
        for (int i = 0; i < map.size(); i++) {
            if (sourceName.equals(map.get(i).getName())) {
                map.get(i).getUnits().removeUnitStorage(units);
                break;
            }
        }
    }

    public boolean getAttackResult(int attackerBonus, int defenderBonus) {
        Random rand = new Random();
        int attacker = rand.nextInt(20) + 1 + attackerBonus;
        int defender = rand.nextInt(20) + 1 + defenderBonus;
        return attacker > defender;
    }

    public void updateAllNeighbor(Territory t) {
        for (int i = 0; i < map.size(); i++) {
            for (Map.Entry<Integer, ArrayList<String>> e : map.get(i).getNeighbor().entrySet()) {
                if (e.getValue().contains(t.getName())) {
                    map.get(i).updateNeighbor(t);
                    break;
                }
            }
        }
    }

    public void executeAttackBehavior(Behavior b) {
        String destName = b.getDestination().getName();
        unitStorage units = b.getUnits();
        System.out.println("Attack " + destName + " using " + units.printUnits() + " units.");
        for (int i = 0; i < map.size(); i++) {
            if (destName.equals(map.get(i).getName())) {
                int sig = 0;
                while (units.getRemainUnits() != 0 && map.get(i).getUnits().getRemainUnits() != 0) {
                    sig++;
                    int attackerBonus = 0;
                    int defenderBonus = 0;
                    if (sig % 2 == 1) {
                        attackerBonus = units.getHighestLevel();
                        defenderBonus = map.get(i).getUnits().getLowestLevel();
                    } else {
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
                    System.out.println("Attack Success, remain " + units.printUnits() + " units");
                    map.get(i).setOwnID(b.getOwnID());
                    map.get(i).getUnits().setUnitsStorage(units);
                    updateAllNeighbor(map.get(i));
                } else {
                    System.out.println("Attack fail, remain " + map.get(i).getUnits().printUnits() + " units");
                }
                break;
            }
        }
    }

    public void checkAndExecuteAttackBehavior(ArrayList<Behavior> behaviorArrayList) {
        Random rand = new Random();
        ArrayList<Integer> playerIDs = new ArrayList<>();
        for (Behavior b : behaviorArrayList) {
            if (ruleChecker.checkBehavior(restFood.get(b.getOwnID() - 1), b, map) == null) {
                moveUnitCuzAttack(b);
                if (!playerIDs.contains(b.getOwnID())) {
                    playerIDs.add(b.getOwnID());
                }
            } else {
                System.out.println("Attack denied because: "+ruleChecker.checkBehavior(restFood.get(b.getOwnID() - 1), b, map));
                behaviorArrayList.remove(b);
                if (behaviorArrayList.isEmpty()) {
                    break;
                }
            }
        }
        List<Integer> orders = new ArrayList<>();
        for (int i = 1; i <= playerIDs.size(); i++) {
            orders.add(i);
        }
        Map<Integer, Map<String, unitStorage>> unitMap = new HashMap<>();//playerID, destname, units
        for (Behavior b : behaviorArrayList) {
            if (unitMap.containsKey(b.getOwnID())) {
                if (unitMap.get(b.getOwnID()).containsKey(b.getDestination().getName())) {
                    unitMap.get(b.getOwnID()).get(b.getDestination().getName()).addUnitStorage(b.getUnits());
                } else {
                    unitMap.get(b.getOwnID()).put(b.getDestination().getName(), b.getUnits());
                }
            } else {
                unitMap.put(b.getOwnID(), new HashMap<>());
                unitMap.get(b.getOwnID()).put(b.getDestination().getName(), b.getUnits());
            }
        }
        ArrayList<Behavior> mergedBehaviorList = new ArrayList<>();
        Map<Integer, Integer> givenOrder = new HashMap<>();//order, playerID
        for (Integer i : playerIDs) {
            int index = rand.nextInt(orders.size());
            givenOrder.put(orders.get(index), i);
            orders.remove(index);
        }
        for (int i = 1; i <= playerIDs.size(); i++) {
            Map<String, unitStorage> curr = unitMap.get(givenOrder.get(i));
            for (Map.Entry<String, unitStorage> e : curr.entrySet()) {
                Behavior addedBehavior = new Behavior(null, getTerritoryByName(e.getKey(), map), givenOrder.get(i), "Attack");
                addedBehavior.getUnits().addUnitStorage(e.getValue());
                mergedBehaviorList.add(addedBehavior);
            }
        }
        for (Behavior b : mergedBehaviorList) {
            executeAttackBehavior(b);
        }
    }

    public void sendPlayerStatus(PlayerInfo playerInfo) throws IOException {
        int count = 0;
        for (Territory t : map) {
            if (t.getOwnID() == playerInfo.getPlayerID()) {
                count++;
                break;
            }
        }
        playerInfo.getOut().println(Integer.toString(count));
    }

    public void sendPlayerRestCost(PlayerInfo playerInfo) throws IOException {
        int cost = restCost.get(playerInfo.getPlayerID() - 1);
        int food = restFood.get(playerInfo.getPlayerID() - 1);
        int techLevel = techLevelList.get(playerInfo.getPlayerID() - 1);
        playerInfo.getOut().println(cost);
        playerInfo.getOut().println(food);
        playerInfo.getOut().println(techLevel);
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
}
