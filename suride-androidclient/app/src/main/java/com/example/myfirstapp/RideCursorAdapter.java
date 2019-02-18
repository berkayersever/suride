package com.example.myfirstapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RideCursorAdapter extends CursorAdapter{


    public RideCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.custom_row_rides_list_new, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Ride ride = new Ride(cursor);

        try {
            ((TextView)view.findViewById(R.id.userID)).setText(ride.getOwnerName());
            ((TextView)view.findViewById(R.id.destination)).setText(ride.getDestination());

            TextView tvNumSeats = view.findViewById(R.id.numSeats);
            tvNumSeats.setText(Integer.toString(ride.getNumSeats()));

            if (ride.getNumSeats() == 0 ) {
                tvNumSeats.setTextColor(Color.RED);
            } else {
                tvNumSeats.setTextColor(Color.BLACK);
            }

            Date rideDate = ride.getDepartureDate();
            String time = new SimpleDateFormat("HH:mm").format(rideDate);
            String day = new SimpleDateFormat("MM/dd").format(rideDate);
            ((TextView)view.findViewById(R.id.departureTime)).setText(time);
            ((TextView)view.findViewById(R.id.departureDate)).setText(day);

        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }


}
