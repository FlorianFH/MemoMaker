package com.example.florian.memomaker.app;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    //Variablen
    ListView listViewtodo;
    ListView listViewmemo;
    ListView listViewArchivTodo;
    ListView listViewArchivMemo;
    String[] values;
    String[] valuesMemo;
    String[] archiveTodoArray;
    String[] archiveMemoArray;

    SQLiteDatabase mydb;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";

    private static final String ARG_SECTION_NUMBER = "section_number";


    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_archive, container, false);


        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){

            View todoView = inflater.inflate(R.layout.fragment_todo, container, false);

            listViewtodo = (ListView) todoView.findViewById(R.id.listViewTodo);

            //Ladefunktion der Datenbank
            values = loadTodoData();

            //Adapter für To-Do definieren
            //Kontext, Layout je Reihe, ID der TextView mit den Daten, Arraydaten
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
                    android.R.id.text1, values);

            //Array mit Adapter verknüpfen
            listViewtodo.setAdapter(adapter1);

            return todoView;
        }//Ende If TO-DO List-View


        //Arrays für ARCHIVE
        if(getArguments().getInt(ARG_SECTION_NUMBER)== 2){

            View memoView = inflater.inflate(R.layout.fragment_memo, container, false);
            //ListView initialisieren
            listViewmemo = (ListView) memoView.findViewById(R.id.listViewMemo);

            // Ladefunktion der Datenbank
            valuesMemo = loadMemoData();

            //Adapter für MemoView
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, valuesMemo);

            //Array mit Adapter verknüpfen
            listViewmemo.setAdapter(adapter2);

            return memoView;
        }//Ende If MEMO List-View

        if (getArguments().getInt(ARG_SECTION_NUMBER)== 3){

            View archiveView = inflater.inflate(R.layout.fragment_archive, container, false);

            //ListView initialisieren
            listViewArchivTodo = (ListView)archiveView.findViewById(R.id.listViewTodo);
            listViewArchivMemo = (ListView)archiveView.findViewById(R.id.listViewMemo);

            //Ladefunktion der Datenbank
            archiveTodoArray = loadTodoDataArchive();
            archiveMemoArray = loadMemoDataArchive();

            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, archiveTodoArray);

            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, archiveMemoArray);

            //Array mit Adapter verknüpfen
            listViewArchivTodo.setAdapter(adapter3);
            listViewArchivMemo.setAdapter(adapter4);

            return archiveView;
        }//Ende If ARCHIVE List-View
        return rootView;
    }//Ende onCreateView


    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
    }


    //DB
    public String[] loadTodoData() {

        String[] tdData;
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 0 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("PRIORITY");
            Integer cindex1 = allrows.getColumnIndex("DESCRIPTION");
            tdData = new String[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    tdData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    i++;
                } while (allrows.moveToNext());
                //Test
                Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            tdData = new String[0];
        }
        return tdData;
    }//Ende loadTodoData


    public String[] loadMemoData() {

        String[] mmData;
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 0 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("DATEMEMO");
            Integer cindex1 = allrows.getColumnIndex("DESCRIPTION");
            mmData = new String[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    mmData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    i++;
                } while (allrows.moveToNext());
                //Test
                Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            mmData = new String[0];
        }
        return mmData;
    }//Ende loadMemoData


    // TODO:Archiv --> noch testen
    public String[] loadTodoDataArchive() {

        String[] tdData;
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 1 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("PRIORITY");
            Integer cindex1 = allrows.getColumnIndex("DESCRIPTION");
            tdData = new String[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    tdData[i] = allrows.getString(cindex1);
                    i++;
                } while (allrows.moveToNext());
                //Test
                Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            tdData = new String[0];
        }
        return tdData;
    }//Ende loadTodoDataArchive


    //TODO: Archiv --> noch testen
    public String[] loadMemoDataArchive() {

        String[] mmData;
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 1 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("DATEMEMO");
            Integer cindex1 = allrows.getColumnIndex("DESCRIPTION");
            mmData = new String[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    mmData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    i++;
                } while (allrows.moveToNext());
                //Test
                Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            mmData = new String[0];
        }
        return mmData;
    }//Ende loadMemoDataArchiv

}
