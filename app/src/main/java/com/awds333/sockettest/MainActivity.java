package com.awds333.sockettest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {

    Client client;
Context c;
    Handler h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new Client("46.101.96.234", 4444,this);
        client.addObserver(this);
        Thread thread =new Thread(client);
        c = this;
        thread.start();
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ((Button)findViewById(R.id.button)).setText(client.getMasage());
            }
        };
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.finish();
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        h.sendEmptyMessage(1);
    }
}
