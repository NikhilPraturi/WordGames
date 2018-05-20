package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by root on 25/1/18.
 */

public class service_server  extends Service{

    ServerSocket ss = null;
    Socket ssoc = null;
    DataOutputStream output = null;
    BufferedReader server_inp = null;
    IBinder mBinder = new LocalBinder();
    Intent intent = null;
    String TAG = "LOG";
    int mStartMode = START_NOT_STICKY;
    String inpt;
    boolean waiting = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public service_server getServerServiceInstance() {
            return service_server.this;
        }
    }

    public void CreateServer() {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                Log.d(TAG,"Trying to open service socket...");
                try {
                    ss = new ServerSocket(0);
                    Log.d(TAG,"Waiting For Opponent to Join... at "+ getLocalIpAddress() + "-- port :" + ss.getLocalPort());
                    interfc.server_port = ss.getLocalPort();
                    BroadCast("SOCKET_CREATED","");
                    //status.setText("Waiting For Opponent to Join... " );
                } catch (IOException e) {
                    //status.setText("Error Creating Server Socket!" + "\n");
                    System.exit(1);
                }
                while (waiting) {
                    try {
                        ssoc = ss.accept();
                        //status.setText("connection is accepted at :" + "\n");
                        Log.d(TAG,"connection is accepted at :" + ssoc.getInetAddress()+ "-- port :" + ssoc.getLocalPort() + "\n");
                        output = new DataOutputStream(ssoc.getOutputStream());
                        server_inp = new BufferedReader(new InputStreamReader(ssoc.getInputStream()));


                        //sendScore(String.valueOf(20));
                        BroadCast("SOCKET_CONNECTED_TO_CLIENT","");
                        waitForInput();

                        waiting = false;

                    } catch (IOException ioe) {
                        //status.setText("Server failed to connect" + "\n");
                        System.exit(1);
                    }
                }
            }
        });

        thread.start();

    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void waitForInput() throws IOException{
        while ((inpt = server_inp.readLine())!=null)
        {
            Log.d(TAG,"from server: " +inpt);
            BroadCast("score_update",inpt);
//BroadCast("word_update",inpt);
        }
    }

    public void sendScore(final String si){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    output.writeBytes(si);
                    output.write(13);
                    output.write(10);
                    output.flush();
                    Log.d(TAG,"server: score sent -" + si );
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();

    }
/*
    public void sendword(final String word)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    output.writeBytes(word);
                    output.write(13);
                    output.write(10);
                    output.flush();
                    Log.d(TAG,"server: word sent -" + word );
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
*/



    public void BroadCast(String name,String data){
        intent = new Intent();
        intent.setAction(interfc.package_name+"."+name);
        intent.putExtra("data",data);
        getApplicationContext().sendBroadcast(intent);
        Log.d(TAG,"Broadcasting" + interfc.package_name+"."+name );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Server Service Started");
        return mStartMode;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
//        try {
//
//            ss.close();
//            ssoc.close();
//            Log.d(TAG,"socket disconnected");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        this.stopSelf();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Toast.makeText(this, "service onDestroy", Toast.LENGTH_LONG).show();
        try {
            if(ss!=null&&ssoc!=null) {
                ss.close();
                ssoc.close();
                Log.d(TAG, "socket disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
