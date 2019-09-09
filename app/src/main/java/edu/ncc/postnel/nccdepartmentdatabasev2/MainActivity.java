package edu.ncc.postnel.nccdepartmentdatabasev2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;

import static android.R.attr.data;

/***************************************************************************************************
 ****** This is the Project 3 for the Mobile application Course in Nassau Community College  *******
 *
 * This application uses the Nassau Community College Department Directory to provide a table with
 * information to be parsed with SQLITE. This is Main class that contains the code for the
 * launcher page of the developed application. The code in this activity controls the clicks on
 * various buttons that allow the user to run queries. This activity extends ListActivity which
 * enables the parsed returned data to be displayed in a list. Comments for each implemented method
 * can be found above each method's signature.
 *
 * ***********************  Vagner Machado - Fall 2016 - N00820127  ********************************
 **************************************************************************************************/

public class MainActivity extends ListActivity {

    //instance data
    private DepartmentInfoSource datasource;        //an DeptInfoSource object
    private ArrayAdapter<DepartmentEntry> adapter;  //an array to support display of data queried
    private static final String QUERY = "";         //search query entered by user
    public static final String TAG = "debugging";   //debugger
    String query;                                   //will point to string query entered by user

    @Override
    /**
     * onCreate method - here the layout and Bundle data are loaded.
     * In this application, the bundle data is not implement for screen rotation
     * @param savedInstanceState - the Bundle data used to recreate the app after screen rotation
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //case this activity started by an search intent, query is not empty
        Intent intent = getIntent();
        query = intent.getStringExtra(QUERY);
        Log.d(TAG, "This is query: " + query);

        datasource = new DepartmentInfoSource(this);
        datasource.open();

        List<DepartmentEntry> values = datasource.getAllDepartments();

        // add departments to the database if it is currently empty
        if (values.isEmpty())
        {
            new ParseURL().execute();
        }
    }

    /**
     * onClick method - controls the clicks with predefined queries
     * @param view - the Button View clicked
     */
    public void onClick(View view) {
        DepartmentEntry dept;
        List<DepartmentEntry> values;
        switch (view.getId()) {
            //queries for depts with word dean
            case R.id.dean:
                values = datasource.queryDean();
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
                break;
            //queries for depts located in tower, and organizes data by floor
            case R.id.tower:
                values = datasource.queryTower();
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
                break;
            //queries for depts that ocntain center on location or name
            case R.id.center:
                values = datasource.queryCenter();
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
                break;
            //queries for depts located in Cluster/Building A through D
            case R.id.clusterBuilding:
                values = datasource.queryClusterBuildingAtoD();
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * onDestroy method - closes the DeptInfoSource object
     */
    public void onDestroy()
    {
        datasource.close();
        super.onDestroy();
    }

    /**
     * onClickSearch method - call method for when user clicks on search button
     * Starts the Search Activity, where user can enter the term to be queried,
     * and with it the request code
     */
    public void onClickSearch(View v)
    {
        if(v.getId() == R.id.searchButton) {
            Intent intent = new Intent(this, Search.class);
            startActivityForResult(intent, 0);
        }
    }

    /**
     * onActivityResult method - callback method for when the user types a query into
     * the edit Text of search.java. That entry is placed in a bundle and retrieved from that
     * bundle in the onActivityResult method. The query passed by the user is used as a
     * parameter to call method querySearch. The matching items are displayed in the UI
     * @param requestCode - The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode - The integer result code returned by the child activity through its setResult().
     * @param data - An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String query = "";
        DepartmentEntry dept;
        List<DepartmentEntry> values;
        if (data != null) {
            Bundle b = data.getExtras();
            query = b.getString("query");
            values = datasource.querySearch(query);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * ParseURL class gets the table data as the app is launched
     * AsyncTask allows this to happen asynchronously
     */
    private class ParseURL extends AsyncTask<Void, Void, String> {

        @Override
        /**
         * doInBackground method - gets the table data from NCC page
         * @param - the last of them is params ( variable length param)
         */
        protected String doInBackground(Void... params) {
            String str;
            String deptName;
            String deptPhone;
            String deptLocation;
            Document doc;
            int count = 0;
            //iteration through the table
            try {
                // connect to the webpage
                doc = Jsoup.connect("http://www.ncc.edu/contactus/deptdirectory.shtml").get();

                // find the body of the webpage
                Elements tableEntries = doc.select("tbody");
                for (Element e : tableEntries)
                {
                    // look for a row in the table
                    Elements trs = e.getElementsByTag("tr");

                    // for each element in the row (there are 5)
                    for (Element e2 : trs)
                    {
                        // get the table descriptor
                        Elements tds = e2.getElementsByTag("td");

                        // ignore the first row
                        if (count > 0) {
                            // get the department name and remove the formatting tags
                            deptName = tds.get(0).text();

                            // get the department phone number
                            deptPhone = tds.get(1).text();

                            // get the department location
                            deptLocation = tds.get(4).text();

                            datasource.addDept(deptName, deptLocation, deptPhone);
                        }
                        count++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        /**
         * According to comment, displays UI element's title if available
         */
        protected void onPostExecute(String result) {
            //if you had a ui element, you could display the title
            Log.d("PARSING", "async task has completed");
        }
    }
}
