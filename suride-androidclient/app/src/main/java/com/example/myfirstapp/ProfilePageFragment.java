package com.example.myfirstapp;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Emre on 24-Apr-18.
 */

public class ProfilePageFragment extends Fragment {
    private MainActivity mainActivity;

    public static ProfilePageFragment newInstance() {
        ProfilePageFragment fragment = new ProfilePageFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_page_new, container, false);
        TextView tvUserName = (TextView) view.findViewById(R.id.tv_ProfileName);
        TextView tvRating = (TextView) view.findViewById(R.id.tv_Rating);
        TextView tvEmail = (TextView) view.findViewById(R.id.tv_Email);
        TextView tvRideCount = (TextView) view.findViewById(R.id.tv_TotalRides);

        String username = mainActivity.getUser().getUsername();
        DBHelper dbHelper = new DBHelper(mainActivity.getApplicationContext());

        User user = dbHelper.getUserByName(username);

        tvUserName.setText(user.getUsername());

        double averageRating = user.getAverageRating();
        tvRating.setText(Double.toString(averageRating));

        tvRideCount.setText(Integer.toString(user.getRatingCount()));

        //        tvEmail.setText(user.getEmail());
        tvEmail.setText(user.getEmail());

        // listen to sign-out button
        view.findViewById(R.id.button_signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.signOut();
            }
        });

        return view;
    }
}
