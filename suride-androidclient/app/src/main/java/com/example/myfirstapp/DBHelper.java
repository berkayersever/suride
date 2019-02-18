package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import java.sql.Date;

public class DBHelper extends SQLiteOpenHelper {

    // USER TABLE
    public static final String TABLE_USERS = "Users";
    public static final String USER_ID = "_id"; // primary key
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_TOTAL_RATING = "user_total_rating";
    public static final String USER_RATING_COUNT = "user_rating_count";
    public static final String USER_OWNED_RIDE_ID = "user_owned_ride_id"; // foreign key
    public static final String USER_JOINED_RIDE_ID = "user_joined_ride_id"; // foreign key

    // RIDE TABLE
    public static final String TABLE_RIDES = "Rides";
    public static final String RIDE_ID = "_id"; // primary key
    public static final String RIDE_OWNER_ID = "ride_owner_id"; // foreign key
    public static final String RIDE_OWNER_NAME = "ride_owner_name";
    public static final String RIDE_DESTINATION = "ride_destination";
    public static final String RIDE_DESCRIPTION = "ride_description";
    public static final String RIDE_DEPARTURE_DATE = "ride_departure_date";
    public static final String RIDE_NUMSEATS = "ride_numseats";

    // COMMENT TABLE
    public static final String TABLE_COMMENTS = "Comments";
    public static final String COMMENT_ID = "_id"; // primary key
    public static final String COMMENT_TEXT = "comment_text";
    public static final String COMMENT_OWNER_ID = "comment_owner_id"; // foreign key
    public static final String COMMENT_OWNER_NAME = "comment_owner_name"; // foreign key
    public static final String COMMENT_DATE = "comment_date";
    public static final String COMMENT_ON_RIDE_ID = "comment_on_ride_id"; // foreign key

    private static final String DATABASE_NAME = "uber.db";
    private static final int DATABASE_VERSION = 31;


    // Users table creation sql statement
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USERS + "("
            + USER_ID + " integer primary key autoincrement, "
            + USER_EMAIL + " text not null, "
            + USER_NAME + " text not null, "
            + USER_TOTAL_RATING + " integer, "
            + USER_RATING_COUNT + " integer, " //no boolean storage class in SQLite
            + USER_OWNED_RIDE_ID + " integer, "
            + USER_JOINED_RIDE_ID + " integer"
            + ");";

    // Rides table creation sql statement
    private static final String CREATE_RIDES_TABLE = "create table if not exists "
            + TABLE_RIDES + "("
            + RIDE_ID + " integer primary key autoincrement, "
            + RIDE_OWNER_ID + " integer, "
            + RIDE_OWNER_NAME + " text not null, "
            + RIDE_DESTINATION + " text not null, "
            + RIDE_DEPARTURE_DATE + " text not null, "
            + RIDE_DESCRIPTION + " text not null, "
            + RIDE_NUMSEATS + " integer not null, "
            + "FOREIGN KEY("+RIDE_ID+") REFERENCES "
            + TABLE_USERS + "(" + USER_ID + ")"
            + ");";

    // Comments table creation sql statement
    private static final String CREATE_COMMENTS_TABLE = "create table if not exists "
            + TABLE_COMMENTS + "("
            + COMMENT_ID + " integer primary key autoincrement, "
            + COMMENT_OWNER_ID + " integer not null, "
            + COMMENT_OWNER_NAME + " text not null, "
            + COMMENT_DATE + " text not null, "
            + COMMENT_TEXT + " text not null, "
            + COMMENT_ON_RIDE_ID + " integer not null, "
            + "FOREIGN KEY("+COMMENT_OWNER_ID+") REFERENCES "
            + TABLE_USERS + "(" + USER_ID + "),"
            + "FOREIGN KEY("+COMMENT_OWNER_NAME+") REFERENCES "
            + TABLE_USERS + "(" + USER_NAME + "),"
            + "FOREIGN KEY("+COMMENT_ON_RIDE_ID+") REFERENCES "
            + TABLE_RIDES + "(" + RIDE_ID + ")"
            + ");";


    public void addData(SQLiteDatabase db){
//        insertUser(db,"Cenk");
//        insertUser(db,"Erdem");
//        insertComment(db);
//        insertRide("Besiktas", new Date(), 5, "Gelin");
//        insertRide("GS", new Date(), 5, "Gelmeyin");
    }

