package edu.hmc.dxie.bucketlist;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ItemModel {
    
    private String itemText;
    private boolean completed; // This might not actually be the preferred solution

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
    
    public void complete() {
        this.completed = true;
    }
    
    // This is necessary for the ArrayAdapter to properly work
    public String toString() {
        return this.itemText;
    }
}
