package edu.hmc.dxie.bucketlist;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
                && currentItem.isInCategory(activeCategories)) {
            itemView = inflater.inflate(R.layout.bucket_list_item, parent, false);

            //Get the View Objects so that their background colors can be set
            RelativeLayout boxLayout = (RelativeLayout) itemView.findViewById(R.id.item_box_layout);
            LinearLayout deadlinePriorityLayout
                    = (LinearLayout) itemView.findViewById(R.id.deadlinepriority_layout);
            LinearLayout costDurationLayout
                    = (LinearLayout) itemView.findViewById(R.id.costduration_layout);
            LinearLayout traveldistanceLayout
                    = (LinearLayout) itemView.findViewById(R.id.traveldistance_layout);

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


            //Set the texts of the parameter values
            String deadlineString = currentItem.getDeadline();
            if (currentItem.hasNoDeadline()) {
                deadline_text.setText("----------");
            } else if (deadlineString.length() > 10) {
                deadline_text.setText(deadlineString.substring(0, 10) + "...");
            } else {
                deadline_text.setText(deadlineString);
            }

            priority_text.setText(Integer.toString(currentItem.getPriority()));

            String costString = Double.toString(currentItem.getCost());
            if (currentItem.hasNoCost()) {
                cost_text.setText("----------");
            } else if (costString.length() > 10) {
                cost_text.setText(costString.substring(0, 10) + "...");
            } else {
                cost_text.setText(costString);
            }

            String durationString = currentItem.getDuration();
            if (currentItem.hasNoDuration()) {
                duration_text.setText("----------");
            } else if (durationString.length() > 10) {
                duration_text.setText(durationString.substring(0, 10) + "...");
            } else {
                duration_text.setText(durationString);
            }

            String traveldistanceString = currentItem.getTravelDistance();
            if(currentItem.hasNoTravelDistance()) {
                traveldistance_text.setText("----------");
            } else if (traveldistanceString.length() > 10) {
                traveldistance_text.setText(traveldistanceString.substring(0, 10) + "...");
            } else {
                traveldistance_text.setText(traveldistanceString);
            }

            //Handle the checkbox
            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.item_checkbox_completed);
            checkBox.setChecked(accomplishedToggle);
            checkBox.setTag(currentItem);
            checkBox.setOnClickListener(this);
            
            if(accomplishedToggle) {
                text.setBackgroundColor(res.getColor(R.color.light_blue));
                boxLayout.setBackgroundColor(res.getColor(R.color.light_blue));
                traveldistanceLayout.setBackgroundColor(res.getColor(R.color.light_blue));
                costDurationLayout.setBackgroundColor(res.getColor(R.color.light_blue));
                deadlinePriorityLayout.setBackgroundColor(res.getColor(R.color.light_blue));
                duration_text.setBackgroundColor(res.getColor(R.color.light_blue));
                cost_text.setBackgroundColor(res.getColor(R.color.light_blue));
                deadline_text.setBackgroundColor(res.getColor(R.color.light_blue));
                priority_text.setBackgroundColor(res.getColor(R.color.light_blue));
                traveldistance_text.setBackgroundColor(res.getColor(R.color.light_blue));
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
