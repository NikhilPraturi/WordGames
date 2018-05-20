package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChatMplyTwoPlayers extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMultiplayChatMessage> adapter;
    LinearLayout activity_main;
String emailone,emailtwo;
    String val;
    boolean istimeover=false;
    EditText ed2;
    int opponentscore = 0;
    boolean isTimeFinished;
    TextView textView6wonytct;
    Button send;
    boolean isFirebase_cleared = true;
    boolean first_time = true;
    boolean ismine, opp;
    ListView lv;
    boolean firstemailperson=true;
    int you = 0;
    EditText ed;
    Button cantytct;
    boolean isConnectedFirstTime=true;
    boolean sendBymy;
    String curent_user, oppo_user;
    GameTimeFive gameTime;
    TextView sco_opp, sco_you, minutesytct, tvytct;
    CountDownTimer countDownTimer;
    String hint;
    DatabaseReference databaseReference;
    boolean firstmsg=true;
boolean yo=false;
    int count=0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Snackbar.make(activity_main, "you have been signed out", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_mply_two_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.removeValue();


        cantytct = (Button) findViewById(R.id.cantytct);
        textView6wonytct = (TextView) findViewById(R.id.textView6wonytct);
        gameTime = new GameTimeFive(5 * 60000, 1000);
        gameTime.setSourceActivity(this);
        gameTime.start();
        minutesytct = (TextView) findViewById(R.id.minutesytct);
        tvytct = (TextView) findViewById(R.id.tvytct);

        sco_opp = (TextView) findViewById(R.id.textView10opponentytct);
        sco_you = (TextView) findViewById(R.id.textView11myytct);

                      countDownTimer = new CountDownTimer(11000, 1000) {

                          public void onTick(long millisUntilFinished) {

                              tvytct.setText("Time: " + millisUntilFinished / 1000 + " sec");

                              istimeover=false;


                          }


                          public void onFinish() {
                              istimeover=true;
         /*                     send.setVisibility(View.INVISIBLE);
                              try {


                                  Thread.sleep(2000);
                                  send.setVisibility(View.VISIBLE);
                              }
                              catch (Exception e)
                              {
                                  e.printStackTrace();
                              }
*/
                              start();
                          }

                      }.start();

 ed = (EditText) findViewById(R.id.messageEditytct);

        send = (Button) findViewById(R.id.chatSendButtonytct);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firstmsg) {
                    firstmsg = false;

                    val = ed.getText().toString();

                    you += val.length();

                   // sco_you.setText("you:" + you);
                    String lastletter = String.valueOf(val.charAt(val.length() - 1));

                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMultiplayChatMessage(ismine, val, FirebaseAuth.getInstance().getCurrentUser().getEmail(), lastletter, you));

                    curent_user = FirebaseAuth.getInstance().getCurrentUser().getEmail();


                    ed2 = ed;

                    Toast.makeText(ChatMplyTwoPlayers.this, curent_user, Toast.LENGTH_LONG).show();
/*
                if(curent_user!=null)
{
    Toast.makeText(ChatMplyTwoPlayers.this,curent_user,Toast.LENGTH_LONG).show();
}*/

                    ed.setText("");


                }
                else {
                    val = ed.getText().toString();

                    String first_letter = String.valueOf(val.trim().charAt(0));
                   if(hint.length()!=0){
                    if (first_letter.equals(hint)) {


                        sendBymy = true;

                        you += val.length();
                        databaseReference.removeValue();

                        // sco_you.setText("you:" + you);
                        String lastletter = String.valueOf(val.charAt(val.length() - 1));

                        FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMultiplayChatMessage(ismine, val, FirebaseAuth.getInstance().getCurrentUser().getEmail(), lastletter, you));

                        curent_user = FirebaseAuth.getInstance().getCurrentUser().getEmail();


                        ed2 = ed;

                        Toast.makeText(ChatMplyTwoPlayers.this, curent_user, Toast.LENGTH_LONG).show();
/*
                if(curent_user!=null)
{
    Toast.makeText(ChatMplyTwoPlayers.this,curent_user,Toast.LENGTH_LONG).show();
}*/

                        ed.setText("");
                    }
                    else{
                        Toast.makeText(ChatMplyTwoPlayers.this,"word must starts with:"+hint,Toast.LENGTH_LONG).show();
                    }
                    }

                }
            }
        });


        activity_main = (LinearLayout) findViewById(R.id.lm);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);


        } else {
            Snackbar.make(activity_main, "welcome" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            displayChatMesage();

        }

    }


    public void displayChatMesage() {
       if(isConnectedFirstTime)
       {
           databaseReference= FirebaseDatabase.getInstance().getReference();
           databaseReference.removeValue();

           isConnectedFirstTime=false;
       }

       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key : keys) {
                    ChatMultiplayChatMessage cc = key.getValue(ChatMultiplayChatMessage.class);
                    String email = cc.getMessageUser();

                    if(firstemailperson) {

                        emailone=email;
                        firstemailperson=false;
                    }

                    if (email != null) {
                        if(emailone!=null) {
                            if (emailone.equals(email)) {
                                sco_you.setText("you:" + you);
                            }
                            else {
                                opponentscore = cc.getScore();
                                sco_opp.setText("opponent:" + opponentscore);


                            }

                        }


                    }
                    }

