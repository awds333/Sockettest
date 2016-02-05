package com.awds333.sockettest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {

    Client client;
    LinearLayout l;
    LinearLayout.LayoutParams p;
    Context c;
    Handler h;
    ArrayList<TextView> t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new Client("46.101.96.234", 4444);
        client.addObserver(this);
        t = new ArrayList<TextView>();
        Thread thread = new Thread(client);
        c = this;
        l = (LinearLayout) findViewById(R.id.lin);
        p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thread.start();
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!client.getMessage().equals("")) {
                    t.add(new TextView(c));
                    t.get(t.size()-1).setText(client.getMessage());
                    l.addView(t.get(t.size()-1),0,p);
                } else {
                    Toast.makeText(c, client.getException(), Toast.LENGTH_LONG).show();
                }
            }
        };
        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
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
