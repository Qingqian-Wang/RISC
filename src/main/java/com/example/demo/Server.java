package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    public ArrayList<Socket> playerSocketList;
    public ArrayList<Integer> gamePortList;
    public final ArrayList<Integer> gameMaxPlayerNum;
    public ArrayList<Integer> gameCurrentPlayerNum;
    public int port;
    public ServerSocket serverSocket;

    /*
     * Constructor to create a Server object
     *
     * @param port the port number on which the server will listen for incoming
     * connections
     *
     * @throws IOException if an I/O error occurs when opening the server socket
     */
    public Server(int port, ArrayList<Integer> gamePortList, ArrayList<Integer> gameMaxPlayerNum) throws IOException {
        playerSocketList = new ArrayList<>();
        this.gamePortList = gamePortList;
        this.gameMaxPlayerNum = gameMaxPlayerNum;
        this.gameCurrentPlayerNum = new ArrayList<>();
        for (int i = 0; i < gameMaxPlayerNum.size(); i++) {
            gameCurrentPlayerNum.add(0);
        }
        this.port = port;
        serverSocket = new ServerSocket(this.port);
    }

    /*
     * Accepts players to the server
     *
     * @param playerNum the number of players that will be accepted
     *
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    public void acceptPlayer() throws IOException {
        int playerRestNum = 0;
        for (Integer i : gameMaxPlayerNum) {
            playerRestNum += i;
        }
        while (playerRestNum != 0) {
            Socket playerSocket = serverSocket.accept();
            System.out.println("Accept new connection from " + playerSocket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
            out.println("There currently are " + gamePortList.size() + " games run on this server.");
            ObjectMapper objectMapper = new ObjectMapper();
            out.println(objectMapper.writeValueAsString(gamePortList));
            for (int j = 0; j < gameCurrentPlayerNum.size(); j++) {
                if (gameCurrentPlayerNum.get(j) < gameMaxPlayerNum.get(j)) {
                    out.println("A:Game " + (j + 1) + " is available");
                } else {
                    out.println("U:Game " + (j + 1) + " is unavailable");
                }
            }
            String response = in.readLine();
            if (response.length() != 0) {
                String[] tokens = response.split(" ");
                for (String token : tokens) {
                    gameCurrentPlayerNum.set(Integer.parseInt(token) - 1, gameCurrentPlayerNum.get(Integer.parseInt(token) - 1) + 1);
                }
            }
            playerRestNum = 0;
            for (int i = 0; i < gameMaxPlayerNum.size(); i++) {
                playerRestNum += (gameMaxPlayerNum.get(i) - gameCurrentPlayerNum.get(i));
            }
            playerSocketList.add(playerSocket);
        }
    }

    public void initGames() throws IOException {
        Game game1 = new Game(gamePortList.get(0), gameMaxPlayerNum.get(0));
        Game game2 = new Game(gamePortList.get(1), gameMaxPlayerNum.get(1));
        new Thread(game1).start();
        new Thread(game2).start();
    }

    public static void main(String[] args) throws Exception {
        int serverPort = 9999;
        int gamePort1 = 9000;
        int gamePort2 = 8000;
        int totalPlayerNum1 = 2;
        int totalPlayerNum2 = 2;
        if (totalPlayerNum1 < 2 || totalPlayerNum1 > 5 || totalPlayerNum2 < 2 || totalPlayerNum2 > 5) {
            System.out.println("The total number of players should be between 2 and 5");
        } else {
            ArrayList<Integer> gamePortList = new ArrayList<>();
            gamePortList.add(gamePort1);
            gamePortList.add(gamePort2);
            ArrayList<Integer> totalPlayerNumList = new ArrayList<>();
            totalPlayerNumList.add(totalPlayerNum1);
            totalPlayerNumList.add(totalPlayerNum2);
            Server s = new Server(serverPort, gamePortList, totalPlayerNumList);
            s.initGames();
            s.acceptPlayer();
        }
    }
}
