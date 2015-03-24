package edu.hmc.dxie.bucketlist;

import android.content.Context;
import android.graphics.Color;
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
    private boolean accomplishedToggle; //if true, viewing accomplished items. if false, viewing unaccomplished items
    private String query;

    public bucketlistArrayAdapter(Context context, int textViewResourceId , ArrayList<ItemModel> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
        accomplishedToggle = false;
        query = "";
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
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
        if(currentItem != null
                && currentItem.toString().toLowerCase().contains(query.toLowerCase())
                && currentItem.getCompleted() == accomplishedToggle)
        {
            itemView = inflater.inflate(R.layout.bucket_list_item, parent, false);

            TextView text = (TextView) itemView.findViewById(R.id.bucketListText);
            text.setText(currentItem.toString());

            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.item_checkbox_completed);
            checkBox.setChecked(accomplishedToggle);
            checkBox.setTag(currentItem);
            checkBox.setOnClickListener(this);

            //Here is where to customize the feature that allows users to visually
            //distinguish completed and uncompleted tasks.  It looks really ugly right now
            text.setTextColor(Color.WHITE);
            
            if(accomplishedToggle) {
                text.setBackgroundColor(Color.parseColor("#1EBE39")); //green
            } else {
                text.setBackgroundColor(Color.RED);
            }
        } else {
            itemView = inflater.inflate(R.layout.empty_list_item, parent, false);
        }

        return itemView;
    }

    public void setAccomplishedToggle(boolean toggle){
        this.accomplishedToggle = toggle;      
    }

    public void setSearchQuery(String query) {this.query = query; }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_checkbox_completed:
                CheckBox checkBox = (CheckBox) v;
                ItemModel item = (ItemModel) checkBox.getTag();
                if (checkBox.isChecked()) {
                    item.complete();
                } else {
                    item.uncomplete();
                } notifyDataSetChanged();
                break;
        }
    }
}
