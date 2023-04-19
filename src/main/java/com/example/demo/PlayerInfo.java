package com.example.demo;

import java.io.*;
import java.net.Socket;


/*
 * This class represents a player in a networked game
 * It contains information about the player's socket, input and output streams, and ID
 */

public class PlayerInfo {
    public final Socket playerSocket;

    public BufferedReader in;

    public PrintWriter out;
    public final int playerID;



    public PlayerInfo(Socket playerSocket, int playerID) throws IOException {
        this.playerSocket = playerSocket;
        this.playerID = playerID;
        out = new PrintWriter(playerSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        playerSocket.close();
    }

}
