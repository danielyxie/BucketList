package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

/**
 * Created by justisallen on 4/12/15.
 */
public class Category {

    private String name;
    private int icon;
    private boolean state = false; // If true, show items in this category

    // NO DEFAULT CONSTRUCTOR!

    public Category(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public int getIcon() {
        return this.icon;
    }

    public boolean getState() {
        return this.state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
