package edu.hmc.dxie.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ListActivity extends ActionBarActivity {

    ListModel bucketModel;
    ListView bucketView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    
    public enum RequestCode{
        ADD_ITEM_REQUEST(0);
        
        private int value;
        
        private RequestCode(int value) {
            this.value = value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Create a ListModel for holding bucketModel list items
        bucketModel = new ListModel();
        
        // Access the ListView
        bucketView = (ListView) findViewById(R.id.bucketlistview);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                mNameList);

        // Set the ListView to use the ArrayAdapter
        bucketView.setAdapter(mArrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds the "new" and "search" buttons to the action bar.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Add button pushed
        if (id == R.id.menu_item_add) {
            
            // Go to AddActivity
            Intent addItem = new Intent(this, AddActivity.class);
            startActivityForResult(addItem, RequestCode.ADD_ITEM_REQUEST.value);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the request was to add an item
        if (requestCode == RequestCode.ADD_ITEM_REQUEST.value) {
            
            // If the request went well
            if (resultCode == Activity.RESULT_OK) {
                
                // Get the new item
                String itemText = data.getStringExtra("result");
                
                // Add the item to the bucketModel
                bucketModel.addItem(itemText);
                
                // Update the bucketView
                mArrayAdapter.notifyDataSetChanged();
            }
        }
    }
}
