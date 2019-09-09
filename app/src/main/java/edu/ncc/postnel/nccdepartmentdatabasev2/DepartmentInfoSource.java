package edu.ncc.postnel.nccdepartmentdatabasev2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************************************
 ****** This is the Project 3 for the Mobile application Course in Nassau Community College  *******
 *
 * This application uses the Nassau Community College Department Directory to provide a table with
 * information to be parsed with SQLITE. This is DepartmentInfoSource class that contains the code
 * to run the prededined and custom queries. Comments for each implemented method can be found above
 * each method's signature.
 *
 * ***********************  Vagner Machado - Fall 2016 - N00820127  ********************************
 **************************************************************************************************/

public class DepartmentInfoSource {
    //instance data
    private SQLiteDatabase database;
    private DepartmentInfoHelper deptHelper;
    private static final String TAG = "debugging";
    private static String ORDER_LOCATION = DepartmentInfoHelper.LOCATION;
    private static String ORDER_NAME = DepartmentInfoHelper.NAME;
    private static String ORDER_ID = DepartmentInfoHelper._ID;
    private String[] allColumns = { DepartmentInfoHelper._ID, DepartmentInfoHelper.NAME, DepartmentInfoHelper.LOCATION,
            DepartmentInfoHelper.PHONE, };


    public DepartmentInfoSource(Context context) {
        deptHelper = new DepartmentInfoHelper(context);
    }

    public void open() throws SQLException {
        database = deptHelper.getWritableDatabase();
    }

    public void close() {
        deptHelper.close();
    }

    /**
     * addDept - adds a dept to the table (?)
     * @param name - dept name
     * @param location - dept location
     * @param phone - dept phone
     * @return entry - a dept entry
     */
    public DepartmentEntry addDept(String name, String location, String phone) {
        ContentValues values = new ContentValues();
        values.put(DepartmentInfoHelper.NAME, name);
        values.put(DepartmentInfoHelper.LOCATION, location);
        values.put(DepartmentInfoHelper.PHONE, phone);
        long insertId = database.insert(DepartmentInfoHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns, DepartmentInfoHelper._ID + " = " +insertId, null, null, null, null);
        cursor.moveToFirst();
        DepartmentEntry entry = cursorToEntry(cursor);
        cursor.close();
        return entry;
    }

    /**
     * deleteDept - deletes a dept from the data received
     * @param dept - the DepartmentEntry to be deleted
     */
    public void deleteDept(DepartmentEntry dept) {
        long id = dept.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(DepartmentInfoHelper.TABLE_NAME, DepartmentInfoHelper._ID
                + " = " + id, null);
    }

    /**
     * getAllDepartments method - gets all depts from table
     * @return - depts - a list with all the dept data
     */
    public List<DepartmentEntry> getAllDepartments() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }

    /**
     * queryDepartmens method - queries for departments with id from 50 to 75,order by id
     * @return depts - a list with values that match the user query
     */
    public List<DepartmentEntry> queryDepartments() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME,
                allColumns, DepartmentInfoHelper._ID + " >= 50 AND " +
                        DepartmentInfoHelper._ID + " <= 75 ",
                null, null, null, ORDER_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }

    /**
     * private method cursorToEntry - used to iterate and assign data queried
     * @param cursor - a Cursor object
     * @return - entry - a Dept Entry object
     */
    private DepartmentEntry cursorToEntry(Cursor cursor) {
        DepartmentEntry entry = new DepartmentEntry();
        entry.setId(cursor.getLong(0));
        entry.setName(cursor.getString(1));
        entry.setLocation(cursor.getString(2));
        entry.setPhone(cursor.getString(3));
        return entry;
    }

    /**
     * queryDean method - queries for depts that have the word Dean
     * @return depts - a list with values that match the user query
     */
    public List<DepartmentEntry> queryDean() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        //This is call to show all departments with 'Dean'
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.NAME + " LIKE ? ",
                new String[] { "%dean%" },
                null, null, ORDER_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }
    /**
     * queryTower method - queries for depts in the tower and sorts data by floor
     * @return depts - a list with values that match the user query
     */
    public List<DepartmentEntry> queryTower() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        //This is call to show all departments with 'Dean'
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.LOCATION + " LIKE ? ",
                new String[] { "%tower%" },
                null, null, ORDER_LOCATION);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }

    /**
     * queryCenter method - queries for the word center in location or dept name
     * @return depts - a list with values that match the user query
     */
    public List<DepartmentEntry> queryCenter() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        //This is call to show all departments with 'Dean'
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.NAME + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? ",
                new String[] { "%center%", "%center%" },
                null, null, ORDER_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }

    /**
     * queryClusterBuildingAtoD method - queries for depts in Cluster/Building A through D
     * @return depts - a list with values that match the user query
     */
    public List<DepartmentEntry> queryClusterBuildingAtoD() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? OR " +
                        DepartmentInfoHelper.LOCATION + " LIKE ? ",
                //the omitted building and cluster variation would not return any result
                new String[] {
                        "%Cluster%A%", "%A%Cluster%",
                        "%Cluster%B%", "%B%Cluster%",
                        "%Cluster%C%", "%C%Cluster%",
                        "%Cluster%D%", "%D%Cluster%",
                        "%Building%A%", "%A%Building%",
                        "%Building%B%", "%B%Building%",
                        "%Building%C%", "%C%Building%",
                        "%Building%D%", "%D%Building%"},
                null, null, ORDER_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }

    /**
     * querySerch method - queries for the user typed entry
     * @param search - the strint the user typed
     * @return depts - a list with values that match the user query
     */
    public List<DepartmentEntry> querySearch(String search) {
        //String val = "office"; //test
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        Log.d(TAG, "inside the querySearch with string: " + search);
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.NAME + " LIKE ? ",
                new String[] {"%" + search + "%"},
                null, null, ORDER_ID);
        Log.d(TAG, "This is the query specifier: " + "%" + search + "%"); //it was not returnin without concat % before and after param
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return dpts;
    }

    /******************************** QUERY EXAMPLES **********************************************/

    //This is the original call for cluster and building
        /*Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.LOCATION + " LIKE ? OR " + DepartmentInfoHelper.LOCATION + " LIKE ? ",
                new String[] { "%Cluster%", "%Building%" },
                null, null, ORDER_BY);*/

    // this call shows only the cluster c entries
       /* Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.LOCATION + " LIKE ? ",
                new String[] { "%Cluster%C%" },
                null, null, ORDER_BY); */

    //This is the call to show C Cluster and Cluster C
        /*Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.LOCATION + " LIKE ? OR " + DepartmentInfoHelper.LOCATION + " LIKE ? ",
                new String[] { "%Cluster%C%", "%C%Cluster%" },
                null, null, ORDER_BY);*/

    //This is the call to show Student Department Center
    //The capitalization does not matter in the String [] (the param), hence will find C Cluster and c cluster
    //The String [] stuff is necessary when using the selection clause (3rd param)
    //Ordered by Dept name
        /*Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper.LOCATION + " LIKE ? ",
                new String[] { "%Student Services Center%"},
                //instead of hanving string[], set that to null and substitute ? after like with single quote 'cluster%c'
                null, null, ORDER_DEPT);*/

       /* Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME, allColumns,
                DepartmentInfoHelper._ID + " BETWEEN 50 AND 75 ",
                null,
                //instead of hanving string[], set that to null and substitute ? after like with single quote 'cluster%c'
                null, null, ORDER_ID);*/
}