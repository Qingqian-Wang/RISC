package com.example.demo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/*
 *Interface for objects that can be sent and received over a network connection.
 * */
public interface NetworkObject {
    public void sendList(ObjectOutputStream out) throws Exception;

    public void receiveList(ObjectInputStream in) throws Exception;
}
