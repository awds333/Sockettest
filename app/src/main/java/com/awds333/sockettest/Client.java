package com.awds333.sockettest;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class Client extends Observable implements Runnable {
    private Socket socket;
    private String ip;
    private int port;
    private BufferedReader reader;
    private boolean finish;
    private String masage;
    private Context c;

    Client(String ip, int port, Context c) {
        this.ip = ip;
        this.port = port;
        this.c = c;
        finish = false;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket = new Socket(ip, port);
            while (!socket.isConnected()) {
                Thread.sleep(100);
            }
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!finish) {
                masage = reader.readLine();
                setChanged();
                notifyObservers();
                Thread.sleep(100);
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public void finish() {
        finish = true;
    }

    public String getMasage() {
        return masage;
    }
}