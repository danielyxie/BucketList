package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class ViewItemActivity extends ActionBarActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    Intent viewItem;
    ItemModel currentItem;
    
    TextView itemTextView;
    Button completeButton;

    // Deadline layout View objects
    EditText deadlineValueEditText;
    Spinner deadlineViewSpinner;    // This Spinner is used only for viewing the deadline.

    // Priority layout View objects
    TextView priorityTextView;
    SeekBar prioritySeekBar;

    // Time cost layout View objects
    EditText timecostValueEditText;
    Spinner timecostViewSpinner;

    // Money Cost layout View objects
    EditText moneycostEditText;

    // Travel Distance layout View objects
    EditText traveldistanceValueEditText;
    Spinner traveldistanceViewSpinner;


    public enum RequestCode{
        EDIT_ITEM
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Get the text of the item to be viewed
        viewItem = getIntent();
        String serializedItem = viewItem.getStringExtra("item");
        currentItem = ItemModel.deserialize(serializedItem);

        // Initialize the Item Name textView and set its text
        itemTextView = (TextView) findViewById(R.id.text_itemtext);

        //////////////////////////////////////////////
        // Initialize DEADLINE PARAMETER View Objects
        //////////////////////////////////////////////

        //Split the deadline into its value and its unit
        String deadlineString = currentItem.getDeadline();
        String deadlineSplit[] = deadlineString.split(" ");
        String deadlineValue = deadlineSplit[0];
        String deadlineUnit = deadlineSplit[1];

        //Initialize EditText
        deadlineValueEditText = (EditText) findViewById(R.id.param_deadline_value);

        //Initialize both spinners

        ArrayAdapter<CharSequence> deadlineAdapter = ArrayAdapter.createFromResource(this,
                R.array.deadline_units, android.R.layout.simple_spinner_item);
        deadlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deadlineViewSpinner = (Spinner) findViewById(R.id.param_deadline_viewspinner);
        deadlineViewSpinner.setAdapter(deadlineAdapter);

        // Disable the edit feature
        deadlineValueEditText.setKeyListener(null);
        deadlineValueEditText.setEnabled(false);
        deadlineViewSpinner.setClickable(false);


        //////////////////////////////////////////////
        // Initialize PRIORITY PARAMETER View Objects
        //////////////////////////////////////////////

        //Initialize the TextView that displays the Priority value
        priorityTextView = (TextView) findViewById(R.id.param_priority);

        //Initialize the SeekBar used for changing the priority value
        prioritySeekBar = (SeekBar) findViewById(R.id.param_priority_seekbar);
        prioritySeekBar.setOnSeekBarChangeListener(this);

        // Disable the edit feature
        prioritySeekBar.setEnabled(false);

        ////////////////////////////////////////////////
        // Initialize MONEY COST PARAMETER View Objects
        ////////////////////////////////////////////////

        moneycostEditText = (EditText) findViewById(R.id.param_moneycost);

        // Disable the edit feature
        moneycostEditText.setKeyListener(null);
        moneycostEditText.setEnabled(false);

        //////////////////////////////////////////////
        // Initialize TIME COST PARAMETER View Objects
        //////////////////////////////////////////////

        //Split the time cost into its value and units
        String timecostString = currentItem.getTimeCost();
        String timecostSplit[] = timecostString.split(" ");
        String timecostValue = timecostSplit[0];
        String timecostUnit = timecostSplit[1];

        //Initialize the time cost's EditText
        timecostValueEditText = (EditText) findViewById(R.id.param_timecost_value);

        //Initialize Spinners
        ArrayAdapter<CharSequence> timeCostAdapter = ArrayAdapter.createFromResource(this,
                R.array.timecost_units, android.R.layout.simple_spinner_item);
        timeCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timecostViewSpinner = (Spinner) findViewById(R.id.param_timecost_viewspinner);
        timecostViewSpinner.setAdapter(timeCostAdapter);

        // Disable the edit feature
        timecostValueEditText.setKeyListener(null);
        timecostValueEditText.setEnabled(false);
        timecostViewSpinner.setClickable(false);

        /////////////////////////////////////////////////////
        // Initialize TRAVEL DISTANCE PARAMETER View Objects
        /////////////////////////////////////////////////////

        //Split the travel distance into its value and its unit
        String traveldistanceString = currentItem.getTravelDistance();
        String traveldistanceSplit[] = traveldistanceString.split(" ");
        String traveldistanceValue = traveldistanceSplit[0];
        String traveldistanceUnit = traveldistanceSplit[1];


        //Initialize EditText
        traveldistanceValueEditText = (EditText) findViewById(R.id.param_traveldistance_value);

        //Initialize Spinners
        ArrayAdapter<CharSequence> travelDistanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.traveldistance_units, android.R.layout.simple_spinner_item);
        travelDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        traveldistanceViewSpinner = (Spinner) findViewById(R.id.param_traveldistance_viewspinner);
        traveldistanceViewSpinner.setAdapter(travelDistanceAdapter);


        // Disable the edit feature
        traveldistanceValueEditText.setKeyListener(null);
        traveldistanceValueEditText.setEnabled(false);
        traveldistanceViewSpinner.setClickable(false);


        //////////////////////
        // Initialize BUTTONS
        //////////////////////

        // Get the "Complete" Button
        completeButton = (Button) findViewById(R.id.button_complete);
        completeButton.setOnClickListener(this);
        
        // Choose whether to show a "Complete" or "Uncomplete" Button
        if (currentItem.getCompleted()) {
            completeButton.setText("Uncomplete");
        } else {
            completeButton.setText("Complete");
        }

        refreshItemData(currentItem);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch(id) {
            // "Edit" button
            case R.id.menu_item_edit:

                // Serialize the new item to JSON
                String serializedItem = currentItem.serialize();

                // Go to EditActivity
                Intent editItem = new Intent(this, EditItemActivity.class);
                editItem.putExtra("item", serializedItem);
                startActivityForResult(editItem, RequestCode.EDIT_ITEM.ordinal());
                return true;

            // "Delete" button
            case R.id.menu_item_discard:
                viewItem.putExtra("button clicked", "Delete");
                setResult(RESULT_OK, viewItem);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     * Handles what processes are run when the "Complete" button is pressed.
     */
    @Override
    public void onClick(View v) {
        
        // Check which button was clicked
        switch (v.getId()) {
            
            // "Complete" button
            case R.id.button_complete:
                viewItem.putExtra("button clicked", completeButton.getText());
                setResult(RESULT_OK, viewItem);
                finish();
                break;

            default:
                setResult(RESULT_CANCELED, viewItem);
                finish();
                break;
        }
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
        viewItem.putExtra("button clicked", "Back");

        // Serialize the new item to JSON
        String serializedItem = currentItem.serialize();

        // Store the serialized item in the intent
        viewItem.putExtra("item", serializedItem);
        setResult(RESULT_OK, viewItem);
        this.finish();

        return viewItem;
    }

    /*
     * This method implements what happens when the Android's "Back" button is pressed.  Note
     * that this is different than the "Up" button on the Action Bar
     */
    @Override
    public void onBackPressed() {
        viewItem.putExtra("button clicked", "Back");

        // Serialize the new item to JSON
        String serializedItem = currentItem.serialize();

        // Store the serialized item in the intent
        viewItem.putExtra("item", serializedItem);
        setResult(RESULT_OK, viewItem);
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


    /*
     * Returns the index of the array of deadline units that corresponds with the input unit
     * for the deadline parameter.  This is the same array used by the deadline Spinner.
     * The input String must be only the units of the parameter, such as "week(s)" or "month(s)"
     */
    public int getDeadlineSpinnerIndex(String deadlineUnit){
        switch(deadlineUnit) {
            case "day(s)":
                return 0;
            case "week(s)":
                return 1;
            case "month(s)":
                return 2;
            case "year(s)":
                return 3;
            default:
                return 0;
        }
    }

    /*
     * Returns the index of the array of travel distance units that corresponds with the input unit
     * for the travel distance parameter.  This is the same array used by the travel distance Spinner.
     * The input String must be only the units of the parameter, such as "miles(s)"
     */
    public int getTraveldistanceSpinnerIndex(String traveldistanceUnit){
        switch(traveldistanceUnit) {
            case "mile(s)":
                return 0;
            case "kilometer(s)":
                return 1;
            default:
                return 0;
        }
    }

    /*
     * This method handles what happens when the application returns to the ListActivity.
     * It manages and processes Intents from the AddActivity and the ViewItemActivity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String serializedItem = data.getStringExtra("item");
        currentItem = ItemModel.deserialize(serializedItem);
        refreshItemData(currentItem);
    }


    /*
     * Returns the index of the array of time cost units that corresponds with the input unit
     * for the time cost parameter.  This is the same array used by the time cost Spinner.
     * The input String must be only the units of the parameter, such as "miles(s)"
     */
    public int getTimecostSpinnerIndex(String timecostUnit){
        switch(timecostUnit) {
            case "minute(s)":
                return 0;
            case "hour(s)":
                return 1;
            case "day(s)":
                return 2;
            case "week(s)":
                return 3;
            case "month(s)":
                return 4;
            case "year(s)":
                return 5;
            default:
                return 0;
        }
    }

    private void refreshItemData(ItemModel item){

        // Initialize the Item Name textView and set its text
        itemTextView.setText(item.getItemText());

        //////////////////////////////////////////////
        // Initialize DEADLINE PARAMETER View Objects
        //////////////////////////////////////////////

        //Split the deadline into its value and its unit
        String deadlineString = item.getDeadline();
        String deadlineSplit[] = deadlineString.split(" ");
        String deadlineValue = deadlineSplit[0];
        String deadlineUnit = deadlineSplit[1];

        //Initialize EditText
        deadlineValueEditText.setText(deadlineValue);

        //Initialize spinner
        int spinnerVal = getDeadlineSpinnerIndex(deadlineUnit);

        deadlineViewSpinner.setSelection(spinnerVal);



        //////////////////////////////////////////////
        // Initialize PRIORITY PARAMETER View Objects
        //////////////////////////////////////////////

        //Initialize the TextView that displays the Priority value
        if (item.getPriority() != -1) {
            priorityTextView.setText(String.valueOf(item.getPriority()));
        }

        //Initialize the SeekBar used for changing the priority value
        if (item.getPriority() != - 1) {
            prioritySeekBar.setProgress(item.getPriority());
        }

        ////////////////////////////////////////////////
        // Initialize MONEY COST PARAMETER View Objects
        ////////////////////////////////////////////////

        if(item.getMoneyCost() != -1) {
            moneycostEditText.setText(String.valueOf(item.getMoneyCost()));
        }


        //////////////////////////////////////////////
        // Initialize TIME COST PARAMETER View Objects
        //////////////////////////////////////////////

        //Split the time cost into its value and units
        String timecostString = item.getTimeCost();
        String timecostSplit[] = timecostString.split(" ");
        String timecostValue = timecostSplit[0];
        String timecostUnit = timecostSplit[1];

        spinnerVal = getTimecostSpinnerIndex(timecostUnit);

        //Initialize the time cost's EditText
        timecostValueEditText.setText(timecostValue);

        //Initialize Spinners
        timecostViewSpinner.setSelection(spinnerVal);

        /////////////////////////////////////////////////////
        // Initialize TRAVEL DISTANCE PARAMETER View Objects
        /////////////////////////////////////////////////////

        //Split the travel distance into its value and its unit
        String traveldistanceString = item.getTravelDistance();
        String traveldistanceSplit[] = traveldistanceString.split(" ");
        String traveldistanceValue = traveldistanceSplit[0];
        String traveldistanceUnit = traveldistanceSplit[1];

        spinnerVal = getTraveldistanceSpinnerIndex(traveldistanceUnit);

        //Initialize EditText
        traveldistanceValueEditText.setText(traveldistanceValue);

        //Initialize Spinners
        traveldistanceViewSpinner.setSelection(spinnerVal);
    }

}
