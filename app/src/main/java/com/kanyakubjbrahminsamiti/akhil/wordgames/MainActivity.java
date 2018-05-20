package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    Button solobtn,multibtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

        }
        catch (Exception e)
        {
            Log.e("error",e.getMessage());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        solobtn=(Button)findViewById(R.id.solobtn);
        multibtn=(Button)findViewById(R.id.multibtn);
        solobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ListOfGames.class);
                startActivity(intent);
            }
        });

    multibtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        Intent intent=new Intent(MainActivity.this,ActivityMainTwo.class);


     /*  intent.putExtra("multiplay","multiplay");
            intent.putExtra("gameType","multi");
*/
     startActivity(intent);

        }
    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
