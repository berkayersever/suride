package com.example.myfirstapp;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Emre on 30-Apr-18.
 */

public class ViewSingleRideFragment extends Fragment {

    private MainActivity mainActivity;
    private CommentCursorAdapter commentsAdapter;
    private DBHelper dbHelper;
    private Cursor cursor;
    private Ride ride;

    public static ViewSingleRideFragment newInstance() {
        ViewSingleRideFragment fragment = new ViewSingleRideFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        dbHelper = new DBHelper(mainActivity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the selected ride, current user, and current view
        String strRide = getArguments().getString("Ride");
        ride = new Gson().fromJson(strRide, Ride.class);
        User user = mainActivity.getUser();
        View view = inflater.inflate(R.layout.fragment_view_single_ride, container, false);

        updateUI(user, ride, view);

        TextView tvRiderName = (TextView) view.findViewById(R.id.tv_Rider);
        TextView tvRideDestination = (TextView) view.findViewById(R.id.tv_Destination);
        TextView tvRideDescription = (TextView) view.findViewById(R.id.tv_Description);
        TextView tvRidedDepartureTime = (TextView) view.findViewById(R.id.tv_DepartureTime);
        final TextView tvRideNumSeats = (TextView) view.findViewById(R.id.tv_RideNumSeats);
        ListView lvRideComments = (ListView) view.findViewById(R.id.lv_RideComments);

//        // Set adapter for comments
        cursor = dbHelper.getCommentsByRideID(ride.getId());
        commentsAdapter = new CommentCursorAdapter(mainActivity.getApplicationContext(), cursor);
        lvRideComments.setAdapter(commentsAdapter);

        // Set Text View texts
        tvRiderName.setText(ride.getOwnerName());
        tvRideDestination.setText(ride.getDestination());
        tvRideDescription.setText(ride.getDescription());
        tvRidedDepartureTime.setText(new SimpleDateFormat("HH:mm").format(ride.getDepartureDate()));
        tvRideNumSeats.setText(Integer.toString(ride.getNumSeats()));

        // Listen for new comments
        Button btComment = (Button) view.findViewById(R.id.bt_Comment);
        final EditText etComment = (EditText) view.findViewById(R.id.et_Comment);

        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = etComment.getText().toString();
                mainActivity.addCommentToRide(commentText, ride.getId());
                cursor = dbHelper.getCommentsByRideID(ride.getId());
                commentsAdapter.changeCursor(cursor);
                etComment.setText("");
            }
        });

        // Listen for delete ride button
        ImageButton btDeleteRide = view.findViewById(R.id.ib_DeleteRide);

        btDeleteRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.updateOnDeleteRide(ride.getId());
            }
        });


        // Listen for joins to ride
        final Button btJoinRide = (Button) view.findViewById(R.id.bt_JoinRide);


        Log.i("User/Ride Info", "\nName: " + user.getUsername()
                + "\nUser ID: " + user.getId()
                + "\nJoined Ride ID: " + Long.toString(user.getJoinedRideId())
                + "\nRide ID of page: " + Long.toString(ride.getId())
        );

        if (user.getJoinedRideId().equals( ride.getId())) {
            System.out.println("in");
            btJoinRide.setVisibility(View.GONE);
        }

        btJoinRide.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick (View v) {
               Toast.makeText(mainActivity, "You joined the ride", Toast.LENGTH_SHORT).show();
               int remainingNumSeats = ride.getNumSeats()-1;
               if (remainingNumSeats >= 0) {
                   // Update
                   mainActivity.updateOnJoinRide(ride.getId(), remainingNumSeats);
                   cursor = dbHelper.getCommentsByRideID(ride.getId());
                   commentsAdapter.changeCursor(cursor);

                   // Update local ride data as well
                   ride.setNumSeats(remainingNumSeats);
                   tvRideNumSeats.setText(Integer.toString(remainingNumSeats));
               } else {
                   Toast.makeText(mainActivity, "There is no seats left", Toast.LENGTH_SHORT).show();
               }
               btJoinRide.setVisibility(View.GONE);
           }
        });


        return view;
    }

    public void updateUI(User user, Ride ride, View view) {
        ImageButton btDeleteRide = view.findViewById(R.id.ib_DeleteRide);
        if(user.getUsername().equals(ride.getOwnerName())){
            btDeleteRide.setVisibility(View.VISIBLE);
        } else {
            btDeleteRide.setVisibility(View.GONE);
        }

        Button btJoinRide =  view.findViewById(R.id.bt_JoinRide);
        // If the ride already belongs to the user
        if(user.getUsername().equals(ride.getOwnerName())){
            btJoinRide.setVisibility(View.GONE);
        }
        // If the ride has no seat left
        else if(ride.getNumSeats() <= 0 ) {
            btJoinRide.setVisibility(View.GONE);
        }
        else {
            btJoinRide.setVisibility(View.VISIBLE);
        }
    }
}
