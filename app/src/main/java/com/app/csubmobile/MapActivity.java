package com.app.csubmobile;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.csubmobile.data.BuildingItem;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static final String TAG = "MapActivity";
    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    private MapView mapView;
    private MapboxMap map;
    private com.github.clans.fab.FloatingActionButton getDirectionActionButton, searchActionButton, showMarkersActionButton, buildingActionButton;
    private LocationServices locationServices;
    private static final int PERMISSIONS_LOCATION = 0;
    private Position origin;
    private Position destination;
    private LocationManager locationManager;
    private PolylineOptions newroute;
    private DrawerLayout mDrawerLayout;
    private Intent i;
    private BuildingItem selectedBuilding;
    private List<BuildingItem> buildings = new ArrayList<BuildingItem>();
    private List<LatLng> polygon;
    private Criteria criteria;
    private String bestProvider;
    private MarkerView clickedMarker;
    private Boolean markersOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        polygon = new ArrayList<LatLng>();
        markersOn = false;

        // Checking for extra contents
        i = getIntent();
        selectedBuilding = (BuildingItem) i.getSerializableExtra("Building");

        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        MapboxAccountManager.start(this, getString(R.string.access_token));
        // This contains the MapView in XML and needs to be called after the account manager
        setContentView(R.layout.map_layout);

        locationServices = LocationServices.getLocationServices(MapActivity.this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // FAB button instantiation
        getDirectionActionButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.getDirectionBtn);
        searchActionButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.searchBtn);
        showMarkersActionButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.showMarkersBtn);
        buildingActionButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.buildingListBtn);

        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        // Create a mapView
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                new ParseCSUBGeoJson().execute();

                // if this is launched from search activity then we get the selected building that
                // user searched
                if (selectedBuilding != null) {
                    new ShowSelectedBuildingMarker().execute();
                }
                /*
                // University Advancement #7
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350418, -119.106344))
                        .title("University Advancement")
                        .snippet("University Advancement"));
                */


                mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {

                        // The info window layout is created dynamically, parent is the info window
                        // container
                        LinearLayout parent = new LinearLayout(MapActivity.this);
                        parent.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        parent.setOrientation(LinearLayout.VERTICAL);

                        // prettify this sucker
                        parent.setBackgroundColor(Color.parseColor("#ffffff"));
                        parent.setPadding(10, 0, 10, 15);
                        drawBorder(parent);

                        // Building Images and Text Descriptions
                        ImageView buildingImage = new ImageView(MapActivity.this);
                        TextView description = new TextView(MapActivity.this);

                        // set marker to visible on click
                        clickedMarker = (MarkerView) marker;
                        clickedMarker.setVisible(true);

                        // set destination to marker clicked and center
                        destination = Position.fromCoordinates(marker.getPosition().getLongitude(), marker.getPosition().getLatitude());
                        centerCamera(marker);

                        switch (marker.getTitle()) {
                            case "Well Core Repository":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Facilities/Corporation Yard":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Emergency Operation Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Modular East I":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Modular East II":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Modular East III":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Rohan Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.rohan));
                                description.setText(marker.getSnippet());
                                break;
                            case "Numenor Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.numenor));
                                description.setText(marker.getSnippet());
                                break;
                            case "Rivendell Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.rivendell));
                                description.setText(marker.getSnippet());
                                break;
                            case "Dobry Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.dobry));
                                description.setText(marker.getSnippet());
                                break;
                            case "Entwood Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.entwood));
                                description.setText(marker.getSnippet());
                                break;
                            case "Lorien Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Child Care":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.childcare));
                                description.setText(marker.getSnippet());
                                break;
                            case "Tennis Shower Locker":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Rowdy":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Amphitheatre":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.ampitheatre));
                                description.setText(marker.getSnippet());
                                break;
                            case "Hardt Field":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Modular West":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "University Grill":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Doré Theatre":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.dore_theatre));
                                description.setText(marker.getSnippet());
                                break;
                            case "Music Building":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.music_building));
                                description.setText(marker.getSnippet());
                                break;
                            case "J.R. Hillman Aquatic Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Handball Courts":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "J. Antonino Sports Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Greenhouse":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.coming_soon));
                                description.setText(marker.getSnippet());
                                break;
                            case "Student Union":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.student_union));
                                description.setText(marker.getSnippet());
                                break;
                            case "Student Recreation Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.studentrec));
                                description.setText(marker.getSnippet());
                                break;
                            case "University Police":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.police));
                                description.setText(marker.getSnippet());
                                break;
                            case "Physical Education":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.pe));
                                description.setText(marker.getSnippet());
                                break;
                            case "Icardo Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.icarrdo));
                                description.setText(marker.getSnippet());
                                break;
                            case "Coffee House - Peets Coffee":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.peets));
                                description.setText(marker.getSnippet());
                                break;
                            case "Student Health Services":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.healthservices));
                                description.setText(marker.getSnippet());
                                break;
                            case "Engineering Modulars FAB Lab":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.fab));
                                description.setText(marker.getSnippet());
                                break;
                            case "Office of the President":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.president));
                                description.setText(marker.getSnippet());
                                break;
                            case "Dezember Leadership Development Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.leadership_development));
                                description.setText(marker.getSnippet());
                                break;
                            case "Extended University":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.extended_university));
                                description.setText(marker.getSnippet());
                                break;
                            case "Business Development Center Classrooms":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.busdevclass));
                                description.setText(marker.getSnippet());
                                break;
                            case "Business Admin & Faculty Offices":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.bus_dev_off));
                                description.setText(marker.getSnippet());
                                break;
                            case "Faculty Towers":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.facultytower));
                                description.setText(marker.getSnippet());
                                break;
                            case "Fine Arts":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.finearts));
                                description.setText(marker.getSnippet());
                                break;
                            case "Classroom Building":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.classroom_bduilding));
                                description.setText(marker.getSnippet());
                                break;
                            case "Runner Cafe":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.cafe));
                                description.setText(marker.getSnippet());
                                break;
                            case "Computing/Telecom Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.telecommunications));
                                description.setText(marker.getSnippet());
                                break;
                            case "Romberg Nursing Center":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.nursinged));
                                description.setText(marker.getSnippet());
                                break;
                            case "Performing Arts":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.performingarts));
                                description.setText(marker.getSnippet());
                                break;
                            case "Lecture Building":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.lecture_building));
                                description.setText(marker.getSnippet());
                                break;
                            case "Central Plant Operations":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.educational_oppourtunity));
                                description.setText(marker.getSnippet());
                                break;
                            case "University Advancement":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.educational_oppourtunity));
                                description.setText(marker.getSnippet());
                                break;
                            case "Administration":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.admin_west_a));
                                description.setText(marker.getSnippet());
                                break;
                            case "Administration East":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.admin_east));
                                description.setText(marker.getSnippet());
                                break;
                            case "Administration West":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.admin_west));
                                description.setText(marker.getSnippet());
                                break;
                            case "Student Services":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.studentservices));
                                description.setText(marker.getSnippet());
                                break;
                            case "Education Building":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.education));
                                description.setText(marker.getSnippet());
                                break;
                            case "Dorothy Donahoe Hall":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.donahoe_hall));
                                description.setText(marker.getSnippet());
                                break;
                            case "Science I":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.sci_1));
                                description.setText(marker.getSnippet());
                                break;
                            case "Science II":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.sci_2));
                                description.setText(marker.getSnippet());
                                break;
                            case "Science III":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.sci_3));
                                description.setText(marker.getSnippet());
                                break;
                            case "Library":
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.library));
                                description.setText(marker.getSnippet());
                                break;
                            default:
                                // By default all markers without a matching title will use the
                                // SCI-III
                                buildingImage.setImageDrawable(ContextCompat.getDrawable(
                                        MapActivity.this, R.drawable.sci_3));
                                break;
                        }

                        // Set the size of the image
                        buildingImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(700, 550));

                        // Stylizing description box
                        description.setBackgroundColor(Color.parseColor("#ffffff"));
                        description.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        description.setLayoutParams(new android.view.ViewGroup.LayoutParams(700, ViewGroup.LayoutParams.WRAP_CONTENT));
                        description.setTextSize(18);
                        description.setTypeface(null, Typeface.BOLD);

                        // add imageview and textview to parent layout
                        parent.addView(buildingImage);
                        parent.addView(description);

                        return parent;
                    }


                });

                getDirectionActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (map != null) {
                            toggleGps(!map.isMyLocationEnabled());
                            if (map.getMyLocation() != null) {
                                // Set the origin as user location only if we can get their location
                                origin = Position.fromCoordinates(map.getMyLocation().getLongitude(), map.getMyLocation().getLatitude());
                            } else {
                                return;
                            }

                            // check for crashes if destination/origin is empty or not selected
                            if (destination != null && origin != null) {
                                try {
                                    // Get route from API
                                    getRoute(origin, destination);
                                } catch (ServicesException servicesException) {
                                    servicesException.printStackTrace();
                                }

                            } else {
                                Toast.makeText(MapActivity.this, "Please select a destination first.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                searchActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (map != null) {
                            Intent i = new Intent(getApplicationContext(), BuildingsActivity.class);
                            i.putExtra("Buildings", (Serializable) buildings);
                            startActivity(i);
                            finish();
                        }
                    }
                });

                showMarkersActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*if (map != null) {
                            new ShowAllMarkers().execute();
                        }*/
                    }
                });

                buildingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (map != null) {
                            Intent i = new Intent(getApplicationContext(), ListBuildings.class);
                            i.putExtra("Buildings", (Serializable) buildings);
                            startActivity(i);
                            finish();
                        }
                    }
                });

            }
        });
        // runtime permission check~~~~~~
        //getPermission();

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

    private void drawPolygon(MapboxMap mapboxMap, ArrayList<ArrayList<LatLng>> polygon) {
        for (int i = 0; i < polygon.size(); i++) {
            mapboxMap.addPolygon(new PolygonOptions()
                    .addAll(polygon.get(i))
                    .fillColor(Color.parseColor("#3bb2d0")));
        }
    }

    private class ShowAllMarkers extends AsyncTask<Void,Void,List<LatLng>> {

        @Override
        protected void onPostExecute(List<LatLng> points) {
            if (buildings.size() > 0 && markersOn == false) {
                for (int i = 0; i < buildings.size(); i++) {
                    map.addMarker(new MarkerViewOptions()
                            .position(new LatLng(buildings.get(i).getLng(), buildings.get(i).getLat()))
                            .title(buildings.get(i).getName())
                            .snippet(buildings.get(i).getName())
                            .visible(true));
                }
                markersOn = true;
                map.resetNorth();
            } else if (markersOn == true) {
                List<Marker> markers = map.getMarkers();
                for (int i = 0; i < markers.size(); i++) {
                    map.removeMarker(markers.get(i));
                }
                markersOn = false;
            }
        }

        @Override
        protected List<LatLng> doInBackground(Void... voids) {
            return null;
        }
    }

    private class ShowSelectedBuildingMarker extends AsyncTask<Void,Void,List<LatLng>> {

        @Override
        protected void onPostExecute(List<LatLng> points) {
            Marker selected = map.addMarker(new MarkerViewOptions()
                    .position(new LatLng(selectedBuilding.getLng(), selectedBuilding.getLat()))
                    .title(selectedBuilding.getName())
                    .snippet(selectedBuilding.getName())
                    .visible(true));
            // set selected building as destination
            destination = Position.fromCoordinates(selected.getPosition().getLongitude(), selected.getPosition().getLatitude());
            // center to that marker
            centerCamera(selected);
        }

        @Override
        protected List<LatLng> doInBackground(Void... voids) {
            return null;
        }
    }

    private class ParseCSUBGeoJson extends AsyncTask<Void, Void, List<LatLng>> {

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<LatLng> markers = new ArrayList<>();
        ArrayList<ArrayList<LatLng>> polyTemp = new ArrayList<ArrayList<LatLng>>();
        List<Double> Lng = new ArrayList<>();
        List<Double> Lat = new ArrayList<>();

        @Override
        protected List<LatLng> doInBackground(Void... voids) {
            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open("features.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                inputStream.close();
                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    if (geometry != null) {
                        String type = geometry.getString("type");

                        // Checking for Point only~~~
                        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Point")) {
                            JSONObject properties = feature.getJSONObject("properties");
                            if (!TextUtils.isEmpty(properties.getString("title"))) {
                                String title = properties.getString("title");
                                // Get the marker coordinates here~~~
                                JSONArray coords = geometry.getJSONArray("coordinates");
                                double lng = coords.getDouble(1);
                                double lat = coords.getDouble(0);
                                LatLng latLng = new LatLng(coords.getDouble(1), coords.getDouble(0));
                                markers.add(latLng);
                                buildings.add(new BuildingItem(title, title, lng, lat));
                                titles.add(title);
                            }
                        }

                        // Checking for polygon coordinates
                        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Polygon")) {
                            JSONArray coord1 = geometry.getJSONArray("coordinates");
                            for (int j = 0; j < coord1.length(); j++) {
                                ArrayList<LatLng> inner = new ArrayList<LatLng>();
                                JSONArray coord2 = coord1.getJSONArray(j);
                                for (int k = 0; k < coord2.length(); k++) {
                                    JSONArray coord3 = coord2.getJSONArray(k);
                                    double lng = coord3.getDouble(1);
                                    double lat = coord3.getDouble(0);
                                    Lng.add(lng);
                                    Lat.add(lat);
                                    LatLng latLng = new LatLng(lng, lat);
                                    inner.add(new LatLng(lng, lat));
                                }
                                polyTemp.add(inner);
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
            }

            return markers;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);
            drawPolygon(map, polyTemp);
            /*map.addMarker(new MarkerOptions()
                    .position(new LatLng(35.349827, -119.101502))
                    .title("Student Union")
                    .snippet("Student Union"));*/
            /*if (markers.size() > 0) {
                for (int i = 0; i < points.size(); i++) {
                    Marker marker = map.addMarker(new MarkerViewOptions()
                            .position(markers.get(i))
                            .title(titles.get(i))
                            .snippet(titles.get(i))
                            .visible(false));
                }
            }*/
        }
    }

    private void centerCamera(Marker marker) {
        CameraPosition position = (new CameraPosition.Builder()
                .target(marker.getPosition())
                .tilt(30)
                .build());
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 1000);
    }

    private void drawBorder(LinearLayout parent) {
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white bg
        border.setStroke(3, 0xFF000000); //black border
        border.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8}); // rounded corner

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // deprecated but just in case user runs sdk lower than jellybean
            parent.setBackgroundDrawable(border);
        } else {
            parent.setBackground(border);
        }

    }

    private void getRoute(Position origin, Position destination) throws ServicesException {

        client = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setProfile(DirectionsCriteria.PROFILE_CYCLING)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().getRoutes().size() < 1) {
                    Log.e(TAG, "No routes found");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(TAG, "Distance: " + currentRoute.getDistance());
                Toast.makeText(
                        MapActivity.this,
                        "Route is " + currentRoute.getDistance() + " meters long.",
                        Toast.LENGTH_SHORT).show();

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(MapActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // remove any routes if exists
        if (!map.getPolylines().isEmpty()) {
            map.removePolyline(newroute.getPolyline());
        }

        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }
        if (!map.getPolylines().isEmpty()) {
            map.removePolyline(newroute.getPolyline());
        }
        newroute = new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#009688"))
                .width(10);
        // Draw Points on MapView
        map.addPolyline(newroute);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (client != null) {
            client.cancelCall();
        }
        mapView.onDestroy();
    }

    // Navigation Drawer---------------------------------------------------------------------------- //
    // Function below is there to check permissions, and perform runtime requests if necessary.
    // Please do not remove or change the below function. If you do, let me know - Jonathan
    //
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // show explaination to user here

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // show explaination to user here

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // show explaination to user here

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            }
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
                    .setMessage("Developers: \n - Quy Nguyen \n - Jonathan Dinh \n - John Hargreaves \n - Kevin Jenkin")
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


    // Display User location------------------------------------------------------------------------ //
    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            if (!locationServices.areLocationPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            } else {
                enableLocation(true);
            }
        } else {
            enableLocation(false);
        }
    }

    private void enableLocation(boolean enabled) {
        if (enabled) {
            // If we have the last location of the user, we can move the camera to that position.
            Location lastLocation = locationServices.getLastLocation();
            if (lastLocation != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
            }

            locationServices.addLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
                        //locationServices.removeLocationListener(this);
                    }
                }
            });
            getDirectionActionButton.setImageResource(R.drawable.ic_menu_mylocation);
        } else {
            getDirectionActionButton.setImageResource(R.drawable.ic_menu_mylocation);
        }
        // Enable or disable the location layer on the map
        map.setMyLocationEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation(true);
            }
        }
    }


}