package edu.hmc.dxie.bucketlist;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 */
public class AddActivity extends ActionBarActivity implements View.OnClickListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        
        Button addButton = (Button) findViewById(R.id.button_confirm_add);
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

        // Get the text from the EditText
        EditText addEditText = (EditText) findViewById(R.id.text_add);
        
        // Take what was typed into the EditText and save in ItemModel
        String itemText = addEditText.getText().toString();
        

    }
}
