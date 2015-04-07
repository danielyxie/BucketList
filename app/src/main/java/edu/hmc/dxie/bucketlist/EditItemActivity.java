package edu.hmc.dxie.bucketlist;

import android.content.Intent;
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

/**
 * Created by Kaitlyn Anderson on 4/3/2015.
 */



public class EditItemActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {


    TextView itemTextView;
    Intent viewItem;

    ItemModel currentItem;

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

    private int position;    //index of item in bucket list

    //Key listeners for every parameter
    private KeyListener deadlineListener;
    private KeyListener moneycostListener;
    private KeyListener timecostListener;
    private KeyListener traveldistanceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Get the text of the item to be viewed
        viewItem = getIntent();
        String serializedItem = viewItem.getStringExtra("item");
        currentItem = ItemModel.deserialize(serializedItem);

        position = viewItem.getIntExtra("item position", 0);

        // Initialize the Item Name textView and set its text
        itemTextView = (TextView) findViewById(R.id.text_itemtext);

        //////////////////////////////////////////////
        // Initialize DEADLINE PARAMETER View Objects
        //////////////////////////////////////////////


        //Initialize EditText
        deadlineValueEditText = (EditText) findViewById(R.id.param_deadline_value);


        ArrayAdapter<CharSequence> deadlineAdapter = ArrayAdapter.createFromResource(this,
                R.array.deadline_units, android.R.layout.simple_spinner_item);
        deadlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deadlineSpinner = (Spinner) findViewById(R.id.param_deadline_spinner);
        deadlineSpinner.setAdapter(deadlineAdapter);

        // Get the listener for the EditText
        deadlineListener = deadlineValueEditText.getKeyListener();


        //////////////////////////////////////////////
        // Initialize PRIORITY PARAMETER View Objects
        //////////////////////////////////////////////

        //Initialize the TextView that displays the Priority value
        priorityTextView = (TextView) findViewById(R.id.param_priority);

        //Initialize the SeekBar used for changing the priority value
        prioritySeekBar = (SeekBar) findViewById(R.id.param_priority_seekbar);
        prioritySeekBar.setOnSeekBarChangeListener(this);


        ////////////////////////////////////////////////
        // Initialize MONEY COST PARAMETER View Objects
        ////////////////////////////////////////////////

        moneycostEditText = (EditText) findViewById(R.id.param_moneycost);

        // Get the listener for the EditText
        moneycostListener = moneycostEditText.getKeyListener();


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

        timecostSpinner = (Spinner)findViewById(R.id.param_timecost_spinner);
        timecostSpinner.setAdapter(timeCostAdapter);


        // Get the listener for the EditText
        timecostListener = timecostValueEditText.getKeyListener();

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

        traveldistanceSpinner = (Spinner)findViewById(R.id.param_traveldistance_spinner);
        traveldistanceSpinner.setAdapter(travelDistanceAdapter);

        // Get the listener for the EditText
        traveldistanceListener = traveldistanceValueEditText.getKeyListener();

        refreshItemData(currentItem);

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
                Intent editItem = getIntent();
                editItem.putExtra("item", serializedItem);
                setResult(RESULT_OK, editItem);
                finish();
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
        viewItem.putExtra("item position", position);
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
        viewItem.putExtra("item position", position);
        setResult(RESULT_OK, viewItem);
        this.finish();
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

        deadlineSpinner.setSelection(spinnerVal);



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
        timecostSpinner.setSelection(spinnerVal);

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
        traveldistanceSpinner.setSelection(spinnerVal);
    }

}
