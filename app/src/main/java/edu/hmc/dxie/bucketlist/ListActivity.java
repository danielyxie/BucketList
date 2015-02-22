package edu.hmc.dxie.bucketlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

// TODO: Consider replacing magic strings with constants in strings.xml
public class ListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListModel bucketModel;
    ListView bucketView;
    bucketlistArrayAdapter bucketArrayAdapter;
    SharedPreferences persistentData;

    public enum RequestCode{
        ADD_ITEM_REQUEST, VIEW_ITEM_REQUEST
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Get persistent data
        persistentData = getSharedPreferences("persistent data", Context.MODE_PRIVATE);
        
        // Get the JSON of the bucketModel if it exists, otherwise get an empty string
        String bucketModelJSON = persistentData.getString("bucket model", "");
        
        // Check whether a bucketModel exists
        if (bucketModelJSON.isEmpty()) {
            
            // If one does not exist, create a new ListModel for holding bucketModel list items
            bucketModel = new ListModel();
        } else {
            
            // Otherwise, recover the preexisting bucketModel
            bucketModel = ListModel.deserialize(bucketModelJSON);
        }

        // Create an ArrayAdapter for the ListView
        bucketArrayAdapter = new bucketlistArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                bucketModel.getBucket());

        // Get the ListView
        bucketView = (ListView) findViewById(R.id.bucketlistview);

        // Set the ListView to use the ArrayAdapter
        bucketView.setAdapter(bucketArrayAdapter);
        
        // Set this activity to react to list items being processed
        bucketView.setOnItemClickListener(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();

        // Serialize the current state of the bucketModel to JSON
        String serializedData = bucketModel.serialize();
                
        // Save the serialized data into a shared preference
        SharedPreferences.Editor dataEditor = persistentData.edit();
        dataEditor.putString("bucket model", serializedData);
        dataEditor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds the "new" and "search" buttons to the action bar.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Add button pushed
        if (id == R.id.menu_item_add) {
            
            // Go to AddActivity
            Intent addItem = new Intent(this, AddActivity.class);
            startActivityForResult(addItem, RequestCode.ADD_ITEM_REQUEST.ordinal());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the request was to add an item
        if (requestCode == RequestCode.ADD_ITEM_REQUEST.ordinal()) {
            
            // If the request went well
            if (resultCode == Activity.RESULT_OK) {
                
                // Get the new item
                String itemText = data.getStringExtra("item text");
                
                // Add the item to the bucketModel
                bucketModel.addItem(itemText);

                // Update the bucketView
                bucketArrayAdapter.notifyDataSetChanged();
            }
            
        // If the request was to view an item
        } else if (requestCode == RequestCode.VIEW_ITEM_REQUEST.ordinal()) {
            
            // If the request went well
            if (resultCode == Activity.RESULT_OK) {
                
                // Get the viewed item's position; default to 0 (should never happen)
                int position = data.getIntExtra("item position", 0);
                
                // Get the button clicked
                String buttonClicked = data.getStringExtra("button clicked");
                
                // If the "Complete" button was clicked
                if (buttonClicked.equals("Complete")) {
                    
                    // Mark the item as completed
                    bucketModel.getItem(position).complete();
                    // TODO: Modify the adapter so that completed items are indicated (green background?)
                    // TODO: ^^ This might not be the best solution. The user story is probably better
                    //          as we'll want that functionality in the future. So we can change this to
                    //          remove the completed item as in delete (though we'll probably want to
                    //          add a completeItem method to ListModel). Maybe store all completed items
                    //          in a separate ArrayList and display them in a separate Activity?
                    //          We'll also need a "View Completed Items" button in this Activity if
                    //          we choose this option.
                    //  On second thought, I think it would be nicer if, instead of a separate activity,
                    //      the user stayed on the current activity, but their accomplished items
                    //      appeared.
                    
                // If the "Delete" button was clicked
                } else if (buttonClicked.equals("Delete")) {
                    
                    // Delete the item
                    bucketModel.removeItem(position);
                }

                // Update the bucketView
                bucketArrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
        // Get the clicked item
        ItemModel clickedItem = bucketModel.getItem(position);

        // Go to ViewItemActivity with the clicked item
        Intent viewItem = new Intent(this, ViewItemActivity.class);
        viewItem.putExtra("item text", clickedItem.toString());
        viewItem.putExtra("item position", position);
        startActivityForResult(viewItem, RequestCode.VIEW_ITEM_REQUEST.ordinal());
    }


    /* Custom ArrayAdapter that will allow the ListView to support several views, formats, and
     * control data assignment.  Essentially, this custom ArrayAdapter will allow us to customize
     * the UI of the ListView.  It simply overrides the getView() function in ArrayAdapter
     */
    private class bucketlistArrayAdapter extends ArrayAdapter<ItemModel> {

        private Context mContext;
        private int id;
        private ArrayList<ItemModel> items ;

        public bucketlistArrayAdapter(Context context, int textViewResourceId , ArrayList<ItemModel> list )
        {
            super(context, textViewResourceId, list);
            mContext = context;
            id = textViewResourceId;
            items = list;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent)
        {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(R.layout.bucket_list_item, parent, false);

            TextView text = (TextView) itemView.findViewById(R.id.bucketListText);

            if(items.get(position) != null )
            {
                Log.d("bucket list debug", items.get(position).toString());

                if(items.get(position).toString() == null ) {
                    Log.d("bucket list debug", "null");
                }

                text.setTextColor(Color.WHITE);
                text.setText(items.get(position).toString());
                //text.setBackgroundColor(Color.RED);
                //int color = Color.argb( 200, 255, 64, 64 );
                //text.setBackgroundColor( color );
            }

            return itemView;
        }

    }
}






