package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.app.Service;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by root on 25/1/18.
 */

public class service_client extends Service {

    Socket cls;
    BufferedReader client_inp = null;
    BufferedReader kybd = null;
    IBinder mBinder = new LocalBinder();
    Intent intent = null;
    String TAG = "LOG";
    DataOutputStream output = null;
    int mStartMode = START_NOT_STICKY;
    String inpt;
    NsdManager mNsdManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public service_client getClientServiceInstance() {
            return service_client.this;
        }
    }

    public void ConnectToServerSocket() {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {

                    cls = new Socket(interfc.server_ip, interfc.server_port);
                    client_inp = new BufferedReader(new InputStreamReader(cls.getInputStream()));
                    output = new DataOutputStream(cls.getOutputStream());
                    Log.d(TAG,"connection is accepted at :" + cls.getInetAddress().toString() + " - port :" + cls.getPort() + "\n");
                    //status.setText("connection is accepted at :" + "\n");
                    BroadCast("SOCKET_CONNECTED_TO_SERVER","");
                    //sendScore(String.valueOf(10));
                    waitForInput();

                } catch (UnknownHostException uh) {
                    Log.d(TAG,"i don't know the host");
                    System.exit(0);
                } catch (IOException e) {
                    Log.d(TAG,"Error Creating Client Socket!" + "\n" + e);
                    e.printStackTrace();
                }

            }
        });

        thread.start();

    }

    public void waitForInput() throws IOException{
        while ((inpt = client_inp.readLine())!=null)
        {
            Log.d(TAG,"from server: " +inpt);
            BroadCast("score_update",inpt);

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
    public void sendWord(final String word)
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

    }

    public void BroadCast(String name,String data){
        intent = new Intent();
        intent.setAction(interfc.package_name+"."+name);
        intent.putExtra("data",data);
        getApplicationContext().sendBroadcast(intent);
        Log.d(TAG,"Broadcasting" + interfc.package_name+name );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Client Service Started");
        return mStartMode;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
//        try {
//            cls.close();
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
            if(cls!=null) {
                cls.close();
                Log.d(TAG, "socket disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
