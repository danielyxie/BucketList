package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ItemModel {
    
    private String itemText;
    private boolean completed;
    private String deadline;
    private double moneyCost;
    private int priority;
    private String timeCost;
    private String travelDistance;

    // Default initializes all values. To set them use available mutator methods.
    public ItemModel() {
        this.itemText = "";
        this.completed = false;
        this.deadline = "";
        this.moneyCost = -1;
        this.priority = -1;
        this.timeCost = "";
        this.travelDistance = "";
    }
    
    ////////////////////
    // Accessor Methods
    ////////////////////
    
    public String getItemText() {
        return this.itemText;
    }

    public boolean getCompleted() {
        return this.completed;
    }
    
    public String getDeadline() {
        return this.deadline;
    }
    
    public double getMoneyCost() {
        return this.moneyCost;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public String getTimeCost() {
        return this.timeCost;
    }
    
    public String getTravelDistance() {
        return this.travelDistance;
    }
    
    ////////////////////
    // Mutator Methods
    ////////////////////
    
    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public void complete() {
        this.completed = true;
    }
    
    public void uncomplete() {
        this.completed = false;
    }
    
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    
    public void setMoneyCost(double moneyCost) {
        this.moneyCost = moneyCost;        
    }
    
    public void setPriority(int priority) {
        this.priority = priority;        
    }
    
    public void setTimeCost(String timeCost) {
        this.timeCost = timeCost;        
    }
    
    public void setTravelDistance(String travelDistance) {
        this.travelDistance = travelDistance;
    }

    // This is necessary for the ArrayAdapter to properly work
    public String toString() {
        return this.itemText;
    }    

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ItemModel deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ItemModel.class);
    }
}
