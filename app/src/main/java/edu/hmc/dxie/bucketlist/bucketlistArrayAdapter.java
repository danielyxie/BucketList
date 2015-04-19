package edu.hmc.dxie.bucketlist;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by justisallen on 2/24/15.
 */
/**
 * Custom ArrayAdapter that will allow the ListView to support several views, formats, and
 * control data assignment.  Essentially, this custom ArrayAdapter will allow us to customize
 * the UI of the ListView.  It simply overrides the getView() function in ArrayAdapter
 */
public class bucketlistArrayAdapter extends ArrayAdapter<ItemModel> implements View.OnClickListener {


    private Context mContext;
    private int id;
    private ArrayList<ItemModel> items;
    private ArrayList<Category> categories;
    private ArrayList<Category> activeCategories;
    private boolean accomplishedToggle; //if true, viewing accomplished items. if false, viewing unaccomplished items
    private String query;

    public bucketlistArrayAdapter(Context context, int textViewResourceId,
                                  ArrayList<ItemModel> list, ArrayList<Category> categories)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
        this.categories = categories;
        activeCategories = getActiveCategories();
        accomplishedToggle = false;
        query = "";
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        Resources res = mContext.getResources();
        
        //A LayoutInflater instantiates a layout XML file into its corresponding View
        //objects.  It can be used to customize how every ItemModel in the bucketlist
        //ListView appears on the ListActivity.
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView;
        
        ItemModel currentItem = items.get(position);

        // Only show list item if:
        //  1) item exists
        //  2) item contains search text
        //  3) item matches the criteria of toggled accomplished tab
        //  4) item is in an active category
        if(currentItem != null
                && currentItem.toString().toLowerCase().contains(query.toLowerCase())
                && currentItem.getCompleted() == accomplishedToggle
                && currentItem.isInCategory(activeCategories))
        {
            itemView = inflater.inflate(R.layout.bucket_list_item, parent, false);

            //Initialize the item name
            TextView text = (TextView) itemView.findViewById(R.id.bucketListText);
            text.setText(currentItem.toString());

            //Initialize the item parameters
            TextView deadline_text = (TextView) itemView.findViewById(R.id.item_deadline);
            TextView priority_text = (TextView) itemView.findViewById(R.id.item_priority);
            TextView cost_text = (TextView) itemView.findViewById(R.id.item_cost);
            TextView duration_text = (TextView) itemView.findViewById(R.id.item_duration);
            TextView traveldistance_text =
                                     (TextView) itemView.findViewById(R.id.item_traveldistance);

            deadline_text.setText(currentItem.getDeadline());
            priority_text.setText(Integer.toString(currentItem.getPriority()));
            cost_text.setText(Double.toString(currentItem.getCost()));
            duration_text.setText(currentItem.getDuration());
            traveldistance_text.setText(currentItem.getTravelDistance());

            //Handle the checkbox
            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.item_checkbox_completed);
            checkBox.setChecked(accomplishedToggle);
            checkBox.setTag(currentItem);
            checkBox.setOnClickListener(this);
            
            if(accomplishedToggle) {
                text.setBackgroundColor(res.getColor(R.color.light_blue));
            } else {
                text.setBackgroundColor(res.getColor(R.color.white));
            }
        } else {
            itemView = inflater.inflate(R.layout.empty_list_item, parent, false);
        }

        return itemView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        activeCategories = getActiveCategories();
    }

    public void setAccomplishedToggle(boolean toggle){
        this.accomplishedToggle = toggle;      
    }

    public void setSearchQuery(String query) {this.query = query; }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_checkbox_completed:
                ItemModel item = (ItemModel) v.getTag();
                item.toggleCompleted();
                notifyDataSetChanged();
                break;
        }
    }

    private ArrayList<Category> getActiveCategories() {
        ArrayList<Category> activeCategories = new ArrayList<>();

        // Iterate through all of the categories
        for (Category category : categories) {

            // Only add active categories
            if (category.getState()) {
                activeCategories.add(category);
            }
        }
        return activeCategories;
    }
}
