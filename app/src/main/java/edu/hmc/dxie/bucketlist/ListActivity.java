package edu.hmc.dxie.bucketlist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// TODO: Consider replacing magic strings with constants in strings.xml
public class ListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
                                                               View.OnClickListener,
                                                               AdapterView.OnItemSelectedListener,
                                                               SearchView.OnQueryTextListener{

    ListModel bucketModel;
    ListView bucketView;
    bucketlistArrayAdapter bucketArrayAdapter;
    SharedPreferences persistentData;
    Spinner sortSpinner;
    ImageButton sortButton;
    boolean sortAscending = true;    //Determines whether sorting by ascending or descending items
    SearchView searchView;


    public enum RequestCode{
        ADD_ITEM_REQUEST, VIEW_ITEM_REQUEST
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Get persistent data
        persistentData = getSharedPreferences("persistent data", Context.MODE_PRIVATE);

        // Get the JSON of the bucketModel if it exists, otherwise get an empty string
        String bucketModelJSON = persistentData.getString("bucket model", "");

        // Check whether a bucketModel exists

        // If a bucketModel does not exist
        if (bucketModelJSON.isEmpty()) {
            
            // Create a new one for holding list items
            bucketModel = new ListModel();
        } else {
            
            // Otherwise, recover the existing bucketModel
            bucketModel = ListModel.deserialize(bucketModelJSON);
        }

        // Setup an ArrayAdapter for the ListView
        bucketArrayAdapter = new bucketlistArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                bucketModel.getBucket());
        bucketView = (ListView) findViewById(R.id.bucketlistview);
        bucketView.setAdapter(bucketArrayAdapter);

        // Set this activity to react to list items being processed
        bucketView.setOnItemClickListener(this);

        // Set greeting
        bucketView.setOnItemClickListener(this);
        
        setGreeting();

        // Set defaults for toggle/"tab" buttons
        ToggleButton unaccomplishedToggle = (ToggleButton) findViewById(R.id.list_toggle_unaccomplished);
        ToggleButton accomplishedToggle = (ToggleButton) findViewById(R.id.list_toggle_accomplished);
        unaccomplishedToggle.setChecked(true);
        accomplishedToggle.setChecked(false);
        unaccomplishedToggle.setOnClickListener(this);
        accomplishedToggle.setOnClickListener(this);

        //Initialize the spinner that is used as a menu for sorting
        //This spinner will let users choose which parameter to sort by.
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> sortSpinnerAdapter = ArrayAdapter.createFromResource(this,
                                   R.array.sort_params, android.R.layout.simple_spinner_item);
        sortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortSpinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        //Initialize the ImageButton that lets users to choose whether to sort in
        //ascending or descending order
        sortButton = (ImageButton) findViewById(R.id.sort_button);

        sortButton.setOnClickListener(this);
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
        searchView.setQueryHint(getString(R.string.search_hint));

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
     * Implements what happens when an item is selected on the Sort Spinner.  Selecting a
     * parameter on the Sort Spinner will sort the bucket list by that parameter
     */
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        sortBucketList();

        //Update the bucketview
        this.bucketArrayAdapter.notifyDataSetChanged();
    }


    /*
     * Implements what happens when nothing is selected on the Sort Spinner
     */
    public void onNothingSelected(AdapterView<?> adapterView)
    {

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

            //Sort ImageButton for choosing ascending or descending sorting order
            case R.id.sort_button:
                if (sortAscending) {
                    int id = getResources().getIdentifier("edu.hmc.dxie.bucketlist:drawable/sort_down_arrow", null, null);
                    sortButton.setImageResource(id);
                    sortAscending = false;
                } else {
                    int id = getResources().getIdentifier("edu.hmc.dxie.bucketlist:drawable/sort_up_arrow", null, null);
                    sortButton.setImageResource(id);
                    sortAscending = true;
                }

                sortBucketList();
                //Update the bucketview
                this.bucketArrayAdapter.notifyDataSetChanged();
        }
    }


    /*
     * Handles the greeting that tells users how to add an item to their Bucket List.  This
     * greeting will only show if the Bucket List has no items
     */
    public void setGreeting(){
        // Sets the greeting text to invisible when items in bucket
        TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        if(bucketModel.isEmpty()){
            greetingTextView.setVisibility(View.VISIBLE);
        } else {
            greetingTextView.setVisibility(View.GONE);
        }

    }

    /* Sort the Bucket List.  This function automatically calls
     * helper functions to determine how to sort (ascending/descending,
     * what parameters, etc.)
     */
    public void sortBucketList() {
        String param = (String) sortSpinner.getSelectedItem();

        ItemModel itemModelObject = new ItemModel();

        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            switch(param) {
                case "Item Name":
                    paramMethod = itemModelClass.getMethod("getItemText", null);
                    break;
                case "Deadline":
                    paramMethod = itemModelClass.getMethod("getDeadlineForSort", null);
                    break;
                case "Priority":
                    paramMethod = itemModelClass.getMethod("getPriorityForSort", null);
                    break;
                case "Cost":
                    paramMethod = itemModelClass.getMethod("getMoneyCost", null);
                    break;
                case "Duration":
                    paramMethod = itemModelClass.getMethod("getDurationForSort", null);
                    break;
                case "Travel Distance":
                    paramMethod = itemModelClass.getMethod("getTravelDistanceForSort", null);
                    break;
                default:
                    return;
            }
            if (sortAscending) {
                sortBucketListAscending(paramMethod);
            } else {
                sortBucketListDescending(paramMethod);
            }
        } catch (NoSuchMethodException e) {
            Log.w("bucket list", "can't get method");
            e.printStackTrace();
        }
    }

    /*
     * The method used to sort the Bucket List by parameters in ascending order.
     * The "Sort Selection menu" will call this sorting function when a menu item
     * is selected by the user.
     */
    public void sortBucketListAscending(Method paramMethod) {
        int length = bucketModel.size();

        if (length > 2) try {
            quickSortAscending(0, length - 1, paramMethod);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void quickSortAscending(int lowerIndex, int higherIndex, Method paramMethod) throws InvocationTargetException, IllegalAccessException {

        int i = lowerIndex;
        int j = higherIndex;

        ItemModel i_item;
        Object i_obj;
        double i_value = 0;
        String i_string = null;

        ItemModel j_item;
        Object j_obj;
        double j_value = 0;
        String j_string = null;

        ItemModel itemModelObject = new ItemModel();

        Class itemModelClass = itemModelObject.getClass();
        Method getItemTextMethod = null;

        try {
            getItemTextMethod = itemModelClass.getMethod("getItemText", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Calculate pivot number.  Here, the middle of the array will always be the pivot
        double pivot = 0;
        String pivotString = null;

        int pivotIndex = lowerIndex + (higherIndex - lowerIndex) / 2;
        ItemModel pivotItem = bucketModel.getItem(pivotIndex);
        Object pivotObj = paramMethod.invoke(pivotItem, null);
        if (paramMethod.equals(getItemTextMethod)) {
            pivotString = (String)pivotObj;
        } else {
            pivot = (double)pivotObj;
        }

        // Divide into two arrays
        while (i <= j) {

            i_item = bucketModel.getItem(i);
            j_item = bucketModel.getItem(j);

            i_obj = paramMethod.invoke(i_item, null);
            j_obj = paramMethod.invoke(j_item, null);

            if (paramMethod.equals(getItemTextMethod)) {
                i_string = (String) i_obj;
                j_string = (String) j_obj;
            } else {
                i_value = (double) i_obj;
                j_value = (double) j_obj;
            }

            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            if (paramMethod.equals(getItemTextMethod)) {
                int iCompare = i_string.compareTo(pivotString);
                while (iCompare < 0) {
                    i++;
                    i_item = bucketModel.getItem(i);
                    i_obj = paramMethod.invoke(i_item, null);
                    i_string = (String) i_obj;
                    iCompare = i_string.compareTo(pivotString);
                }

                int jCompare = j_string.compareTo(pivotString);
                while (jCompare > 0) {
                    j--;
                    j_item = bucketModel.getItem(j);
                    j_obj = paramMethod.invoke(j_item, null);
                    j_string = (String) j_obj;
                    jCompare = j_string.compareTo(pivotString);
                }
                if (i <= j) {
                    bucketModel.swapItems(i, j);
                    //move index to next position on both sides
                    i++;
                    j--;
                }
            } else {
                while (i_value < pivot) {
                    i++;
                    i_item = bucketModel.getItem(i);
                    i_obj = paramMethod.invoke(i_item, null);
                    i_value = (double) i_obj;
                }
                while (j_value > pivot) {
                    j--;
                    j_item = bucketModel.getItem(j);
                    j_obj = paramMethod.invoke(j_item, null);
                    j_value = (double) j_obj;
                }
                if (i <= j) {
                    bucketModel.swapItems(i, j);
                    //move index to next position on both sides
                    i++;
                    j--;
                }
            }
        }
        // call quickSortAscending() method recursively
        if (lowerIndex < j)
            quickSortAscending(lowerIndex, j, paramMethod);
        if (i < higherIndex)
            quickSortAscending(i, higherIndex, paramMethod);

    }

    /*
     * The method used to sort the Bucket List by parameters in descending order
     * The "Sort Selection menu" will
     * call this sorting function when a menu item is selected by the user.
     */
    public void sortBucketListDescending(Method paramMethod) {
        int length = bucketModel.size();

        if (length > 2) try {
            quickSortDescending(0, length - 1, paramMethod);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void quickSortDescending(int lowerIndex, int higherIndex, Method paramMethod) throws InvocationTargetException, IllegalAccessException {

        int i = lowerIndex;
        int j = higherIndex;

        ItemModel i_item;
        Object i_obj;
        double i_value = 0;
        String i_string = null;

        ItemModel j_item;
        Object j_obj;
        double j_value = 0;
        String j_string = null;

        ItemModel itemModelObject = new ItemModel();

        Class itemModelClass = itemModelObject.getClass();
        Method getItemTextMethod = null;

        try {
            getItemTextMethod = itemModelClass.getMethod("getItemText", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Calculate pivot number.  Here, the middle of the array will always be the pivot
        double pivot = 0;
        String pivotString = null;

        int pivotIndex = lowerIndex + (higherIndex - lowerIndex) / 2;
        ItemModel pivotItem = bucketModel.getItem(pivotIndex);
        Object pivotObj = paramMethod.invoke(pivotItem, null);
        if (paramMethod.equals(getItemTextMethod)) {
            pivotString = (String)pivotObj;
        } else {
            pivot = (double)pivotObj;
        }



        // Divide into two arrays
        while (i <= j) {

            i_item = bucketModel.getItem(i);
            j_item = bucketModel.getItem(j);

            i_obj = paramMethod.invoke(i_item, null);
            j_obj = paramMethod.invoke(j_item, null);

            if (paramMethod.equals(getItemTextMethod)) {
                i_string = (String) i_obj;
                j_string = (String) j_obj;
            } else {
                i_value = (double) i_obj;
                j_value = (double) j_obj;
            }

            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            if (paramMethod.equals(getItemTextMethod)) {
                int iCompare = i_string.compareTo(pivotString);
                while (iCompare < 0) {
                    i++;
                    i_item = bucketModel.getItem(i);
                    i_obj = paramMethod.invoke(i_item, null);
                    i_string = (String) i_obj;
                    iCompare = i_string.compareTo(pivotString);
                }

                int jCompare = j_string.compareTo(pivotString);
                while (jCompare > 0) {
                    j--;
                    j_item = bucketModel.getItem(j);
                    j_obj = paramMethod.invoke(j_item, null);
                    j_string = (String) j_obj;
                    jCompare = j_string.compareTo(pivotString);
                }
                if (i <= j) {
                    bucketModel.swapItems(i, j);
                    //move index to next position on both sides
                    i++;
                    j--;
                }
            } else {
                while (i_value > pivot) {
                    i++;
                    i_item = bucketModel.getItem(i);
                    i_obj = paramMethod.invoke(i_item, null);
                    i_value = (double) i_obj;
                }
                while (j_value < pivot) {
                    j--;
                    j_item = bucketModel.getItem(j);
                    j_obj = paramMethod.invoke(j_item, null);
                    j_value = (double) j_obj;
                }
                if (i <= j) {
                    bucketModel.swapItems(i, j);
                    //move index to next position on both sides
                    i++;
                    j--;
                }
            }
        }
        // call quickSortAscending() method recursively
        if (lowerIndex < j)
            quickSortDescending(lowerIndex, j, paramMethod);
        if (i < higherIndex)
            quickSortDescending(i, higherIndex, paramMethod);

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






