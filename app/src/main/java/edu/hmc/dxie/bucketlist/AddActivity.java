package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class AddActivity extends ActionBarActivity implements View.OnClickListener {
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Get the "Add" Button
        Button addButton = (Button) findViewById(R.id.add_button_confirm);
        addButton.setOnClickListener(this);
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
            
            // Get the other EditTexts
            EditText deadlineEditText = (EditText) findViewById(R.id.add_edittext_deadline);
            EditText priorityEditText = (EditText) findViewById(R.id.add_edittext_priority);
            EditText moneyCostEditText = (EditText) findViewById(R.id.add_edittext_moneycost);
            EditText timeCostEditText = (EditText) findViewById(R.id.add_edittext_timecost);
            EditText travelDistanceEditText = (EditText) findViewById(R.id.add_edittext_traveldistance);
            
            // Get their associated data
            String deadline = deadlineEditText.getText().toString();
            String priority = priorityEditText.getText().toString();
            String moneyCost = moneyCostEditText.getText().toString();
            String timeCost = timeCostEditText.getText().toString();
            String travelDistance = travelDistanceEditText.getText().toString();
            
            // Set the parameters of the new item
            //  only perform numerical conversions and set value if strings are nonempty
            newItem.setItemText(itemText);
            newItem.setDeadline(deadline);
            
            if (!priority.isEmpty()) {
                int priorityNum = Integer.parseInt(priority);
                newItem.setPriority(priorityNum);
            } if (!moneyCost.isEmpty()) {
                double moneyCostNum = Double.parseDouble(moneyCost);
                newItem.setMoneyCost(moneyCostNum);
            }
            
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
}
