package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

/**
 * Created by justisallen on 4/12/15.
 */
public class Category {

    private String name;
    private int iconID;
    private boolean state = true; // If true, show items in this category

    // NO DEFAULT CONSTRUCTOR!

    public Category(String name, int iconID) {
        this.name = name;
        this.iconID = iconID;
    }

    public String getName() {
        return this.name;
    }

    public int getIcon() {
        return this.iconID;
    }

    public boolean getState() {
        return this.state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(int iconID) {
        this.iconID = iconID;
    }

    public void toggleState() {
        this.state = !this.state;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Category deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Category.class);
    }
}
