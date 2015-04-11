package edu.hmc.dxie.bucketlist;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Random;

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

    ItemModel[] items = {item0, item1, item2,
            item3, item4, item5,
            item6, item7, item8,
            item9};

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

    public void sortedParamCheckAscending() {
        //Iterate through all items in the bucket list and check that each is
        //less than or equal to the last
        ItemModel i;
        ItemModel j;
        for(int k = 0; k < mListActivity.bucketModel.size() - 1; k++) {
            i = mListActivity.bucketModel.getItem(k);
            j = mListActivity.bucketModel.getItem(k+1);
            assertTrue("sortedParamCheckAscending failed", i.getPriority() <= j.getPriority());
        }
    }

    /*Checks that the Bucket List is properly sorted (in descending order, for now).
     *It simply iterates through the Bucket List and checks that each item is less
     *than or equal to the last.
     *
     * NOTE: This function, as it is right now, requires that each item has the same
     * values for every parameter (which is currently the case).  May change this later
     */

    public void sortedParamCheckDescending() {
        ItemModel i;
        ItemModel j;
        for(int k = 0; k < mListActivity.bucketModel.size() - 1; k++) {
            i = mListActivity.bucketModel.getItem(k);
            j = mListActivity.bucketModel.getItem(k+1);
            assertTrue("sortedParamCheckAscending failed", i.getPriority() >= j.getPriority());
        }
    }

    /*Checks that the Bucket List is properly sorted by name (in ascending order, for now).
     *It simply iterates through the Bucket List and checks that each String is lexicographically
     * below the next string
     *
     * NOTE: This function, as it is right now, requires that each item has the same
     * values for every parameter (which is currently the case).  May change this later
     */

    public void sortedNameCheckAscending() {
        ItemModel i, j;
        String i_Name, j_Name;
        int comp;
        for(int k = 0; k < mListActivity.bucketModel.size() - 1; k++) {
            i = mListActivity.bucketModel.getItem(k);
            j = mListActivity.bucketModel.getItem(k+1);
            i_Name = i.getItemText();
            j_Name = j.getItemText();
            comp = i_Name.compareTo(j_Name);
            assertTrue("sortedParamCheckAscending failed", comp <= 0);
        }
    }

    /*Checks that the Bucket List is properly sorted by name (in descending order, for now).
     *It simply iterates through the Bucket List and checks that each String is lexicographically
     * below the next string
     *
     * NOTE: This function, as it is right now, requires that each item has the same
     * values for every parameter (which is currently the case).  May change this later
     */

    public void sortedNameCheckDescending() {
        ItemModel i, j;
        String i_Name, j_Name;
        int comp;
        for(int k = 0; k < mListActivity.bucketModel.size() - 1; k++) {
            i = mListActivity.bucketModel.getItem(k);
            j = mListActivity.bucketModel.getItem(k+1);
            i_Name = i.getItemText();
            j_Name = j.getItemText();
            comp = i_Name.compareTo(j_Name);
            assertTrue("sortedParamCheckDescending failed", comp >= 0);
        }
    }


    /* Helper function for inserting items into the mock bucketModel. This
     * function randomly chooses one of the ItemModels item0 and item9 and
     * inserts it into bucketModel
     */
    public void addRandomItem() {
        Random r = new Random();
        int randInt = r.nextInt(10);
        mListActivity.bucketModel.addItem(items[randInt]);
    }


    //Basic sorting test to sort 10 items with distinct values using the
    //Priority parameter
    public void testBasicSortPriority() {
        mListActivity.bucketModel.clearItems();

        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getPriorityForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Basic sorting test to sort 10 items with distinct values using the
    //Deadline parameter
    public void testBasicSortDeadline() {
        mListActivity.bucketModel.clearItems();

        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDeadlineForSort");
            Log.w("bucket list", "Method found for basicsortdeadline");
        } catch (NoSuchMethodException e) {
            Log.w("bucket list", "Method not found in basicsortdeadline");
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Basic sorting test to sort 10 items with distinct values using the
    //Deadline parameter
    public void testBasicSortTravelDistance() {
        mListActivity.bucketModel.clearItems();

        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getTravelDistanceForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Basic sorting test to sort 10 items with distinct values using the
    //Duration parameter
    public void testBasicSortDuration() {
        mListActivity.bucketModel.clearItems();

        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDurationForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Basic sorting test to sort 10 items with distinct values using the
    //Duration parameter
    public void testBasicSortMoneyCost() {
        mListActivity.bucketModel.clearItems();

        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getCost");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test items that have identical (repeating) values to check that the
    //Sort still works properly for the Deadline parameter
    public void testRepeatsDeadline() {
        mListActivity.bucketModel.clearItems();

        mListActivity.bucketModel.addItem(item5);
        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }
        mListActivity.bucketModel.addItem(item5);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDeadlineForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test items that have identical (repeating) values to check that the
    //Sort still works properly for the Priority parameter
    public void testRepeatsPriority() {
        mListActivity.bucketModel.clearItems();

        mListActivity.bucketModel.addItem(item4);
        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }
        mListActivity.bucketModel.addItem(item4);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getPriorityForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test items that have identical (repeating) values to check that the
    //Sort still works properly for the Travel Distance parameter
    public void testRepeatsTravelDistance() {
        mListActivity.bucketModel.clearItems();

        mListActivity.bucketModel.addItem(item3);
        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }
        mListActivity.bucketModel.addItem(item3);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getTravelDistanceForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test items that have identical (repeating) values to check that the
    //Sort still works properly for the Duration parameter
    public void testRepeatsDuration() {
        mListActivity.bucketModel.clearItems();

        mListActivity.bucketModel.addItem(item2);
        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }
        mListActivity.bucketModel.addItem(item2);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDurationForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test items that have identical (repeating) values to check that the
    //Sort still works properly for the Money Cost parameter
    public void testRepeatsMoneyCost() {
        mListActivity.bucketModel.clearItems();

        mListActivity.bucketModel.addItem(item7);
        //Add 10 random items
        for(int i = 0; i < 10; i++) {
            addRandomItem();
        }
        mListActivity.bucketModel.addItem(item7);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getCost");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }


    //Test sorting on an empty bucket list when sorting by the Priority parameter
    public void testEmptyPriority() {
        mListActivity.bucketModel.clearItems();

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getPriorityForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test sorting on an empty bucket list when sorting by the Deadline parameter
    public void testEmptyDeadline() {
        mListActivity.bucketModel.clearItems();

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDeadlineForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test sorting on an empty bucket list when sorting by the Travel Distance parameter
    public void testEmptyTravelDistance() {
        mListActivity.bucketModel.clearItems();

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getTravelDistanceForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test sorting on an empty bucket list when sorting by the Duration parameter
    public void testEmptyDuration() {
        mListActivity.bucketModel.clearItems();

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDurationForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test sorting on an empty bucket list when sorting by the Money Cost parameter
    public void testEmptyMoneyCost() {
        mListActivity.bucketModel.clearItems();

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getCost");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedParamCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedParamCheckDescending();
    }

    //Test sorting by "Item Name" when all the first letters are different
    public void testNameBasic1() {
        mListActivity.bucketModel.clearItems();
        mListActivity.bucketModel.addItem(new ItemModel("bork"));
        mListActivity.bucketModel.addItem(new ItemModel("dez"));
        mListActivity.bucketModel.addItem(new ItemModel("nuk"));
        mListActivity.bucketModel.addItem(new ItemModel("gir"));
        mListActivity.bucketModel.addItem(new ItemModel("arma"));
        mListActivity.bucketModel.addItem(new ItemModel("dei"));
        mListActivity.bucketModel.addItem(new ItemModel("zyl"));
        mListActivity.bucketModel.addItem(new ItemModel("tuf"));
        mListActivity.bucketModel.addItem(new ItemModel("neg"));

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getItemText");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedNameCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedNameCheckDescending();
    }

    //Test sorting by "Item Name" when all of the first letters are identical but the
    //second letters are the same.  Also add some repeats words in there.
    public void testNameBasic2() {
        mListActivity.bucketModel.clearItems();
        mListActivity.bucketModel.addItem(new ItemModel("bk"));
        mListActivity.bucketModel.addItem(new ItemModel("bd"));
        mListActivity.bucketModel.addItem(new ItemModel("bf"));
        mListActivity.bucketModel.addItem(new ItemModel("be"));
        mListActivity.bucketModel.addItem(new ItemModel("ba"));
        mListActivity.bucketModel.addItem(new ItemModel("bn"));
        mListActivity.bucketModel.addItem(new ItemModel("bm"));
        mListActivity.bucketModel.addItem(new ItemModel("bz"));
        mListActivity.bucketModel.addItem(new ItemModel("bm"));

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getItemText");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedNameCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedNameCheckDescending();
    }

    //Test sorting by "Item Name" with non-alphanumeric characters
    public void testNameBasic3() {
        mListActivity.bucketModel.clearItems();
        mListActivity.bucketModel.addItem(new ItemModel("!~#@#"));
        mListActivity.bucketModel.addItem(new ItemModel("%$@#%"));
        mListActivity.bucketModel.addItem(new ItemModel("~~!!@"));
        mListActivity.bucketModel.addItem(new ItemModel(">:<?><"));
        mListActivity.bucketModel.addItem(new ItemModel("<?>{}{"));
        mListActivity.bucketModel.addItem(new ItemModel("!@$)(*"));
        mListActivity.bucketModel.addItem(new ItemModel(" **?>"));

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getItemText");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);
        sortedNameCheckAscending();
        mListActivity.sortBucketListDescending(paramMethod);
        sortedNameCheckDescending();
    }

    //Test that units are converted correctly when sorting by the Deadline parameter
    //(e.g. 1 week > 6 days, etc.)
    public void testConversionDeadline() {
        mListActivity.bucketModel.clearItems();
        ItemModel mock1 = new ItemModel("1");
        ItemModel mock2 = new ItemModel("2");
        ItemModel mock3 = new ItemModel("3");
        ItemModel mock4 = new ItemModel("4");
        ItemModel mock5 = new ItemModel("5");
        mock1.setDeadline("6", "day(s)");
        mock2.setDeadline("3", "week(s)");
        mock3.setDeadline("2", "month(s)");
        mock4.setDeadline("3", "month(s)");
        mock5.setDeadline("1", "year(s)");

        mListActivity.bucketModel.addItem(mock3);
        mListActivity.bucketModel.addItem(mock2);
        mListActivity.bucketModel.addItem(mock5);
        mListActivity.bucketModel.addItem(mock1);
        mListActivity.bucketModel.addItem(mock4);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDeadlineForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);

        assertTrue("testConversionDeadline failed",
                    mListActivity.bucketModel.getItem(0).getDeadline().equals("6 day(s)"));
        assertTrue("testConversionDeadline failed",
                    mListActivity.bucketModel.getItem(1).getDeadline().equals("3 week(s)"));
        assertTrue("testConversionDeadline failed",
                    mListActivity.bucketModel.getItem(2).getDeadline().equals("2 month(s)"));
        assertTrue("testConversionDeadline failed",
                    mListActivity.bucketModel.getItem(3).getDeadline().equals("3 month(s)"));
        assertTrue("testConversionDeadline failed",
                    mListActivity.bucketModel.getItem(4).getDeadline().equals("1 year(s)"));
    }

    //Test that units are converted correctly when sorting by the Deadline parameter
    //(e.g. 1 week > 6 days, etc.)
    public void testConversionDuration() {
        mListActivity.bucketModel.clearItems();
        ItemModel mock1 = new ItemModel("1");
        ItemModel mock2 = new ItemModel("2");
        ItemModel mock3 = new ItemModel("3");
        ItemModel mock4 = new ItemModel("4");
        ItemModel mock5 = new ItemModel("5");
        ItemModel mock6 = new ItemModel("6");
        ItemModel mock7 = new ItemModel("7");
        ItemModel mock8 = new ItemModel("8");
        ItemModel mock9 = new ItemModel("9");
        ItemModel mock10 = new ItemModel("10");

        mock1.setDuration("200", "minute(s)");
        mock2.setDuration("4", "hour(s)");
        mock3.setDuration("1", "day(s)");
        mock4.setDuration("25", "hour(s)");
        mock5.setDuration("5", "day(s)");
        mock6.setDuration("1", "week(s)");
        mock7.setDuration("1", "month(s)");
        mock8.setDuration("1", "year(s)");
        mock9.setDuration("15", "month(s)");
        mock10.setDuration("2", "year(s)");

        mListActivity.bucketModel.addItem(mock3);
        mListActivity.bucketModel.addItem(mock2);
        mListActivity.bucketModel.addItem(mock5);
        mListActivity.bucketModel.addItem(mock1);
        mListActivity.bucketModel.addItem(mock8);
        mListActivity.bucketModel.addItem(mock10);
        mListActivity.bucketModel.addItem(mock4);
        mListActivity.bucketModel.addItem(mock7);
        mListActivity.bucketModel.addItem(mock9);
        mListActivity.bucketModel.addItem(mock6);

        ItemModel itemModelObject = new ItemModel();
        Class<?> itemModelClass = itemModelObject.getClass();
        Method paramMethod = null;

        try {
            paramMethod = itemModelClass.getMethod("getDurationForSort");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        mListActivity.sortBucketListAscending(paramMethod);

        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(0).getDuration().equals("200 minute(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(1).getDuration().equals("4 hour(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(2).getDuration().equals("1 day(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(3).getDuration().equals("25 hour(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(4).getDuration().equals("5 day(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(5).getDuration().equals("1 week(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(6).getDuration().equals("1 month(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(7).getDuration().equals("1 year(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(8).getDuration().equals("15 month(s)"));
        assertTrue("testConversionDuration failed",
                mListActivity.bucketModel.getItem(9).getDuration().equals("2 year(s)"));
    }
}