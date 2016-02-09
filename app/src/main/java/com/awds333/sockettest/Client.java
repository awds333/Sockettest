package com.awds333.sockettest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
    private String message_out;
    private PrintWriter out;

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
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
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
                out = null;
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
    public void sendMessage(final String message_out1){
        if(out!=null&&message_out1.length()>0){
            this.message_out =message_out1;
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    out.println(message_out);
                }
            });
            tr.start();
        }
    }

}