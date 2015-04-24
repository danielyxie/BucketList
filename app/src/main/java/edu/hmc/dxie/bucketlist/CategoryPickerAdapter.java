package edu.hmc.dxie.bucketlist;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

/**
 * Created by justisallen on 4/24/15.
 */
public class CategoryPickerAdapter extends ArrayAdapter<Category> {

    private Context mContext;
    private int id;
    private ArrayList<Category> items;
    private ArrayList<Category> itemCats;

    public CategoryPickerAdapter(Context context, int textViewResourceId, ArrayList<Category> list, ArrayList<Category> itemCats)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
        this.itemCats = itemCats;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        Resources res = mContext.getResources();
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View catView;

        Category currentCat = items.get(position);

        if (currentCat != null) {
            catView = inflater.inflate(R.layout.category_picker_item, parent, false);

            // Initialize the item name and icon
            ((CheckedTextView) catView).setText(currentCat.getName());
            ((CheckedTextView) catView).setCompoundDrawablesWithIntrinsicBounds(currentCat.getIconID(), 0, 0, 0);
            ((CheckedTextView) catView).setChecked( itemCats.contains(currentCat) );
            return catView;
        }
        return v;
    }
}
