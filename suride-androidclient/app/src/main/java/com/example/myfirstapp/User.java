package com.example.myfirstapp;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emre on 25-Apr-18.
 */

public class User {
    private Long id;
    private String username;
    private String email;
    private int totalRating;
    private int ratingCount;
    private Long ownedRideId;
    private Long joinedRideId;
    private List<Ride> previousRides;

    public User(String username) {
        this.username = username;
        this.previousRides = new ArrayList<Ride>();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(DBHelper.USER_ID));
        this.email = cursor.getString(cursor.getColumnIndex(DBHelper.USER_EMAIL));
        this.username = cursor.getString(cursor.getColumnIndex(DBHelper.USER_NAME));
        this.totalRating = cursor.getInt(cursor.getColumnIndex(DBHelper.USER_TOTAL_RATING));
        this.ratingCount = cursor.getInt(cursor.getColumnIndex(DBHelper.USER_RATING_COUNT));
        this.ownedRideId = cursor.getLong(cursor.getColumnIndex(DBHelper.USER_OWNED_RIDE_ID));
        this.joinedRideId = cursor.getLong(cursor.getColumnIndex(DBHelper.USER_JOINED_RIDE_ID));
        this.previousRides = null;
    }

    public double getAverageRating() {
        double rating;
        if (ratingCount == 0 ){
            rating = 0.0;
        }
        else {
            rating =  (double) totalRating /  ratingCount;
        }
        return rating;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getOwnedRideId() {
        return ownedRideId;
    }

    public Long getJoinedRideId() {
        return joinedRideId;
    }

    public List<Ride> getPreviousRides() {
        return previousRides;
    }

    public void setPreviousRides(List<Ride> previousRides) {
        this.previousRides = previousRides;
    }
}
