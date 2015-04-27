package edu.hmc.dxie.bucketlist;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by justisallen on 4/14/15.
 */
public class CategoryArrayAdapter extends ArrayAdapter<Category> {

    private Context mContext;
    private int id;
    private ArrayList<Category> items;

    public CategoryArrayAdapter(Context context, int textViewResourceId, ArrayList<Category> list)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        Resources res = mContext.getResources();
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View catView;

        Category currentCat = items.get(position);

        if (currentCat != null) {
            catView = inflater.inflate(R.layout.category, parent, false);

            // Initialize the item name and icon
            ToggleButton button = (ToggleButton) catView.findViewById(R.id.category_toggle);
            button.setTextOff(currentCat.getName());
            button.setTextOn(currentCat.getName());
            button.setButtonDrawable(currentCat.getIconID());

            // Handle ToggleButton clicking
            button.setChecked(currentCat.getState());
            button.setTag(currentCat);
            button.setOnClickListener((ListActivity) mContext);
            return catView;
        }
        return v;
    }
}
