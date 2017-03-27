///*
//package com.app.csubmobile;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
//import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
//
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.ExponentialBackOff;
//
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.client.util.DateTime;
//
//import com.google.api.services.calendar.model.*;
//
//import android.Manifest;
//import android.accounts.AccountManager;
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CalendarView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
//
//import android.support.v4.app.NavUtils;
//import android.view.Gravity;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//
//
//public class ScheduleActivity extends Activity
//        implements EasyPermissions.PermissionCallbacks {
//    GoogleAccountCredential mCredential;
//    private TextView mOutputText;
//    private Button mCallApiButton;
//    ProgressDialog mProgress;
//
//    CalendarView calendar;
//
//    static final int REQUEST_ACCOUNT_PICKER = 1000;
//    static final int REQUEST_AUTHORIZATION = 1001;
//    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
//    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
//
//    private static final String BUTTON_TEXT = "Call Google Calendar API";
//    private static final String PREF_ACCOUNT_NAME = "accountName";
//    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
//
//    DrawerLayout drawer;
//
//    */
///*@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LinearLayout activityLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        activityLayout.setLayoutParams(lp);
//        activityLayout.setOrientation(LinearLayout.VERTICAL);
//        activityLayout.setPadding(16, 16, 16, 16);
//
//        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        mCallApiButton = new Button(this);
//        mCallApiButton.setText(BUTTON_TEXT);
//        mCallApiButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCallApiButton.setEnabled(false);
//                mOutputText.setText("");
//                getResultsFromApi();
//                mCallApiButton.setEnabled(true);
//            }
//        });
//        activityLayout.addView(mCallApiButton);
//
//        mOutputText = new TextView(this);
//        mOutputText.setLayoutParams(tlp);
//        mOutputText.setPadding(16, 16, 16, 16);
//        mOutputText.setVerticalScrollBarEnabled(true);
//        mOutputText.setMovementMethod(new ScrollingMovementMethod());
//        mOutputText.setText(
//                "Click the \'" + BUTTON_TEXT +"\' button to test the API.");
//        activityLayout.addView(mOutputText);
//
//        mProgress = new ProgressDialog(this);
//        mProgress.setMessage("Calling Google Calendar API ...");
//
//        setContentView(activityLayout);
//
//        // Initialize credentials and service object.
//        mCredential = GoogleAccountCredential.usingOAuth2(
//                getApplicationContext(), Arrays.asList(SCOPES))
//                .setBackOff(new ExponentialBackOff());
//    }
//    *//*
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.schedule_layout);
//
//        calendar = (CalendarView) findViewById(R.id.calendar);
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
//                Toast.makeText(getApplicationContext(), (month + 1) + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
//
//
//    */
///**
//     * Attempt to call the API, after verifying that all the preconditions are
//     * satisfied. The preconditions are: Google Play Services installed, an
//     * account was selected and the device currently has online access. If any
//     * of the preconditions are not satisfied, the app will prompt the user as
//     * appropriate.
//     *//*
//
//    private void getResultsFromApi() {
//        if (!isGooglePlayServicesAvailable()) {
//            acquireGooglePlayServices();
//        } else if (mCredential.getSelectedAccountName() == null) {
//            chooseAccount();
//        } else if (!isDeviceOnline()) {
//            mOutputText.setText("No network connection available.");
//        } else {
//            new MakeRequestTask(mCredential).execute();
//        }
//    }
//
//    */
///**
//     * Attempts to set the account used with the API credentials. If an account
//     * name was previously saved it will use that one; otherwise an account
//     * picker dialog will be shown to the user. Note that the setting the
//     * account to use with the credentials object requires the app to have the
//     * GET_ACCOUNTS permission, which is requested here if it is not already
//     * present. The AfterPermissionGranted annotation indicates that this
//     * function will be rerun automatically whenever the GET_ACCOUNTS permission
//     * is granted.
//     *//*
//
//    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
//    private void chooseAccount() {
//        if (EasyPermissions.hasPermissions(
//                this, Manifest.permission.GET_ACCOUNTS)) {
//            String accountName = getPreferences(Context.MODE_PRIVATE)
//                    .getString(PREF_ACCOUNT_NAME, null);
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                getResultsFromApi();
//            } else {
//                // Start a dialog from which the user can choose an account
//                startActivityForResult(
//                        mCredential.newChooseAccountIntent(),
//                        REQUEST_ACCOUNT_PICKER);
//            }
//        } else {
//            // Request the GET_ACCOUNTS permission via a user dialog
//            EasyPermissions.requestPermissions(
//                    this,
//                    "This app needs to access your Google account (via Contacts).",
//                    REQUEST_PERMISSION_GET_ACCOUNTS,
//                    Manifest.permission.GET_ACCOUNTS);
//        }
//    }
//
//    */
///**
//     * Called when an activity launched here (specifically, AccountPicker
//     * and authorization) exits, giving you the requestCode you started it with,
//     * the resultCode it returned, and any additional data from it.
//     *
//     * @param requestCode code indicating which activity result is incoming.
//     * @param resultCode  code indicating the result of the incoming
//     *                    activity result.
//     * @param data        Intent (containing result data) returned by incoming
//     *                    activity result.
//     *//*
//
//    @Override
//    protected void onActivityResult(
//            int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_GOOGLE_PLAY_SERVICES:
//                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
//                } else {
//                    getResultsFromApi();
//                }
//                break;
//            case REQUEST_ACCOUNT_PICKER:
//                if (resultCode == RESULT_OK && data != null &&
//                        data.getExtras() != null) {
//                    String accountName =
//                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                    if (accountName != null) {
//                        SharedPreferences settings =
//                                getPreferences(Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString(PREF_ACCOUNT_NAME, accountName);
//                        editor.apply();
//                        mCredential.setSelectedAccountName(accountName);
//                        getResultsFromApi();
//                    }
//                }
//                break;
//            case REQUEST_AUTHORIZATION:
//                if (resultCode == RESULT_OK) {
//                    getResultsFromApi();
//                }
//                break;
//        }
//    }
//
//    */
///**
//     * Respond to requests for permissions at runtime for API 23 and above.
//     *
//     * @param requestCode  The request code passed in
//     *                     requestPermissions(android.app.Activity, String, int, String[])
//     * @param permissions  The requested permissions. Never null.
//     * @param grantResults The grant results for the corresponding permissions
//     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
//     *//*
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(
//                requestCode, permissions, grantResults, this);
//    }
//
//    */
///**
//     * Callback for when a permission is granted using the EasyPermissions
//     * library.
//     *
//     * @param requestCode The request code associated with the requested
//     *                    permission
//     * @param list        The requested permission list. Never null.
//     *//*
//
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> list) {
//        // Do nothing.
//    }
//
//    */
///**
//     * Callback for when a permission is denied using the EasyPermissions
//     * library.
//     *
//     * @param requestCode The request code associated with the requested
//     *                    permission
//     * @param list        The requested permission list. Never null.
//     *//*
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> list) {
//        // Do nothing.
//    }
//
//    */
///**
//     * Checks whether the device currently has a network connection.
//     *
//     * @return true if the device has a network connection, false otherwise.
//     *//*
//
//    private boolean isDeviceOnline() {
//        ConnectivityManager connMgr =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//    }
//
//    */
///**
//     * Check that Google Play services APK is installed and up to date.
//     *
//     * @return true if Google Play Services is available and up to
//     * date on this device; false otherwise.
//     *//*
//
//    private boolean isGooglePlayServicesAvailable() {
//        GoogleApiAvailability apiAvailability =
//                GoogleApiAvailability.getInstance();
//        final int connectionStatusCode =
//                apiAvailability.isGooglePlayServicesAvailable(this);
//        return connectionStatusCode == ConnectionResult.SUCCESS;
//    }
//
//    */
///**
//     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
//     * Play Services installation via a user dialog, if possible.
//     *//*
//
//    private void acquireGooglePlayServices() {
//        GoogleApiAvailability apiAvailability =
//                GoogleApiAvailability.getInstance();
//        final int connectionStatusCode =
//                apiAvailability.isGooglePlayServicesAvailable(this);
//        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
//            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
//        }
//    }
//
//
//    */
///**
//     * Display an error dialog showing that Google Play Services is missing
//     * or out of date.
//     *
//     * @param connectionStatusCode code describing the presence (or lack of)
//     *                             Google Play Services on this device.
//     *//*
//
//    void showGooglePlayServicesAvailabilityErrorDialog(
//            final int connectionStatusCode) {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        Dialog dialog = apiAvailability.getErrorDialog(
//                ScheduleActivity.this,
//                connectionStatusCode,
//                REQUEST_GOOGLE_PLAY_SERVICES);
//        dialog.show();
//    }
//
//    */
///**
//     * An asynchronous task that handles the Google Calendar API call.
//     * Placing the API calls in their own task ensures the UI stays responsive.
//     *//*
//
//    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
//        private com.google.api.services.calendar.Calendar mService = null;
//        private Exception mLastError = null;
//
//        MakeRequestTask(GoogleAccountCredential credential) {
//            HttpTransport transport = AndroidHttp.newCompatibleTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            mService = new com.google.api.services.calendar.Calendar.Builder(
//                    transport, jsonFactory, credential)
//                    .setApplicationName("Google Calendar API Android Quickstart")
//                    .build();
//        }
//
//        */
///**
//         * Background task to call Google Calendar API.
//         *
//         * @param params no parameters needed for this task.
//         *//*
//
//        @Override
//        protected List<String> doInBackground(Void... params) {
//            try {
//                return getDataFromApi();
//            } catch (Exception e) {
//                mLastError = e;
//                cancel(true);
//                return null;
//            }
//        }
//
//        */
///**
//         * Fetch a list of the next 10 events from the primary calendar.
//         *
//         * @return List of Strings describing returned events.
//         * @throws IOException
//         *//*
//
//        private List<String> getDataFromApi() throws IOException {
//            // List the next 10 events from the primary calendar.
//            DateTime now = new DateTime(System.currentTimeMillis());
//            List<String> eventStrings = new ArrayList<String>();
//            Events events = mService.events().list("primary")
//                    .setMaxResults(10)
//                    .setTimeMin(now)
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .execute();
//            List<Event> items = events.getItems();
//
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//                    // All-day events don't have start times, so just use
//                    // the start date.
//                    start = event.getStart().getDate();
//                }
//                eventStrings.add(
//                        String.format("%s (%s)", event.getSummary(), start));
//            }
//            return eventStrings;
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            mOutputText.setText("");
//            mProgress.show();
//        }
//
//        @Override
//        protected void onPostExecute(List<String> output) {
//            mProgress.hide();
//            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
//            } else {
//                output.add(0, "Data retrieved using the Google Calendar API:");
//                mOutputText.setText(TextUtils.join("\n", output));
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mProgress.hide();
//            if (mLastError != null) {
//                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    showGooglePlayServicesAvailabilityErrorDialog(
//                            ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                    .getConnectionStatusCode());
//                } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                    startActivityForResult(
//                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
//                            ScheduleActivity.REQUEST_AUTHORIZATION);
//                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
//                }
//            } else {
//                mOutputText.setText("Request cancelled.");
//            }
//        }
//    }
//}
//
//
//*/
package com.app.csubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.csubmobile.Volley.SlideShow;


public class ScheduleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuRight) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // Launching Map
            Intent i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_news) {
            // Launching News
            Intent i = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_events) {
            // Launching Events
            Intent i = new Intent(getApplicationContext(), EventsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_directory) {
            // Launching Directory
            Intent i = new Intent(getApplicationContext(), DirectoryActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_socialmedia) {
            // Launching Social Media
            Intent i = new Intent(getApplicationContext(), SocialMediaActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_dining) {
            // Launching Dining
            Intent i = new Intent(getApplicationContext(), DiningActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_transportation) {
            // Launching Transportation
            Intent i = new Intent(getApplicationContext(), TransportationActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_schedule) {
            // Launching Schedule
            Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_blackboard) {
            // Launching Blackboard/Moodle
            Intent i = new Intent(getApplicationContext(), Blackboard_MoodleActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            // Launching About Simple Dialog
            new AlertDialog.Builder(this)
                    .setTitle("About CSUB TEAM")
                    .setMessage("Developers: \n - Quy Nguyen \n - Jonathan Dinh \n - John Hargreaves \n - Kevin Jenkin \n\n Copyright \u00a9 2017" + "\n")
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(getApplicationContext(), SlideShow.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
