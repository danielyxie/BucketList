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

    // Constructor used for testing purposes only.
    // Takes in a integer as an argument and initializes the name and all parameter
    // values to be that integer
    public ItemModel(int i) {
        this.itemText = Integer.toString(i);
        this.completed = false;
        this.deadline = Integer.toString(i) + " day(s)";
        this.moneyCost = i;
        this.priority = i;
        this.timeCost = Integer.toString(i) + " minute(s)";
        this.travelDistance = Integer.toString(i) + " mile(s)";
    }

    // Constructor used for testing purposes only.
    // Takes in a String as an argument and initializes the name of the
    // ItemModel
    public ItemModel(String name) {
        this.itemText = name;
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

    ////////////////////////////
    // Accessor Methods for Sort
    ////////////////////////////

    //Convert deadline to days, and cast it as a double
    public double getDeadlineForSort(){
        String split[] = this.deadline.split(" ");
        double deadlineValue = Double.parseDouble(split[0]);
        String deadlineUnit = split[1];

        switch (deadlineUnit) {
            case "day(s)":
                return deadlineValue;
            case "week(s)":
                return deadlineValue * 7.0;
            case "month(s)":
                return deadlineValue * 30.0;
            case "year(s)":
                return deadlineValue * 365.0;
            default:
                return 0;
        }
    }


    //Cast priority as a double
    public double getPriorityForSort(){
        return (double)this.priority;
    }

    //convert duration to minutes, and cast it as a double
    //TO-DO: Need some case for handling int overflow
    public double getDurationForSort(){
        String split[] = this.timeCost.split(" ");
        double durationValue = Double.parseDouble(split[0]);
        String durationUnit = split[1];

        switch (durationUnit) {
            case "minute(s)":
                return durationValue;
            case "hour(s)":
                return durationValue * 60.0;
            case "day(s)":
                return durationValue * 1440.0;
            case "week(s)":
                return durationValue * 10080.0;
            case "month(s)":
                return durationValue * 43200.0;
            case "year(s)":
                return durationValue * 525949.0;
            default:
                return 0;
        }
    }

    //convert travel distance to miles, and cast it as a double
    public double getTravelDistanceForSort(){
        String split[] = this.travelDistance.split(" ");
        double travelDistanceValue = Double.parseDouble(split[0]);
        String travelDistanceUnit = split[1];

        switch (travelDistanceUnit) {
            case "mile(s)":
                return travelDistanceValue;
            case "kilometer(s)":
                return travelDistanceValue * 0.621371;
            default:
                return 0;
        }
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

    public void setDeadline(String number, String unit) {
        this.deadline = number + " " + unit;
    }

    public void setMoneyCost(String moneyCost) {
        if (!moneyCost.isEmpty()) {
            this.moneyCost = Double.parseDouble(moneyCost);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTimeCost(String timeCost, String unit) {
        this.timeCost = timeCost + " " + unit;
    }

    public void setTimeCost(String timeCost) {
        this.timeCost = timeCost;
    }

    public void setTravelDistance(String travelDistance, String unit) {
        this.travelDistance = travelDistance + " " + unit;
    }

    @Override
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
