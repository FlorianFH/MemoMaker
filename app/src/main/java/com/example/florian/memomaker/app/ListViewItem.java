package com.example.florian.memomaker.app;

import android.widget.CheckBox;

/**
 * Created by Sebastian on 08.03.2016.
 */
public class ListViewItem {

    private int id;
    private String katDat;
    private String itemName;
    private int archiveTag;
    private boolean cBox;


    public ListViewItem() {

    }


    public ListViewItem(int i, String kd, String in, int at) {

        this.id = i;
        this.katDat = kd;
        this.itemName = in;
        this.archiveTag = at;
        if (this.archiveTag == 1) {
            cBox = true;
        }
        else {
            cBox = false;
        }

    }


    public int getId() {
        return id;
    }


    public String getIdToString() {
        return ("" + id);
    }


    public String getName() {
        return itemName;
    }


    public String getKatDat() {
        return katDat;
    }


    public int getArchiveTag() {
        return archiveTag;
    }


    public String getArchiveTagToString() {
        return ("" + archiveTag);
    }


    public void setArchiveTag(int at) {
        this.archiveTag = at;
    }


    public boolean getCheckbox() {
        return cBox;
    }


    public void setCheckbox(boolean checkbox) {
        this.cBox = checkbox;
    }

}
