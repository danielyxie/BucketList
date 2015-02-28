package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class AddActivity extends ActionBarActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        
        // Initialize the Deadline spinner with its units
        Spinner spinner = (Spinner) findViewById(R.id.add_spinner_deadline);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.deadline_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initialize the "Add" Button
        Button addButton = (Button) findViewById(R.id.add_button_confirm);
        addButton.setOnClickListener(this);
        
        // Initialize the priority SeekBar
        SeekBar prioritySeekBar = (SeekBar) findViewById(R.id.add_seekbar_priority);
        prioritySeekBar.setOnSeekBarChangeListener(this);
    }

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

    @Override
    public void onClick(View v) {

        // Get the EditText for adding an item
        EditText textEditText = (EditText) findViewById(R.id.add_edittext_text);

        // Get the text from the EditText
        String itemText = textEditText.getText().toString();
        
        // If there is no text
        if (itemText.isEmpty()) {
            
            // Inform the user and stay on the Activity
            textEditText.setError("Woops! The text field is empty. Insert text to add an item to your bucket list");

        // Otherwise, return to MainListActivity with the text
        } else {

            // Create the item object
            ItemModel newItem = new ItemModel();
            
            // Get the other Widgets
            EditText deadlineEditText = (EditText) findViewById(R.id.add_edittext_deadline);
            Spinner  deadlineSpinner = (Spinner) findViewById(R.id.add_spinner_deadline);
            SeekBar prioritySeekBar = (SeekBar) findViewById(R.id.add_seekbar_priority);
            EditText moneyCostEditText = (EditText) findViewById(R.id.add_edittext_moneycost);
            EditText timeCostEditText = (EditText) findViewById(R.id.add_edittext_timecost);
            EditText travelDistanceEditText = (EditText) findViewById(R.id.add_edittext_traveldistance);
            
            // Get their associated data
            String deadlineNum = deadlineEditText.getText().toString();
            String deadlineUnit = deadlineSpinner.getSelectedItem().toString();
            int priority = prioritySeekBar.getProgress();
            String moneyCost = moneyCostEditText.getText().toString();
            String timeCost = timeCostEditText.getText().toString();
            String travelDistance = travelDistanceEditText.getText().toString();
            
            // Set the parameters of the new item
            newItem.setItemText(itemText);
            newItem.setDeadline(deadlineNum, deadlineUnit);
            newItem.setPriority(priority);
            newItem.setMoneyCost(moneyCost);
            newItem.setTimeCost(timeCost);
            newItem.setTravelDistance(travelDistance);
            
            // Serialize the new item to JSON
            String serializedItem = newItem.serialize();
            
            // Store the serialized item in the intent
            Intent addItem = getIntent();
            addItem.putExtra("item", serializedItem);
            setResult(RESULT_OK, addItem);
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // Get the priority label TextView
        TextView priorityLabelTextView = (TextView) findViewById(R.id.add_textview_priority_label);

        // Get the priority label
        String priorityLabel = Integer.toString(progress);

        // Set the label text
        priorityLabelTextView.setText(priorityLabel);
        
        /* Partial code for getting text above Thumb
        int thumbOffset = seekBar.getThumb().getBounds().centerX();
        int seekBarOffset = seekBar.getLeft();
        priorityLabelTextView.setX(seekBarOffset+thumbOffset);*/
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
