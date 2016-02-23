package com.example.florian.memomaker.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Studium on 20.02.2016.
 */
public class NeueTodoActivity extends AppCompatActivity{

    //DB
    SQLiteDatabase mydb;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";

    EditText prio;
    private String textPrio;
    private String text;
    EditText editText;
    Button saveButton;
    public static final String MY_PREFS_NAME = "InstantSaveTodo";


    private void updateProperties(){
        // Priorität
        prio =(EditText)findViewById(R.id.prio);
        textPrio = prio.getText().toString();

        editText = (EditText)findViewById(R.id.newTodo);
        saveButton = (Button)findViewById(R.id.savebutton);

        text = editText.getText().toString();
    }//Ende updateProperties


    private void saveSettings(){
        updateProperties();

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();

        edit.putString("KEY_TEXT", text);
        //Datum
        edit.putString("KEY_PRIO", textPrio);
        edit.commit();
    }//Ende saveSettings


    private void loadSettings(){

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        text = shared.getString("KEY_TEXT","");

        //Datum
        textPrio = shared.getString("KEY_PRIO","");
        prio = (EditText) findViewById(R.id.prio);
        prio.setText(textPrio);

        editText = (EditText) findViewById(R.id.newTodo);
        saveButton = (Button)findViewById(R.id.savebutton);

        editText.setText(text);
    }//Ende loadSettings


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
        setContentView(R.layout.add_new_todoitem);

        final Button saveButton = (Button) findViewById(R.id.savebutton);
        EditText editText = (EditText) findViewById(R.id.newTodo);

        // Test löschen DB
        //dropTable();

        loadSettings();
        createTable();

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
                //Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                saveSettings();
                inserIntoTable();
                finish();
                Toast.makeText(getApplicationContext(), "Eintrag gespeichert", Toast.LENGTH_LONG).show();
            }
        });

    }//Ende onCreate


    //DB
    public void createTable() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (ID INTEGER PRIMARY KEY, TYPE TEXT, " +
                    "DATEMEMO DATE, PRIORITY CHAR, DESCRIPTION TEXT, ARCHIVE INTEGER);");
            mydb.close();

            Toast.makeText(getApplicationContext(), "Erstellen erfolgreich", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende createTable


    public void inserIntoTable() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , '" + textPrio + "', '" + text + "', 0)");
            /* Testdaten
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                            "VALUES('todo', '' , 'A', 'Testtodo1',0)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo2',0)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo3',0)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo4',0)");
            */
            mydb.close();

            Toast.makeText(getApplicationContext(), "Speichern erfolgreich", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende instertIntoTable


    public void dropTable() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            //String test = ("DROP " + TABLE);
            mydb.execSQL("DROP TABLE " + TABLE);
            mydb.close();

            Toast.makeText(getApplicationContext(), "Loeschen erfolgreich", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Loeschen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende dropTable

}

