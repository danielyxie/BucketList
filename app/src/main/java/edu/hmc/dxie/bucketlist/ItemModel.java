package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ItemModel {
    
    private String itemText;
    private boolean completed;
    private String deadline;
    private int moneyCost;
    private int priority;
    private int timeCost;
    private String travelDistance;

    // TODO: I think we'll probably want a default constructor in the future
    //          We'll likely want some initial state before we know what parameters are set.
    //          It may also be useful since not every parameter should be required when
    //              creating a new item
    
    public ItemModel(String itemText) {
        this.itemText = itemText;
        this.completed = false;
    }
    
    public String getItemText() {
        return this.itemText;
    }

    // This is necessary for the ArrayAdapter to properly work
    public String toString() {
        return this.itemText;
    }

    public void complete() {
        this.completed = true;
    }
    
    public void uncomplete() {
        this.completed = false;
    }
    
    public boolean getCompleted() { return this.completed; }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ItemModel deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ItemModel.class);
    }
}
