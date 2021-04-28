package com.prototype.nuru.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prototype.nuru.models.Report;
import com.prototype.nuru.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "nuru";

    // Table names
    private static final String TABLE_USER = "users";
    private static final String TABLE_REPORT = "reports";

    // Common columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CREATED_AT = "created_at";

    // User Table Columns names
    private static final String COLUMN_USER_FNAME = "fname";
    private static final String COLUMN_USER_LNAME = "lname";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Reports Table Columns names
    private static final String COLUMN_REPORT_POST = "post";
    private static final String COLUMN_REPORT_TAG = "tags";
    private static final String COLUMN_REPORT_LOCATION = "location";
    private static final String COLUMN_REPORT_MEDIA = "media";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_FNAME + " TEXT,"
            + COLUMN_USER_LNAME + " TEXT," + COLUMN_USER_PHONE + " TEXT," + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_CREATED_AT + " TEXT" + ")";

    private String CREATE_REPORT_TABLE = "CREATE TABLE " + TABLE_REPORT + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_REPORT_POST + " TEXT,"
            + COLUMN_REPORT_TAG + " TEXT," + COLUMN_REPORT_LOCATION + " TEXT," + COLUMN_REPORT_MEDIA + " TEXT,"
            + COLUMN_CREATED_AT + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_REPORT_TABLE = "DROP TABLE IF EXISTS " + TABLE_REPORT;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_REPORT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Tables if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_REPORT_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * This method is used to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FNAME, user.getFname());
        values.put(COLUMN_USER_LNAME, user.getLname());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_CREATED_AT, getDateTime());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is used to fetch a single user by id
     *
     * @return User user
     */
    public User getUser(int id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_USER_FNAME,
                COLUMN_USER_LNAME,
                COLUMN_USER_PHONE,
                COLUMN_USER_PASSWORD,
                COLUMN_CREATED_AT,
                COLUMN_USER_EMAIL
        };

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                COLUMN_ID + " = " + id,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
        user.setFname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FNAME)));
        user.setLname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
        user.setDateCreated(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)));

        cursor.close();
        db.close();

        // return user
        return user;
    }

    /**
     * This method is used to fetch a single user by email and password
     *
     * @return User user
     */
    public User fetchUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_USER_FNAME,
                COLUMN_USER_LNAME,
                COLUMN_USER_PHONE,
                COLUMN_USER_PASSWORD,
                COLUMN_CREATED_AT,
                COLUMN_USER_EMAIL
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);

        // Traversing through all rows and adding to list
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
        user.setFname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FNAME)));
        user.setLname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
        user.setDateCreated(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)));

        cursor.close();
        db.close();

        // return user
        return user;
    }

    /**
     * This method is used to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FNAME, user.getFname());
        values.put(COLUMN_USER_LNAME, user.getLname());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_EMAIL, user.getEmail());

        // updating row
        db.update(TABLE_USER, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is used to update user record by email
     *
     * @param user
     */
    public void updateUser1(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FNAME, user.getFname());
        values.put(COLUMN_USER_LNAME, user.getLname());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_EMAIL, user.getEmail());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }

    /**
     * This method is used to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is used to create report record
     *
     * @param report
     */
    public void addReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REPORT_POST, report.getPost());
        values.put(COLUMN_REPORT_TAG, report.getTags());
        values.put(COLUMN_REPORT_LOCATION, report.getLocation());
        values.put(COLUMN_REPORT_MEDIA, report.getMedia());
        values.put(COLUMN_CREATED_AT, getDateTime());

        // Inserting Row
        db.insert(TABLE_REPORT, null, values);
        db.close();
    }

    /**
     * This method is used to fetch a single user
     *
     * @return Report report
     */
    public Report getReport(int id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_REPORT_POST,
                COLUMN_REPORT_TAG,
                COLUMN_REPORT_MEDIA,
                COLUMN_REPORT_LOCATION,
                COLUMN_CREATED_AT
        };

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table

        Cursor cursor = db.query(TABLE_REPORT, //Table to query
                columns,    //columns to return
                COLUMN_ID + " = " + id,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor != null)
            cursor.moveToFirst();

        Report report = new Report();
        report.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
        report.setPost(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_POST)));
        report.setTags(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_TAG)));
        report.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_LOCATION)));
        report.setMedia(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_MEDIA)));
        report.setDateCreated(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)));

        cursor.close();
        db.close();

        // return report
        return report;
    }

    /**
     * This method is used to fetch all reports as a list
     *
     * @return list
     */
    public ArrayList<Report> getAllReports() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_REPORT_POST,
                COLUMN_REPORT_TAG,
                COLUMN_REPORT_MEDIA,
                COLUMN_REPORT_LOCATION,
                COLUMN_CREATED_AT
        };
        // sorting orders
        String sortOrder =
                COLUMN_CREATED_AT + " DESC";
        ArrayList<Report> reportList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_REPORT, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Report report = new Report();
                report.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                report.setPost(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_POST)));
                report.setTags(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_TAG)));
                report.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_LOCATION)));
                report.setMedia(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_MEDIA)));
                report.setDateCreated(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)));
                // Adding user record to list
                reportList.add(report);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return report list
        return reportList;
    }

    /**
     * This method is used to update report record
     *
     * @param report
     */
    public void updateReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REPORT_POST, report.getPost());
        values.put(COLUMN_REPORT_TAG, report.getTags());
        values.put(COLUMN_REPORT_MEDIA, report.getMedia());

        // updating row
        db.update(TABLE_USER, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(report.getId())});
        db.close();
    }

    /**
     * This method is used to delete report record
     *
     * @param report
     */
    public void deleteReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_REPORT, COLUMN_ID + " = ?",
                new String[]{String.valueOf(report.getId())});
        db.close();
    }

    /**
     * This method is used to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT id FROM admin WHERE email = '' AND password = '';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}

