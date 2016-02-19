package com.example.florian.memomaker.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    ListView listViewtodo;
    ListView listViewmemo;
    ListView listViewArchivTodo;
    ListView listViewArchivMemo;
    String[] values;
    String[] valuesMemo;
    String[] archiveTodoArray;
    String[] archiveMemoArray;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 0){

            listViewtodo = (ListView) rootView.findViewById(R.id.listViewTodo);
            listViewmemo = (ListView) rootView.findViewById(R.id.listViewMemo);



            //TEST-Array mit Daten für die Listview
            values = new String[]{
                    "Neue Activity starten",
                    "Link zur FH-Website",
                    "Kontakte editieren",
                    "Foto machen",
                    "Musik abspielen",
                    "Eigene Funktion",
                    "Mehr",
                    "funktionen",
                    "A",
                    "B",
                    "test",
                    "Poster",
                    "Peter",
                    "HORST",
                    "dirk"
            };

            //TEST-Array für MemoView
            valuesMemo = new String[]{
                    "Erstes Testelement",
                    "Zweites Element",
                    "MEMO",
                    "Mehr",
                    "elemente",
                    "befülle die",
                    "SCHEISS",
                    "LISTÖÖÖEEE"
            };

            //Adapter für To-Do definieren
            //Kontext, Layout je Reihe, ID der TextView mit den Daten, Arraydaten
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
                    android.R.id.text1, values);

            //Adapter für MemoView
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, valuesMemo);

            //Array mit Adapter verknüpfen
            listViewtodo.setAdapter(adapter1);
            listViewmemo.setAdapter(adapter2);


        }//Ende If AKTIVE List-View


        //Arrays für ARCHIVE
        if(getArguments().getInt(ARG_SECTION_NUMBER)== 1){

            //ListView initialisieren
            listViewArchivTodo = (ListView) rootView.findViewById(R.id.listViewTodo);
            listViewArchivMemo = (ListView) rootView.findViewById(R.id.listViewMemo);

            archiveTodoArray = new String[]{
                    "Neue Activity starten",
                    "Link zur FH-Website",
                    "Kontakte editieren",
                    "Foto machen",
                    "Musik abspielen",
                    "Eigene Funktion",
                    "Mehr",
                    "funktionen",
                    "A",
                    "B",
            };

            archiveMemoArray = new String[]{
                    "Kontakte editieren",
                    "Foto machen",
                    "Musik abspielen",
                    "Eigene Funktion",
                    "Mehr",
                    "funktionen",
                    "A",
                    "B",
                    "ftntdh",
                    "gerhgs",
                    "Peter",
                    "HORST",
            };


            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, archiveTodoArray);

            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, archiveMemoArray);

            //Array mit Adapter verknüpfen
            listViewArchivTodo.setAdapter(adapter3);
            listViewArchivMemo.setAdapter(adapter4);
        }//Ende If ARCHIVE List-View


        return rootView;
    }

    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
        //Save Data
        //TODO: aktuelle Daten abspeichern
    }
}
