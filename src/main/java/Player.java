import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player {
    public Socket clientSocket;

    public int playerID;

    public int status;

    public int serverPort;

    public int totalNumPlayer;

    private BufferedReader in;

    private PrintWriter out;

    private BasicChecker ruleChecker;

    private BasicChecker checkUpgrade;

    public int watchingPattern = 0;

    // initialize  player by server port number
    public Player(int serverPort) {
        this.serverPort = serverPort;
        this.status = 1;
        ruleChecker = new OriginChecker(null);
    }

    // receive player status from server
    public void updateStatus() throws IOException, ClassNotFoundException {
        status = Integer.parseInt(in.readLine());
    }
    // create socket to connect with server
    public void connectToServer() throws IOException {
        this.clientSocket = new Socket("localhost", serverPort);
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
        this.playerID = dataIn.readInt();
        this.totalNumPlayer = dataIn.readInt();
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


    private boolean checkAttackBehavior(String s, ArrayList<Territory> map){
        String[] tokens = s.split(" ");
        if(tokens.length != 4){
            return false;
        }
        try{
            int number = Integer.parseInt(tokens[0]);
            if(number <= 0){
                return false;
            }
        }catch (NumberFormatException e){
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
                unit.add(Integer.parseInt(tokens[0].split("|")[i]));
                if(unit.get(i) < 0){
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

//    // use the info of OwnID and Unit to update GlobalMap object
//    private void updateMap(GlobalMap gm, ArrayList<Integer> ownID, ArrayList<Integer> UnitInfo){
//
//        for(int i = 0; i < ownID.size(); i++){
//            gm.getMapArrayList().get(i).setOwnID(ownID.get(i));
//            gm.getMapArrayList().get(i).setUnit(UnitInfo.get(i));
//        }
//    }



    // the game play function
    public void playGame() {
        try {
            // continuous receive message from server
            String inputLine;
            while (true) {
                inputLine = in.readLine();
                System.out.println(inputLine);
                // handle game start situation
                if (inputLine.equals("Game Start")) {
//                    DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
//                    int totalUnit = dataIn.readInt();
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
                            for (int i = 0; i < tokens.length; i++) {
                                sum += Integer.parseInt(tokens[i]);
                            }
                            if (sum != totalUnit) {
                                System.out.println("Total number of unit is not equal to " + totalUnit + ".");
                                numbers = null;
                            }
                        }
                    }
                    // send unit information for territory back to server
                    out.println(response);
                } else if (inputLine.equals("Turn Start")) {
                    updateStatus();
                    // get GlobalMap object from server
                    ObjectMapper objectMapper = new ObjectMapper();
                    ArrayList<Territory> currentMap = objectMapper.readValue(in.readLine(),new TypeReference<ArrayList<Territory>>(){});
//                    current.receiveList(in);
                    // print the current map information
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i <= totalNumPlayer; i++) {
                        sb.append("Player").append(i).append(":").append(System.lineSeparator());
                        for (int j = 0; j < currentMap.size(); j++) {
                            if (currentMap.get(j).getOwnID() == i) {
                                sb.append(currentMap.get(j).getUnits().printUnits() + " units in " + currentMap.get(j).getName() +
                                        "(next to:");
                                ArrayList<String> neighborName = new ArrayList<>();
                                for (Map.Entry<Integer, ArrayList<String>> e : currentMap.get(j).getNeighbor().entrySet()) {
                                    for (int x = 0; x < e.getValue().size(); x++) {
                                        neighborName.add(e.getValue().get(x));
                                    }
                                }
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
                    System.out.println(sb.toString());
                    // if the player has no territory, ask if the user want to stay
                    if(status == 0 && watchingPattern == 0){
                        System.out.println("you lose the game, do you want to watch the rest of the game? enter yes to watch");
                        InputStreamReader sr = new InputStreamReader(System.in);
                        BufferedReader bf = new BufferedReader(sr);
                        String response = bf.readLine();
                        if(response.equals("yes")){
                            watchingPattern = 1;
                        }else{
                            BehaviorList behaviorList = new BehaviorList(playerID, -1);
                            out.println(objectMapper.writeValueAsString(behaviorList));
                            break;
                        }
                    }
                    BehaviorList behaviorList = new BehaviorList(playerID, status);
                    if(watchingPattern == 1){   // if the player is in the watching pattern then don't add any order
                        out.println(objectMapper.writeValueAsString(behaviorList));
                    }else {
                        // if the player has some territory
                        while (true) {
                            System.out.println("You are player " + playerID + ", what would you like to do?");
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
                                System.out.println("You are player " + playerID + ", what would you like to do?");
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
                                        behavior = new Behavior(getTerritoryByName(tokens[1], currentMap), getTerritoryByName(tokens[2], currentMap), unit, playerID, "Move");
                                    } else if(response.toUpperCase().charAt(0) == 'A'){// attack behavior initialize
                                        behavior = new Behavior(getTerritoryByName(tokens[1], currentMap), getTerritoryByName(tokens[2], currentMap), unit, playerID, "Attack");
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
                                    while (!checkAttackBehavior(behaviorInfo, currentMap)) {// check here need more change
                                        System.out.println("Your input is not in correct format, please " +
                                                "check your unit, Territory, currentLevel and TargetLevel and try again");
                                        System.out.println("Please entry your behavior in this format:Unit Territory currentLevel targetLevel");
                                        behaviorInfo = bf.readLine();
                                    }
                                    String[] tokens = behaviorInfo.split(" ");

                                    behavior = new upgradeBehavior(getTerritoryByName(tokens[1], currentMap), playerID,"Upgrade", Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[0]));


                                    // check if the unit and source is correct for the behavior
                                    if (checkUpgrade.checkBehavior(behavior, currentMap) != null) {
                                        System.out.println(ruleChecker.checkBehavior(behavior, currentMap));
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
                } else if (inputLine.equals("Game Over")) {// if game is over, quit
                    break;
                }
            }
            System.out.println("Server connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        if(args.length!=1){
            System.out.println("Invalid input");
        } else {
            int serverPort = Integer.parseInt(args[0]);

            Player p1 = new Player(serverPort);
            p1.connectToServer();
            p1.playGame();
        }
    }
}