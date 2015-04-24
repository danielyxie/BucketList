package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Kaitlyn Anderson on 4/3/2015.
 */



public class EditItemActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {

    Resources res;
    Intent editItem;
    ItemModel currentItem;

    EditText itemTextEditText;

    // Deadline layout View objects
    EditText deadlineValueEditText;
    Spinner deadlineSpinner;

    // Priority layout View objects
    TextView priorityTextView;
    SeekBar prioritySeekBar;

    // Duration layout View objects
    EditText durationValueEditText;
    Spinner durationSpinner;

    // Cost layout View objects
    EditText costEditText;

    //Travel Distance layout View objects
    EditText travelDistanceValueEditText;
    Spinner travelDistanceSpinner;

    // Stuff for categories
    CategoryList categories;
    ArrayList<Category> itemCats;

    //Notes View objects
    LinedEditText notesEditText;

    // Key listeners for every parameter
    KeyListener itemTextListener;
    KeyListener deadlineListener;
    KeyListener costListener;
    KeyListener durationListener;
    KeyListener travelDistanceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        res = getResources();

        // Get the item to be viewed
        editItem = getIntent();
        String serializedItem = editItem.getStringExtra("item");
        currentItem = ItemModel.deserialize(serializedItem);
        
        // Initialize the editing UI
        initItemTextEditing();
        initDeadlineEditing();
        initPriorityEditing();
        initCostEditing();
        initDurationEditing();
        initTravelDistanceEditing();
        initCategoryEditing();
        initNotesEditing();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch(id) {
            // "Finish Editing" button
            case R.id.menu_item_finish_edit:
                /* When the user is finished editing, the app should take them back to
                 * ViewItemActivity
                 */
    
                String itemText = itemTextEditText.getText().toString().trim();
                currentItem.setItemText(itemText);
                String deadlineValue = deadlineValueEditText.getText().toString();
                String deadlineUnit = deadlineSpinner.getSelectedItem().toString();
                currentItem.setDeadline(deadlineValue, deadlineUnit);
                currentItem.setPriority(prioritySeekBar.getProgress());
                currentItem.setCost((costEditText.getText()).toString());
                String durationValue = durationValueEditText.getText().toString();
                String durationUnit = durationSpinner.getSelectedItem().toString();
                currentItem.setDuration(durationValue, durationUnit);
                String travelDistanceValue = travelDistanceValueEditText.getText().toString();
                String travelDistanceUnit = travelDistanceSpinner.getSelectedItem().toString();
                currentItem.setTravelDistance(travelDistanceValue, travelDistanceUnit);
                String n = notesEditText.getText().toString();
                currentItem.setNotes(n);

                // Serialize the new item to JSON
                String serializedItem = currentItem.serialize();

                // Store the serialized item in the intent
                editItem.putExtra("item", serializedItem);
                setResult(RESULT_OK, editItem);
                finish();
                return true;

            // "Delete" button
            case R.id.menu_item_discard:
                editItem.putExtra("item", "");
                setResult(RESULT_OK, editItem);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     * This function manages the SeekBar that is used for editing the Priority parameter
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // Get the priority label TextView
        TextView priorityLabelTextView = (TextView) findViewById(R.id.param_priority);

        // Get the priority label
        String priorityLabel = Integer.toString(progress);

        // Set the label text
        priorityLabelTextView.setText(priorityLabel);
    }


    /*
     * This function handles what occurs when the "Back/Up" button on the Action Bar
     * is pressed.
     */
    @Override
    public Intent getSupportParentActivityIntent() {

        // Serialize the new item to JSON
        String serializedItem = currentItem.serialize();

        // Store the serialized item in the intent
        editItem.putExtra("item", serializedItem);
        setResult(RESULT_OK, editItem);
        finish();

        return editItem;
    }

    /*
     * This method implements what happens when the Android's "Back" button is pressed.  Note
     * that this is different than the "Up" button on the Action Bar
     */
    @Override
    public void onBackPressed() {

        // Serialize the new item to JSON
        String serializedItem = currentItem.serialize();

        // Store the serialized item in the intent
        editItem.putExtra("item", serializedItem);
        setResult(RESULT_OK, editItem);
        finish();
    }

