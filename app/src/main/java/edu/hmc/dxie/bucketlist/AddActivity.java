package edu.hmc.dxie.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by justisallen and kaitlynanderson on 2/15/15.
 * The AddActivity is the view/controller for adding an item to the bucket list.
 */
public class AddActivity extends ActionBarActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {

    private CategoryList categories;
    private ArrayList<Category> itemCats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initialize the Deadline spinner with its units
        Spinner deadlineSpinner = (Spinner) findViewById(R.id.add_spinner_deadline);
        ArrayAdapter<CharSequence> deadlineAdapter = ArrayAdapter.createFromResource(this,
                R.array.deadline_units, android.R.layout.simple_spinner_item);
        deadlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deadlineSpinner.setAdapter(deadlineAdapter);


        // Initialize the Duration spinner with its units
        Spinner durationSpinner = (Spinner) findViewById(R.id.add_spinner_duration);
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(this,
                R.array.duration_units, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);

        // Initialize the Travel Distance spinner with its units
        Spinner travelDistanceSpinner = (Spinner) findViewById(R.id.add_spinner_traveldistance);
        ArrayAdapter<CharSequence> travelDistanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.traveldistance_units, android.R.layout.simple_spinner_item);
        travelDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelDistanceSpinner.setAdapter(travelDistanceAdapter);

        // Initialize the Category Picker
        itemCats = new ArrayList<>();
        String serializedCategories = getIntent().getStringExtra("categories");
        categories = CategoryList.deserialize(serializedCategories);
        CategoryPickerAdapter catPickerAdapter = new CategoryPickerAdapter(this,
                android.R.layout.simple_list_item_1,
                categories.getCategories(), itemCats);
        ListView categoryPicker = (ListView) findViewById(R.id.add_categories);
        categoryPicker.setAdapter(catPickerAdapter);
        categoryPicker.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        categoryPicker.setOnItemClickListener(this);

        // Initialize the "Add" Button
        Button addButton = (Button) findViewById(R.id.add_button_confirm);
        addButton.setOnClickListener(this);

        // Initialize the priority SeekBar
        SeekBar prioritySeekBar = (SeekBar) findViewById(R.id.add_seekbar_priority);
        prioritySeekBar.setOnSeekBarChangeListener(this);
    }


    /*
     * This method implements what happens when the "Add" button on the main Activity screen
     * is pressed. The name of the new item and its parameter values are obtained from the
     * View Objects and are used to create an ItemModel.  This ItemModel is then passed
     * back to the ListActivity through an Intent.
     */
    @Override
    public void onClick(View v) {

        // Get the EditText for adding an item
        EditText textEditText = (EditText) findViewById(R.id.add_edittext_text);

        // Get the text from the EditText
        String itemText = textEditText.getText().toString().trim();

        // If there is no text
        if (itemText.isEmpty()) {

            // Inform the user and stay on the Activity
            textEditText.setError(getString(R.string.empty_text_error));

            // Otherwise, return to MainListActivity with the text
        } else {

            // Create the item object
            ItemModel newItem = new ItemModel();

            // Get the other Widgets
            EditText deadlineEditText = (EditText) findViewById(R.id.add_edittext_deadline);
            Spinner  deadlineSpinner = (Spinner) findViewById(R.id.add_spinner_deadline);
            SeekBar prioritySeekBar = (SeekBar) findViewById(R.id.add_seekbar_priority);
            EditText costEditText = (EditText) findViewById(R.id.add_edittext_cost);
            EditText durationEditText = (EditText) findViewById(R.id.add_edittext_duration);
            Spinner durationSpinner = (Spinner) findViewById(R.id.add_spinner_duration);
            EditText travelDistanceEditText = (EditText) findViewById(R.id.add_edittext_traveldistance);
            Spinner travelDistanceSpinner = (Spinner) findViewById(R.id.add_spinner_traveldistance);

            // Get their associated data
            String deadlineNum = deadlineEditText.getText().toString();
            String deadlineUnit = deadlineSpinner.getSelectedItem().toString();
            int priority = prioritySeekBar.getProgress();
            String cost = costEditText.getText().toString();
            String duration = durationEditText.getText().toString();
            String durationUnit = durationSpinner.getSelectedItem().toString();
            String travelDistance = travelDistanceEditText.getText().toString();
            String travelDistanceUnit = travelDistanceSpinner.getSelectedItem().toString();

            // Set the parameters of the new item
            newItem.setItemText(itemText);
            newItem.setDeadline(deadlineNum, deadlineUnit);
            newItem.setPriority(priority);
            newItem.setCost(cost);
            newItem.setDuration(duration, durationUnit);
            newItem.setTravelDistance(travelDistance, travelDistanceUnit);
            newItem.setCategories(itemCats);

            // Serialize the new item to JSON
            String serializedItem = newItem.serialize();

            // Store the serialized item in the intent
            Intent addItem = getIntent();
            addItem.putExtra("item", serializedItem);
            setResult(RESULT_OK, addItem);
            finish();
        }
    }


    /*
     * This method handles the implementation of the SeekBar.  The SeekBar in this activity
     * is used to change the Priority parameter.
     */
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView catPicker = (CheckedTextView) view;
        catPicker.toggle();
        if (catPicker.isChecked()) {
            itemCats.add(categories.getCategory(position));
        } else {
            itemCats.remove(categories.getCategory(position));
        }
    }
}