    private long insertUser(SQLiteDatabase db, String name, String email) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(USER_NAME, name);
        values.put(USER_EMAIL, email);
        values.put(USER_TOTAL_RATING, 0);
        values.put(USER_RATING_COUNT, 0);
        values.put(USER_OWNED_RIDE_ID, -1);
        values.put(USER_JOINED_RIDE_ID, -1);
        // Insert the new row, returning the primary key value of the new row
        long newUserId = db.insert(TABLE_USERS, null, values);
//        db.close();
        return newUserId;
    }

    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    private long insertRide(SQLiteDatabase db, String destination, Date departureDate, int numSeats, String description, Long ownerID, String ownerName) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RIDE_DESTINATION, destination);
        values.put(RIDE_DEPARTURE_DATE, persistDate(departureDate));
        values.put(RIDE_NUMSEATS, numSeats);
        values.put(RIDE_DESCRIPTION, description);
        values.put(RIDE_OWNER_ID, ownerID);
        values.put(RIDE_OWNER_NAME, ownerName);
        // Insert the new row, returning the primary key value of the new row
        long newRideId = db.insert(TABLE_RIDES, null, values);
//        db.close();
        return newRideId;
    }

    public long insertRide(String destination, Date departureDate, int numSeats, String description, Long ownerID, String ownerName) {
        SQLiteDatabase db = getWritableDatabase();
        return insertRide(db, destination, departureDate, numSeats, description, ownerID, ownerName);
    }

    public long insertComment(String username, Long userID, Date date, String text, Long rideID) {
        SQLiteDatabase db = getWritableDatabase();
        return insertComment(db, username, userID, date, text, rideID);
    }
    private long insertComment(SQLiteDatabase db, String username, Long userID, Date date, String text, Long rideID) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COMMENT_OWNER_NAME, username);
        values.put(COMMENT_OWNER_ID, userID);
        values.put(COMMENT_DATE, persistDate(date));
        values.put(COMMENT_TEXT, text);
        values.put(COMMENT_ON_RIDE_ID, rideID);
        // Insert the new row, returning the primary key value of the new row
        long newCommentId = db.insert(TABLE_COMMENTS, null, values);
//        db.close();
        return newCommentId;
    }

    public long insertUser(String name, String email){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        return insertUser(db, name, email);
    }

    private long changeRideSeats (SQLiteDatabase db, Long ride_id, int newNumSeats){
        ContentValues newValues = new ContentValues();
        newValues.put(RIDE_NUMSEATS, newNumSeats);
        return db.update(TABLE_RIDES, newValues,"_id="+ride_id, null);
    }

    public long changeRideSeats(Long ride_id, int newNumSeats){
        SQLiteDatabase db = getWritableDatabase();
        return changeRideSeats(db, ride_id, newNumSeats);
    }

    private long setUserJoinedRideID (SQLiteDatabase db, Long userID, Long rideID){
        ContentValues newValues = new ContentValues();
        newValues.put(USER_JOINED_RIDE_ID, rideID);
        return db.update(TABLE_USERS, newValues,"_id="+userID, null);
    }

    public long setUserJoinedRideID(Long userID, Long rideID){
        SQLiteDatabase db = getWritableDatabase();
        return setUserJoinedRideID(db, userID, rideID);
    }

    private void incrementRideCount (SQLiteDatabase db, Long userID){
        String valueToIncrementBy = "1";
        String[] bindingArgs = new String[]{ valueToIncrementBy, Long.toString( userID) };

        db.execSQL("UPDATE " + TABLE_USERS +
                        " SET " + USER_RATING_COUNT + " = " + USER_RATING_COUNT + " + ?" +
                " WHERE " + USER_ID + " = ?",
                bindingArgs);
        db.close();

        return;
    }

    public void incrementRideCount(Long userID) {
        SQLiteDatabase db = getWritableDatabase();
        incrementRideCount(db, userID);
    }

    public long deleteRideByID(Long rideID) {
        SQLiteDatabase db = getWritableDatabase();
        return deleteRideByID(db, rideID);
    }

    private long deleteRideByID(SQLiteDatabase db, Long rideID) {
        return db.delete(TABLE_RIDES,"_id="+rideID,null);
    }

    public Cursor getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                USER_ID,
                USER_NAME,
                USER_TOTAL_RATING,
                USER_RATING_COUNT,
                USER_OWNED_RIDE_ID,
                USER_JOINED_RIDE_ID,
                USER_EMAIL
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                USER_TOTAL_RATING + " DESC";

        Cursor c = db.query(
                TABLE_USERS,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                              // The sort order

        );
//        db.close();
        return c;
    }

    public Cursor getAllRides(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                RIDE_ID,
                RIDE_DESTINATION,
                RIDE_DEPARTURE_DATE,
                RIDE_DESCRIPTION,
                RIDE_NUMSEATS,
                RIDE_OWNER_ID,
                RIDE_OWNER_NAME
        };

        Cursor c = db.query(
                TABLE_RIDES,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                RIDE_DEPARTURE_DATE                              // The sort order
        );
//        db.close();
        return c;
    }

    public Cursor getAllComments(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COMMENT_ID,
                COMMENT_TEXT,
                COMMENT_OWNER_NAME,
                COMMENT_ON_RIDE_ID,
                COMMENT_DATE
        };

        Cursor c = db.query(
                TABLE_COMMENTS,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                              // The sort order
        );
