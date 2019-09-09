package edu.ncc.postnel.nccdepartmentdatabasev2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/***************************************************************************************************
 ****** This is the Project 3 for the Mobile application Course in Nassau Community College  *******
 *
 * This application uses the Nassau Community College Department Directory to provide a table with
 * information to be parsed with SQLITE. This is DeptInfoHelper class that contains the code for the
 * setup for SQLite database, URI scheme and final Strings to be used in the application.
 *
 * ***********************  Vagner Machado - Fall 2016 - N00820127  ********************************
 **************************************************************************************************/
public class DepartmentInfoHelper extends SQLiteOpenHelper {

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "edu.ncc.nccdepartmentdatabase";

    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    // table name
    public static final String TABLE_NAME = "departments";

    // columns in the table
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String LOCATION = "location";
    public static final String PHONE = "phone";

    // database version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "departmentInformation.db";

    public DepartmentInfoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /**
     * onCreate method - creates a SQLite database table
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME +
                " TEXT, " + LOCATION + " TEXT, " + PHONE + " TEXT);");
    }

    @Override
    /**
     * onUpgrade method - verifies if the database has a current table version. If it is not,
     * the table is dropped.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

