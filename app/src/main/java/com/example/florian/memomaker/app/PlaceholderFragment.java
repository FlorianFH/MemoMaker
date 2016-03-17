package com.example.florian.memomaker.app;


import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import android.support.v7.view.ActionMode;
import android.view.ActionMode.Callback;
import java.util.Set;

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
    ArrayList<ListViewItem> values;
    ArrayList<ListViewItem> valuesMemo;
    ArrayList<ListViewItem> archiveTodoArray;
    ArrayList<ListViewItem> archiveMemoArray;

    SQLiteDatabase mydb;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";

    private static final String ARG_SECTION_NUMBER = "section_number";

    // private static String[] longMenu1;

    ActionMode mActionMode;
    //ActionMode.Callback mActionModeCallback;
    //SelectionAdapter mAdapter;
    ItemAdapter adapter1;
    ItemAdapter adapter2;
    ItemAdapter adapter3;
    ItemAdapter adapter4;
    CheckBox myCheckBox1;
    CheckBox myCheckBox2;
    CheckBox myCheckBox3;
    CheckBox myCheckBox4;



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

        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);


        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){

            View todoView = inflater.inflate(R.layout.fragment_todo, container, false);

            listViewtodo = (ListView) todoView.findViewById(R.id.listViewTodo);

            //Ladefunktion der Datenbank
            values = loadTodoData();

            // Long click menu
            //longMenu1 = getResources().getStringArray(R.array.longmenu1);
            //Arrays.sort(longMenu1);

            //Adapter für To-Do definieren
            //Kontext, Layout je Reihe, ID der TextView mit den Daten, Arraydaten
            //ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
            //       android.R.id.text1, values);

            //Array mit Adapter verknüpfen
            //listViewtodo.setAdapter(adapter1);

            adapter1 = new ItemAdapter(getContext(), R.layout.listview_row, values);
            listViewtodo.setAdapter(adapter1);
            registerForContextMenu(listViewtodo);
            listViewtodo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


            /* Anfang MultiSelect mit Selectionadapter
            mAdapter = new SelectionAdapter(getContext(),
                    R.layout.listview_row, android.R.id.text1, values);
            listViewtodo.setAdapter(mAdapter);
            registerForContextMenu(listViewtodo);
            listViewtodo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listViewtodo.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                private int nr = 0;

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub
                    mAdapter.clearSelection();
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub

                    nr = 0;
                    MenuInflater inflater = getActivity().getMenuInflater();
                    inflater.inflate(R.menu.menu_delete1, menu);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // TODO Auto-generated method stub
                    switch (item.getItemId()) {

                        case R.id.delete_selected:
                            nr = 0;
                            mAdapter.clearSelection();
                            mode.finish();
                    }
                    return false;
                }

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    // TODO Auto-generated method stub
                    if (checked) {
                        nr++;
                        mAdapter.setNewSelection(position, checked);

                    } else {
                        nr--;
                        mAdapter.removeSelection(position);

                    }
                    mode.setTitle(nr + " selected");

                }
            });


            listViewtodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    // TODO Auto-generated method stub

                    listViewtodo.setItemChecked(position, !mAdapter.isPositionChecked(position));
                    arg0.setSelected(true);
                    return false;
                }
            });
         Ende MultiSelect mit Selectionadapter*/


            listViewtodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewtodo.getItemAtPosition(pos);

                        Toast.makeText(getActivity(), "Kurzer Click :) " + pos + " " + lvi.getName(), Toast.LENGTH_LONG).show();
                        Log.v("long clicked", "pos: " + pos);
                        // Start the CAB using the ActionMode.Callback defined above
                        listViewtodo.setItemChecked(pos, true);
                        // mActionMode = getActivity().startActionMode(mActionModeCallback);
                        //listViewtodo.setItemChecked(pos, true);
                        //view.setSelected(true);
                    }

                }
            });

            //Erste Version AdapterLongclick
            listViewtodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    if (mActionMode != null) {
                        return false;
                    }

                    //String str=listViewtodo.getItemAtPosition(pos).toString();
                    ListViewItem lvi = (ListViewItem) listViewtodo.getItemAtPosition(pos);

                    Toast.makeText(getActivity(), "Laaaaanger Click :) " + pos + " " + lvi.getName(), Toast.LENGTH_LONG).show();
                    Log.v("long clicked", "pos: " + pos);
                    // Start the CAB using the ActionMode.Callback defined above
                    listViewtodo.setItemChecked(pos, true);
                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                    //listViewtodo.setItemChecked(pos, true);
                    //view.setSelected(true);

                    ListViewItem r ;
                    CheckBox c;

                    for(int i=0;i<listViewtodo.getCount();i++){
                        r =(ListViewItem) listViewtodo.getAdapter().getItem(i);
                        //if(r.getCheckbox()){
                        //    Toast.makeText(getContext(), r.getName() + " " + r.getCheckbox() , Toast.LENGTH_SHORT).show();
                        //}
                        c = (CheckBox) getActivity().findViewById(R.id.checkBox1);
                        c.setEnabled(true);
                    }

                    return true;
                }
            });// Ende erste Version AdapterLongclick

            return todoView;
        }//Ende If TO-DO List-View


        //Arrays für ARCHIVE
        if(getArguments().getInt(ARG_SECTION_NUMBER)== 2){

            View memoView = inflater.inflate(R.layout.fragment_memo, container, false);
            //ListView initialisieren
            listViewmemo = (ListView) memoView.findViewById(R.id.listViewMemo);

            // Ladefunktion der Datenbank
            valuesMemo = loadMemoData();

            adapter2 = new ItemAdapter(getContext(), R.layout.listview_row, valuesMemo);
            listViewmemo.setAdapter(adapter2);
            registerForContextMenu(listViewmemo);
            listViewmemo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            /*
            //Adapter für MemoView
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, valuesMemo);

            //Array mit Adapter verknüpfen
            listViewmemo.setAdapter(adapter2);
            */

            return memoView;
        }//Ende If MEMO List-View


        if (getArguments().getInt(ARG_SECTION_NUMBER)== 3){

            View archiveView = inflater.inflate(R.layout.fragment_archive, container, false);

            //ListView initialisieren
            listViewArchivTodo = (ListView)archiveView.findViewById(R.id.listViewTodoArchive);
            listViewArchivMemo = (ListView)archiveView.findViewById(R.id.listViewMemoArchiv);

            //Ladefunktion der Datenbank
            archiveTodoArray = loadTodoDataArchive();
            adapter3 = new ItemAdapter(getContext(), R.layout.listview_row, archiveTodoArray);
            listViewArchivTodo.setAdapter(adapter3);
            registerForContextMenu(listViewArchivTodo);
            listViewArchivTodo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            archiveMemoArray = loadMemoDataArchive();
            adapter4 = new ItemAdapter(getContext(), R.layout.listview_row, archiveMemoArray);
            listViewArchivMemo.setAdapter(adapter4);
            registerForContextMenu(listViewArchivMemo);
            listViewArchivMemo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            /*
            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, archiveTodoArray);

            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, archiveMemoArray);

            //Array mit Adapter verknüpfen
            listViewArchivTodo.setAdapter(adapter3);
            listViewArchivMemo.setAdapter(adapter4);
            */

            return archiveView;
        }//Ende If ARCHIVE List-View

        return rootView;
    }//Ende onCreateView


    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume(){
        super.onResume();
        setMenuVisibility(true);
    }


    //DB
    // Loadtododata mit ListViewItemObject
    public ArrayList<ListViewItem> loadTodoData() {
        // m_parts.add(new
        ArrayList<ListViewItem> tdData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 0 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex1 = allrows.getColumnIndex("PRIORITY");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");
            //tdData = new ListViewItem[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    //tdData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    tdData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //tdData = new ListViewItem[0];
        }

        return tdData;
    }//Ende loadTodoData


    // LoadMemoDate mit ArrayList Object
    public ArrayList<ListViewItem> loadMemoData() {

        ArrayList<ListViewItem> mmData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 0 " +
                    "order by DATEMEMO, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex1 = allrows.getColumnIndex("DATEMEMO");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    mmData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //mmData = new String[0];
        }
        return mmData;
    }//Ende loadMemoData


    // LoadtododataArchiv mit ListViewItemObject
    public ArrayList<ListViewItem> loadTodoDataArchive() {
        // m_parts.add(new
        ArrayList<ListViewItem> tdData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 1 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex1 = allrows.getColumnIndex("PRIORITY");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");
            //tdData = new ListViewItem[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    //tdData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    tdData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //tdData = new ListViewItem[0];
        }

        return tdData;
    }//Ende loadTodoDataArchiv


    // LoadMemoDataArchiv mit ArrayList Object
    public ArrayList<ListViewItem> loadMemoDataArchive() {

        ArrayList<ListViewItem> mmData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 1 " +
                    "order by DATEMEMO, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex1 = allrows.getColumnIndex("DATEMEMO");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    mmData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //mmData = new String[0];
        }
        return mmData;

    }//Ende loadMemoDataArchiv


    @Override
    public void setMenuVisibility (final boolean visible){
        super.setMenuVisibility(visible);

        if(getActivity() != null){

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    values = loadTodoData();
                    adapter1 = new ItemAdapter(getContext(), R.layout.listview_row, values);
                    listViewtodo.setAdapter(adapter1);
                }
            }

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                    valuesMemo = loadMemoData();
                    adapter2 = new ItemAdapter(getContext(), R.layout.listview_row, valuesMemo);
                    listViewmemo.setAdapter(adapter2);
                }
            }

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

                    archiveTodoArray = loadTodoDataArchive();
                    adapter3 = new ItemAdapter(getContext(), R.layout.listview_row, archiveTodoArray);
                    listViewArchivTodo.setAdapter(adapter3);

                    archiveMemoArray = loadMemoDataArchive();
                    adapter4 = new ItemAdapter(getContext(), R.layout.listview_row, archiveMemoArray);
                    listViewArchivMemo.setAdapter(adapter4);
                }

            }

        }
    }//ENDE setVisibleMenu

    /**
     * MENU
     */
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listViewTodo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //menu.setHeaderTitle([info.position]);
            String[] menuItems = getResources().getStringArray(R.array.longmenu1);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.longmenu1);
        String menuItemName = menuItems[menuItemIndex];
        Toast.makeText(getActivity().getApplicationContext(), "Laaaaanger Click :) " +menuItemName, Toast.LENGTH_LONG).show();
        return true;
    }
