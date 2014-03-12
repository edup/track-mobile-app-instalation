package com.example.trackingapp;

import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.nikkii.embedhttp.HttpServer;
import org.nikkii.embedhttp.handler.HttpRequestHandler;
import org.nikkii.embedhttp.impl.HttpRequest;
import org.nikkii.embedhttp.impl.HttpResponse;
import org.nikkii.embedhttp.impl.HttpStatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

public class MainActivity extends ActionBarActivity {
    TextView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is for testing purposes
        tView = (TextView) findViewById(R.id.textView);

        //This is for iOS
        //
        //This socket may be temporary open
        //I will let it open forever for testing purposes
        //You create here a simple webserver
        //that will answer to http get calls
        //
        //Responses:
        // any call except "/pixel.png": the server answers with a variable helper => var app_enabled=true;
        //get "/pixel.png?trackingId" it sends a 1x1 pixel, but you need to send the get with your tracking id
        try {
            int port = 50000; // 843 flash policy port
            HttpServer server = new HttpServer();
            server.addRequestHandler(new HttpRequestHandler() {
                @Override
                public HttpResponse handleRequest(HttpRequest request) {
                    String pixelUri="/pixel.png";
                    //Log.d("TEST", "Request: " + request.getUri() + ", queryString: " + request.getQueryString() + ", ");

                    //Because you're in a different thread you need to
                    //send a message via handdler
                    //Log.d("TEST", "Thread id no request: " + Thread.currentThread().getId());
                    if(request.getQueryString()!=null && request.getQueryString().length()>0 && request.getUri().compareTo(pixelUri)==0) {
                        Bundle b = new Bundle();
                        b.putString("tracking-id", request.getQueryString()); // for example
                        Message m = new Message();
                        m.setData(b);
                        textViewManager.sendMessage(m);
                    }

                    //Send a 1x1 pixel
                    if(request.getUri().compareTo(pixelUri)==0) {
                        return new HttpResponse(HttpStatus.OK, "<img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3gMLEzkoURASRwAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAMSURBVAjXY4jZHgoAAtsBaQQBI8gAAAAASUVORK5CYII=' />");
                    }else{
                        //Send an helper (variable) for processing this in the client side
                        return new HttpResponse(HttpStatus.OK, "var app_enabled=true;");
                    }
                }
            });
            server.bind(port);
            server.start();

        } catch (Exception e) {
            tView.setText("Error creating server: " + e.getMessage());
            e.printStackTrace();
        }

        //This is for android
        //
        try{
            int port = 50001; // 843 flash policy port
            TrackingServerAndroid s = new TrackingServerAndroid(port);
            s.start();
            s.addListener( new TrackingServerAndroid.OnMessageListener() {
                @Override
                public void onMessage(String msg) {
                    Bundle b = new Bundle();
                    b.putString("tracking-id", msg); // for example
                    Message m = new Message();
                    m.setData(b);
                    textViewManager.sendMessage(m);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //textView handler for update the tracking id in the screen
    Handler textViewManager=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String trackingId = msg.getData().getString("tracking-id");
            //Log.d("TEST", "Changing text to msg " + trackingId);
            tView.setText("TrackingId: "+ trackingId);
        }
    };

}
