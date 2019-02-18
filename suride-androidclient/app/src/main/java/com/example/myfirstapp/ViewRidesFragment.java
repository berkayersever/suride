package com.example.myfirstapp;

//import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Emre on 24-Apr-18.
 */

public class ViewRidesFragment extends Fragment {
    private ListAdapter adapter;
    private static final String VIEW_FRAGMENT_TAG = "Take this view_rides_fragment";
    private MainActivity mainActivity;
    List<Ride> ridesList;
    private View view;
    private DBHelper dbHelper;

    public static ViewRidesFragment newInstance() {
        ViewRidesFragment fragment = new ViewRidesFragment();
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

        view = inflater.inflate(R.layout.fragment_view_rides, container, false);

        // Listen to post button
        view.findViewById(R.id.button_postRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.postRide();
            }
        });


        // Set the adapter for list view;
        ListView tasksListView = (ListView) view.findViewById(R.id.listView_rides);
        Cursor cursor = dbHelper.getAllRides();
        RideCursorAdapter adapter = new RideCursorAdapter(mainActivity.getApplicationContext(), cursor);
        tasksListView.setAdapter(adapter);

        // Set the listener for list view
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
//                System.out.println("Position: " + position);
//                System.out.println("ID: " + id);

                Ride ride = dbHelper.getRideByID(id);
                String strRide = (new Gson()).toJson(ride);
                bundle.putString("Ride", strRide);

                ViewSingleRideFragment fragment = new ViewSingleRideFragment();
                fragment.setArguments(bundle);

                // Hide Post Button
                View rootView = view.getRootView();
                FloatingActionButton buttonPostRide = (FloatingActionButton) rootView.findViewById(R.id.button_postRide);
                buttonPostRide.setVisibility(View.GONE);

                // Change Fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack("");
                transaction.commit();
            }
        });

        return view;
    }

    // TODO: Read rides_list from database on data change
}
