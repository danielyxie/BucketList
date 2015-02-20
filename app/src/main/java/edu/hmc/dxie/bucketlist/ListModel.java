package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class ListModel {
    
    private ArrayList<ItemModel> bucket;
    
    public ListModel() {
        this.bucket = new ArrayList<>();
    }
    
    public void addItem(String itemText) {
        ItemModel item = new ItemModel(itemText);
        this.bucket.add(item);
    }
    
    public void addItem(ItemModel item) {
        this.bucket.add(item);
    }
    
    public ArrayList<ItemModel> getBucket() {
        return this.bucket;
    }
    
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    public static ListModel deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ListModel.class);
    }
}
