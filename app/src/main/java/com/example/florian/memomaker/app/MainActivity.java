package com.example.florian.memomaker.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";
    SQLiteDatabase mydb;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(),getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        boolean checkDB = doesDatabaseExist(getApplicationContext(), DBMEMO);
        if (!checkDB) {
            createTable();
        }

        // Testdaten archiv generieren
        //generateTestdataArchiv();

    }//Ende onCreate


    // Check if DB exists
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }//Ende onOptionsItemSelected


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        protected Context mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }


        //Anzahl der Tabs
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


        //Titel der Tabs
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return mContext.getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return mContext.getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return mContext.getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }//Ende getPageTitle

    }//Ende SectionPagerAdapter-Klasse


    public void fab(View v){

        switch (v.getId()){

            case R.id.addNewMemo:
                Intent explicitIntentNeuesMemo = new Intent (this, NeuesMemoActivity.class);
                startActivity(explicitIntentNeuesMemo);
                break;

            case R.id.addNewTodo:
                Intent explicitIntentNeueTodo = new Intent (this, NeueTodoActivity.class);
                startActivity(explicitIntentNeueTodo);
                break;

            default:
                break;
        }
    }//Ende fab-Methode


    //DB
    public void createTable() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (ID INTEGER PRIMARY KEY, TYPE TEXT, " +
                    "DATEMEMO DATE, PRIORITY CHAR, DESCRIPTION TEXT, ARCHIVE INTEGER);");
            mydb.close();

            //Toast.makeText(getApplicationContext(), "Erstellen erfolgreich", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende createTable


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


    // Generieren von Testdaten fuers Archiv
    public void generateTestdataArchiv() {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            //mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
            //        "VALUES('todo', '' , '" + textPrio + "', '" + text + "', 0)");

            // Testdaten TodoArchiv
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo1',1)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo2',1)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo3',1)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , 'A', 'Testtodo4',1)");

            // Testdaten MemoArchiv
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('memo', '26042006' , '', 'Testmemo1',1)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('memo', '21052006' , '', 'Testmemo1',1)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('memo', '14062006' , '', 'Testmemo3',1)");
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('memo', '25062006' , '', 'Testmemo4',1)");

            mydb.close();

            Toast.makeText(getApplicationContext(), "Testdaten generiert", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Testdaten Archiv", Toast.LENGTH_LONG).show();
        }
    }//Ende instertIntoTable TESTARCHIVE

}
