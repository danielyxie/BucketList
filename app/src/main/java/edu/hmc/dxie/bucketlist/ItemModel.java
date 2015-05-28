package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ItemModel {

    private String itemText;
    private boolean completed;
    private String deadline;
    private double cost;
    private int priority;
    private String duration;
    private String travelDistance;
    private ArrayList<Category> categories;
    private String notes;

    // Default initializes all values. To set them use available mutator methods.
    public ItemModel() {
        this.itemText = "";
        this.completed = false;
        this.deadline = " days";
        this.cost = -1;
        this.priority = 0;
        this.duration = " minutes";
        this.travelDistance = " miles";
        this.categories = new ArrayList<>();
        this.notes = new String();
    }

    // Constructor used for testing purposes only.
    // Takes in a integer as an argument and initializes the name and all parameter
    // values to be that integer
    public ItemModel(int i) {
        this.itemText = Integer.toString(i);
        this.completed = false;
        this.deadline = Integer.toString(i) + " days";
        this.cost = i;
        this.priority = i;
        this.duration = Integer.toString(i) + " minutes";
        this.travelDistance = Integer.toString(i) + " miles";
    }

    // Constructor used for testing purposes only.
    // Takes in a String as an argument and initializes the name of the
    // ItemModel
    public ItemModel(String name) {
        this.itemText = name;
        this.completed = false;
        this.deadline = " days";
        this.cost = -1;
        this.priority = 0;
        this.duration = " minutes";
        this.travelDistance = " miles";
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

    public double getCost() {
        return this.cost;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getTravelDistance() {
        return this.travelDistance;
    }

    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    public String getCategoriesText() {
        String categories = "";
        for (Category category : this.categories) {
            categories += category.getName() + ", ";
        }
        if (!categories.isEmpty()) {
            categories = categories.substring(0, categories.length() - 2);
        }
        return categories;
    }

    public String getNotes() { return this.notes; }

    public boolean hasNoDeadline() {
        return this.deadline.equals(" days") || this.deadline.equals(" weeks") ||
                this.deadline.equals(" months") || this.deadline.equals(" years");
    }

    public boolean hasNoCost() { return this.cost == -1; }

    public boolean hasNoDuration() {
        return this.duration.equals(" minutes") || this.duration.equals(" hours") ||
                this.duration.equals(" days") || this.duration.equals(" weeks") ||
                this.duration.equals(" months") || this.duration.equals(" years");
    }

    public boolean hasNoTravelDistance() {
        return this.travelDistance.equals(" miles") || this.travelDistance.equals(" kilometers");
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
            case "days":
                return deadlineValue;
            case "weeks":
                return deadlineValue * 7.0;
            case "months":
                return deadlineValue * 30.0;
            case "years":
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
        String split[] = this.duration.split(" ");
        double durationValue = Double.parseDouble(split[0]);
        String durationUnit = split[1];

        switch (durationUnit) {
            case "minutes":
                return durationValue;
            case "hours":
                return durationValue * 60.0;
            case "days":
                return durationValue * 1440.0;
            case "weeks":
                return durationValue * 10080.0;
            case "months":
                return durationValue * 43200.0;
            case "years":
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
            case "miles":
                return travelDistanceValue;
            case "kilometers":
                return travelDistanceValue * 0.621371;
            default:
                return 0;
        }
    }

    ////////////////////
    // Mutator (setter) Methods
    ////////////////////

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    public void setDeadline(String number, String unit) {
        this.deadline = number + " " + unit;
    }

    public void setCost(String cost) {
        if (!cost.isEmpty()) {
            this.cost = Double.parseDouble(cost);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDuration(String duration, String unit) {
        this.duration = duration + " " + unit;
    }

    public void setTravelDistance(String travelDistance, String unit) {
        this.travelDistance = travelDistance + " " + unit;
    }

    public void setNotes(String n) {
        this.notes = n;
    }

    // Checks whether the item is in one of the specified categories
    public boolean isInCategory(ArrayList<Category> categories) {

        // Create a shallow copy of the specified categories
        //  because the next method, retainAll, modifies the Container on which it is called,
        ArrayList<Category> shallowCategories = new ArrayList<>(categories);

        // Gets the intersection of the item's categories and the specified categories
        //  and stores the result in the given categories Container
        boolean changed = shallowCategories.retainAll(this.categories);

        // Returns true if the intersection is non-empty,
        //  or if the intersection is empty, but the active categories were empty to begin
        return !shallowCategories.isEmpty() || !changed;
    }

    public void addCategory(Category category) {
        if (!categories.contains(category)) {
            this.categories.add(category);
        }
    }

    public void removeCategory(Category category) {
        if (categories.contains(category)) {
            this.categories.remove(category);
        }
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
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
