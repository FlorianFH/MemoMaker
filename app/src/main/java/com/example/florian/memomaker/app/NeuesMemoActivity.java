package com.example.florian.memomaker.app;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.TaskStackBuilder;



import org.joda.time.DateTime;

import java.util.Calendar;



/**
 * Created by Florian on 19.02.2016.
 */
public class NeuesMemoActivity extends AppCompatActivity {

    //DB
    SQLiteDatabase mydb;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";

    // Variablen für DatePicker
    private String textDate;
    int year;
    int month;
    int day;
    DateTime currentDate, futureDate;
    static final int DATE_DIALOG_ID = 999;

    //Variablen für die Notification
    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifyID = 33;

    //Variablen
    private String text;
    EditText editText;
    Button dateButton;

    public static final String MY_PREFS_NAME = "InstantSave";


    private void updateProperties() {

        editText = (EditText) findViewById(R.id.newMemo);
        dateButton = (Button) findViewById(R.id.memoDate);

        //Wenn noch kein Datum gesetzt ist, wird das Datum in einer Woche gewählt
        if (day == 0 && month == 0 && year == 0) {
            futureDate = rememberWeek();
            day = futureDate.getDayOfMonth();
            month = futureDate.getMonthOfYear();
            year = futureDate.getYear();
            //Toast kann später raus
            Toast.makeText(getApplicationContext(), "Zukunft gesetzt", Toast.LENGTH_LONG).show();
        }

        // Datum als String speichern
        textDate = day + "." + month + "." + year;

        // Textfeld
        text = editText.getText().toString();
    }//Ende updatePoperties


    private void saveSettings() {

        updateProperties();

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();

        edit.putString("KEY_TEXT", text);
        edit.putString("KEY_DATE", textDate);
        edit.apply();
    }//Ende saveSettings


    private void loadSettings() {

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        text = shared.getString("KEY_TEXT", "");
        textDate = shared.getString("KEY_DATE", "");

        editText = (EditText) findViewById(R.id.newMemo);

        editText.setText(text);
    }//Ende loadSettings


    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadSettings();
    }


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_new_memoitem);

        EditText editText = (EditText) findViewById(R.id.newMemo);
        Button dateButton = (Button) findViewById(R.id.memoDate);

        loadSettings();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProperties();
                saveSettings();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

    }//ENDE onCreate


    //DatePicker
    @Override
    protected Dialog onCreateDialog(int id) {

        //Calender auf das aktuelle Datum setzen
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //Aufruf eines DatePickerDialogs
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePicker, year, month, day);
        }
        return null;
    }//Ende onCreateDialog


    //DatePickerDialog
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            //Das vom User gewuenschte Datum abspeichern
            year = selectedYear;
            month = selectedMonth;
            month = month + 1;
            day = selectedDay;
        }
    };//Ende private DatePickerDialog


    //Ab in die Zukunft
    public DateTime rememberWeek() {

        //Aktuelles Datum mittels Joda-Time um eine Woche nach vorne verschieben
        currentDate = new DateTime();
        currentDate = currentDate.plusDays(7);
        return currentDate;
    }


    public void createTable() {

        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (ID INTEGER PRIMARY KEY, TYPE TEXT, " +
                    "DATEMEMO DATE, PRIORITY CHAR, DESCRIPTION TEXT, ARCHIVE INTEGER);");
            mydb.close();

            Toast.makeText(getApplicationContext(), "Erstellen erfolgreich", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende createTable


    public void inserIntoTable() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('memo', '" + textDate + "' , '', '" + text + "', 0)");
            /* Testdaten
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                            "VALUES('todo', '' , 'A', 'Testtodo1', 0)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo2', 0)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo3', 0)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo4', 0)");
            */
            mydb.close();

            Toast.makeText(getApplicationContext(), "Speichern erfolgreich", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende insertIntoTable


    public void dropTable() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            //String test = ("DROP " + TABLE);
            mydb.execSQL("DROP TABLE " + TABLE);
            mydb.close();

            Toast.makeText(getApplicationContext(), "Loeschen erfolgreich", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Loeschen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende dropTable


    public void save(View v) {
        saveSettings();
        inserIntoTable();
        finish();
        showNotification();
        Toast.makeText(getApplicationContext(), "Eintrag gespeichert", Toast.LENGTH_LONG).show();
    }


    //Errinnerung das eine Memo abgearbeitet werden muss
    @TargetApi(16)
    public void showNotification() {

//        mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
//
//        Cursor allrows = mydb.rawQuery("select DESCRIPTION from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 0 and DATEMEMO = " + currentDate, null);
//
//        Integer cindex1 = allrows.getColumnIndex("DESCRIPTION");
//
//        String inhalt = allrows.getString(cindex1);
//
//        mydb.close();

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle("Deine Memo wird fällig...")
                .setContentText("inhalt")//TODO: hier soll der eigentliche Inhalt der Memo aus der Datenbank stehen
                .setTicker("Alert Memo")
                .setSmallIcon(R.drawable.ic_assignment_late_24dp);

        Intent intent = new Intent(this, MainActivity.class);

        /*
        * Der TaskStackBuilder sorgt dafür das nach dem anklicken der Notification der User
        * in der App bleibt und diese nicht direkt geschlossen wird wenn der Zurück-Button
        * betätigt wird.*/
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);

        //Der PendingIntent öffnet die App wenn die Notification angeklickt wird.
        PendingIntent pIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pIntent);

        //Nach dem anklicken der Notification wird diese beendet und nicht weiter angezeigt
        notificationBuilder.setAutoCancel(true);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notificationBuilder.build());

        isNotificActive = true;
    }//Ende showNotification

}