package com.example.myfirstapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Emre on 09-Apr-18.
 */

public class Ride {
    private Long id;
    private Long ownerID;
    private String ownerName;
    private String destination;
    private Date departureDate;
    private int numSeats;
    private String description;
    private List<Comment> comments;
    private List<String> ridees;

    // Constructor
    public Ride(Long ownerID, String ownername, String destination, Date departureDate, int numSeats, String description) {
        this.ownerID = ownerID;
        this.ownerName = ownername;
        this.destination = destination;
        this.departureDate = departureDate;
        this.numSeats = numSeats;
        this.description = description;
        this.comments = new ArrayList<Comment>();
        this.ridees = new ArrayList<String>();
    }

    public Ride(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(DBHelper.RIDE_ID));
        this.ownerID = cursor.getLong(cursor.getColumnIndex(DBHelper.RIDE_OWNER_ID));
        this.ownerName = cursor.getString(cursor.getColumnIndex(DBHelper.RIDE_OWNER_NAME));
        this.destination = cursor.getString(cursor.getColumnIndex(DBHelper.RIDE_DESTINATION));
        this.departureDate = new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.RIDE_DEPARTURE_DATE)));
        this.numSeats = cursor.getInt(cursor.getColumnIndex(DBHelper.RIDE_NUMSEATS));
        this.description = cursor.getString(cursor.getColumnIndex(DBHelper.RIDE_DESCRIPTION));
    }

    public static class Comment {
        private Long id;
        private String text;
        private String ownerUsername;
        private Long ownerID;
        private Long rideID;
        private Date date;

        public Comment(String text, String ownerUsername, Long ownerID, Long rideID, Date date) {
            this.text = text;
            this.ownerUsername = ownerUsername;
            this.ownerID = ownerID;
            this.rideID = rideID;
            this.date = date;
        }

        public Comment(Cursor cursor) {
            this.id = cursor.getLong(cursor.getColumnIndex(DBHelper.COMMENT_ID));
            this.text = cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT_TEXT));
            this.ownerID = cursor.getLong(cursor.getColumnIndex(DBHelper.COMMENT_OWNER_ID));
            this.rideID = cursor.getLong(cursor.getColumnIndex(DBHelper.COMMENT_ON_RIDE_ID));
            this.ownerUsername = cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT_OWNER_NAME));
            this.date = new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COMMENT_DATE)));
        }

        public Long getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getOwnerUsername() {
            return ownerUsername;
        }

        public Long getOwnerID() {
            return ownerID;
        }

        public Long getRideID() {
            return rideID;
        }

        public Date getDate() {
            return date;
        }
    }

    public void addComment (Comment comment) {
        this.comments.add(comment);
    }

    // Ridee take a seat from ride
    public void takeSeat(String ridee) {
        this.ridees.add(ridee);
        this.numSeats--;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getRidees() {
        return ridees;
    }

    public void setRidees(List<String> ridees) {
        this.ridees = ridees;
    }
}
