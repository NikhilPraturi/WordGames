package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MultiplayForHostJoin extends AppCompatActivity {

TextView tv;
    String hostmine,joinmine;
int count=0;
    Button ho,join;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay_for_host_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
tv=(TextView)findViewById(R.id.textView4);
ho=(Button)findViewById(R.id.button2hostcount);
join=(Button)findViewById(R.id.button5joincount);
ho.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

ho.setVisibility(View.INVISIBLE);
join.setVisibility(View.INVISIBLE);
tv.setText("Waiting for the opponent!!!");
count("hostmine");
    }
});

join.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ho.setVisibility(View.INVISIBLE);
        join.setVisibility(View.INVISIBLE);
tv.setText("Waiting for host!!!");
    count("joinmine");
    }
});
    }

public void count(String whichmine)
{
    if(whichmine.equals("hostmine"))
    {
        hostmine=whichmine;
    }
    if(whichmine.equals("joinmine"))
    {
        joinmine=whichmine;
    }
    count++;
    if(count==2)
    {
        Intent i=new Intent(MultiplayForHostJoin.this,ChatMplyTwoPlayers.class);

i.putExtra("host",hostmine);
i.putExtra("join",joinmine);
        startActivity(i);
    }
}

}
