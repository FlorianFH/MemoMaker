package com.example.florian.memomaker.app;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Florian on 20.03.2016.
 */
public class DBManager {

    private Context context;
    int indexID, indexType, indexPriority, indexDate , indexDescription, indexArchive;

    public DBManager(Context context){
     this.context = context;
    }


    public ListViewItem insertData(ListViewItem item){
        SQLiteDatabase mydb;
        String DBMEMO = "memomaker.db";
        String TABLE = "mmdata";

        mydb = context.openOrCreateDatabase(DBMEMO,Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("TYPE",item.getItemType());
        values.put("DATEMEMO",item.getDatum());
        values.put("PRIORITY",item.getPrio());
        values.put("DESCRIPTION",item.getDescription());
        values.put("ARCHIVE", item.getArchiveTag());

        //schreiben des Items in die Tabelle und Rückgabe der ID
        long id = mydb.insert(TABLE,null, values);
        mydb.close();

        if (id > -1) {
            return getListViewItemByID((int) id);
        }

        return null;
    }//Ende insertData


    //Gibt genau einen Datensatz mit spezifischer ID zurück
    public ListViewItem getListViewItemByID(int id){
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where ID = '"+id+"'", null);

            indexID = allrows.getColumnIndex("ID");
            indexType = allrows.getColumnIndex("TYPE");
            indexDate = allrows.getColumnIndex("DATEMEMO");
            indexPriority = allrows.getColumnIndex("PRIORITY");
            indexDescription = allrows.getColumnIndex("DESCRIPTION");
            indexArchive = allrows.getColumnIndex("ARCHIVE");

            //Datensatz als ListViewItem abspeichern und zurückgeben
            if(allrows.moveToFirst()) {
                 return new ListViewItem(allrows.getInt(indexID), allrows.getString(indexType), allrows.getString(indexDate), allrows.getString(indexPriority),
                            allrows.getString(indexDescription), allrows.getInt(indexArchive));
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
        }
        return null;
    }//Ende getListViewItemByID


    //Laden aller TO-DO Daten
    public ArrayList<ListViewItem> loadTodoData() {

        ArrayList<ListViewItem> tdData = new ArrayList<ListViewItem>();
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 0 " +
                    "order by PRIORITY, DESCRIPTION", null);

            indexID = allrows.getColumnIndex("ID");
            indexType = allrows.getColumnIndex("TYPE");
            indexPriority = allrows.getColumnIndex("PRIORITY");
            indexDescription = allrows.getColumnIndex("DESCRIPTION");
            indexArchive = allrows.getColumnIndex("ARCHIVE");

            //Arraylist mit Daten füllen
            if(allrows.moveToFirst()) {
                do {
                    tdData.add(new ListViewItem(allrows.getInt(indexID), allrows.getString(indexType), allrows.getString(indexPriority),
                            allrows.getString(indexDescription), allrows.getInt(indexArchive)));
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
        }
        return tdData;
    }//Ende loadTodoData*/


    //Laden aller Memo Daten
    public ArrayList<ListViewItem> loadMemoData() {

        ArrayList<ListViewItem> mmData = new ArrayList<ListViewItem>();
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 0 " +
                    "order by DATEMEMO, DESCRIPTION", null);
            indexID = allrows.getColumnIndex("ID");
            indexType = allrows.getColumnIndex("TYPE");
            indexDate = allrows.getColumnIndex("DATEMEMO");
            indexDescription = allrows.getColumnIndex("DESCRIPTION");
            indexArchive = allrows.getColumnIndex("ARCHIVE");

            //Arraylist mit Daten füllen
            if(allrows.moveToFirst()) {
                do {
                    //Konvertierung des Datums von Long nach String in Datumsformat
                    String dateAsLong = allrows.getString(indexDate);
                    DateFormat df = new DateFormat();
                    String datum = df.format("dd-MM-yyyy",Long.parseLong(dateAsLong)).toString();

                    mmData.add(new ListViewItem(allrows.getInt(indexID), allrows.getString(indexType), datum,
                            allrows.getString(indexDescription), allrows.getInt(indexArchive)));
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
        }
        return mmData;
    }//Ende loadMemoData*/


    // LoadtododataArchiv
    public ArrayList<ListViewItem> loadTodoDataArchive() {

        ArrayList<ListViewItem> tdData = new ArrayList<ListViewItem>();
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 1 " +
                    "order by PRIORITY, DESCRIPTION", null);

            indexID = allrows.getColumnIndex("ID");
            indexType = allrows.getColumnIndex("TYPE");
            indexPriority = allrows.getColumnIndex("PRIORITY");
            indexDescription = allrows.getColumnIndex("DESCRIPTION");
            indexArchive = allrows.getColumnIndex("ARCHIVE");

            //Arraylist mit Daten füllen
            if(allrows.moveToFirst()) {
                do {
                    tdData.add(new ListViewItem(allrows.getInt(indexID), allrows.getString(indexType), allrows.getString(indexPriority),
                            allrows.getString(indexDescription), allrows.getInt(indexArchive)));
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
        }
        return tdData;
    }//Ende loadTodoDataArchiv


     // LoadMemoDataArchiv
    public ArrayList<ListViewItem> loadMemoDataArchive() {

        ArrayList<ListViewItem> mmData = new ArrayList<ListViewItem>();
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 1 " +
                    "order by DATEMEMO, DESCRIPTION", null);

            indexID = allrows.getColumnIndex("ID");
            indexType = allrows.getColumnIndex("TYPE");
            indexDate = allrows.getColumnIndex("DATEMEMO");
            indexDescription = allrows.getColumnIndex("DESCRIPTION");
            indexArchive = allrows.getColumnIndex("ARCHIVE");

            //Arraylist mit Daten füllen
            if(allrows.moveToFirst()) {
                do {
                    //Konvertierung des Datums von Long nach String in Datumsformat
                    String dateAsLong = allrows.getString(indexDate);
                    DateFormat df = new DateFormat();
                    String datum = df.format("dd-MM-yyyy",Long.parseLong(dateAsLong)).toString();

                    mmData.add(new ListViewItem(allrows.getInt(indexID), allrows.getString(indexType),datum,
                            allrows.getString(indexDescription), allrows.getInt(indexArchive)));
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
        }
        return mmData;
    }//Ende loadMemoDataArchiv


    public void updateTable (int archiveTag, int itemID){
        SQLiteDatabase mydb;
        String DBMEMO = "memomaker.db";
        String TABLE = "mmdata";

        mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
        mydb.execSQL("UPDATE " + TABLE + " SET ARCHIVE = " + archiveTag +
                " WHERE ID = " + itemID);
        mydb.close();

        if (archiveTag == 0){
            Toast.makeText(context,"Datensatz wiederhergestellt", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Ins Archiv verschoben", Toast.LENGTH_LONG).show();
        }
    }//Ende updateTable


    public void deleteDatasetFromDB(int id) {
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("DELETE FROM " + TABLE + " WHERE ID = " + id);
            mydb.close();

            Toast.makeText(context, "Datensatz wurde gelöscht", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Fehler beim Löschen aus der Datenbank", Toast.LENGTH_LONG).show();
        }

    }//Ende deleteDataset


    //Löschen aller Datensätze
    //WIRD NOCH NICHT UNTERSTÜTZT
    public void dropTable() {
        SQLiteDatabase mydb;
        String DBMEMO = "memomaker.db";
        String TABLE = "mmdata";
        try {
            mydb = context.openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            //String test = ("DROP " + TABLE);
            mydb.execSQL("DROP TABLE " + TABLE);
            mydb.close();

            Toast.makeText(context, "Loeschen erfolgreich", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Fehler beim Loeschen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende dropTable

}


