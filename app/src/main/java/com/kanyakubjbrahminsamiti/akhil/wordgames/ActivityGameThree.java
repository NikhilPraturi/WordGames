package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public class ActivityGameThree extends AppCompatActivity {
    ChatMessageModel chatMessage;
    private ArrayList<ChatMessageModel> chatHistory = new ArrayList<>();
    private ChatAdapter adapterone;

    boolean isMine = true;

    @BindView(R.id.host)
    Button host_Btn;
    @BindView(R.id.join)
    Button join_Btn;

    @BindView(R.id.parentgameacg)
LinearLayout parentgameacg;

    @BindView(R.id.messagesContaineracg)
    ListView listView;
    @BindView(R.id.select_layoutacg)
    LinearLayout select_layout;
    TextView status;

//    @BindView(R.id.connecting)LinearLayout connection_LL;

  //  @BindView(R.id.recyclerView)RecyclerView recyclerView;
   // @BindView(R.id.game_layout)LinearLayout game_layout;
    @BindView(R.id.chatSendButtonacg)Button ok;
    @BindView(R.id.cantacg)Button skip;
    @BindView(R.id.textView11myacg)TextView own_score;
    @BindView(R.id.textView10opponentacg)TextView opponent_score;
    @BindView(R.id.messageEditacg)
    EditText ET;
    String TAG = "LOG";
    String WORD_UPDATE=interfc.package_name+".word_update";
    String SCORE_UPDATE = interfc.package_name+".score_update";
    String SOCKET_CREATED = interfc.package_name+".SOCKET_CREATED";
    String SOCKET_CONNECTED_TO_CLIENT = interfc.package_name+".SOCKET_CONNECTED_TO_CLIENT";
    String SOCKET_CONNECTED_TO_SERVER = interfc.package_name+".SOCKET_CONNECTED_TO_SERVER";
    IntentFilter filter;
    service_server server;
    service_client client;
    boolean boundedToServer = false;
    boolean boundedToClient = false;
    Intent intent;
    NsdManager.RegistrationListener mRegistrationListener;
    String mServiceName;
    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdServiceInfo mService;
    NsdManager.DiscoveryListener mDiscoveryListener;
    LinearLayoutManager linearLayoutManager;
    boolean socc,serve;
    Thread thread;
    boolean isTimeOver=false;
    long start;
    int min_game_duration=1;
    boolean is_server = true;
    String gameType="";

    Intent i;

    String hostmine;
    String joinmine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_three);
        ButterKnife.bind(this);
       status=(TextView)findViewById(R.id.textView7);
        gameType = getIntent().getStringExtra("gameType");


        if(gameType.equals("multi")) {

            host_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_layout.setVisibility(View.INVISIBLE);
                 status.setVisibility(View.VISIBLE);
                    status.setText("Waiting for opponent...");

                    CreateServer();
                    interfc.my_score++;

    hostmine="hostmine";
                }
            });


            join_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_layout.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.VISIBLE);
                    status.setText("Searching For Host...");
                    initializeResolveListener();
                    initializeDiscoveryListener();
                    startNsdDescovery();
                    is_server = false;
                    interfc.my_score++;
                    //CreateClient();
                    //startActivity(new Intent(Activity_Game.this,Activity_Game.class));

                joinmine="joinmine";
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            filter = new IntentFilter();
            filter.addAction(SCORE_UPDATE);
            filter.addAction(SOCKET_CREATED);
            filter.addAction(SOCKET_CONNECTED_TO_SERVER);
            filter.addAction(SOCKET_CONNECTED_TO_CLIENT);
       //     filter.addAction(WORD_UPDATE);
            registerReceiver(receiver, filter);

        }
    }

    public void CreateServer(){

        intent = new Intent(getApplicationContext(), service_server.class);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (!isMyServiceRunning(service_server.class)) {
                        Log.d(TAG,"Opening Background Socket");
                        startService(intent);
                    } else {
                        Log.d(TAG, "Service Already Running ");
                    }
                    bindService(intent, Server_serviceConnection,0);

                }

            }).run();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }

    }

    public void CreateClient(){

        intent = new Intent(getApplicationContext(), service_client.class);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (!isMyServiceRunning(service_client.class)) {
                        Log.d(TAG,"Opening Background Socket");
                        startService(intent);
                    } else {
                        Log.d(TAG, "Service Already Running ");
                    }
                    bindService(intent, Client_serviceConnection,0);

                }

            }).run();
        }catch (Exception e){Log.d(TAG,e.toString());}

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String action = intent.getAction();
                Log.d(TAG, "BroadCast Received:" + action);
                if (action.equals(SCORE_UPDATE)) {


                }




                if (action.equals(SOCKET_CREATED)) {
                    initializeRegistrationListener();
                    registerService();
                }

                if (action.equals(SOCKET_CONNECTED_TO_CLIENT)) {
                    select_layout.setVisibility(View.INVISIBLE);
                    //parentgameacg.setVisibility(View.VISIBLE);

                   // status.setVisibility(View.INVISIBLE);
                    i=new Intent(ActivityGameThree.this,ChatMplyTwoPlayers.class);


startActivity(i);
                    //setupRecyclerView(recyclerView);
/*
                thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            startGame();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });

                thread.start();
*/


                }

                if (action.equals(SOCKET_CONNECTED_TO_SERVER)) {
                    //  connection_LL.setVisibility(View.GONE);
                    // game_layout.setVisibility(View.VISIBLE);

                   // status.setVisibility(View.INVISIBLE);
serve=true;
                    select_layout.setVisibility(View.INVISIBLE);
                 //   parentgameacg.setVisibility(View.VISIBLE);
                    i=new Intent(ActivityGameThree.this,ChatMplyTwoPlayers.class);


                    startActivity(i);

                    //setupRecyclerView(recyclerView);
                /*thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            startGame();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });

                thread.start();*/


                }
            }
            catch (Exception e)
            {
                Log.e("e",e.getMessage());
            }
        }};


    ServiceConnection Server_serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundedToServer = true;
            Log.d(TAG,"Bounded to service_server :" + boundedToServer);
            service_server.LocalBinder binder = (service_server.LocalBinder) service;
            server = binder.getServerServiceInstance();
            server.CreateServer();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundedToServer = false;
            Log.d(TAG,"Bounded to service :" + boundedToServer);
        }
    };

    ServiceConnection Client_serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundedToClient = true;
            Log.d(TAG,"Bounded to service_server :" + boundedToClient);
            service_client.LocalBinder binder = (service_client.LocalBinder) service;
            client = binder.getClientServiceInstance();
            client.ConnectToServerSocket();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundedToClient = false;
            Log.d(TAG,"Bounded to service_client :" + boundedToClient);
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void registerService() {
        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName(interfc.server_user_name+"@WordGames");
        serviceInfo.setServiceType(interfc.SERVICE_TYPE);
        serviceInfo.setPort(interfc.server_port);

        mNsdManager = (NsdManager) this.getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

        Log.d(TAG,"Trying to register service :"+serviceInfo);

    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = NsdServiceInfo.getServiceName();
                Log.d(TAG,"Servcie :" +mServiceName+" is Registered. Type:"+NsdServiceInfo.getServiceType());
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed! Put debugging code here to determine why.
                Log.d(TAG,"Registration Failed "+ errorCode);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
                Log.d(TAG,"Unregistered :"+arg0);
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed. Put debugging code here to determine why.
                Log.d(TAG,"Failed to UnRegister :"+errorCode);
            }
        };
    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                Log.d(TAG, "Service discovery success " + service);