//        db.close();
        return c;
    }

    public User getUserByName(String username){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                USER_ID,
                USER_NAME,
                USER_TOTAL_RATING,
                USER_RATING_COUNT,
                USER_OWNED_RIDE_ID,
                USER_JOINED_RIDE_ID,
                USER_EMAIL
        };

        String selection = USER_NAME + " like ?";
        String []  selectionArgs = {username};

        Cursor c = db.query(
                TABLE_USERS,  // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        User user = null;
        if(c.moveToFirst()) { // returns false if the cursor is empty!
            user = new User(c);
        }
        c.close();
        return user;
    }

    public List<Long> getUserIDsByJoinedRideID (Long joinedRideID) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                USER_ID,
                USER_NAME,
                USER_TOTAL_RATING,
                USER_RATING_COUNT,
                USER_OWNED_RIDE_ID,
                USER_JOINED_RIDE_ID,
                USER_EMAIL
        };

        String selection = USER_JOINED_RIDE_ID + " like ?";
        String []  selectionArgs = {Long.toString(joinedRideID)};

        Cursor c = db.query(
                TABLE_USERS,  // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        User user = null;
        List<Long> allUserIDs = new ArrayList<Long>();
        if(c.moveToFirst()) { // returns false if the cursor is empty!
            user = new User(c);
            allUserIDs.add(user.getId());
        }
        c.close();
        return allUserIDs;
    }

    public Ride getRideByID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                RIDE_ID,
                RIDE_DESCRIPTION,
                RIDE_DESTINATION,
                RIDE_NUMSEATS,
                RIDE_DEPARTURE_DATE,
                RIDE_OWNER_NAME,
                RIDE_OWNER_ID
        };

        String selection = RIDE_ID + " like ?";
        String []  selectionArgs = {Long.toString(id)};

        Cursor c = db.query(
                TABLE_RIDES,  // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        Ride ride = null;
        if(c.moveToFirst()) { // returns false if the cursor is empty!
            ride = new Ride(c);
        }
        c.close();
        return ride;
    }

    public Cursor getCommentsByRideID (Long rideID) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COMMENT_ID,
                COMMENT_ON_RIDE_ID,
                COMMENT_OWNER_NAME,
                COMMENT_OWNER_ID,
                COMMENT_DATE,
                COMMENT_TEXT
        };

        String selection = COMMENT_ON_RIDE_ID + " like ?";
        String []  selectionArgs = {Long.toString(rideID)};

        Cursor c = db.query(
                TABLE_COMMENTS,  // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return c;

    }

    public void printTableUsers(){
        Cursor cursor = getAllUsers();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            long id = cursor.getLong(cursor.getColumnIndex(USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            int userRating = cursor.getInt(cursor.getColumnIndex(USER_TOTAL_RATING));
            int userRatingCount = cursor.getInt(cursor.getColumnIndex(USER_RATING_COUNT));
            int userRideId = cursor.getInt(cursor.getColumnIndex(USER_OWNED_RIDE_ID));

            Log.d("CS 310", id + "\t" + userName + "\t" + userRating + "\t" + userRatingCount + "\t"+ userRideId);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void printTableRides(){
        Cursor cursor = getAllRides();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            long id = cursor.getLong(cursor.getColumnIndex(RIDE_ID));
            String rideDestination = cursor.getString(cursor.getColumnIndex(RIDE_DESTINATION));
            Date rideDate = new Date(cursor.getLong(cursor.getColumnIndex(RIDE_DEPARTURE_DATE)));
            String cuteDate = new SimpleDateFormat("HH:mm").format(rideDate);

            Log.d("Ride Test", id + "\t" + rideDestination + "\t" + cuteDate);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void printTableComments(){
        Cursor cursor = getAllComments();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            long id = cursor.getLong(cursor.getColumnIndex(COMMENT_ID));
            long rideId = cursor.getLong(cursor.getColumnIndex(COMMENT_ON_RIDE_ID));
            String commentText = cursor.getString(cursor.getColumnIndex(COMMENT_TEXT));
            String commentOwnerName = cursor.getString(cursor.getColumnIndex(COMMENT_OWNER_NAME));
            Date commentDate = new Date(cursor.getLong(cursor.getColumnIndex(COMMENT_DATE)));
            String cuteDate = new SimpleDateFormat("HH:mm").format(commentDate);

            Log.d("Comments Test", id + "\tRide:" + rideId + "\t" + commentOwnerName + "\t" + commentText + "\t" + cuteDate);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_RIDES_TABLE);
        sqLiteDatabase.execSQL(CREATE_COMMENTS_TABLE);
        addData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Downgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }
}
