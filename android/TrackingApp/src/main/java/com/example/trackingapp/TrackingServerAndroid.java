/**
 * Created by edup on 3/10/14.
 */
package com.example.trackingapp;


import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class TrackingServerAndroid extends WebSocketServer {


    public TrackingServerAndroid(int port) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public TrackingServerAndroid(InetSocketAddress address) {
        super( address );
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        //this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );
        Log.d("TEST", conn.getRemoteSocketAddress().getAddress().getHostAddress() + " joined!");
        askTrackingId(conn);
    }

    public void askTrackingId( WebSocket conn) {
        Log.d("TEST", "Sending question....");
        conn.send( "{\"question\":\"tracking-id\"}" );
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {

    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        Log.d("TEST", conn.getRemoteSocketAddress().getAddress().getHostAddress() + " msg: " + message );
        for (int i=0; i!= onMessageListeners.size(); i++) {
            OnMessageListener listener = onMessageListeners.get(i);
            listener.onMessage(message);
        }
    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public interface OnMessageListener {
        public void onMessage(String msg);
    }

    //LISTENER STUFF
    private LinkedList<OnMessageListener> onMessageListeners = new LinkedList<OnMessageListener>();

    public void addListener(OnMessageListener listener){
        onMessageListeners.add(listener);
    }

    public boolean removeListener(OnMessageListener listener){
        return onMessageListeners.remove(listener);
    }

}


//MAIN