package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class ViewItemActivity extends ActionBarActivity implements View.OnClickListener {


    Intent viewItem;
    ItemModel currentItem;
    
    TextView itemTextView;
    TextView deadlineTextView;
    TextView priorityTextView;
    SeekBar prioritySeekBar;
    TextView timecostTextView;
    TextView moneycostTextView;
    TextView traveldistanceTextView;

    Button completeButton;


    public enum RequestCode{
        EDIT_ITEM
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Initialize the parameter View objects
        itemTextView = (TextView) findViewById(R.id.text_itemtext);
        deadlineTextView = (TextView) findViewById(R.id.param_deadline_value);
        priorityTextView = (TextView) findViewById(R.id.param_priority);
        prioritySeekBar = (SeekBar) findViewById(R.id.param_priority_seekbar);
        prioritySeekBar.setEnabled(false);
        moneycostTextView = (TextView) findViewById(R.id.param_moneycost);
        timecostTextView = (TextView) findViewById(R.id.param_timecost_value);
        traveldistanceTextView = (TextView) findViewById(R.id.param_traveldistance_value);

        // Get the item to be viewed
        viewItem = getIntent();
        String serializedItem = viewItem.getStringExtra("item");
        currentItem = ItemModel.deserialize(serializedItem);

        refreshItemData(currentItem);
        
        // Initialize the Complete button
        completeButton = (Button) findViewById(R.id.button_complete);
        completeButton.setOnClickListener(this);
        
        // Choose whether to show a "Complete" or "Uncomplete" Button
        if (currentItem.getCompleted()) {
            completeButton.setText("Uncomplete");
        } else {
            completeButton.setText("Complete");
        }
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
     * Refreshes the parameters of the item being displayed
     */
    private void refreshItemData(ItemModel item){
        itemTextView.setText( item.getItemText() );
        deadlineTextView.setText( item.getDeadline() );

        //Initialize the TextView that displays the Priority value
        if (item.getPriority() != -1) {
            priorityTextView.setText(String.valueOf(item.getPriority()));
        }

        //Initialize the SeekBar used for changing the priority value
        if (item.getPriority() != - 1) {
            prioritySeekBar.setProgress( item.getPriority() );
        }

        if(item.getMoneyCost() != -1) {
            moneycostTextView.setText(String.valueOf(item.getMoneyCost()));
        }

        timecostTextView.setText( item.getTimeCost() );
        traveldistanceTextView.setText( item.getTravelDistance() );
    }

}
