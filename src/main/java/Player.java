import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Runnable{
    public Socket clientSocket;

    public int playerID;

    public int status;

    public int serverPort;

    public int totalNumPlayer;

    private BufferedReader in;

    private PrintWriter out;

    private BasicChecker ruleChecker;

    public Player(int serverPort, int id, int totalNumPlayer) {
        this.serverPort = serverPort;
        this.playerID = id;
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

    private void playOneTurn(){}

    private void checkBehavior(ArrayList<Behavior> list){}

    public void connectToServer() throws IOException {
        this.clientSocket = new Socket("localhost",serverPort);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                // handle game start situation
                if(inputLine.equals("Game Start")){
                    DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
                    int totalUnit = dataIn.readInt();
                    String response = null;
                    int[] numbers = null;
                    Scanner scanner = new Scanner(System.in);
                    while (numbers == null) {
                        System.out.println("The total number of unit you can use in your territory is "+totalUnit);
                        System.out.print("Enter a string of space-separated numbers where the sum of them is "+totalUnit);
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
                        if(numbers!=null){
                            int sum = 0;
                            for(int i = 0; i < tokens.length; i++){
                                sum += Integer.parseInt(tokens[i]);
                            }
                            if(sum != totalUnit){
                                System.out.println("Total number of unit is not equal to "+totalUnit+".");
                                numbers = null;
                            }
                        }
                    }
                    out.println(response);
                }
            }
            System.out.println("Server connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
