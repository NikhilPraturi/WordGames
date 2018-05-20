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
import android.os.CountDownTimer;
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
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public class ActivityGameTwo extends AppCompatActivity {

   @BindView(R.id.count)
   TextView seccount;
    @BindView(R.id.mincount)

    TextView mincount;


    @BindView(R.id.host)
    Button host_Btn;
    @BindView(R.id.join)
    Button join_Btn;
    @BindView(R.id.select_layout)
    LinearLayout select_layout;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.score_board)
    LinearLayout scbd;
    @BindView(R.id.connecting)LinearLayout connection_LL;
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.game_layout)LinearLayout game_layout;
    @BindView(R.id.ok)Button ok;
    @BindView(R.id.skip)Button skip;
    @BindView(R.id.OwnScore_ET)TextView own_score;
    @BindView(R.id.OpponentScore_ET)TextView opponent_score;
    @BindView(R.id.ET)
    EditText ET;
    String TAG = "LOG";
    String SCORE_UPDATE = interfc.package_name+".score_update";
    String SOCKET_CREATED = interfc.package_name+".SOCKET_CREATED";
    String SOCKET_CONNECTED_TO_CLIENT = interfc.package_name+".SOCKET_CONNECTED_TO_CLIENT";
    String SOCKET_CONNECTED_TO_SERVER = interfc.package_name+".SOCKET_CONNECTED_TO_SERVER";
    IntentFilter filter;
    service_server server;
    boolean isTimeFinished;
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
    AdapterRecyclerTwo adapter;
    Thread thread;
    GameTimeThree gameTimeThree;
    boolean isTimeOver=false;
    long start;
    int min_game_duration=1;
    boolean is_server = true;
    String gameType="";
    CountDownTimer countDownTimer;
    String checkword;
    int sysscore,userscore;
    long milli=10000;
    long milsec=1000;
    boolean issecfinished;
    int count=10;
    int checkcount=10;
boolean iswordgenerated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);
        ButterKnife.bind(this);


        gameType = getIntent().getStringExtra("gameType");


        if(gameType.equals("multi")) {

            host_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_layout.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.VISIBLE);
                    status.setText("Waiting for opponent...");
                    CreateServer();
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
                    //CreateClient();
                    //startActivity(new Intent(Activity_Game.this,Activity_Game.class));
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (interfc.words_set.contains(ET.getText().toString())) {
                        String word = ET.getText().toString();
                        interfc.my_score += (ET.getText().length());
                        if (is_server) {
                            server.sendScore(String.valueOf(interfc.my_score));
                        } else {
                            client.sendScore(String.valueOf(interfc.my_score));
                        }
                        own_score.setText(String.valueOf(interfc.my_score));
                    }

                    ET.setText("");
                }
            });

