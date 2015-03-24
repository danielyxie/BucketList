package edu.hmc.dxie.bucketlist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

// TODO: Consider replacing magic strings with constants in strings.xml
public class ListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
                                                               View.OnClickListener,
                                                               SearchView.OnQueryTextListener{

    ListModel bucketModel;
    ListView bucketView;
    bucketlistArrayAdapter bucketArrayAdapter;
    SharedPreferences persistentData;
    SearchView searchView;




    public enum RequestCode{
        ADD_ITEM_REQUEST, VIEW_ITEM_REQUEST
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

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
        
        // Set greeting
        setGreeting();
       
        // Set defaults for toggle/"tab" buttons
        ToggleButton unaccomplishedToggle = (ToggleButton) findViewById(R.id.list_toggle_unaccomplished);
        ToggleButton accomplishedToggle = (ToggleButton) findViewById(R.id.list_toggle_accomplished);
        unaccomplishedToggle.setChecked(true);
        accomplishedToggle.setChecked(false);
        unaccomplishedToggle.setOnClickListener(this);
        accomplishedToggle.setOnClickListener(this);
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


    /*
     * Initializes the Action Bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds the "new" and "search" buttons to the action bar.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        // Sets up the searchView widget for Searching through tasks.
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search for an item!");

        return true;
    }


    /*
     * This method implements what happens when the buttons on the Action Bar are pressed.
     */
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


    /*
     * This method handles what happens when the application returns to the ListActivity.
     * It manages and processes Intents from the AddActivity and the ViewItemActivity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the request was to add an item
        if (requestCode == RequestCode.ADD_ITEM_REQUEST.ordinal()) {
            
            // If the request went well
            if (resultCode == Activity.RESULT_OK) {
                
                // Recover the new item
                String serializedItem = data.getStringExtra("item");
                ItemModel newItem = ItemModel.deserialize(serializedItem);
                
                // Add the item to the bucketModel
                bucketModel.addItem(newItem);

                // Update the bucketView
                bucketArrayAdapter.notifyDataSetChanged();
            }
            
        // If the request was to view an item
        } else if (requestCode == RequestCode.VIEW_ITEM_REQUEST.ordinal()) {

            // If the request went well
            if (resultCode == Activity.RESULT_OK) {
                
                // Get the viewed item's position; default to 0 (should never happen)
                int position = data.getIntExtra("item position", 0);

                // Get the serialized item
                String serializedItem = data.getStringExtra("item");
                ItemModel viewedItem = ItemModel.deserialize(serializedItem);
                bucketModel.setItem(position, viewedItem);
                
                // Get the button clicked
                String buttonClicked = data.getStringExtra("button clicked");

                switch (buttonClicked) {
                    case "Complete":

                        // Mark the item as completed
                        bucketModel.getItem(position).complete();
                        break;

                    case "Uncomplete":

                        // Mark the item as not completed
                        bucketModel.getItem(position).uncomplete();
                        break;

                    case "Edit":
                        break;

                    case "Delete":
                        // Delete the item
                        bucketModel.removeItem(position);
                        break;

                    default:
                        break;
                }

                // Update the bucketView
                bucketArrayAdapter.notifyDataSetChanged();
            }
        }
        setGreeting();
    }


    /*
     * Handles what occurs when one of the items in the Bucket List's ListView is pressed.
     * The data for the selected bucket list item is passed through an Intent and the
     * ViewItemActivity is launched.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Get the clicked item
        ItemModel clickedItem = bucketModel.getItem(position);
        
        // Serialize the item to JSON
        String serializedItem = clickedItem.serialize();

        // Go to ViewItemActivity with the clicked item
        Intent viewItem = new Intent(this, ViewItemActivity.class);
        viewItem.putExtra("item", serializedItem);
        viewItem.putExtra("item position", position);
        startActivityForResult(viewItem, RequestCode.VIEW_ITEM_REQUEST.ordinal());
    }


    /*
     * Implements the "Accomplished" and "Unaccomplished" navigation tabs
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // "Accomplished" selected
            case R.id.list_toggle_accomplished:
                if(((ToggleButton) v).isChecked()){
                    ((ToggleButton) v).setChecked(true);
                    bucketArrayAdapter.setAccomplishedToggle(true);
                    ToggleButton unaccomplishedToggle = (ToggleButton) findViewById(R.id.list_toggle_unaccomplished);
                    unaccomplishedToggle.setChecked(false);
                } else {
                    ((ToggleButton) v).setChecked(true);
                }
                // Update the bucketView
                bucketArrayAdapter.notifyDataSetChanged();
                break;

            // "Unaccomplished" selected
            case R.id.list_toggle_unaccomplished:
                if(((ToggleButton) v).isChecked()){
                    ((ToggleButton) v).setChecked(true);
                    bucketArrayAdapter.setAccomplishedToggle(false);
                    ToggleButton accomplishedToggle = (ToggleButton) findViewById(R.id.list_toggle_accomplished);
                    accomplishedToggle.setChecked(false);
                } else {
                    ((ToggleButton) v).setChecked(true);
                }
                // Update the bucketView
                bucketArrayAdapter.notifyDataSetChanged();
                break;            
        }
    }
    
    public void setGreeting(){
        // Sets the greeting text to invisible when items in bucket
        TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        if(bucketModel.isEmpty()){
            greetingTextView.setVisibility(View.VISIBLE);
        } else {
            greetingTextView.setVisibility(View.GONE);
        }
        
    }
    
    // Not really utilized but it refines search when submit button is pressed.
    @Override
    public boolean onQueryTextSubmit(String s) {
        bucketArrayAdapter.setSearchQuery(searchView.getQuery().toString());
        bucketArrayAdapter.notifyDataSetChanged();
        return true;
    } 

    // Updates the list live as the user types in their search query.
    @Override
    public boolean onQueryTextChange(String s) {
        bucketArrayAdapter.setSearchQuery(searchView.getQuery().toString());
        bucketArrayAdapter.notifyDataSetChanged();
        return true;
    }
}






