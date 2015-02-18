package edu.hmc.dxie.bucketlist;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ItemModel {
    
    private String item;

    // TODO: I think we'll probably want a default constructor in the future
    //          We'll likely want some initial state before we know what parameters are set.
    //          It may also be useful since not every parameter should be required when
    //              creating a new item
    
    public ItemModel(String item) {
        this.item = item;
    }
    
    public String getItem() {
        return item;
    }
}