//                if (!service.getServiceType().equals("")) {
//                    // Service type is the string containing the protocol and
//                    // transport layer for this service.
//                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
//                }
//                else if (service.getServiceName().equals(mServiceName)) {
//                    // The name of the service tells the user what they'd be
//                    // connecting to. It could be "Bob's Chat App".
//                    Log.d(TAG, "Same machine: " + mServiceName);
//                }
                if (service.getServiceName().contains("@WordGames")){
                    mNsdManager.resolveService(service, mResolveListener);
                }

            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

//                if (serviceInfo.getServiceName().equals(mServiceName)) {
//                    Log.d(TAG, "Same IP.");
//                    return;
//                }
                mService = serviceInfo;
                int port = mService.getPort();
                InetAddress host = mService.getHost();

                interfc.server_port = port;
                interfc.server_ip = host.getHostAddress();

                CreateClient();
            }
        };
    }

    public void startNsdDescovery(){
        mNsdManager = (NsdManager) this.getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(
                interfc.SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

    }

    public void resolveService(NsdServiceInfo service){
        mNsdManager.resolveService(service, mResolveListener);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            mNsdManager.unregisterService(mRegistrationListener);
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    public void displayMessage(ChatMessageModel message) {

        adapterone.add(message);
        ET.setTextColor(Color.BLACK);

        Log.e("error", adapterone.toString());
        adapterone.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        listView.setSelection(listView.getCount() - 1);
    }

    }


