package com.example.florian.memomaker.app;

/**
 * Created by Sebastian on 09.03.2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ItemAdapter extends ArrayAdapter<ListViewItem> {

    // declaring our ArrayList of items
    private ArrayList<ListViewItem> objects;

    /*
    *here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<ListViewItem> objects,
    * because it is the list of objects we want to display.
    */
    public ItemAdapter(Context context, int textViewResourceId, ArrayList<ListViewItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }


    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_row, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current ListViewItem object.
		 */
        ListViewItem i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView itemID = (TextView) v.findViewById(R.id.id_lvr1);
            TextView tkatdat = (TextView) v.findViewById(R.id.lb_katdat);
            TextView itemDescription = (TextView) v.findViewById(R.id.lb_text1);
            TextView attributeCheckboxValue = (TextView) v.findViewById(R.id.at_cbox_value);
            CheckBox checkBoxButton = (CheckBox) v.findViewById(R.id.checkBox1);


            // check to see if each individual textview is null.
            // if not, assign some text!
            if (itemID != null){
                itemID.setText(i.getIdToString());
            }
            if (tkatdat != null){
                tkatdat.setText(i.getDatum());
            }
            if (itemDescription != null){
                itemDescription.setText(i.getDescription());
            }
            if (attributeCheckboxValue != null){
                attributeCheckboxValue.setText(i.getArchiveTagToString());
            }
            if (checkBoxButton != null){
                if (i.getCheckbox()){
                    checkBoxButton.setChecked(true);
                }else{
                    checkBoxButton.setChecked(false);
                }
            }

        }

        // the view must be returned to our activity
        return v;

    }//Ende getView


    //Additions
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();


    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
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
    }


    public void clearSelection() {

        mSelection = new HashMap<Integer, Boolean>();
    }
}