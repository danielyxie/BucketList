package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ListModel {
    
    private ArrayList<ItemModel> bucket = new ArrayList<>();

    /*
     * No explicit constructor necessary because all class fields are assigned a specific value
     * on initialization.
     */

    public int size() { return bucket.size(); }

    public void addItem(ItemModel item) {
        this.bucket.add(item);
    }

    public void addAll(ArrayList<ItemModel> items) { this.bucket.addAll(items); }
    
    public ArrayList<ItemModel> getBucket() {
        return this.bucket;
    }
    
    public ItemModel getItem(int position) {
        return this.bucket.get(position);
    }

    public void setItem(int position, ItemModel newItem) { bucket.set(position, newItem); }

    public void swapItems(int position1, int position2) {
        Collections.swap(bucket, position1, position2);
    }

    public void removeItem(int position) {
        this.bucket.remove(position);
    }

    public void clearItems() { this.bucket.clear(); }
    
    public boolean isEmpty() {return this.bucket.isEmpty(); }
    
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    public static ListModel deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ListModel.class);
    }
}