*/

    //ActionModeCallback Start
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_delete1, menu);

            if ((getArguments().getInt(ARG_SECTION_NUMBER) == 1) || (getArguments().getInt(ARG_SECTION_NUMBER) == 2)){
                menu.findItem(R.id.delete_selected).setVisible(false);
            }

            //myCheckBox1 = (CheckBox) getActivity().findViewById(R.id.checkBox1);
            //myCheckBox1.setEnabled(true);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.delete_selected:
                    Toast.makeText(getActivity().getApplicationContext(), "Hier kommt es", Toast.LENGTH_LONG).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.move_selected:
                    Toast.makeText(getActivity().getApplicationContext(), "Hier kommt es wieder", Toast.LENGTH_LONG).show();
                    if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                    }
                    else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

                    }
                    else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

                    }
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                default:
                    return false;
            }
        }//ENDE onActionItemClicked


        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                //myCheckBox1.setEnabled(false);
                //myCheckBox1 = null;
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

            }

            mActionMode = null;
        }
    };//Ende ActionMode


    //@Override
    //public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
    //    Toast.makeText(getActivity().getApplicationContext(), "Veränderung", Toast.LENGTH_LONG).show();
    //}
    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String[] menuItems = getResources().get.getStringArray(R.menu.menu_delete1);
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_delete1, menu);
    }
    */

    /* //SelectionAdapter
    class SelectionAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

        public SelectionAdapter(Context context, int resource,
                                int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public void setNewSelection(int position, boolean value) {
            mSelection.put(position, value);
            notifyDataSetChanged();
        }

        public boolean isPositionChecked(int position) {
            Boolean result = mSelection.get(position);
            return result == null ? false : result;
        }

        public Set<Integer> getCurrentCheckedPosition() {
            return mSelection.keySet();
        }

        public void removeSelection(int position) {
            mSelection.remove(position);
            notifyDataSetChanged();
        }

        public void clearSelection() {
            mSelection = new HashMap<Integer, Boolean>();
            notifyDataSetChanged();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            v.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_light)); //default color

            if (mSelection.get(position) != null) {
                v.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
            }
            return v;
        }
    }
    */
}
