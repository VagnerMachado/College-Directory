package edu.ncc.postnel.nccdepartmentdatabasev2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/***************************************************************************************************
 ****** This is the Project 3 for the Mobile application Course in Nassau Community College  *******
 *
 * This application uses the Nassau Community College Department Directory to provide a table with
 * information to be parsed with SQLITE. This is Search class that contains the code for the
 * launcher page of the developed application. The code in this activity controls the value typed
 * by the user and uses it to run a query. Comments for each implemented method can be found above
 * each method's signature.
 *
 * ***********************  Vagner Machado - Fall 2016 - N00820127  ********************************
 **************************************************************************************************/

public class Search extends AppCompatActivity {
    EditText textField;
    Bundle bundle;

    @Override
    /**
     * onCreate method - instance data is initialized and icon is placed on action bar
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        textField = (EditText)findViewById(R.id.searchBox);

        //places icon on action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ncc_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    /**
     * onClick method - handles the click on go button
     * warns the user if editText is empty
     * trims the entry and passes it with the intent
     * @param view - the one and only 'go' button
     */
    public void onClick(View view)
    {
        String query =(((EditText) findViewById(R.id.searchBox)).getText().toString()).trim();
        if (query.length() == 0)
        {
            // if nothing is in text field, a toast is displayed with warning message
            Toast toast = Toast.makeText(getApplicationContext(), R.string.no_name, Toast.LENGTH_LONG);
            toast.setGravity(0, 0, 300);
            toast.show();
        }
        else {
            bundle = new Bundle();
            bundle.putString("query", query);
            this.getIntent().putExtras(bundle);
            this.setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}