/*
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isTimeFinished) {
                        Thread tt = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    startGame();

                                } catch (Exception e) {
                                    Log.e("startgame2", e.getMessage());
                                }

                            }
                        });
                        tt.start();

                        countDownTimer.start();
                    }
                }
            });

*/
            filter = new IntentFilter();
            filter.addAction(SCORE_UPDATE);
            filter.addAction(SOCKET_CREATED);
            filter.addAction(SOCKET_CONNECTED_TO_SERVER);
            filter.addAction(SOCKET_CONNECTED_TO_CLIENT);
            registerReceiver(receiver, filter);

        }else {


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (interfc.words_set.contains(ET.getText().toString())) {
                        String word = ET.getText().toString();
                        interfc.my_score += (ET.getText().length());
                        own_score.setText(String.valueOf(interfc.my_score));
                    }

                    ET.setText("");
                }
            });


            connection_LL.setVisibility(View.GONE);
            game_layout.setVisibility(View.VISIBLE);

            setupRecyclerView(recyclerView);
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
    boolean worddisplayed;

    public void startGame() throws InterruptedException {

        start = System.currentTimeMillis();


        while (!isTimeFinished) {


            Random randomizer = new Random();


if(count==0||count==10||count==9) {
    adapter.clear();

        String word = shuffle(interfc.words.get(randomizer.nextInt(interfc.words.size())));
        for (int i = 0; i < word.length(); i++) {

            adapter.add_data(String.valueOf(word.charAt(i)));
            thread.sleep(1000);

    }
    if (count == 0) {

        count = 10;
    }
    if(count<=0)
    {
        count=10;
    }
}
}




            if(isTimeFinished){
                isTimeOver = true;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countScores();
                    }
                });


            }
        }



    public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    public void countScores() {

        connection_LL.setVisibility(View.VISIBLE);
        select_layout.setVisibility(View.GONE);
        status.setVisibility(View.VISIBLE);
        game_layout.setVisibility(View.GONE);
        String result = "Time Over.\n";

        if (gameType.equals("multi")) {
            if (isTimeFinished == true) {

                if (interfc.my_score > interfc.opponent_score) {
                    result += " you won. \n You: " + interfc.my_score + "  Opponent: " + interfc.opponent_score;
                } else {
                    if (interfc.my_score < interfc.opponent_score) {
                        result += " you lost. \n You: " + interfc.my_score + "  Opponent: " + interfc.opponent_score;
                    } else {
                        result += " Its a tie. \n You: " + interfc.my_score + "  Opponent: " + interfc.opponent_score;
                    }

                }
            }
        }
        else {
            result += "Your Score : " + interfc.my_score;

        }
        status.setTextColor(Color.BLACK);
            status.setText(result);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG,"BroadCast Received:" + action);
            if (action.equals(SCORE_UPDATE)) {
                Log.d(TAG,"new score :"+ intent.getStringExtra("data") );
                Integer score = Integer.parseInt(intent.getStringExtra("data"));
                interfc.opponent_score = score;
                opponent_score.setText(String.valueOf(interfc.opponent_score));

            }
            if(action.equals(SOCKET_CREATED)){
                initializeRegistrationListener();
                registerService();
            }

            if(action.equals(SOCKET_CONNECTED_TO_CLIENT)){
                skip.setVisibility(View.VISIBLE);
                mincount.setVisibility(View.VISIBLE);
                seccount.setVisibility(View.VISIBLE);
                connection_LL.setVisibility(View.GONE);
                game_layout.setVisibility(View.VISIBLE);
                scbd.setVisibility(View.VISIBLE);
                skip.setVisibility(View.INVISIBLE);
mytimer();

countsec();


                own_score.setVisibility(View.VISIBLE);
                opponent_score.setVisibility(View.VISIBLE);
                setupRecyclerView(recyclerView);
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
            }

            if(action.equals(SOCKET_CONNECTED_TO_SERVER)){
                skip.setVisibility(View.VISIBLE);
                mincount.setVisibility(View.VISIBLE);
                seccount.setVisibility(View.VISIBLE);
                connection_LL.setVisibility(View.GONE);
                game_layout.setVisibility(View.VISIBLE);
                own_score.setVisibility(View.VISIBLE);
                opponent_score.setVisibility(View.VISIBLE);

                skip.setVisibility(View.INVISIBLE) ;
                mytimer();

                countsec();

                setupRecyclerView(recyclerView);
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

    public void setupRecyclerView(RecyclerView recyclerView) {

        adapter = new AdapterRecyclerTwo(ActivityGameTwo.this,new ArrayList<String>());
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        Log.d("LOG", "recyclerview adapter set");
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
public void mytimer()
{

    mincount.setVisibility(View.VISIBLE);
    gameTimeThree = new GameTimeThree(5*60000, 1000);
    gameTimeThree.setSourceActivity(this);
    gameTimeThree.start();




}
    public void onCountDownTimerTickEvent(long millisUntilFinished) {
        Long minutes,seconds;
        minutes= TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

        seconds=TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
        this.mincount.setText(minutes+":"+seconds);
    }
    public void onCountDownTimerFinishEvent()
    {
/*

        this.finish();
        Intent intent=new Intent(SoloGameModuleTwo.this,ResultsPage.class);
        //intent.putExtra("sys",String.valueOf(system_score));
        intent.putExtra("user",String.valueOf(user_score));
        startActivity(intent);
*/


        isTimeFinished=true;
        mincount.setText("00");
        mincount.setVisibility(View.INVISIBLE);
        seccount.setVisibility(View.INVISIBLE);


        //  Toast.makeText(ChatMplyTwoPlayers.this,"Time Up",Toast.LENGTH_LONG).show();
  }
  void countsec()
  {

      countDownTimer=   new CountDownTimer(milli, milsec) {

          public void onTick(long millisUntilFinished) {
seccount.setVisibility(View.VISIBLE);
              seccount.setText("Time: " + millisUntilFinished / 1000 + " sec");

  count--;
              checkcount--;
  if(count<=0)
  {
      count=10;
  }
          }

          public void onFinish() {
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {

issecfinished=true;


                }
            });t.start();
              start();
          }
      }.start();
  }

}
