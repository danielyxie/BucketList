package edu.hmc.dxie.bucketlist;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by justisallen on 4/13/15.
 */
public class CategoryList {

    private ArrayList<Category> categories = new ArrayList<>();

    public CategoryList(String[] names, int[] iconIDs) {

        // Assert that the arrays have equal length
        if (BuildConfig.DEBUG && names.length != iconIDs.length) {
            throw new AssertionError("The arrays in the array CategoryList constructor do not have equal length!");
        }

        // If so, add the categories
        for (int i = 0; i < names.length; i++) {
            addCategory(new Category(names[i], iconIDs[i]));
        }
    }

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
