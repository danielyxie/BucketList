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

    // Default initializes all values. To set them use available mutator methods.
    public ItemModel() {
        this.itemText = "";
        this.completed = false;
        this.deadline = "";
        this.moneyCost = -1;
        this.priority = -1;
        this.timeCost = -1;
        this.travelDistance = "";
    }
    
    public String getItemText() {
        return this.itemText;
    }
    
    public void setItemText(String itemText) {
        this.itemText = itemText;
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
