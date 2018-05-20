package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SoloGameModuleTwo extends AppCompatActivity {
Button moduleTwoButton,cantbtn;
EditText moduleTwoEditIext;
TextView textview4seconds,textView5mins,opponent,you,textView6Won;
CountDownTimer countDownTimer;
RecyclerView recyclerView;
boolean isnahclicked;

boolean isuserenteredcorrect=false;
    int min_game_duration=1;
    LinearLayoutManager linearLayoutManager;
    int system_score=0;
    int user_score=0;
    boolean continuoustime=true;
GameTimeTwo gameTime;
boolean isTimeFinished;
boolean timeup=false;

    Thread thread;
    Adapter_Recycler adapter;

boolean isTimeOver;
Long start;
boolean isSysgen=false;
    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_game_module_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cantbtn=(Button)findViewById(R.id.cantmoduletwo);
        textview4seconds=(TextView)findViewById(R.id.textView4seconds);
        textView5mins=(TextView)findViewById(R.id.textView5mins);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        moduleTwoButton = (Button) findViewById(R.id.moduletwobtnsend);
        moduleTwoEditIext = (EditText) findViewById(R.id.moduletwoedittext);
        textView6Won=(TextView)findViewById(R.id.textView6Won);
opponent=(TextView)findViewById(R.id.textView5opponent);
you=(TextView)findViewById(R.id.textView6you);
        setupRecyclerView(recyclerView);

        gameTime = new GameTimeTwo(5*60000, 1000);
        gameTime.setSourceActivity(this);
        gameTime.start();
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    startGame();
                }
                catch (Exception e) {
                    Log.e("startgame exception", e.getMessage());
                }
            }
        });t.start();
        if(continuoustime) {
    countDownTimer = new CountDownTimer(11000, 1000) {
        @Override
        public void onTick(long l) {
            if(isSysgen) {
                opponent.setText("opponent: " + String.valueOf(system_score));
            isSysgen=false;
            }
            you.setText("you: "+String.valueOf(user_score));

            textview4seconds.setText("Time: " + l / 1000 + " sec");
        }

        @Override
        public void onFinish() {
if(!isTimeFinished) {
    if (!isnahclicked) {
        timeup = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startGame();

                } catch (Exception e) {
                    Log.e("startgame exception", e.getMessage());
                }
            }
        });
        t.start();

        start();

    }
}
        }
    }.start();

}

cantbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(!isTimeFinished) {
            Thread tt = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        isnahclicked = true;
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
        moduleTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         if(!isTimeFinished){
                if (interfc.words_set.contains(moduleTwoEditIext.getText().toString())) {
                    String word = moduleTwoEditIext.getText().toString();
                    user_score += (moduleTwoEditIext.getText().length());
                    //  you.setText("you: "+String.valueOf(user_score));
                    //   own_score.setText(String.valueOf(interfc.my_score));
                    Log.e("userenteredtext", "userenteredtext");
                    isuserenteredcorrect = true;

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
                else {
                    Toast.makeText(SoloGameModuleTwo.this, "incorrect", Toast.LENGTH_LONG).show();
                }
             moduleTwoEditIext.setText("");

         }


            }
        });
    }

    public void startGame() throws InterruptedException {


        Random randomizer = new Random();
        String word = shuffle(interfc.words.get(randomizer.nextInt(interfc.words.size())));

system_score+=word.length();
        if (timeup == true) {
            adapter.clear();
            timeup = false;
        }
        if(isuserenteredcorrect==true)
        {
            adapter.clear();
            countDownTimer.start();
            isuserenteredcorrect=false;
        }
        if (isnahclicked==true)
        {
            adapter.clear();
            countDownTimer.start();
            isnahclicked=false;
        }

        for (int i = 0; i < word.length(); i++) {
            adapter.add_data(String.valueOf(word.charAt(i)));
            thread.sleep(1000);
        }
isSysgen=true;


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


    public void setupRecyclerView(RecyclerView recyclerView) {

        adapter = new Adapter_Recycler(SoloGameModuleTwo.this,new ArrayList<String>()){
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
              LinearLayout ll=  (LinearLayout)  holder.itemView;
              int a=ll.getChildCount();
              for (int i=0;i<a;i++) {
                 View v= ll.getChildAt(i);

              if(v instanceof TextView)
              {
                  ((TextView) v).setTextColor(Color.BLACK);
              }
              }
                Log.d("holder",holder.itemView.toString());

            }
        };
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

    public void onCountDownTimerTickEvent(long millisUntilFinished) {
        Long minutes,seconds;
        minutes= TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

        seconds=TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
        this.textView5mins.setText(minutes+":"+seconds);
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
    textView5mins.setText("00");
    Toast.makeText(SoloGameModuleTwo.this,"Time Up!!",Toast.LENGTH_LONG).show();
        recyclerView.setVisibility(View.INVISIBLE);
        moduleTwoEditIext.setVisibility(View.INVISIBLE);
        moduleTwoButton.setVisibility(View.INVISIBLE);
        cantbtn.setVisibility(View.INVISIBLE);
        textView6Won.setVisibility(View.VISIBLE);
        if(system_score>user_score)
        {

            textView6Won.setTextColor(Color.BLACK);
            textView6Won.setText("Opponent Won!!");
        }
        else {
            textView6Won.setTextColor(Color.BLACK);

            textView6Won.setText("User Won!!");

        }

        if(user_score==system_score)
        {
            textView6Won.setText("User Won!!");

        }

countDownTimer.cancel();
    }
}