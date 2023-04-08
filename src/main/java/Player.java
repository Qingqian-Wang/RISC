import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player implements Runnable {
    public Socket clientSocket;

    public int playerID;

    public int status;

    public int serverPort;

    public int totalNumPlayer;

    private BufferedReader in;

    private PrintWriter out;

    private BasicChecker ruleChecker;

    public int watchingPattern = 0;

    public Player(int serverPort, int id, int totalNumPlayer) {
        this.serverPort = serverPort;
        this.playerID = id;
        this.status = 1;
        this.totalNumPlayer = totalNumPlayer;
        ruleChecker = new OriginChecker(null);
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public int getID() {
        return playerID;
    }

    public int getStatus() {
        return status;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setID(int playerID) {
        this.playerID = playerID;
    }

    private void playOneTurn() {
    }

    private void checkBehavior(ArrayList<Behavior> list) {
    }

    public void updateStatus() throws IOException {
        DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
        status = dataIn.readInt();
    }
    public void connectToServer() throws IOException {
        this.clientSocket = new Socket("localhost", serverPort);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
        setID(dataIn.readInt());
    }

    private boolean checkBehaviorInputFormatHelper(String name, ArrayList<Territory> map) {
        for (Territory t : map) {
            if (t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBehaviorInputFormat(String s, ArrayList<Territory> map) {
        String[] tokens = s.split(" ");
        if (tokens.length != 3) {
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
        return checkBehaviorInputFormatHelper(tokens[1], map) && checkBehaviorInputFormatHelper(tokens[2], map)
                && (!tokens[1].equals(tokens[2]));
    }

    private Territory getTerritoryByName(String s, ArrayList<Territory> map) {
        for (Territory t : map) {
            if (t.getName().equals(s)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while (true) {
                inputLine = in.readLine();
                System.out.println(inputLine);
                // handle game start situation
                if (inputLine.equals("Game Start")) {
                    DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
                    int totalUnit = dataIn.readInt();
                    String response = null;
                    int[] numbers = null;
                    Scanner scanner = new Scanner(System.in);
                    while (numbers == null) {
                        System.out.println("The total number of unit you can use in your territory is " + totalUnit);
                        System.out.print("Enter a string of space-separated numbers where the sum of them is " + totalUnit);
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
                    out.println(response);
                } else if (inputLine.equals("Turn Start")) {
                    updateStatus();
                    if(status == 0 && watchingPattern == 0){
                        System.out.println("you lose the game, do you want to watch the rest of the game? enter yes to watch");
                        InputStreamReader sr = new InputStreamReader(System.in);
                        BufferedReader bf = new BufferedReader(sr);
                        String response = bf.readLine();
                        if(response == "yes"){
                            watchingPattern = 1;
                        }else{
                            closeTheConnection();
                        }
                    }else {
                        // add an if-else to check current status, use it to determine what it needs to print
                        GlobalMap current = new GlobalMap();
                        current.receiveList(clientSocket);
                        ArrayList<Territory> currentMap = current.getMapArrayList();
                        for (int i = 1; i <= totalNumPlayer; i++) {
                            System.out.println("Player " + i + ":");
                            for (int j = 0; j < currentMap.size(); j++) {
                                if (currentMap.get(j).getOwnID() == i) {
                                    System.out.print(currentMap.get(j).getUnit() + " units in " + currentMap.get(j).getName() +
                                            "(next to:");
                                    ArrayList<String> neighborName = new ArrayList<>();
                                    for (Map.Entry<Integer, ArrayList<String>> e : currentMap.get(j).getNeighbor().entrySet()) {
                                        for (int x = 0; x < e.getValue().size(); x++) {
                                            neighborName.add(e.getValue().get(x));
                                        }
                                    }
                                    for (int x = 0; x < neighborName.size(); i++) {
                                        System.out.print(" " + neighborName.get(x));
                                        if (x != neighborName.size() - 1) {
                                            System.out.print(",");
                                        } else {
                                            System.out.println(")");
                                        }
                                    }
                                }
                            }
                        }
                        BehaviorList behaviorList = new BehaviorList(playerID);
                        if(watchingPattern == 1){   // if the player is in the watching pattern then don't add any order
                            behaviorList.sendList(clientSocket);
                        }else {
                            while (true) {
                                System.out.println("You are player " + playerID + ", what would you like to do?");
                                System.out.println("(M)ove");
                                System.out.println("(A)ttack");
                                System.out.println("(D)one");
                                InputStreamReader sr = new InputStreamReader(System.in);
                                BufferedReader bf = new BufferedReader(sr);
                                String response = bf.readLine();
                                while (response.length() != 1 || (response.toUpperCase().charAt(0) != 'M'
                                        && response.toUpperCase().charAt(0) != 'A' && response.toUpperCase().charAt(0) != 'D')) {
                                    System.out.println("Your input is not in correct format, try again");
                                    System.out.println("You are player " + playerID + ", what would you like to do?");
                                    System.out.println("(M)ove");
                                    System.out.println("(A)ttack");
                                    System.out.println("(D)one");
                                    response = bf.readLine();
                                }
                                if (response.toUpperCase().charAt(0) == 'M' || response.toUpperCase().charAt(0) == 'A') {
                                    Behavior behavior = null;
                                    while (behavior == null) {
                                        System.out.println("Please entry your behavior in this format:Unit SourceTerritory DestinationTerritory");
                                        String behaviorInfo = bf.readLine();
                                        while (!checkBehaviorInputFormat(behaviorInfo, currentMap)) {
                                            System.out.println("Your input is not in correct format, please " +
                                                    "check your unit, SourceTerritory, and DestinationTerritory and try again");
                                            System.out.println("Please entry your behavior in this format:Unit SourceTerritory DestinationTerritory");
                                            behaviorInfo = bf.readLine();
                                        }
                                        String[] tokens = behaviorInfo.split(" ");
                                        if (response.toUpperCase().charAt(0) == 'M') {
                                            behavior = new Behavior(getTerritoryByName(tokens[1], currentMap), getTerritoryByName(tokens[2], currentMap), Integer.parseInt(tokens[0]), playerID, "Move");
                                        } else {
                                            behavior = new Behavior(getTerritoryByName(tokens[1], currentMap), getTerritoryByName(tokens[2], currentMap), Integer.parseInt(tokens[0]), playerID, "Attack");
                                        }
                                        if (ruleChecker.checkBehavior(behavior, currentMap) != null) {
                                            System.out.println(ruleChecker.checkBehavior(behavior, currentMap));
                                            behavior = null;
                                        }
                                    }
                                    if (response.toUpperCase().charAt(0) == 'M') {
                                        behaviorList.addToMoveList(behavior);
                                    } else {
                                        behaviorList.addToAttackList(behavior);
                                    }
                                } else if (response.toUpperCase().charAt(0) == 'D') {
                                    behaviorList.sendList(clientSocket);
                                    break;
                                }
                            }
                        }
                    }
                } else if (inputLine.equals("Game Over")) {
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
    public static void main(String[] args) {

    }
}
