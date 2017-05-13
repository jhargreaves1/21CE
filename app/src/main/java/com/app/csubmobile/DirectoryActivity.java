package com.app.csubmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import com.app.csubmobile.Volley.SlideShow;

import java.util.ArrayList;
import java.util.List;

public class DirectoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private ProgressDialog pDialog;
    public static List<String> directory = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private WebView myWebView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        /*get_data(URL_FACULTY, directory);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_layout);
        /*if (get_data(URL_FACULTY, directory))
            setList();*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* WebView Start */
        myWebView = (WebView) findViewById(R.id.myWebView);
        myWebView.loadUrl("http://www.csub.edu/directory/#/list");
        myWebView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        /* Navigation Drawer Start */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Use When the user clicks a link from a web page in your WebView
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
            } else if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
            } else if (url.startsWith("mailto:")) {
                url = url.substring(7);
                String body = "Body of message.";
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.setType("application/octet-stream");
                mail.putExtra(Intent.EXTRA_EMAIL, new String[]{url});
                mail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                mail.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(mail);
                return true;
            }
            return true;
        }

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

    /*private void setList() {
        ListView listView = (ListView) findViewById(R.id.directory);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directory);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }*/

    /*private boolean get_data(String url, final List list) {
        pDialog.setMessage("Loading data ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONArray jObj = new JSONArray(response);
                    JSONObject json = null;
                    final String[] name = new String[jObj.length()];
                    for (int i = 0; i < jObj.length(); i++) {
                        json = jObj.getJSONObject(i);
                        String title = (json.isNull("faculty_title")) ? null : json.getString("faculty_title");
                        String fullname = new String();
                        if (title != null)
                            fullname = title + " " + json.getString("faculty_fname") + " " + json.getString("faculty_lname");
                        else
                            fullname = json.getString("faculty_fname") + " " + json.getString("faculty_lname");
                        name[i] = fullname;
                    }
                    if (list.isEmpty()) {
                        for (int i = 0; i < name.length; i++) {
                            list.add(name[i]);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(DirectoryActivity.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(strReq);
        return true;
    }*/

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
