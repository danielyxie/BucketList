package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ViewItemActivity extends ActionBarActivity implements View.OnClickListener {

    //Should these be private?
    Intent viewItem;
    Button completeButton;
    Button editButton;
    Button deleteButton;
    ItemModel currentItem;
    EditText deadlineEditText;
    EditText priorityEditText;
    EditText timecostEditText;
    EditText moneycostEditText;
    EditText traveldistanceEditText;

    private boolean edit;

    //Key listeners for every parameter
    private KeyListener deadlineListener;
    private KeyListener priorityListener;
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
        
        // Get the Item Name textView and set its text
        TextView itemTextView = (TextView) findViewById(R.id.text_itemtext);
        itemTextView.setText(currentItem.getItemText());

        //////////////////////
        // DEADLINE PARAMETER
        //////////////////////

        deadlineEditText = (EditText) findViewById(R.id.param_deadline);
        deadlineEditText.setText(currentItem.getDeadline());

        // Get the listener for the EditText
        deadlineListener = deadlineEditText.getKeyListener();

        // Disable the edit feature
        deadlineEditText.setKeyListener(null);
        deadlineEditText.setEnabled(false);


        //////////////////////
        // PRIORITY PARAMETER
        //////////////////////

        priorityEditText = (EditText) findViewById(R.id.param_priority);
        if(currentItem.getPriority() != -1) {
            priorityEditText.setText(String.valueOf(currentItem.getPriority()));
        }

        // Get the listener for the EditText
        priorityListener = priorityEditText.getKeyListener();

        // Disable the edit feature
        priorityEditText.setKeyListener(null);
        priorityEditText.setEnabled(false);


        ////////////////////////
        // MONEY COST PARAMETER
        ////////////////////////

        moneycostEditText = (EditText) findViewById(R.id.param_moneycost);
        if(currentItem.getMoneyCost() != -1) {
            moneycostEditText.setText(String.valueOf(currentItem.getMoneyCost()));
        }

        // Get the listener for the EditText
        moneycostListener = moneycostEditText.getKeyListener();

        // Disable the edit feature
        moneycostEditText.setKeyListener(null);
        moneycostEditText.setEnabled(false);

        //////////////////////
        // TIME COST PARAMETER
        //////////////////////

        timecostEditText = (EditText) findViewById(R.id.param_timecost);
        timecostEditText.setText(currentItem.getTimeCost());

        // Get the listener for the EditText
        timecostListener = timecostEditText.getKeyListener();

        // Disable the edit feature
        timecostEditText.setKeyListener(null);
        timecostEditText.setEnabled(false);

        ////////////////////////////
        // TRAVEL DISTANCE PARAMETER
        ////////////////////////////

        traveldistanceEditText = (EditText) findViewById(R.id.param_traveldistance);
        traveldistanceEditText.setText(currentItem.getTravelDistance());

        // Get the listener for the EditText
        traveldistanceListener = traveldistanceEditText.getKeyListener();

        // Disable the edit feature
        traveldistanceEditText.setKeyListener(null);
        traveldistanceEditText.setEnabled(false);


        ////////////////
        // BUTTONS
        ////////////////

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

        /*// Serialize the new item to JSON
        String serializedItem = currentItem.serialize();

        // Store the serialized item in the intent
        viewItem.putExtra("item", serializedItem);
        setResult(RESULT_OK, viewItem);*/

        return super.onOptionsItemSelected(item);
    }

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
                if (edit) {
                    deadlineEditText.setKeyListener(null);
                    deadlineEditText.setEnabled(false);
                    priorityEditText.setKeyListener(null);
                    priorityEditText.setEnabled(false);
                    moneycostEditText.setKeyListener(null);
                    moneycostEditText.setEnabled(false);
                    timecostEditText.setKeyListener(null);
                    timecostEditText.setEnabled(false);
                    traveldistanceEditText.setKeyListener(null);
                    traveldistanceEditText.setEnabled(false);
                    edit = false;
                    editButton.setText("Edit");

                    //Update the new values of the item
                    currentItem.setDeadline((deadlineEditText.getText()).toString());
                    currentItem.setPriority(Integer.valueOf((priorityEditText.getText()).toString()));
                    currentItem.setMoneyCost((moneycostEditText.getText()).toString());
                    currentItem.setTimeCost((timecostEditText.getText()).toString());
                    currentItem.setTravelDistance((traveldistanceEditText.getText()).toString());
                } else {
                    deadlineEditText.setKeyListener(deadlineListener);
                    deadlineEditText.setEnabled(true);
                    priorityEditText.setKeyListener(priorityListener);
                    priorityEditText.setEnabled(true);
                    moneycostEditText.setKeyListener(moneycostListener);
                    moneycostEditText.setEnabled(true);
                    timecostEditText.setKeyListener(timecostListener);
                    timecostEditText.setEnabled(true);
                    traveldistanceEditText.setKeyListener(traveldistanceListener);
                    traveldistanceEditText.setEnabled(true);
                    edit = true;
                    editButton.setText("Finish Editing");
                }

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
    
    @Override
    public void onBackPressed() {
        
        // Serialize the new item to JSON
        String serializedItem = currentItem.serialize();

        // Store the serialized item in the intent
        viewItem.putExtra("item", serializedItem);
        setResult(RESULT_OK, viewItem);
        finish();
    }
}
