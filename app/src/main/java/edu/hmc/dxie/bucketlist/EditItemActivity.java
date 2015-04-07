package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by Kaitlyn Anderson on 4/3/2015.
 */



public class EditItemActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    Resources res;
    Intent editItem;
    ItemModel currentItem;

    TextView itemTextView;

    //Deadline layout View objects
    EditText deadlineValueEditText;
    Spinner deadlineSpinner;

    //Priority layout View objects
    TextView priorityTextView;
    SeekBar prioritySeekBar;

    //Time cost layout View objects
    EditText timecostValueEditText;
    Spinner timecostSpinner;

    //Money Cost layout View objects
    EditText moneycostEditText;

    //Travel Distance layout View objects
    EditText traveldistanceValueEditText;
    Spinner traveldistanceSpinner;

    //Key listeners for every parameter
    KeyListener deadlineListener;
    KeyListener moneycostListener;
    KeyListener timecostListener;
    KeyListener traveldistanceListener;


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
        initMoneycostEditing();
        initTimecostEditing();
        initTraveldistanceEditing();
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

                String deadlineValue = deadlineValueEditText.getText().toString();
                String deadlineUnit = deadlineSpinner.getSelectedItem().toString();
                currentItem.setDeadline(deadlineValue, deadlineUnit);
                currentItem.setPriority(prioritySeekBar.getProgress());
                currentItem.setMoneyCost((moneycostEditText.getText()).toString());
                String timecostValue = timecostValueEditText.getText().toString();
                String timecostUnit = timecostSpinner.getSelectedItem().toString();
                currentItem.setTimeCost(timecostValue, timecostUnit);
                String traveldistanceValue = traveldistanceValueEditText.getText().toString();
                String traveldistanceUnit = traveldistanceSpinner.getSelectedItem().toString();
                currentItem.setTravelDistance(traveldistanceValue, traveldistanceUnit);

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
        itemTextView = (TextView) findViewById(R.id.text_itemtext);
        itemTextView.setText( currentItem.getItemText() );
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
    
    private void initMoneycostEditing() {
        
        // Initialize the TextView that displays the cost
        moneycostEditText = (EditText) findViewById(R.id.param_moneycost);
        if(currentItem.getMoneyCost() != -1) {
            moneycostEditText.setText(String.valueOf(currentItem.getMoneyCost()));
        }

        // Get the listener for the EditText
        moneycostListener = moneycostEditText.getKeyListener();
    }
    
    private void initTimecostEditing() {

        //Split the time cost into its value and units
        String timecostString = currentItem.getTimeCost();
        String[] timecostSplit = timecostString.split(" ");
        String timecostValue = timecostSplit[0];
        String timecostUnit = timecostSplit[1];

        // Setup displaying/editing for value
        timecostValueEditText = (EditText) findViewById(R.id.param_timecost_value);
        timecostValueEditText.setText(timecostValue);

        // Setup displaying/editing for unit
        ArrayAdapter<CharSequence> timeCostAdapter = ArrayAdapter.createFromResource(this,
                R.array.timecost_units, android.R.layout.simple_spinner_item);
        timeCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timecostSpinner = (Spinner)findViewById(R.id.param_timecost_spinner);
        timecostSpinner.setAdapter(timeCostAdapter);
        String[] units = res.getStringArray(R.array.timecost_units);
        int spinnerVal = Arrays.asList(units).indexOf(timecostUnit);
        timecostSpinner.setSelection(spinnerVal);


        // Get the listener for the EditText
        timecostListener = timecostValueEditText.getKeyListener();
    }
    
    private void initTraveldistanceEditing() {

        //Split the travel distance into its value and its unit
        String traveldistanceString = currentItem.getTravelDistance();
        String traveldistanceSplit[] = traveldistanceString.split(" ");
        String traveldistanceValue = traveldistanceSplit[0];
        String traveldistanceUnit = traveldistanceSplit[1];

        // Setup displaying/editing for value
        traveldistanceValueEditText = (EditText) findViewById(R.id.param_traveldistance_value);
        traveldistanceValueEditText.setText(traveldistanceValue);

        // Setup displaying/editing for unit
        ArrayAdapter<CharSequence> travelDistanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.traveldistance_units, android.R.layout.simple_spinner_item);
        travelDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        traveldistanceSpinner = (Spinner)findViewById(R.id.param_traveldistance_spinner);
        traveldistanceSpinner.setAdapter(travelDistanceAdapter);
        String[] units = res.getStringArray(R.array.traveldistance_units);
        int spinnerVal = Arrays.asList(units).indexOf(traveldistanceUnit);
        traveldistanceSpinner.setSelection(spinnerVal);

        // Get the listener for the EditText
        traveldistanceListener = traveldistanceValueEditText.getKeyListener();
    }

}
