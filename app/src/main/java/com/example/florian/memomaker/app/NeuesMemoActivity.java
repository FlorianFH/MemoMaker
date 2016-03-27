package com.example.florian.memomaker.app;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.Calendar;


/**
 * Created by Florian on 19.02.2016.
 */
public class NeuesMemoActivity extends AppCompatActivity {

    //DB
    DBManager dbManager;

    // Variablen für DatePicker
    private String textDate;
    int year;
    int month;
    int day;
    DateTime currentDate;
    static final int DATE_DIALOG_ID = 999;

    //Variablen
    private String text;
    EditText editText;
    Button dateButton;
    DateTime today;

    public static final String MY_PREFS_NAME = "InstantSave";


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

        //Erzeugen eines DatenbankManagers
        dbManager = new DBManager(this);

        EditText editText = (EditText) findViewById(R.id.newMemo);
        dateButton = (Button) findViewById(R.id.memoDate);
        today = new DateTime(System.currentTimeMillis());
        loadSettings();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProperties();
                saveSettings();
            }
        });

        //OnClick für den DatePickerDialog
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

    }//ENDE onCreate


    //Update der Shared Preferences
    private void updateProperties() {

        editText = (EditText) findViewById(R.id.newMemo);
        dateButton = (Button) findViewById(R.id.memoDate);

        //Wenn noch kein Datum gesetzt ist, wird das Datum in einer Woche gewählt
        if (currentDate == null) {
            rememberWeek();
            //Toast kann später raus
            Toast.makeText(getApplicationContext(), "Zukunft gesetzt", Toast.LENGTH_SHORT).show();
        }

        // Datum als String speichern
        textDate = String.valueOf(currentDate.getMillis());

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


    //Erzeugen eines Dialogs
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
                DatePickerDialog dialog =  new DatePickerDialog(this, datePicker, year, month, day);
                dialog.getDatePicker().setMinDate(new DateTime().getMillis());
                return dialog;
        }
        return null;
    }//Ende onCreateDialog


    //DatePickerDialog
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            //Das vom User gewuenschte Datum im Kalenderformat abspeichern
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            Calendar c = Calendar.getInstance();
            c.set(year,month,day);

            int localMonth = month+1;
            //Zeitstempel erfassen
            currentDate = new DateTime(c.getTimeInMillis());

            //Gewähltes Datum auf dem Buton anzeigen
            dateButton.setText(getString(R.string.memodate)+" "+day + "." + localMonth + "." + year);
        }
    };//Ende private DatePickerDialog


    //Ab in die Zukunft
    public void rememberWeek() {
        //Aktuelles Datum mittels Joda-Time um eine Woche nach vorne verschieben
        currentDate = today;
        currentDate = currentDate.plusDays(7);
    }


    public void save(View v) {
        saveSettings();
        ListViewItem item = new ListViewItem(0,ListViewItem.TYP_MEMO,textDate,text, 0);
        item = dbManager.insertData(item);
        finish();
        notifyAtTime(item);
        clear();
        Toast.makeText(getApplicationContext(), "Eintrag gespeichert", Toast.LENGTH_SHORT).show();
    }


    //Methode zum zurücksetzen der Variablen
    public void clear(){
        text = "";
        textDate ="";
        editText.setText("");
    }


    //Seten eines Alarms, der eine Notification auslöst
    public void notifyAtTime(ListViewItem item){

        //Spezifischer Intnet mit dem entsprechenden Datensatz für den AlarmReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KS_DESCRIPTION,item.getDescription());
        intent.putExtra(AlarmReceiver.KS_NOTIFYID, item.getId());

        //Alarm löst den PendingIntent aus dem Hintergrund aus
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, 0);
        //setzen des Alarmdatums
        int alarmTimeYear = currentDate.getYear();
        int alarmTimeMonth = currentDate.getMonthOfYear();
        int alarmTimeDay = currentDate.getDayOfMonth();

        //setzen des Alarmdatums für den aktuellen Tag
        int alarmTimeYesterdayYear = currentDate.minusDays(1).getYear();
        int alarmTimeYesterdayMonth = currentDate.minusDays(1).getMonthOfYear();
        int alarmTimeYesterdayDay = currentDate.minusDays(1).getDayOfMonth();

        //setzen des aktuellen Systemdatums
        int todayTimeYear = today.getYear();
        int todayTimeMonth = today.getMonthOfYear();
        int todayTimeDay = today.getDayOfMonth();

        //Wenn Datum heute, Erinnerung in einer Stunde
        if(alarmTimeDay == todayTimeDay && alarmTimeMonth == todayTimeMonth && alarmTimeYear == todayTimeYear) {
            currentDate = currentDate.plusHours(1);
        }       //Wenn Datum morgen, Erinnerung 12 Stunden vor Fälligkeit
        else if(alarmTimeYesterdayDay == todayTimeDay && alarmTimeYesterdayMonth == todayTimeMonth
                && alarmTimeYesterdayYear == todayTimeYear) {
            currentDate = currentDate.minusHours(12);
        }
        else {//Wenn Datum gesetzt, Erinnerung einen Tag vorher
            currentDate = currentDate.minusDays(1);
        }
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, currentDate.getMillis(), pendingIntent);
    }//Ende notifyAtTime

}