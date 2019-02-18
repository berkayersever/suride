package com.example.myfirstapp;

import android.app.Activity;
//import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private User user;
    private String userID;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Request Code for Sign-in
    private static final int RC_POST_RIDE = 9002; // Request Code for PostRide
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // TODO: Get the server_client_id from teammates
//                 .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // listen to sign-in button
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        //Add your action onClick
                        selectedFragment = ProfilePageFragment.newInstance();
                        break;
                    case R.id.navigation_rides:
                        selectedFragment = ViewRidesFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.addToBackStack("");
                transaction.commit();
                return true;
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        // A ride was posted, extract the ride and update the list view.
        else if (requestCode == RC_POST_RIDE){
            if (resultCode == Activity.RESULT_OK) {
                String returnValue = data.getStringExtra("Take this ride");
                Ride newRide = new Gson().fromJson(returnValue, Ride.class);
                addRide(newRide);
                setDefaultFragment();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Get Sign-in Account
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
//            Log.i("IDTOKEN", idToken);

            if (idToken != null) {
                try {
                    // send ID Token to server and validate
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("https://yourbackend.example.com/tokensignin");

                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    int statusCode = response.getStatusLine().getStatusCode();
                    final String responseBody = EntityUtils.toString(response.getEntity());
                    Log.i("SERVER RESPONSE", "Signed in as: " + responseBody);

                } catch (UnsupportedEncodingException e) {
                    Log.e("handleSignInResult", "Error sending ID token to backend.", e);
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.e("handleSignInResult", "Error sending ID token to backend.", e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("handleSignInResult", "Error sending ID token to backend.", e);
                    e.printStackTrace();
                }
            }

            //TODO: Verify the integrity of the ID Token
            // https://developers.google.com/identity/sign-in/android/backend-auth#verify-the-integrity-of-the-id-token
            // Signed in successfully, show authenticated UI.
            Log.d("handleSignInResult", "success");
            updateUI(account);
            Log.d("handleSignInResult", account.getDisplayName());

            setUser(account.getDisplayName(), account.getEmail());
            //Manually displaying the View Rides Fragment when sign in succeeds.
            setDefaultFragment();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("handleSignInResult", e.getMessage(), e);
            updateUI(null);
        }
    }

    @Override
    protected  void onStart(){
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            setUser(account.getDisplayName(), account.getEmail());
        }
        updateUI(account);
    }

    public void  updateUI(GoogleSignInAccount account) {
        // References to view objects
        FrameLayout fl = (FrameLayout) findViewById(R.id.frame_layout);
        SignInButton btSignIn = findViewById(R.id.sign_in_button);
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id. navigationView);
        ImageView ivBackground  =(ImageView) findViewById(R.id.iv_Background);
        ImageView ivLogo =(ImageView) findViewById(R.id.iv_Logo);

        if (account != null) {
//            userID = account.getDisplayName();
            fl.setVisibility(View.VISIBLE);
            btSignIn.setVisibility(View.GONE);
            bnv.setVisibility(View.VISIBLE);
            ivBackground.setVisibility(View.GONE);
            ivLogo.setVisibility(View.GONE);
        } else {
//            userID = null;
            fl.setVisibility(View.GONE);
            btSignIn.setVisibility(View.VISIBLE);
            bnv.setVisibility(View.GONE);
            ivBackground.setVisibility(View.VISIBLE);
            ivLogo.setVisibility(View.VISIBLE);
        }
    }

    /* Called when user taps the Post a Ride button */
    public void postRide() {
        if (user != null) {
            Intent i=new Intent(this, PostRideActivity.class);
            i.putExtra("Username", user.getUsername());
            try {
                startActivityForResult(i,RC_POST_RIDE);
            }
            catch(Exception e){
                Log.d("exception", e.getMessage());
            }
        }
    }

    private void addRide(Ride ride) {
        dbHelper.insertRide(ride.getDestination(), ride.getDepartureDate(), ride.getNumSeats(), ride.getDescription(), ride.getOwnerID(), ride.getOwnerName());
        dbHelper.incrementRideCount(ride.getOwnerID());
    }

    public User getUser() {
        if (user != null) {
            return user;
        } else {
            return new User("Please Re-log", "dummy email");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // To prevent empty fragment screen, set the rides list fragment when there is no other.
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            int backStackEntryCount = manager.getBackStackEntryCount();
            if (backStackEntryCount == 0) {
                setDefaultFragment();
            }
        }
    }

    public void updateOnJoinRide(Long rideID, int remainingNumSeats){
        System.out.println("User joining to ride " + Long.toString(rideID) );
        dbHelper.setUserJoinedRideID(user.getId(), rideID);
        dbHelper.changeRideSeats(rideID, remainingNumSeats);
        String text = "I have joined the ride.";
        dbHelper.insertComment(user.getUsername(), user.getId(), new Date(), text, rideID);
        updateCurrentUser();
    }

    public void updateOnDeleteRide(Long rideID) {
        // Get all users who joined on this ride
        List<Long> rideesOnTheRide = dbHelper.getUserIDsByJoinedRideID(rideID);

        // Set their joined ride ids to -1
        for (Long rideeID : rideesOnTheRide) {
            dbHelper.setUserJoinedRideID(rideeID, -1L);
        }

        // Delete the ride
        dbHelper.deleteRideByID(rideID);
        Toast.makeText(this, "Ride has been deleted", Toast.LENGTH_LONG).show();

        updateCurrentUser();

        // Simulate closing fragment
        onBackPressed();
    }

    private void updateCurrentUser() {
        user = dbHelper.getUserByName(user.getUsername());
    }

    public void addCommentToRide(String text, Long rideID) {
        dbHelper.insertComment(user.getUsername(), user.getId(), new Date(), text, rideID);
   }


   private void setUser(String username, String email) {
        //TODO: Add enforcement on @sabanciuniv.edu

       String words[] = username.split(" ");
       if (words.length >= 3) {
           username = words[0] + " " + words[1];
       }

       dbHelper = new DBHelper(this);

       user = dbHelper.getUserByName(username);

       if (user == null) {
           dbHelper.insertUser(username, email);
           user = dbHelper.getUserByName(username);
       }

   }

   private void setDefaultFragment() {
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       transaction.replace(R.id.frame_layout, ViewRidesFragment.newInstance());
       transaction.addToBackStack("");
       transaction.commit();
   }
}
