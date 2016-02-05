package com.awds333.sockettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class Client extends Observable implements Runnable {
    private Socket socket;
    private String ip;
    private String exception;
    private int port;
    private BufferedReader reader;
    private boolean finish;
    private String message;

    Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        finish = false;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            while (!socket.isConnected()) {
                Thread.sleep(100);
            }
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!finish) {
                message = reader.readLine();
                setChanged();
                notifyObservers();
                Thread.sleep(100);
            }
        } catch (IOException e) {
            message = "";
            exception ="нет связи с сервером!";
            setChanged();
            notifyObservers();
        } catch (InterruptedException e) {
            message = "";
            exception ="InterruptedException!!!";
            setChanged();
            notifyObservers();
        } finally {
            try {
                if(socket!=null)
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public void finish() {
        finish = true;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }
}