package com.example.florian.memomaker.app;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by Studium on 20.02.2016.
 */
public class NeuanlageAuswahl extends AppCompatActivity {

    Button addMemo;
    Button addTodo;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.selection_dialog);

        final Button addMemo = (Button) findViewById(R.id.addNewMemo);
        final Button addTodo = (Button) findViewById(R.id.addNewTodo);


        addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                Intent explicitIntentMemo = new Intent (NeuanlageAuswahl.this, NeuesMemoActivity.class);
                startActivity(explicitIntentMemo);
                finish();
            }
        });


        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                Intent explicitIntentTodo = new Intent(NeuanlageAuswahl.this, NeueTodoActivity.class);
                startActivity(explicitIntentTodo);
                finish();
            }
        });

    }//Ende onCreate

}
