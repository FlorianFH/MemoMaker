package com.example.florian.memomaker.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Florian on 19.02.2016.
 */
public class NeuesMemoActivity extends AppCompatActivity{


    private String text;
    EditText editText;
    Button saveButton;
    public static final String MY_PREFS_NAME = "InstantSave";

    private void updateProperties(){
        editText = (EditText)findViewById(R.id.newMemo);
        saveButton = (Button)findViewById(R.id.savebutton);

        text = editText.getText().toString();
    }

    private void saveSettings(){
        updateProperties();

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();

        edit.putString("KEY_TEXT", text);
        edit.commit();
    }

    private void loadSettings(){
        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        text = shared.getString("KEY_TEXT","");

        editText = (EditText) findViewById(R.id.newMemo);
        saveButton = (Button)findViewById(R.id.savebutton);

        editText.setText(text);
    }

    @Override
    public void onPause(){
        super.onPause();
        saveSettings();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadSettings();
    }



    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_new_memoitem);

        final Button saveButton = (Button) findViewById(R.id.savebutton);
        EditText editText = (EditText) findViewById(R.id.newMemo);

        loadSettings();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProperties();
                saveSettings();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


}
