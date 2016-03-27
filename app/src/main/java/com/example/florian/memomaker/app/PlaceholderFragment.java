package com.example.florian.memomaker.app;



import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;



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

    // Arraylists für Werte der Listviews
    ArrayList<ListViewItem> values;
    ArrayList<ListViewItem> valuesMemo;
    ArrayList<ListViewItem> archiveTodoArray;
    ArrayList<ListViewItem> archiveMemoArray;

    // SQL Variablen
    DBManager dbManager;

    // Variable Tabnummer
    public static final String ARG_SECTION_NUMBER = "section_number";

    // Variable für Actionmode
    public ActionMode mActionMode;

    // Adaptervariablen für CustomAdapter
    ItemAdapter adapter1;
    ItemAdapter adapter2;
    ItemAdapter adapter3;
    ItemAdapter adapter4;

    // Customviewpager, ermöglicht deaktivieren von
    // Blättern zwischen Tabs
    CustomViewPager customViewPager;

    // Controllvariable um festzustellen ob bereits ein Eintrag im
    // Actionmode angeklickt/markiert wurde
    public static boolean actionModeCheckedExists = false;

    // Variable zum Zwischenspeichern der angeklickten Position, u.a.
    // um die Markierung entfernen zu können wenn was neues angeklickt wurde
    public static int itemPosSave = -1;

    // Fuer Section 3, zwischenspeichern des Itemtyps, da zwei
    // Listviews mit verschiedenen Items
    public static String itemType;

    // Flags zur bestimmung ob jeweils ein Eintrag in der anderen Listview
    // angeklickt/markiert wurde. In diesem Fall wird eine Markierung in der
    // aktuellen Listview nicht zugelassen. Workaround, da der Uncheck auf die
    // andere Listview nicht sauber funktionierte
    public static boolean lv1Activated = false;
    public static boolean lv2Activated = false;

    // Variable zum Zwischenspeichern der ID des zu löschenden Datensatzes
    // da das Löschen aus der DB in der DialogActivity Klasse erfolgt
    private static int delDatasetID = -1;


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

        final View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
        customViewPager = (CustomViewPager) getActivity().findViewById(R.id.container);

        //Erzeugen des DatenbankManager
        dbManager = new DBManager(getContext());

        //TAB To-Do
        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){

            View todoView = inflater.inflate(R.layout.fragment_todo, container, false);
            listViewtodo = (ListView) todoView.findViewById(R.id.listViewTodo);


            //Laden der Daten für die aktuelle Listview aus der DB
            values = dbManager.loadTodoData();

            // Setzen des CustomAdapters
            adapter1 = new ItemAdapter(getContext(), R.layout.listview_row, values);
            listViewtodo.setAdapter(adapter1);

            // Registrierung und setzen des Auswahlmodus
            registerForContextMenu(listViewtodo);
            listViewtodo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Listener für Click auf Listvieweintrag
            listViewtodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    // Prüfen ob Actionmode aktiv, sonst nichts tun
                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewtodo.getItemAtPosition(pos);

                        // Prüfen ob Checkbox angeclickt
                        if (lvi.getCheckbox()) {
                            // Wenn checked, dann uncheck
                            lvi.setCheckbox(false);
                            // Kontrollvariable und Zwischengespeicherte
                            // Itemposition zurücksetzen
                            actionModeCheckedExists = false;
                            itemPosSave = -1;
                        } else {
                            // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                            if (!actionModeCheckedExists) {
                                // Falls kein anderer Eintrag markiert ist, check ausführen,
                                // Kontrollvariable auf true setzen und Position zwischenspeichern
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            } else {
                                // Falls ein anderer Eintrag markiert ist, erst uncheck
                                // auf alte Position, dann check auf die neue ausführen,
                                // Kontrollvariable auf true setzen und Position zwischenspeichern
                                ListViewItem lviOldPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(false);

                                // Aktuelle Position check
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            }
                        }
                        listViewtodo.setItemChecked(pos, true);
                    }
                }
            });

            // Listener für LongClick auf Listvieweintrag
            listViewtodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    // Prüfen ob Actionmode bereits aktiv, falls ja
                    // dann nichts tun
                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewtodo.getItemAtPosition(pos);

                    // Prüfen ob Checkbox angeclickt
                    if (lvi.getCheckbox()) {
                        // Wenn checked, dann uncheck
                        lvi.setCheckbox(false);
                        // Kontrollvariable und Zwischengespeicherte
                        // Itemposition zurücksetzen
                        actionModeCheckedExists = false;
                        itemPosSave = -1;
                    }
                    else {
                        // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                        if(!actionModeCheckedExists) {
                            // Falls kein anderer Eintrag markiert ist, check ausführen,
                            // Kontrollvariable auf true setzen und Position zwischenspeichern
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        }
                        else {
                            // Falls ein anderer Eintrag markiert ist, erst uncheck
                            // auf alte Position, dann check auf die neue ausführen,
                            // Kontrollvariable auf true setzen und Position zwischenspeichern
                            ListViewItem lviOldPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(false);

                            // Aktuelle Position check
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        }
                    }

                    listViewtodo.setItemChecked(pos, true);
                    // Start des Actionmode
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;

                }
            });

            return todoView;
        }//Ende If TO-DO List-View


        //Arrays für ARCHIVE
        if(getArguments().getInt(ARG_SECTION_NUMBER)== 2){

            View memoView = inflater.inflate(R.layout.fragment_memo, container, false);
            listViewmemo = (ListView) memoView.findViewById(R.id.listViewMemo);

            //Laden der Daten für die aktuelle Listview aus der DB
            valuesMemo = dbManager.loadMemoData();

            // Setzen des CustomAdapters
            adapter2 = new ItemAdapter(getContext(), R.layout.listview_row, valuesMemo);
            listViewmemo.setAdapter(adapter2);

            // Registrierung und setzen des Auswahlmodus
            registerForContextMenu(listViewmemo);
            listViewmemo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Listener für Click auf Listvieweintrag
            listViewmemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    // Prüfen ob Actionmode aktiv, sonst nichts tun
                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewmemo.getItemAtPosition(pos);

                        // Prüfen ob Checkbox angeclickt
                        if (lvi.getCheckbox()) {
                            // Wenn checked, dann uncheck
                            lvi.setCheckbox(false);
                            // Kontrollvariable und Zwischengespeicherte
                            // Itemposition zurücksetzen
                            actionModeCheckedExists = false;
                            itemPosSave = -1;
                        } else {
                            // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                            if (!actionModeCheckedExists) {
                                // Falls kein anderer Eintrag markiert ist, check ausführen,
                                // Kontrollvariable auf true setzen und Position zwischenspeichern
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            } else {
                                // Falls ein anderer Eintrag markiert ist, erst uncheck
                                // auf alte Position, dann check auf die neue ausführen,
                                // Kontrollvariable auf true setzen und Position zwischenspeichern
                                ListViewItem lviOldPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(false);

                                // Aktuelle Position check
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            }
                        }
                        listViewmemo.setItemChecked(pos, true);
                    }
                }
            });

            // Listener für LongClick auf Listvieweintrag
            listViewmemo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    // Prüfen ob Actionmode bereits aktiv, falls ja
                    // dann nichts tun
                    if (mActionMode != null) {
                        return false;
                    }
                    ListViewItem lvi = (ListViewItem) listViewmemo.getItemAtPosition(pos);

                    // Prüfen ob Checkbox angeclickt
                    if (lvi.getCheckbox()) {
                        // Wenn checked, dann uncheck
                        lvi.setCheckbox(false);
                        // Kontrollvariable und Zwischengespeicherte
                        // Itemposition zurücksetzen
                        actionModeCheckedExists = false;
                        itemPosSave = -1;
                    } else {
                        // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                        if (!actionModeCheckedExists) {
                            // Falls kein anderer Eintrag markiert ist, check ausführen,
                            // Kontrollvariable auf true setzen und Position zwischenspeichern
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        } else {
                            // Falls ein anderer Eintrag markiert ist, erst uncheck
                            // auf alte Position, dann check auf die neue ausführen,
                            // Kontrollvariable auf true setzen und Position zwischenspeichern
                            ListViewItem lviOldPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(false);

                            // Aktuelle Position check
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        }
                    }
                    listViewmemo.setItemChecked(pos, true);

                    // Start des Actionmode
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;
                }
            });

            return memoView;
        }//Ende If MEMO List-View

        if (getArguments().getInt(ARG_SECTION_NUMBER)== 3){

            View archiveView = inflater.inflate(R.layout.fragment_archive, container, false);

            //ListViews initialisieren
            listViewArchivTodo = (ListView)archiveView.findViewById(R.id.listViewTodoArchive);
            listViewArchivMemo = (ListView)archiveView.findViewById(R.id.listViewMemoArchive);

            // Routinen fuer erste Listview
            //Laden der Daten für die aktuelle Listview aus der DB
            archiveTodoArray = dbManager.loadTodoDataArchive();

            // Setzen des CustomAdapters
            adapter3 = new ItemAdapter(getContext(), R.layout.listview_row, archiveTodoArray);
            listViewArchivTodo.setAdapter(adapter3);

            // Registrierung und setzen des Auswahlmodus
            registerForContextMenu(listViewArchivTodo);
            listViewArchivTodo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Listener für Click auf Listvieweintrag
            listViewArchivTodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    // Prüfen ob Actionmode aktiv und andere Listview
                    // nicht aktiv, sonst nichts tun
                    if (mActionMode != null && !lv2Activated) {

                        ListViewItem lvi = (ListViewItem) listViewArchivTodo.getItemAtPosition(pos);

                        // Prüfen ob Checkbox unchecked
                        if (!lvi.getCheckbox()) {
                            // Wenn unchecked, dann check
                            lvi.setCheckbox(true);
                            // Kontrollvariablen und Zwischengespeicherte
                            // Itemposition und Itemtyp zurücksetzen
                            actionModeCheckedExists = false;
                            lv1Activated = false;
                            itemPosSave = -1;
                            itemType = null;
                        } else {
                            // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                            if (!actionModeCheckedExists) {
                                // Falls kein anderer Eintrag markiert ist, uncheck ausführen,
                                // Kontrollvariablen auf true setzen, Position und
                                // Itemtyp zwischenspeichern
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                lv1Activated = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            } else {
                                // Falls ein anderer Eintrag markiert ist, erst check
                                // auf alte Position, dann uncheck auf die neue ausführen,
                                // Kontrollvariablen auf true setzen, Itemtyp und
                                // Position zwischenspeichern
                                ListViewItem lviOldPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(true);

                                // Aktuelle Position uncheck
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                lv1Activated = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            }
                        }
                        listViewArchivTodo.setItemChecked(pos, true);
                    }
                }
            });

            // Listener für LongClick auf Listvieweintrag
            listViewArchivTodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    // Prüfen ob Actionmode bereits aktiv, falls ja
                    // dann nichts tun
                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewArchivTodo.getItemAtPosition(pos);

                    // Prüfen ob Checkbox unchecked
                    if (!lvi.getCheckbox()) {
                        // Wenn unchecked, dann check
                        lvi.setCheckbox(true);
                        // Kontrollvariablen und Zwischengespeicherte
                        // Itemposition und Itemtyp zurücksetzen
                        actionModeCheckedExists = false;
                        lv1Activated = false;
                        itemPosSave = -1;
                        itemType = null;
                    } else {
                        // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                        if (!actionModeCheckedExists) {
                            // Falls kein anderer Eintrag markiert ist, uncheck ausführen,
                            // Kontrollvariablen auf true setzen, Position und
                            // Itemtyp zwischenspeichern
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            lv1Activated = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        } else {
                            // Falls ein anderer Eintrag markiert ist, erst check
                            // auf alte Position, dann uncheck auf die neue ausführen,
                            // Kontrollvariablen auf true setzen, Itemtyp und
                            // Position zwischenspeichern
                            ListViewItem lviOldPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(true);

                            // Aktuelle Position uncheck
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            lv1Activated = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        }
                    }
                    listViewArchivTodo.setItemChecked(pos, true);
                    // Start des Actionmode
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;
                }
            });

            // Routinen fuer zweite Listview
            //Laden der Daten für die aktuelle Listview aus der DB
            archiveMemoArray = dbManager.loadMemoDataArchive();

            // Setzen des CustomAdapters
            adapter4 = new ItemAdapter(getContext(), R.layout.listview_row, archiveMemoArray);
            listViewArchivMemo.setAdapter(adapter4);

            // Registrierung und setzen des Auswahlmodus
            registerForContextMenu(listViewArchivMemo);
            listViewArchivMemo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Listener für Click auf Listvieweintrag
            listViewArchivMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {

                    // Prüfen ob Actionmode aktiv und andere Listview
                    // nicht aktiv, sonst nichts tun
                    if (mActionMode != null  && !lv1Activated) {

                        ListViewItem lvi = (ListViewItem) listViewArchivMemo.getItemAtPosition(pos);

                        // Prüfen ob Checkbox unchecked
                        if (!lvi.getCheckbox()) {
                            // Wenn unchecked, dann check
                            lvi.setCheckbox(true);
                            // Kontrollvariablen und Zwischengespeicherte
                            // Itemposition und Itemtyp zurücksetzen
                            actionModeCheckedExists = false;
                            lv2Activated = false;
                            itemPosSave = -1;
                            itemType = null;
                        } else {
                            // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                            if (!actionModeCheckedExists) {
                                // Falls kein anderer Eintrag markiert ist, uncheck ausführen,
                                // Kontrollvariablen auf true setzen, Position und
                                // Itemtyp zwischenspeichern
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                lv2Activated = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            } else {
                                // Falls ein anderer Eintrag markiert ist, erst check
                                // auf alte Position, dann uncheck auf die neue ausführen,
                                // Kontrollvariablen auf true setzen, Itemtyp und
                                // Position zwischenspeichern
                                ListViewItem lviOldPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(true);

                                // Aktuelle Position uncheck
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                lv2Activated = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            }
                        }
                        listViewArchivMemo.setItemChecked(pos, true);
                    }
                }
            });

            // Listener für LongClick auf Listvieweintrag
            listViewArchivMemo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    // Prüfen ob Actionmode bereits aktiv, falls ja
                    // dann nichts tun
                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewArchivMemo.getItemAtPosition(pos);

                    // Prüfen ob Checkbox unchecked
                    if (!lvi.getCheckbox()) {
                        // Wenn unchecked, dann check
                        lvi.setCheckbox(true);
                        // Kontrollvariablen und Zwischengespeicherte
                        // Itemposition und Itemtyp zurücksetzen
                        actionModeCheckedExists = false;
                        lv2Activated = false;
                        itemPosSave = -1;
                        itemType = null;
                    } else {
                        // Prüfen ob bereits ein Eintrag angeclickt/markiert wurde
                        if (!actionModeCheckedExists) {
                            // Falls kein anderer Eintrag markiert ist, uncheck ausführen,
                            // Kontrollvariablen auf true setzen, Position und
                            // Itemtyp zwischenspeichern
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            lv2Activated = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        } else {
                            // Falls ein anderer Eintrag markiert ist, erst check
                            // auf alte Position, dann uncheck auf die neue ausführen,
                            // Kontrollvariablen auf true setzen, Itemtyp und
                            // Position zwischenspeichern
                            ListViewItem lviOldPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(true);

                            // Aktuelle Position uncheck
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            lv2Activated = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        }
                    }
                    listViewArchivMemo.setItemChecked(pos, true);
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;
                }
            });

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


    // Funktion zum aktualisieren der Views nach Neuanlage,
    // Verschieben oder Löschen. Wird im onResume() aufgerufen
    @Override
    public void setMenuVisibility (final boolean visible){
        super.setMenuVisibility(visible);

        if(getActivity() != null){

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    values = dbManager.loadTodoData();

                    adapter1 = new ItemAdapter(getContext(), R.layout.listview_row, values);
                    listViewtodo.setAdapter(adapter1);
                }
            }

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                    valuesMemo = dbManager.loadMemoData();

                    adapter2 = new ItemAdapter(getContext(), R.layout.listview_row, valuesMemo);
                    listViewmemo.setAdapter(adapter2);
                }
            }

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

                    archiveTodoArray = dbManager.loadTodoDataArchive();

                    adapter3 = new ItemAdapter(getContext(), R.layout.listview_row, archiveTodoArray);
                    listViewArchivTodo.setAdapter(adapter3);

                    archiveMemoArray = dbManager.loadMemoDataArchive();

                    adapter4 = new ItemAdapter(getContext(), R.layout.listview_row, archiveMemoArray);
                    listViewArchivMemo.setAdapter(adapter4);

                }
            }
        }
    }//Ende setMenuVisible

    /**
     * MENU
     */

    //ActionModeCallback Start
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_delete1, menu);

            // Blättern im Actionmode verhindern
            customViewPager.setPagingEnabled(false);

            // Löschicon nur im Archiv Actionmode anzeigen
            if ((getArguments().getInt(ARG_SECTION_NUMBER) == 1) || (getArguments().getInt(ARG_SECTION_NUMBER) == 2)){
                menu.findItem(R.id.delete_selected).setVisible(false);
            }

            // Floatingactionbutton ausblenden im Actionmode
            FloatingActionsMenu fabm = (FloatingActionsMenu) getActivity().findViewById(R.id.left_labels);
            fabm.setVisibility(View.INVISIBLE);

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
                    if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                        ListViewItem lviPos = null;
                        if (itemType.equals(ListViewItem.TYP_TODO)) {
                            lviPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            } else {
                                lviPos.setArchiveTag(0);
                            }
                        } else {
                            lviPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            } else {
                                lviPos.setArchiveTag(0);
                            }
                        }

                        // Zu löschenden Datensatz zwischenspeichern
                        PlaceholderFragment.delDatasetID = lviPos.getId();

                        // Löschbestätigunsdialog einblenden
                        confirmDeleteDs();

                    }
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.move_selected:
                    if (itemPosSave != -1) {
                        ListViewItem lviPos = null;
                        // Holen des Checked Status für die Checkboxes
                        // abhängig von Section und setzen der Archivtags
                        // für die Datenbank
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                            lviPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            }
                            else {
                                lviPos.setArchiveTag(0);
                            }
                        }
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                            lviPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            }
                            else {
                                lviPos.setArchiveTag(0);
                            }
                        }
                        // Hier zwei Listviews, von daher nochmal Unterscheidung
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                            if (itemType.equals("todo")) {
                                lviPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                                if (lviPos.getCheckbox()) {
                                    lviPos.setArchiveTag(1);
                                }
                                else {
                                    lviPos.setArchiveTag(0);
                                }
                            }
                            else {
                                lviPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                                if (lviPos.getCheckbox()) {
                                    lviPos.setArchiveTag(1);
                                }
                                else {
                                    lviPos.setArchiveTag(0);
                                }
                            }
                        }

                        if (lviPos != null) {
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            } else {
                                lviPos.setArchiveTag(0);
                            }
                            updateTable(lviPos.getId(), lviPos.getArchiveTag());
                        }
                    }
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {

            // Alte Position uncheck
            if (itemPosSave != -1) {
                ListViewItem lviOldPos;
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    lviOldPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                    lviOldPos.setCheckbox(false);
                }
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                    lviOldPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                    lviOldPos.setCheckbox(false);
                }
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                    if (itemType.equals("todo")) {
                        lviOldPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                        lviOldPos.setCheckbox(false);
                    }
                    else {
                        lviOldPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                        lviOldPos.setCheckbox(false);
                    }
                    itemType = null;


                }


            }
            // Blättern zwischen Tabs wieder erlauben
            customViewPager.setPagingEnabled(true);
            // Floatingactionbutton wieder sichtbar machen
            FloatingActionsMenu fabm = (FloatingActionsMenu) getActivity().findViewById(R.id.left_labels);
            fabm.setVisibility(View.VISIBLE);

            // Actionmode, Kontrollvariablen und Position zurücksetzen
            mActionMode = null;
            actionModeCheckedExists = false;
            lv1Activated = false;
            lv2Activated = false;
            itemPosSave = -1;

            // Manuelles refresh nach Verschieben
            setMenuVisibility(true);
        }
    };

    //Ende ActionMode



    public void updateTable(int id, int arch) {
        try {
            SQLiteDatabase mydb;
            String DBMEMO = "memomaker.db";
            String TABLE = "mmdata";
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("UPDATE " + TABLE + " SET ARCHIVE = " + arch + " WHERE ID = " +id);
            mydb.close();

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                Toast.makeText(getContext(), "Datensatz wiederhergestellt", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(), "Ins Archiv verschoben", Toast.LENGTH_LONG).show();
            }

        } catch(Exception e) {
            Toast.makeText(getContext(), "Fehler beim Schreiben in die Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende updateTable


    public void confirmDeleteDs() {
        // DialogActivity starten um Löschvorgang zu bestätigen
        Intent i = new Intent(getActivity(), DialogActivity.class);
        startActivity(i);
    }


    public static int getDelDatasetID() {
        return PlaceholderFragment.delDatasetID;
    }


    public static void setDelDatasetID(int id) {
        PlaceholderFragment.delDatasetID = id;
    }

}