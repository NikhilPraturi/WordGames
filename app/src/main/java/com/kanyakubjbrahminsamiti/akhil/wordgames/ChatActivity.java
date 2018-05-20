package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.widget.Button;
import android.widget.Toast;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class ChatActivity extends AppCompatActivity {
    private EditText messageET;
    private ListView messagesContainer;
    GameTime gameTime;
    private Button sendBtn,nah_cant_btn;
    private ChatAdapter adapter;
    String getLastLetter;
    List<String> user_entered_list = new ArrayList();
    List sorted_list = new ArrayList();
    private ArrayList<ChatMessageModel> chatHistory = new ArrayList<>();
    List<String> globaldata = new ArrayList<String>();
    boolean isMine = true;
    public String lastLetter;
    String generateRandomWord;
    String user_entered_text;
    boolean wordfound = false;
    int system_score = 0, user_score = 0;
    ChatMessageModel chatMessage;
    TextView minutes,timersec;
    CountDownTimer countDownTimer;
    boolean continuoustime=true;
    boolean isfinished=false;
    boolean timeup=false;
    boolean isTimeFinished;

TextView opponent,my,textView6won;
boolean nah_clicked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);
        minutes = (TextView) findViewById(R.id.minutes);
        timersec = (TextView) findViewById(R.id.timersec);
        textView6won=(TextView) findViewById(R.id.textView6won);
         opponent=(TextView)findViewById(R.id.textView10opponent);

         my=(TextView)findViewById(R.id.textView11my);
nah_cant_btn=(Button)findViewById(R.id.cant);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
long millisec=0;
        messageET.setTextColor(Color.BLACK);

        globaldata = readData();

        generateRandomWord = generateRandomWord(globaldata);
        lastLetter = String.valueOf(generateRandomWord.charAt(generateRandomWord.length() - 1));

        Log.d("lastLetter", lastLetter);
        Log.d("loadDummyHistory", "loadDummyHistory execution");
        loadDummyHistory();
        gameTime = new GameTime(5*60000, 1000);
        gameTime.setSourceActivity(this);
        gameTime.start();
        //startTimer();
        Log.d("initControls execution", "initControls execution");
        initControls();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initControls() {
        messageET.setTextColor(Color.BLACK);
        if(continuoustime) {
         countDownTimer=   new CountDownTimer(11000, 1000) {

                public void onTick(long millisUntilFinished) {

                    timersec.setText("Time: " + millisUntilFinished / 1000 + " sec");

                }

                public void onFinish() {


                    if (!isTimeFinished) {
                        timeup = true;

                        loadDummyHistory();


                 /*   if (isfinished == false) {
                        finish();
                        Intent i=new Intent(ChatActivity.this,ResultsPage.class);
                        i.putExtra("gameover","GameOver");
                        i.putExtra("sys",String.valueOf(system_score));
                        i.putExtra("user",String.valueOf(user_score));
                        startActivity(i);
                    } else {
                 */
                        if (messageET.getCurrentTextColor() == Color.RED) {
                            messageET.setTextColor(Color.BLACK);
                        }
                        start();
                    }
                }

            }.start();
        }

        nah_cant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTimeFinished) {
                    nah_clicked = true;
                    countDownTimer.start();
                    loadDummyHistory();
                }
            }
        });
