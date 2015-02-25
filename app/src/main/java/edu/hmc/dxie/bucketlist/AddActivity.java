package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class AddActivity extends ActionBarActivity implements View.OnClickListener {
    
    EditText addEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Get the "Add" Button
        Button addButton = (Button) findViewById(R.id.button_confirm_add);
        addButton.setOnClickListener(this);
        
        // Get the EditText for adding an item
        addEditText = (EditText) findViewById(R.id.text_add);

        // Clicks the add Button when the Done or Return buttons on the keyboard are pressed
        addEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onClick(v);
                }
                return false;
            }
        });
        
        
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

        // Get the text from the EditText
        String itemText = addEditText.getText().toString();
        
        // If there is no text
        if (itemText.isEmpty()) {
            
            // Inform the user and stay on the Activity
            addEditText.setError("Woops! The text field is empty. Insert text to add an item to your bucket list");

        // Otherwise, return to MainListActivity with the text
        } else {
            
            // Create the item object
            ItemModel newItem = new ItemModel();
            newItem.setItemText(itemText);
            
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
