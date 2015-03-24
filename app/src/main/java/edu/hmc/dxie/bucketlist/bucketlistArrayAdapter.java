package edu.hmc.dxie.bucketlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class bucketlistArrayAdapter extends ArrayAdapter<ItemModel> {


    private Context mContext;
    private int id;
    private ArrayList<ItemModel> items ;
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

        View itemView = inflater.inflate(R.layout.bucket_list_item, parent, false);

        TextView text = (TextView) itemView.findViewById(R.id.bucketListText);

        

        if(items.get(position) != null )
        {
            text.setText(items.get(position).toString());
            
            //Following conditional sets the items not relevant to the current search query to
            //invisible.
            if(items.get(position).toString().toLowerCase().contains(query.toLowerCase())){
                text.setVisibility(View.VISIBLE);
            } else {
                text.setVisibility(View.GONE);
            }
            //Here is where to customize the feature that allows users to visually
            //distinguish completed and uncompleted tasks.  It looks really ugly right now
            text.setTextColor(Color.WHITE);
            
            //Following two commands make the boxes bigger (easier to press)
            text.setHeight(100);
            text.setGravity(0x11);
            if(items.get(position).getCompleted()) {
                text.setBackgroundColor(Color.parseColor("#1EBE39")); //green
                if(!accomplishedToggle){
                    text.setVisibility(View.GONE);
                }
            } else {
                text.setBackgroundColor(Color.RED);
                if(accomplishedToggle){
                    text.setVisibility(View.GONE);
                }
            }
        }

        return itemView;
    }

    public void setAccomplishedToggle(boolean toggle){
        this.accomplishedToggle = toggle;      
    }

    public void setSearchQuery(String query) {this.query = query; }

}
