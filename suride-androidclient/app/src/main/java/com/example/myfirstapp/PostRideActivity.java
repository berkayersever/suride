//TODO: Change this to a Fragment.

package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Emre on 29-Mar-18.
 */

public class PostRideActivity extends AppCompatActivity
{
    private String username;
    private Ride ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ride);

        Bundle b = getIntent().getExtras();
        username = b.getString("Username");

        // Listen to create ride button
        findViewById(R.id.button_CreateRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRide(v);
            }
        });
    }

    public void createRide(View buttonView) {
        // Read values from view

        DBHelper dbHelper = new DBHelper(this);
        User ride_owner = dbHelper.getUserByName(username);
        Long ride_owner_id = ride_owner.getId();

        // Get the parent layout to  be able to read other edit text fields.
        View view = (View) buttonView.getParent();

        EditText etDestination = (EditText) view.findViewById(R.id.editText_Destination);
        TimePicker tpDepartureDate = (TimePicker) view.findViewById(R.id.timePicker_DepartureDate);
        EditText etNumSeats = (EditText) view.findViewById(R.id.editText_NumSeats);
        EditText etDescription = (EditText) view.findViewById(R.id.editText_Description);

        // Process user input from above fields (Dummy values for now)
        // TODO: process user input properly
        try {
            String destination = etDestination.getText().toString();


            Date today = new Date();
            System.out.println(today.getDate());
            Date departureDate = new Date(
                    today.getYear(),
                    today.getMonth(),
                    today.getDate(),
                    tpDepartureDate.getHour(),
                    tpDepartureDate.getMinute()
            );

            int numSeats = Integer.parseInt( etNumSeats.getText().toString() );
            String description = etDescription.getText().toString();

            // Create a new ride object and send it.
            ride = new Ride(ride_owner_id, username, destination, departureDate,numSeats,description);
            addRide(ride);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Number of seats must be valid integer", Toast.LENGTH_SHORT).show();
            Log.e("Create Ride", "Number of seats must be valid integer", e);
        }

    }

    public void addRide(Ride ride) {
        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.
//        resultIntent.putExtra("some_key", ride);

        String strRide = (new Gson()).toJson(ride);
        resultIntent.putExtra("Take this ride", strRide);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
