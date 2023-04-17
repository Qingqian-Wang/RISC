import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player {
    public Map<Integer, GameInfo> gameInfoList;
    public ArrayList<Integer> joinGameList;
    public ArrayList<Integer> gamePortList;
    public int currentGame;
    public int serverPort;


    private final BasicChecker ruleChecker;
    private UpgradeChecker upgradeChecker;


    // initialize  player by server port number
    public Player(int serverPort) {
        this.serverPort = serverPort;
        currentGame = -1;
        gameInfoList = new HashMap<>();
        ruleChecker = new OriginChecker(null);
        upgradeChecker = new UpgradeChecker();
    }

    // receive player status from server
    public void updateStatus() throws IOException {
        gameInfoList.get(currentGame).setStatus(Integer.parseInt(gameInfoList.get(currentGame).getIn().readLine()));
    }

    // create socket to connect with server
    public void connectToServer() throws IOException {
        Socket toServer = new Socket("localhost", serverPort);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = new PrintWriter(toServer.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
        String message = in.readLine();
        System.out.println(message);
        gamePortList = objectMapper.readValue(in.readLine(), new TypeReference<>() {
        });
        ArrayList<Integer> availableList = new ArrayList<>();
        for (int i = 0; i < gamePortList.size(); i++) {
            String availableInfo = in.readLine();
            System.out.println(availableInfo);
            if (availableInfo.charAt(0) == 'A') {
                String[] tokens = availableInfo.split(" ");
                availableList.add(Integer.parseInt(tokens[1]));
            }
        }

        StringBuilder sb = new StringBuilder();
        joinGameList = new ArrayList<>();
        while (true) {
            System.out.println("Which game you want to play? Type '0' if you do not want to join game for this time");
            System.out.println("Available game ID: ");
            for (Integer i : availableList) {
                System.out.println(i);
            }
            InputStreamReader sr = new InputStreamReader(System.in);
            BufferedReader bf = new BufferedReader(sr);
            String response = bf.readLine();
            while (response.length() != 1 || (response.toUpperCase().charAt(0) != '0'
                    && !availableList.contains(Integer.parseInt(response)))) {
                System.out.println("Your input is invalid, please try again");
                System.out.println("Which game you want to play? Type '0' if you do not want to join game for this time");
                System.out.println("Available game ID: ");
                for (Integer i : availableList) {
                    System.out.println(i);
                }
                response = bf.readLine();
            }
            if (response.toUpperCase().charAt(0) == '0') {
                break;
            } else {
                if (!joinGameList.contains(Integer.parseInt(response))) {
                    joinGameList.add(Integer.parseInt(response));
                } else {
                    System.out.println("You already tell me you want to join to the target game");
                }
            }
        }
        for (Integer i : joinGameList) {
            sb.append(i);
            sb.append(' ');
        }
        out.println(sb);
    }

    public void connectToGame() throws IOException {
        for (Integer i : joinGameList) {
            Socket clientSocket = new Socket("localhost", gamePortList.get(i - 1));
            DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
            int playerID = dataIn.readInt();
            int totalNumPlayer = dataIn.readInt();
            GameInfo gameInfo = new GameInfo(clientSocket, totalNumPlayer, playerID, i);
            gameInfoList.put(i, gameInfo);
        }
    }

    // check if the name exist in the map
    private boolean checkBehaviorInputFormatHelper(String name, ArrayList<Territory> map) {
        for (Territory t : map) {
            if (t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkUpgradeBehavior(String s, ArrayList<Territory> map) {
        String[] tokens = s.split(" ");
        if (tokens.length != 4) {
            return false;
        }
        try {
            int number = Integer.parseInt(tokens[0]);
            if (number <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return checkBehaviorInputFormatHelper(tokens[1], map);
    }


    // check if the format of input for creating behavior is correct
    private boolean checkBehaviorInputFormat(String s, ArrayList<Territory> map) {
        String[] tokens = s.split(" ");
        if (tokens.length != 3) {
            return false;
        }
        try {
            ArrayList<Integer> unit = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                unit.add(Integer.parseInt(tokens[0].split("\\|")[i]));
                if (unit.get(i) < 0) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return checkBehaviorInputFormatHelper(tokens[1], map) && checkBehaviorInputFormatHelper(tokens[2], map)
                && (!tokens[1].equals(tokens[2]));
    }

    // get Territory object based on name
    private Territory getTerritoryByName(String s, ArrayList<Territory> map) {
        for (Territory t : map) {
            if (t.getName().equals(s)) {
                return t;
            }
        }
        return null;
    }


    // the game play function
    public void playGame() {
        try {
            // continuous receive message from server
            String inputLine;
            label:
            while (true) {
                if (currentGame == -1) {
                    System.out.println("Please select the game you want to play, type the game ID to select");
                    System.out.println("Game list:");
                    for (Integer i : joinGameList) {
                        System.out.println("Game" + i);
                    }
                    InputStreamReader sr = new InputStreamReader(System.in);
                    BufferedReader bf = new BufferedReader(sr);
                    String response;
                    try {
                        response = bf.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    while (response.length() != 1 || !joinGameList.contains(Integer.parseInt(response))) {
                        System.out.println("Your input is invalid");
                        System.out.println("Please select the game you want to play, type the game ID to select");
                        System.out.println("Game list:");
                        for (Integer i : joinGameList) {
                            System.out.println("Game" + i);
                        }
                        try {
                            response = bf.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    currentGame = Integer.parseInt(response);
                } else if ( joinGameList.size() != 1) {
                    System.out.println("You are currently in Game" + currentGame);
                    System.out.println("Do you want to switch?(yes or no)");
                    InputStreamReader sr = new InputStreamReader(System.in);
                    BufferedReader bf = new BufferedReader(sr);
                    String response;
                    try {
                        response = bf.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    while (!response.equals("yes") && !response.equals("no")) {
                        System.out.println("Your input is invalid");
                        System.out.println("You are currently in Game" + currentGame);
                        System.out.println("Do you want to switch?(yes or no)");
                        try {
                            response = bf.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (response.equals("yes")) {
                        System.out.println("Please select the game you want to play, type the game ID to select");
                        System.out.println("Game list:");
                        for (Integer i : joinGameList) {
                            System.out.println("Game" + i);
                        }
                        try {
                            response = bf.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        while (response.length() != 1 || !joinGameList.contains(Integer.parseInt(response))) {
                            System.out.println("Your input is invalid");
                            System.out.println("Please select the game you want to play, type the game ID to select");
                            System.out.println("Game list:");
                            for (Integer i : joinGameList) {
                                System.out.println("Game" + i);
                            }
                            try {
                                response = bf.readLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        currentGame = Integer.parseInt(response);
                    }
                }
                BufferedReader in = gameInfoList.get(currentGame).getIn();
                PrintWriter out = gameInfoList.get(currentGame).getOut();
                inputLine = in.readLine();
                System.out.println(inputLine);
                // handle game start situation
                switch (inputLine) {
                    case "Game Start" -> {
                        int totalUnit = Integer.parseInt(in.readLine());
                        String response = null;
                        int[] numbers = null;
                        Scanner scanner = new Scanner(System.in);
                        // make sure the input has correct format, if not, let user retry
                        while (numbers == null) {
                            System.out.println("The total number of unit you can use in your territory is " + totalUnit);
                            System.out.println("Enter a string of space-separated numbers where the sum of them is " + totalUnit);
                            response = scanner.nextLine();
                            String[] tokens = response.split(" ");
                            numbers = new int[tokens.length];
                            try {
                                for (int i = 0; i < tokens.length; i++) {
                                    numbers[i] = Integer.parseInt(tokens[i]);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input(format).");
                                numbers = null;
                            }
                            if (numbers != null) {
                                int sum = 0;
                                for (String token : tokens) {
                                    sum += Integer.parseInt(token);
                                }
                                if (sum != totalUnit) {
                                    System.out.println("Total number of unit is not equal to " + totalUnit + ".");
                                    numbers = null;
                                }
                            }
                        }
                        // send unit information for territory back to server
                        out.println(response);
                    }
                    case "Turn Start" -> {
                        updateStatus();
                        // get GlobalMap object from server
                        ObjectMapper objectMapper = new ObjectMapper();
                        ArrayList<Territory> currentMap = objectMapper.readValue(in.readLine(), new TypeReference<>() {
                        });
//                    current.receiveList(in);
                        // print the current map information
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= gameInfoList.get(currentGame).getTotalPlayerNum(); i++) {
                            sb.append("Player").append(i).append(":").append(System.lineSeparator());
                            for (Territory territory : currentMap) {
                                if (territory.getOwnID() == i) {
                                    sb.append(territory.getUnits().printUnits() + " units in " + territory.getName() +
                                            "(next to:");
                                    ArrayList<String> neighborName = new ArrayList<>();
                                    for (Map.Entry<Integer, ArrayList<String>> e : territory.getNeighbor().entrySet()) {
                                        neighborName.addAll(e.getValue());
                                        for (int x = 0; x < neighborName.size(); x++) {
                                            sb.append(" " + neighborName.get(x));
                                            if (x != neighborName.size() - 1) {
                                                sb.append(",");
                                            } else {
                                                sb.append(")").append(System.lineSeparator());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println(sb);
                        // if the player has no territory, ask if the user want to stay
                        if (gameInfoList.get(currentGame).getStatus() == 0 && gameInfoList.get(currentGame).getWatchingPattern() == 0) {
                            System.out.println("you lose the game, do you want to watch the rest of the game? enter yes to watch");
                            InputStreamReader sr = new InputStreamReader(System.in);
                            BufferedReader bf = new BufferedReader(sr);
                            String response = bf.readLine();
                            if (response.equals("yes")) {
                                gameInfoList.get(currentGame).setWatchingPattern(1);
                            } else {
                                BehaviorList behaviorList = new BehaviorList(gameInfoList.get(currentGame).getPlayerID(), -1);
                                out.println(objectMapper.writeValueAsString(behaviorList));
                                break label;
                            }
                        }
                        BehaviorList behaviorList = new BehaviorList(gameInfoList.get(currentGame).getPlayerID(), gameInfoList.get(currentGame).getStatus());
                        if (gameInfoList.get(currentGame).getWatchingPattern() == 1) {   // if the player is in the watching pattern then don't add any order
                            out.println(objectMapper.writeValueAsString(behaviorList));
                        } else {
                            // if the player has some territory
                            while (true) {
                                System.out.println("You are player " + gameInfoList.get(currentGame).getPlayerID() + ", what would you like to do?");
                                System.out.println("(M)ove");
                                System.out.println("(A)ttack");
                                System.out.println("(U)pgrade");
                                System.out.println("(D)one");
                                InputStreamReader sr = new InputStreamReader(System.in);
                                BufferedReader bf = new BufferedReader(sr);
                                String response = bf.readLine();
                                while (response.length() != 1 || (response.toUpperCase().charAt(0) != 'M'
                                        && response.toUpperCase().charAt(0) != 'A' && response.toUpperCase().charAt(0) != 'D' && response.toUpperCase().charAt(0) != 'U')) {
                                    System.out.println("Your input is not in correct format, try again");
                                    System.out.println("You are player " + gameInfoList.get(currentGame).getPlayerID() + ", what would you like to do?");
                                    System.out.println("(M)ove");
                                    System.out.println("(A)ttack");
                                    System.out.println("(U)pgrade");
                                    System.out.println("(D)one");
                                    response = bf.readLine();
                                }
                                // get behavior type
                                if (response.toUpperCase().charAt(0) == 'M' || response.toUpperCase().charAt(0) == 'A') {
                                    Behavior behavior = null;
                                    while (behavior == null) {
                                        System.out.println("Please entry your behavior in this format:Unit(level0|1|2|3|4|5|6 ) SourceTerritory DestinationTerritory");
                                        String behaviorInfo = bf.readLine();
                                        while (!checkBehaviorInputFormat(behaviorInfo, currentMap)) {
                                            System.out.println("Your input is not in correct format, please " +
                                                    "check your unit, SourceTerritory, and DestinationTerritory and try again");
                                            System.out.println("Please entry your behavior in this format:Unit SourceTerritory DestinationTerritory");
                                            behaviorInfo = bf.readLine();
                                        }

                                        String[] tokens = behaviorInfo.split(" ");
                                        ArrayList<Integer> unit = new ArrayList<>();
                                        for (int i = 0; i < 7; i++) {
                                            unit.add(Integer.parseInt(tokens[0].split("|")[i]));
                                        }

                                        if (response.toUpperCase().charAt(0) == 'M') {// move behavior initialize
                                            behavior = new Behavior(getTerritoryByName(tokens[1], currentMap), getTerritoryByName(tokens[2], currentMap), unit, gameInfoList.get(currentGame).getPlayerID(), "Move");
                                        } else if (response.toUpperCase().charAt(0) == 'A') {// attack behavior initialize
                                            behavior = new Behavior(getTerritoryByName(tokens[1], currentMap), getTerritoryByName(tokens[2], currentMap), unit, gameInfoList.get(currentGame).getPlayerID(), "Attack");
                                        }
                                        // check if the unit and source is correct for the behavior
                                        if (ruleChecker.checkBehavior(behavior, currentMap) != null) {
                                            System.out.println(ruleChecker.checkBehavior(behavior, currentMap));
                                            behavior = null;
                                        }
                                    }
                                    // add to arraylist based on the type of behavior
                                    if (response.toUpperCase().charAt(0) == 'M') {
                                        behaviorList.addToMoveList(behavior);
                                    } else {
                                        behaviorList.addToAttackList(behavior);
                                    }
                                } else if (response.toUpperCase().charAt(0) == 'U') {
                                    upgradeBehavior behavior = null;
                                    while (behavior == null) {
                                        System.out.println("Please entry your behavior in this format:Unit Territory currentLevel targetLevel");
                                        String behaviorInfo = bf.readLine();
                                        while (!checkUpgradeBehavior(behaviorInfo, currentMap)) {// check here need more change
                                            System.out.println("Your input is not in correct format, please " +
                                                    "check your unit, Territory, currentLevel and TargetLevel and try again");
                                            System.out.println("Please entry your behavior in this format:Unit Territory currentLevel targetLevel");
                                            behaviorInfo = bf.readLine();
                                        }
                                        String[] tokens = behaviorInfo.split(" ");
                                        behavior = new upgradeBehavior(getTerritoryByName(tokens[1], currentMap), gameInfoList.get(currentGame).getPlayerID(), "Upgrade", Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[0]));
                                        // check if the unit and source is correct for the behavior
                                        if (upgradeChecker.checkMyRule(behavior, currentMap) != null) {
                                            System.out.println(upgradeChecker.checkMyRule(behavior, currentMap));
                                            behavior = null;
                                        }
                                    }
                                    // add to arraylist based on the type of behavior
                                    behaviorList.addToUpgradeList(behavior);
                                } else if (response.toUpperCase().charAt(0) == 'D') {// end turn
                                    out.println(objectMapper.writeValueAsString(behaviorList));
                                    break;
                                }
                            }
                        }
                    }
                    case "Game Over" -> { // if game is over, quit
                        in.close();
                        out.close();
                        gameInfoList.get(currentGame).disconnect();
                        gameInfoList.remove(currentGame);
                        currentGame = -1;
                        if (gameInfoList.isEmpty()) {
                            break label;
                        }
                    }
                }
            }
            System.out.println("Server connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                for (Map.Entry<Integer, GameInfo> e : gameInfoList.entrySet()) {
                    e.getValue().disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Invalid input");
        } else {
            int serverPort = Integer.parseInt(args[0]);
            Player p1 = new Player(serverPort);
            p1.connectToServer();
            p1.connectToGame();
            p1.playGame();
        }
    }
}