    /*
     * onStartTrackingTouch and onStopTrackingTouch are required for implementing SeekBars.
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /////////////////////////////////////////////////////////////
    // Helper functions for initializing the parameter viewing
    ////////////////////////////////////////////////////////////
    
    private void initItemTextEditing() {
        
        // Initialize the TextView that displays the item text
        itemTextEditText = (EditText) findViewById(R.id.param_itemtext);
        itemTextEditText.setText( currentItem.getItemText() );

        // Get the listener for the EditText
        itemTextListener = itemTextEditText.getKeyListener();
    }
    
    private void initDeadlineEditing() {

        // Split the deadline into its value and its unit
        String deadlineString = currentItem.getDeadline();
        String[] deadlineSplit = deadlineString.split(" ");
        String deadlineValue = deadlineSplit[0];
        String deadlineUnit = deadlineSplit[1];
        
        // Setup displaying/editing for value
        deadlineValueEditText = (EditText) findViewById(R.id.param_deadline_value);
        deadlineValueEditText.setText(deadlineValue);
        
        // Setup displaying/editing for unit
        ArrayAdapter<CharSequence> deadlineAdapter = ArrayAdapter.createFromResource(this,
                R.array.deadline_units, android.R.layout.simple_spinner_item);
        deadlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deadlineSpinner = (Spinner) findViewById(R.id.param_deadline_spinner);
        deadlineSpinner.setAdapter(deadlineAdapter);
        String[] units = res.getStringArray(R.array.deadline_units);
        int spinnerVal = Arrays.asList(units).indexOf(deadlineUnit);
        deadlineSpinner.setSelection(spinnerVal);
        
        // Get the listener for the EditText
        deadlineListener = deadlineValueEditText.getKeyListener();
    }

    private void initPriorityEditing() {

        // Initialize the TextView that displays the Priority value
        priorityTextView = (TextView) findViewById(R.id.param_priority);
        if (currentItem.getPriority() != -1) {
            priorityTextView.setText(String.valueOf(currentItem.getPriority()));
        }

        //Initialize the SeekBar used for changing the priority value
        prioritySeekBar = (SeekBar) findViewById(R.id.param_priority_seekbar);
        prioritySeekBar.setOnSeekBarChangeListener(this);
        if (currentItem.getPriority() != - 1) {
            prioritySeekBar.setProgress( currentItem.getPriority() );
        }
    }
    
    private void initCostEditing() {
        
        // Initialize the TextView that displays the cost
        costEditText = (EditText) findViewById(R.id.param_cost);
        if(currentItem.getCost() != -1) {
            costEditText.setText(String.valueOf(currentItem.getCost()));
        }

        // Get the listener for the EditText
        costListener = costEditText.getKeyListener();
    }
    
    private void initDurationEditing() {

        //Split the duration into its value and units
        String durationString = currentItem.getDuration();
        String[] durationSplit = durationString.split(" ");
        String durationValue = durationSplit[0];
        String durationUnit = durationSplit[1];

        // Setup displaying/editing for value
        durationValueEditText = (EditText) findViewById(R.id.param_duration_value);
        durationValueEditText.setText(durationValue);

        // Setup displaying/editing for unit
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(this,
                R.array.duration_units, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner = (Spinner)findViewById(R.id.param_duration_spinner);
        durationSpinner.setAdapter(durationAdapter);
        String[] units = res.getStringArray(R.array.duration_units);
        int spinnerVal = Arrays.asList(units).indexOf(durationUnit);
        durationSpinner.setSelection(spinnerVal);


        // Get the listener for the EditText
        durationListener = durationValueEditText.getKeyListener();
    }
    
    private void initTravelDistanceEditing() {

        //Split the travel distance into its value and its unit
        String travelDistanceString = currentItem.getTravelDistance();
        String[] travelDistanceSplit = travelDistanceString.split(" ");
        String travelDistanceValue = travelDistanceSplit[0];
        String travelDistanceUnit = travelDistanceSplit[1];

        // Setup displaying/editing for value
        travelDistanceValueEditText = (EditText) findViewById(R.id.param_traveldistance_value);
        travelDistanceValueEditText.setText(travelDistanceValue);

        // Setup displaying/editing for unit
        ArrayAdapter<CharSequence> travelDistanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.traveldistance_units, android.R.layout.simple_spinner_item);
        travelDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelDistanceSpinner = (Spinner)findViewById(R.id.param_traveldistance_spinner);
        travelDistanceSpinner.setAdapter(travelDistanceAdapter);
        String[] units = res.getStringArray(R.array.traveldistance_units);
        int spinnerVal = Arrays.asList(units).indexOf(travelDistanceUnit);
        travelDistanceSpinner.setSelection(spinnerVal);

        // Get the listener for the EditText
        travelDistanceListener = travelDistanceValueEditText.getKeyListener();
    }

    private void initCategoryEditing() {
        String serializedCategories = editItem.getStringExtra("categories");
        categories = CategoryList.deserialize(serializedCategories);
        itemCats = currentItem.getCategories();
        CategoryPickerAdapter catPickerAdapter = new CategoryPickerAdapter(this,
                android.R.layout.simple_list_item_1,
                categories.getCategories(), itemCats);
        ListView categoryPicker = (ListView) findViewById(R.id.edit_categories);
        categoryPicker.setAdapter(catPickerAdapter);
        categoryPicker.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        categoryPicker.setOnItemClickListener(this);
    }

    private void initNotesEditing() {
        String n = currentItem.getNotes();

        notesEditText = (LinedEditText) findViewById(R.id.notepad_edit);
        notesEditText.setText(n);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView catPicker = (CheckedTextView) view;
        catPicker.toggle();
        if (catPicker.isChecked()) {
            itemCats.add(categories.getCategory(position));
        } else {
            itemCats.remove(categories.getCategory(position));
        }
    }
}
