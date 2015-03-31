package edu.hmc.dxie.bucketlist;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SortTest extends ActivityInstrumentationTestCase2<ListActivity> {

    public SortTest() {super(ListActivity.class); }

    private ListActivity mListActivity;
    ItemModel item0 = new ItemModel(0);
    ItemModel item1 = new ItemModel(1);
    ItemModel item2 = new ItemModel(2);
    ItemModel item3 = new ItemModel(3);
    ItemModel item4 = new ItemModel(4);
    ItemModel item5 = new ItemModel(5);
    ItemModel item6 = new ItemModel(6);
    ItemModel item7 = new ItemModel(7);
    ItemModel item8 = new ItemModel(8);
    ItemModel item9 = new ItemModel(9);

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mListActivity = getActivity();

        //Clear bucket list tasks
        mListActivity.bucketModel.clearItems();
    }

    /*Checks that the Bucket List is properly sorted (in ascending order, for now).
     *It simply iterates through the Bucket List and checks that each item is less
     *than or equal to the last.
     *
     * NOTE: This function, as it is right now, requires that each item has the same
     * values for every parameter (which is currently the case).  May change this later
     */

    public void sortedCheck() {
        //Iterate through all items in the bucket list and check that each is
        //less than or equal to the last
        ItemModel i;
        ItemModel j;
        for(int k = 0; k < mListActivity.bucketModel.size() - 1; k++) {
            i = mListActivity.bucketModel.getItem(k);
            j = mListActivity.bucketModel.getItem(k+1);
            assertTrue("sortedCheck failed", i.getPriority() <= j.getPriority());
        }
    }

    //Basic sorting test to sort 9 items with distinct values using the
    //Priority parameter
    public void testBasicSortPriority() {
        mListActivity.bucketModel.clearItems();
        mListActivity.bucketModel.addItem(item2);
        mListActivity.bucketModel.addItem(item7);
        mListActivity.bucketModel.addItem(item6);
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item1);
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item4);
        mListActivity.bucketModel.addItem(item0);
        mListActivity.bucketModel.addItem(item9);

        ItemModel itemModelObject = new ItemModel();
        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getPriorityForSort", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketList(paramMethod);

        //Check
        sortedCheck();
    }

    //Basic sorting test to sort 9 items with distinct values using the
    //Deadline parameter
    public void testBasicSortDeadline() {
        mListActivity.bucketModel.clearItems();
        //Add Bucket list items in arbitrary order
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item4);
        mListActivity.bucketModel.addItem(item6);
        mListActivity.bucketModel.addItem(item1);
        mListActivity.bucketModel.addItem(item0);
        mListActivity.bucketModel.addItem(item9);
        mListActivity.bucketModel.addItem(item8);
        mListActivity.bucketModel.addItem(item2);

        ItemModel itemModelObject = new ItemModel();
        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDeadlineForSort", null);
            Log.w("bucket list", "Method found for basicsortdeadline");
        } catch (NoSuchMethodException e) {
            Log.w("bucket list", "Method not found in basicsortdeadline");
            e.printStackTrace();
        }

        mListActivity.sortBucketList(paramMethod);

        sortedCheck();
    }

    //Basic sorting test to sort 9 items with distinct values using the
    //Deadline parameter
    public void testBasicSortTravelDistance() {
        mListActivity.bucketModel.clearItems();
        //Add Bucket list items in arbitrary order
        mListActivity.bucketModel.addItem(item4);
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item6);
        mListActivity.bucketModel.addItem(item2);
        mListActivity.bucketModel.addItem(item0);
        mListActivity.bucketModel.addItem(item9);
        mListActivity.bucketModel.addItem(item8);
        mListActivity.bucketModel.addItem(item1);

        ItemModel itemModelObject = new ItemModel();
        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getTravelDistanceForSort", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketList(paramMethod);

        sortedCheck();
    }

    //Basic sorting test to sort 9 items with distinct values using the
    //Duration parameter
    public void testBasicSortDuration() {
        mListActivity.bucketModel.clearItems();
        //Add Bucket list items in arbitrary order
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item4);
        mListActivity.bucketModel.addItem(item6);
        mListActivity.bucketModel.addItem(item2);
        mListActivity.bucketModel.addItem(item0);
        mListActivity.bucketModel.addItem(item1);
        mListActivity.bucketModel.addItem(item9);
        mListActivity.bucketModel.addItem(item8);

        ItemModel itemModelObject = new ItemModel();
        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDurationForSort", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketList(paramMethod);

        sortedCheck();
    }

    //Basic sorting test to sort 9 items with distinct values using the
    //Duration parameter
    public void testBasicSortMoneyCost() {
        mListActivity.bucketModel.clearItems();
        //Add Bucket list items in arbitrary order
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item4);
        mListActivity.bucketModel.addItem(item2);
        mListActivity.bucketModel.addItem(item6);
        mListActivity.bucketModel.addItem(item1);
        mListActivity.bucketModel.addItem(item9);
        mListActivity.bucketModel.addItem(item0);
        mListActivity.bucketModel.addItem(item8);

        ItemModel itemModelObject = new ItemModel();
        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getMoneyCost", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketList(paramMethod);

        sortedCheck();
    }

    //Test items that have identical (repeating) values to check that the
    //Sort still works properly
    public void testRepeatsPriority() {
        mListActivity.bucketModel.clearItems();
        //Add Bucket list items in arbitrary order
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item4);
        mListActivity.bucketModel.addItem(item3);
        mListActivity.bucketModel.addItem(item1);
        mListActivity.bucketModel.addItem(item9);
        mListActivity.bucketModel.addItem(item5);
        mListActivity.bucketModel.addItem(item1);

        ItemModel itemModelObject = new ItemModel();
        Class itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getPriorityForSort", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketList(paramMethod);

        sortedCheck();
    }

}