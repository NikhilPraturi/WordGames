package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ListOfGames extends AppCompatActivity {
Button fllbtn,mlbtn,bwbtn,rwbtn;
String multiplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
multiplay=getIntent().getStringExtra("multiplay");

        fllbtn = (Button) findViewById(R.id.fllbtn);
        fllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (multiplay==null) {
                    i = new Intent(ListOfGames.this, ChatActivity.class);
             startActivity(i);

                }

            }
        });

        bwbtn = (Button) findViewById(R.id.bwbtn);
        bwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (multiplay==null) {
                    i = new Intent(ListOfGames.this, SoloGameModuleTwo.class);
                    startActivity(i);

                }

            }
        });
/*

        mlbtn=(Button)findViewById(R.id.mlbtn);
        mlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ListOfGames.this,MissingLettersGame.class);
                startActivity(i);

            }
        });

        rwbtn=(Button)findViewById(R.id.rwbtn);
        rwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ListOfGames.this,RearrangingWordGame.class);
                startActivity(i);

            }
        });
    }
*/

    }
}
