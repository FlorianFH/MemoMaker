package com.example.florian.memomaker.app;



/**
 * Created by Sebastian on 08.03.2016.
 */
public class ListViewItem {
    //Variablen
    private int id;
    private String itemType;
    private String datum;
    private String prio;
    private String description;
    private int archiveTag;
    private boolean cBox;

    public final static String TYP_MEMO = "memo";
    public final static String TYP_TODO = "todo";


    //Konstruktor MEMO-Item
    public ListViewItem(int i, String it, String kd, String in, int at){
        this(i,it,kd,"",in,at);
    }


    //Konstruktor TO-DO-Item
    public ListViewItem(int id, String itemType, String datum, String priotitaet, String description, int archiveStatus) {
        this.id = id;
        this.itemType = itemType;
        this.datum = datum;
        this.prio = priotitaet;
        this.description = description;
        this.archiveTag = archiveStatus;
        if (this.archiveTag == 1) cBox = true;
        else cBox = false;
    }

    public int getId() {
        return id;
    }

    public String getIdToString() {
        return ("" + id);
    }

    public String getDescription() {
        return description;
    }

    public String getItemType() {
        return itemType;
    }

    public String getDatum() {
        return datum;
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

    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

}