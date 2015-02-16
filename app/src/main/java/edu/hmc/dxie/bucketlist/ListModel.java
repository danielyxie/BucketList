package edu.hmc.dxie.bucketlist;

import java.util.ArrayList;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ListModel {
    
    private ArrayList<ItemModel> bucket;
    
    public ListModel() {
        bucket = new ArrayList<ItemModel>();
    }
    
    public void addItem(String itemText) {
        ItemModel item = new ItemModel(itemText);
        bucket.add(item);
    }
    
    public void addItem(ItemModel item) {
        bucket.add(item);
    }
}
