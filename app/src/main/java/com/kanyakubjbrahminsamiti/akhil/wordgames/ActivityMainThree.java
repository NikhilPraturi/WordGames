package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMainThree extends AppCompatActivity {

    @BindView(R.id.solo) Button solo_Btn;
    @BindView(R.id.multi)Button multi_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_three);
        ButterKnife.bind(this);

        final Intent i = new Intent(ActivityMainThree.this, ActivityGameThree.class);

        solo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("gameType","solo");
                startActivity(i);
            }
        });
        multi_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("gameType","multi");
                startActivity(i);
            }
        });

    }


}
