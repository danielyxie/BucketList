package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ViewItemActivity extends ActionBarActivity implements View.OnClickListener {

    Intent viewItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Get the text of the item to be viewed
        viewItem = getIntent();
        String itemText = viewItem.getStringExtra("item text");
        
        // Get the TextView
        TextView itemTextView = (TextView) findViewById(R.id.text_itemtext);
        
        // Set its text to be that of the item
        itemTextView.setText(itemText);
        
        // Get the "Complete" Button
        Button completeButton = (Button) findViewById(R.id.button_complete);
        completeButton.setOnClickListener(this);
        
        // Get the "Delete" Button
        Button deleteButton = (Button) findViewById(R.id.button_delete);
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

    @Override
    public void onClick(View v) {
        
        // Check which button was clicked
        switch (v.getId()) {
            
            // "Complete" button
            case R.id.button_complete:
                viewItem.putExtra("button clicked", "Complete");
                setResult(RESULT_OK, viewItem);
                break;
            
            // "Delete" button
            case R.id.button_delete:
                viewItem.putExtra("button clicked", "Delete");
                setResult(RESULT_OK, viewItem);
                break;
                        
            default:
                setResult(RESULT_CANCELED, viewItem);
                break;
        }
        finish();
    }
}
