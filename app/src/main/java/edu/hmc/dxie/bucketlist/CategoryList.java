package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by justisallen on 4/13/15.
 */
public class CategoryList {

    private ArrayList<Category> categories = new ArrayList<>();

    /*
     * No explicit constructor necessary because all class fields are assigned a specific value
     * on initialization.
     */

    public ArrayList<Category> getCategories() {
        return this.categories;
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

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static CategoryList deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CategoryList.class);
    }
}