//                    runOnUiThread();
       //     databaseReference.removeValue();
            }


           /*         private void runOnUiThread() {
                try {
                    Thread.sleep(3000);
                    databaseReference.removeValue();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
           }
*/           @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            ListView listView = (ListView) findViewById(R.id.messagesContainerytct);
            lv = listView;

lv.setAdapter(null);
            adapter = new FirebaseListAdapter<ChatMultiplayChatMessage>(this, ChatMultiplayChatMessage.class, R.layout.list_item_chatmultiplay, FirebaseDatabase.getInstance().getReference()) {
                @Override
                protected void populateView(View v, ChatMultiplayChatMessage model, int position) {
                    TextView messageText, messageUser;

                    messageText = (TextView) v.findViewById(R.id.msgtxt);
                    messageUser = (TextView) v.findViewById(R.id.msguser);
                    String ss = model.getMessageText();
                    hint = model.getLastletter();


if(curent_user!=null) {

    if (!curent_user.equals(messageUser)) {

       // opponentscore = model.getScore();

     //   sco_opp.setText("opponent:" + String.valueOf(opponentscore));
    }
}
                //    Log.d("opponent",String.valueOf(opponentscore));
                    if (ss != null) {
                        messageText.setText(ss);
                    }
                    if (model.getMessageUser() != null) {
                        messageUser.setText(model.getMessageUser());
                    }
                /*
if(ss.length()!=0) {
    String arr[] = ss.split(",");


*/
                    if (hint.length() != 0) {

                        if (ed2 != null) {
                            ed2.setHint("word must starts with:" + hint);
                        }
                    }


                }


            };
            listView.setAdapter(adapter);

        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if (requestCode==SIGN_IN_REQUEST_CODE)
{
    Snackbar.make(activity_main,"Successfully Signed Welcome!",Snackbar.LENGTH_SHORT).show();


    displayChatMesage();
}else{
    Snackbar.make(activity_main,"We Could'nt Sign U In try again later!",Snackbar.LENGTH_SHORT).show();
finish();
}


 }


    public void onCountDownTimerTickEvent(long millisUntilFinished) {
        Long minutes,seconds;
        minutes= TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

        seconds=TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
        this.minutesytct.setText(minutes+":"+seconds);
    }
    public void onCountDownTimerFinishEvent()
    {


        isTimeFinished=true;
        minutesytct.setText("00");
        send.setVisibility(View.INVISIBLE);
        textView6wonytct.setVisibility(View.VISIBLE);
        if(opponentscore>you) {
            textView6wonytct.setTextColor(Color.BLACK);
            textView6wonytct.setText("Opponent Won!!");
        }
        else{
            textView6wonytct.setTextColor(Color.BLACK);
            textView6wonytct.setText("User Won!!");
        }

        lv.setVisibility(View.INVISIBLE);
        minutesytct.setVisibility(View.INVISIBLE);
        sco_you.setVisibility(View.VISIBLE);
        sco_opp.setVisibility(View.VISIBLE);
        tvytct.setVisibility(View.INVISIBLE);
        ed2.setVisibility(View.INVISIBLE);
        cantytct.setVisibility(View.INVISIBLE);
    }

}
