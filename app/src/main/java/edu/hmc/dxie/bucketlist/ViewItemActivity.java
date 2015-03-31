package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class ViewItemActivity extends ActionBarActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //Should these be private?
    Intent viewItem;
    Button completeButton;

    //Button View Objects
    Button editButton;
    Button deleteButton;
    ItemModel currentItem;

    //Deadline layout View objects
    EditText deadlineValueEditText;
    Spinner deadlineViewSpinner;    //This Spinner is used only for viewing the deadline.  It
                                    //it set to be invisible if the user is editing
    Spinner deadlineSpinner;        //This spinner is used only for editing the deadline.  It is
                                    //set to be invisible if the user is only viewing the item

    //Priority layout View objects
    TextView priorityTextView;
    SeekBar prioritySeekBar;

    //Time cost layout View objects
    EditText timecostValueEditText;
    Spinner timecostViewSpinner;
    Spinner timecostSpinner;

    //Money Cost layout View objects
    EditText moneycostEditText;

    //Travel Distance layout View objects
    EditText traveldistanceValueEditText;
    Spinner traveldistanceViewSpinner;
    Spinner traveldistanceSpinner;

    //Keeps track of whether the item is currently being Edited
    //True if item is edited, false if item is just being viewed
    private boolean edit;

    private int position;    //index of item in bucket list

    //Key listeners for every parameter
    private KeyListener deadlineListener;
    private KeyListener moneycostListener;
    private KeyListener timecostListener;
    private KeyListener traveldistanceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        edit = false;

        // Get the text of the item to be viewed
        viewItem = getIntent();
        String serializedItem = viewItem.getStringExtra("item");
        currentItem = ItemModel.deserialize(serializedItem);

        position = viewItem.getIntExtra("item position", 0);

        // Initialize the Item Name textView and set its text
        TextView itemTextView = (TextView) findViewById(R.id.text_itemtext);
        itemTextView.setText(currentItem.getItemText());

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
        deadlineValueEditText.setText(deadlineValue);

        //Initialize both spinners
        int spinnerVal = getDeadlineSpinnerIndex(deadlineUnit);

        ArrayAdapter<CharSequence> deadlineAdapter = ArrayAdapter.createFromResource(this,
                R.array.deadline_units, android.R.layout.simple_spinner_item);
        deadlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deadlineViewSpinner = (Spinner) findViewById(R.id.param_deadline_viewspinner);
        deadlineViewSpinner.setAdapter(deadlineAdapter);
        deadlineViewSpinner.setSelection(spinnerVal);
        deadlineViewSpinner.setBackgroundColor(0xFFE8E8E8);

        deadlineSpinner = (Spinner) findViewById(R.id.param_deadline_spinner);
        deadlineSpinner.setAdapter(deadlineAdapter);
        deadlineSpinner.setSelection(spinnerVal);

        // Get the listener for the EditText
        deadlineListener = deadlineValueEditText.getKeyListener();

        // Disable the edit feature
        deadlineValueEditText.setKeyListener(null);
        deadlineValueEditText.setEnabled(false);
        deadlineViewSpinner.setClickable(false);
        deadlineSpinner.setClickable(false);
        deadlineSpinner.setVisibility(View.GONE);


        //////////////////////////////////////////////
        // Initialize PRIORITY PARAMETER View Objects
        //////////////////////////////////////////////

        //Initialize the TextView that displays the Priority value
        priorityTextView = (TextView) findViewById(R.id.param_priority);
        if (currentItem.getPriority() != -1) {
            priorityTextView.setText(String.valueOf(currentItem.getPriority()));
        }

        //Initialize the SeekBar used for changing the priority value
        prioritySeekBar = (SeekBar) findViewById(R.id.param_priority_seekbar);
        prioritySeekBar.setOnSeekBarChangeListener(this);

        if (currentItem.getPriority() != - 1) {
            prioritySeekBar.setProgress(currentItem.getPriority());
        }

        // Disable the edit feature
        prioritySeekBar.setEnabled(false);

        ////////////////////////////////////////////////
        // Initialize MONEY COST PARAMETER View Objects
        ////////////////////////////////////////////////

        moneycostEditText = (EditText) findViewById(R.id.param_moneycost);
        if(currentItem.getMoneyCost() != -1) {
            moneycostEditText.setText(String.valueOf(currentItem.getMoneyCost()));
        }

        // Get the listener for the EditText
        moneycostListener = moneycostEditText.getKeyListener();

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

        spinnerVal = getTimecostSpinnerIndex(timecostUnit);

        //Initialize the time cost's EditText
        timecostValueEditText = (EditText) findViewById(R.id.param_timecost_value);
        timecostValueEditText.setText(timecostValue);

        //Initialize Spinners
        ArrayAdapter<CharSequence> timeCostAdapter = ArrayAdapter.createFromResource(this,
                R.array.timecost_units, android.R.layout.simple_spinner_item);
        timeCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timecostViewSpinner = (Spinner) findViewById(R.id.param_timecost_viewspinner);
        timecostViewSpinner.setAdapter(timeCostAdapter);
        timecostViewSpinner.setBackgroundColor(0xFFDAC189);
        timecostViewSpinner.setSelection(spinnerVal);

        timecostSpinner = (Spinner)findViewById(R.id.param_timecost_spinner);
        timecostSpinner.setAdapter(timeCostAdapter);
        timecostSpinner.setSelection(spinnerVal);


        // Get the listener for the EditText
        timecostListener = timecostValueEditText.getKeyListener();

        // Disable the edit feature
        timecostValueEditText.setKeyListener(null);
        timecostValueEditText.setEnabled(false);
        timecostViewSpinner.setClickable(false);
        timecostSpinner.setClickable(false);
        timecostSpinner.setVisibility(View.GONE);

        /////////////////////////////////////////////////////
        // Initialize TRAVEL DISTANCE PARAMETER View Objects
        /////////////////////////////////////////////////////

        //Split the travel distance into its value and its unit
        String traveldistanceString = currentItem.getTravelDistance();
        String traveldistanceSplit[] = traveldistanceString.split(" ");
        String traveldistanceValue = traveldistanceSplit[0];
        String traveldistanceUnit = traveldistanceSplit[1];

        spinnerVal = getTraveldistanceSpinnerIndex(traveldistanceUnit);

        //Initialize EditText
        traveldistanceValueEditText = (EditText) findViewById(R.id.param_traveldistance_value);
        traveldistanceValueEditText.setText(traveldistanceValue);

        //Initialize Spinners
        ArrayAdapter<CharSequence> travelDistanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.traveldistance_units, android.R.layout.simple_spinner_item);
        travelDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        traveldistanceViewSpinner = (Spinner) findViewById(R.id.param_traveldistance_viewspinner);
        traveldistanceViewSpinner.setAdapter(travelDistanceAdapter);
        traveldistanceViewSpinner.setBackgroundColor(0xFFE8E8E8);
        traveldistanceViewSpinner.setSelection(spinnerVal);

        traveldistanceSpinner = (Spinner)findViewById(R.id.param_traveldistance_spinner);
        traveldistanceSpinner.setAdapter(travelDistanceAdapter);
        traveldistanceSpinner.setSelection(spinnerVal);

        // Get the listener for the EditText
        traveldistanceListener = traveldistanceValueEditText.getKeyListener();

        // Disable the edit feature
        traveldistanceValueEditText.setKeyListener(null);
        traveldistanceValueEditText.setEnabled(false);
        traveldistanceViewSpinner.setClickable(false);
        traveldistanceSpinner.setClickable(false);
        traveldistanceSpinner.setVisibility(View.GONE);


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

        //Get the "Edit" Button
        editButton = (Button) findViewById(R.id.button_edit);
        editButton.setOnClickListener(this);

        // Get the "Delete" Button
        deleteButton = (Button) findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(this);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_item, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    /*
     * Handles what processes are run when one of the "Complete", "Edit", and
     * "Delete" buttons are pressed.
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

            // "Edit" button
            case R.id.button_edit:
                /* When the Edit button is pressed, check to see whether the item is currently
                 * being edited or not.  If the item is currently being edited, the Activity
                 * should exit the Edit mode by disabling editing for all of the View Objects and
                 * updating the new parameter values for the item. If the item is currently not
                 * being edited, the editing features for each View Object should be enabled.
                 *
                 * The 'edit' variable is used to keep track of whether the item is currently
                 * being edited.
                 */
                if (edit) {
                    //Disable editing for each parameter
                    deadlineValueEditText.setKeyListener(null);
                    deadlineValueEditText.setEnabled(false);
                    deadlineSpinner.setClickable(false);
                    deadlineSpinner.setVisibility(View.GONE);
                    deadlineViewSpinner.setVisibility(View.VISIBLE);

                    prioritySeekBar.setEnabled(false);

                    moneycostEditText.setKeyListener(null);
                    moneycostEditText.setEnabled(false);

                    timecostValueEditText.setKeyListener(null);
                    timecostValueEditText.setEnabled(false);
                    timecostSpinner.setClickable(false);
                    timecostSpinner.setVisibility(View.GONE);
                    timecostViewSpinner.setVisibility(View.VISIBLE);

                    traveldistanceValueEditText.setKeyListener(null);
                    traveldistanceValueEditText.setEnabled(false);
                    traveldistanceSpinner.setClickable(false);
                    traveldistanceSpinner.setVisibility(View.GONE);
                    traveldistanceViewSpinner.setVisibility(View.VISIBLE);

                    edit = false;
                    editButton.setText("Edit");

                    //Update the new parameter values for the item
                    String deadlineValue = deadlineValueEditText.getText().toString();
                    String deadlineUnit = deadlineSpinner.getSelectedItem().toString();
                    int spinnerVal = getDeadlineSpinnerIndex(deadlineUnit);
                    deadlineViewSpinner.setSelection(spinnerVal);
                    currentItem.setDeadline(deadlineValue, deadlineUnit);

                    currentItem.setPriority(prioritySeekBar.getProgress());

                    currentItem.setMoneyCost((moneycostEditText.getText()).toString());

                    String timecostValue = timecostValueEditText.getText().toString();
                    String timecostUnit = timecostSpinner.getSelectedItem().toString();
                    spinnerVal = getTimecostSpinnerIndex(timecostUnit);
                    timecostViewSpinner.setSelection(spinnerVal);
                    currentItem.setTimeCost(timecostValue, timecostUnit);

                    String traveldistanceValue = traveldistanceValueEditText.getText().toString();
                    String traveldistanceUnit = traveldistanceSpinner.getSelectedItem().toString();
                    spinnerVal = getTraveldistanceSpinnerIndex(traveldistanceUnit);
                    traveldistanceViewSpinner.setSelection(spinnerVal);
                    currentItem.setTravelDistance(traveldistanceValue, traveldistanceUnit);
                } else {
                    deadlineValueEditText.setKeyListener(deadlineListener);
                    deadlineValueEditText.setEnabled(true);
                    deadlineSpinner.setClickable(true);
                    deadlineSpinner.setVisibility(View.VISIBLE);
                    deadlineViewSpinner.setVisibility(View.GONE);

                    prioritySeekBar.setEnabled(true);

                    moneycostEditText.setKeyListener(moneycostListener);
                    moneycostEditText.setEnabled(true);

                    timecostValueEditText.setKeyListener(timecostListener);
                    timecostValueEditText.setEnabled(true);
                    timecostSpinner.setClickable(true);
                    timecostSpinner.setVisibility(View.VISIBLE);
                    timecostViewSpinner.setVisibility(View.GONE);

                    traveldistanceValueEditText.setKeyListener(traveldistanceListener);
                    traveldistanceValueEditText.setEnabled(true);
                    traveldistanceSpinner.setClickable(true);
                    traveldistanceSpinner.setVisibility(View.VISIBLE);
                    traveldistanceViewSpinner.setVisibility(View.GONE);


                    edit = true;
                    editButton.setText("Finish Editing");
                }

                viewItem.putExtra("button clicked", "Edit");
                break;
            
            // "Delete" button
            case R.id.button_delete:
                viewItem.putExtra("button clicked", "Delete");
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

}
