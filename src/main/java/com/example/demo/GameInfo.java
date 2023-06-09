package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class GameInfo {
    public int playerID;
    public int gameID;
    public int status;
    public int maximumTechNum;
    public int restCost;
    public int restFood;
    public int totalPlayerNum;
    public int watchingPattern;
    public PrintWriter out;
    public BufferedReader in;
    public Socket clientSocket;

    public GameInfo(){
        this.watchingPattern = 0;
        this.status = 1;
        this.maximumTechNum = 1;
        this.restCost = 50;
        this.restFood = 50;
    }
    public GameInfo(Socket clientSocket, int totalPlayerNum, int playerID, int gameID) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.totalPlayerNum = totalPlayerNum;
        this.playerID = playerID;
        this.gameID = gameID;
        this.watchingPattern = 0;
        this.status = 1;
        this.maximumTechNum = 1;
        this.restCost = 50;
        this.restFood = 50;
    }


    public int getPlayerID() {
        return playerID;
    }

    public int getGameID() {
        return gameID;
    }

    public int getStatus() {
        return status;
    }

    public int getMaximumTechNum() {
        return maximumTechNum;
    }

    public int getRestCost() {
        return restCost;
    }

    public int getRestFood() {
        return restFood;
    }

    public int getTotalPlayerNum() {
        return totalPlayerNum;
    }

    public int getWatchingPattern() {
        return watchingPattern;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMaximumTechNum(int maximumTechNum) {
        this.maximumTechNum = maximumTechNum;
    }

    public void setRestCost(int restCost) {
        this.restCost = restCost;
    }
    public void setRestFood(int restFood){
        this.restFood = restFood;
    }

    public void setWatchingPattern(int watchingPattern) {
        this.watchingPattern = watchingPattern;
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