messageET.setOnFocusChangeListener(new View.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b==true)
        {
            messageET=(EditText) view;
            messageET.setTextColor(Color.BLACK);
        }
    }
});
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isTimeFinished){
                user_entered_text = messageET.getText().toString();
                messageET.setTextColor(Color.BLACK);

                if (TextUtils.isEmpty(user_entered_text)) {
                    return;
                }
                if (user_entered_text != null) {

                    if (user_entered_list.contains(user_entered_text)) {
                        Toast.makeText(ChatActivity.this, "duplicate is not allowed", Toast.LENGTH_LONG).show();

                    } else {

                        if (globaldata.contains(user_entered_text)) {
                            wordfound = true;

                        }

                        String ss = String.valueOf(messageET.getHint());
                        String arr[] = ss.split(":");
                        String data = arr[1].trim();
                        String st = String.valueOf(user_entered_text.trim().charAt(0));

                        if (!data.toString().equals(st)) {
                            Toast.makeText(ChatActivity.this,  ss, Toast.LENGTH_LONG).show();

                        } else {

                            if (wordfound) {
                                isfinished = true;

                                user_entered_list.add(user_entered_text);
                                user_score += user_entered_text.length();
                                my.setText("user: " + String.valueOf(user_score));
                                isMine = false;
                                chatMessage = new ChatMessageModel(2, isMine, user_entered_text, "");
                                getLastLetter = String.valueOf(user_entered_text.charAt(user_entered_text.length() - 1));
                                chatHistory.add(chatMessage);
                                displayMessage(chatMessage);
                                messageET.setText("");
                                wordfound = false;
                                loadDummyHistory();
                                countDownTimer.start();


                            } else {
                                Toast.makeText(ChatActivity.this, "invalid word", Toast.LENGTH_LONG).show();
                                messageET.setTextColor(Color.RED);

                            }
                        }
                        isfinished = false;

                    }
                    }
                }
            }
        });

    }

    public void displayMessage(ChatMessageModel message) {

        adapter.add(message);
        messageET.setTextColor(Color.BLACK);

        Log.e("error", adapter.toString());
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory() {

        String data = null;
        try {

            if(timeup==true)
            {
                data=generateRandomWord(globaldata);

                timeup=false;
            }else {
                data = getDataStartsWithGivenChar(getLastLetter);
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        if (data != null) {
      /*     if(!String.valueOf(data.charAt(0)).equals(getLastLetter))
           {
               sorted_list.clear();
               data=getDataStartsWithGivenChar(getLastLetter);
           }*/


            if(nah_clicked==true){
                data=generateRandomWord(globaldata);
                nah_clicked=false;
            }

      String datalastletter=String.valueOf( data.charAt(data.length()-1));

            messageET.setHint("word must start with: " + datalastletter);
            isMine = true;
            chatMessage = new ChatMessageModel(1, isMine, data, "");
            system_score += data.length();
            opponent.setText("opponent: "+String.valueOf(system_score));


        } else {
            if(nah_clicked==true)
            {
                generateRandomWord=generateRandomWord(globaldata);
                lastLetter = String.valueOf(generateRandomWord.charAt(generateRandomWord.length() - 1));

                nah_clicked=false;
            }
            if(timeup==true)
            {

                generateRandomWord=generateRandomWord(globaldata);
                lastLetter = String.valueOf(generateRandomWord.charAt(generateRandomWord.length() - 1));
timeup=false;
            }

            isMine = true;
            chatMessage = new ChatMessageModel(1, isMine, generateRandomWord, "");
            system_score += generateRandomWord.length();
            opponent.setText("opponent: "+String.valueOf(system_score));
            messageET.setHint("word must start with: " + lastLetter);

        }


        chatHistory.add(chatMessage);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessageModel>());
        messagesContainer.setAdapter(adapter);
        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessageModel message = chatHistory.get(i);
            displayMessage(message);
        }


    }


    public String generateRandomWord(List list) {
        int index;
        String generated;
        Random random = new Random();

        index = random.nextInt(list.size());
        generated = String.valueOf(list.get(index));
        return generated;
    }

    public List<String> readData() {
        String arr[];
        List<String> listdata = new ArrayList<>();
        String data = "";

        StringBuffer sb = new StringBuffer();
        InputStream in = this.getResources().openRawResource(R.raw.words_alpha);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        try {
            if (in != null) {
                while ((data = bufferedReader.readLine()) != null) {
                    sb.append(data + ",");

                }

            }
            arr = sb.toString().split(",");
            listdata.addAll(Arrays.asList(arr));//i want this type of logic so i can add the elements of string once.is it possible?

            Log.d("arr", String.valueOf(arr));
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        return listdata;
    }

    public String getDataStartsWithGivenChar(String first_letter) {
        String data = null;
        if (first_letter != null) {
            Random r = new Random();
            int index;

            for (int i = 0; i < globaldata.size(); i++) {
                char c = globaldata.get(i).charAt(0);
                if (first_letter.equals(String.valueOf(c))) {

                    sorted_list.add(globaldata.get(i));
                    Log.i("list" + i, globaldata.get(i));
                    System.out.println("index:" + i + globaldata.get(i));
                }
            }

            index = (int) (Math.random() * sorted_list.size());
            Object o = sorted_list.get(index);
            data = (String) o;
            Log.i("list", data);
            sorted_list.clear();
            return data;
        } else {
            return null;
        }

    }
    public void onCountDownTimerFinishEvent()
    {

/*this.finish();
        Intent intent=new Intent(ChatActivity.this,ResultsPage.class);
        intent.putExtra("sys",String.valueOf(system_score));
        intent.putExtra("user",String.valueOf(user_score));
        startActivity(intent);*/
countDownTimer.cancel();
minutes.setText("00");



Toast.makeText(ChatActivity.this,"Time Up!!",Toast.LENGTH_LONG).show();
        messagesContainer.setVisibility(View.INVISIBLE);
        messageET.setVisibility(View.INVISIBLE);
        sendBtn.setVisibility(View.INVISIBLE);
        nah_cant_btn.setVisibility(View.INVISIBLE);
        textView6won.setVisibility(View.VISIBLE);

        if(user_score>system_score)
        {
            textView6won.setTextColor(Color.BLACK);
            textView6won.setText("User Won!!");
        }
        else{

            textView6won.setTextColor(Color.BLACK);
            textView6won.setText("Opponent Won!!");

        }
isTimeFinished=true;


    }

    public void onCountDownTimerTickEvent(long millisUntilFinished) {
        Long minutes,seconds;
                minutes= TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

      seconds=TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
this.minutes.setText(minutes+":"+seconds);
      }
}

