package edu.hmc.dxie.bucketlist;

import android.graphics.drawable.Drawable;

/**
 * Created by justisallen on 4/12/15.
 */
public class Category {

    private String name;
    private Drawable icon;
    private boolean state = false; // If true, show items in this category

    // NO DEFAULT CONSTRUCTOR!

    public Category(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public boolean getState() {
        return this.state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void toggleState() {
        this.state = !this.state;
    }
}
