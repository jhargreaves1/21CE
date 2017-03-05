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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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

// Show direction on map


public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private static final String TAG = "MapActivity";
    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    private MapView mapView;
    private MapboxMap map;
    private FloatingActionButton floatingActionButton, searchActionButton;
    private LocationServices locationServices;
    private static final int PERMISSIONS_LOCATION = 0;
    private Position origin;
    private Position destination;
    private LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    private PolylineOptions newroute;
    private DrawerLayout mDrawerLayout;
    public List<BuildingItem> buildings = new ArrayList<BuildingItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        MapboxAccountManager.start(this, getString(R.string.access_token));
        // This contains the MapView in XML and needs to be called after the account manager
        setContentView(R.layout.map_layout);

        locationServices = LocationServices.getLocationServices(MapActivity.this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        searchActionButton = (FloatingActionButton) findViewById(R.id.search_toggle_fab);
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        // SCI-III location
        origin = Position.fromCoordinates(-119.103579, 35.348849);

        // Library location
        //destination = Position.fromCoordinates(-119.107183,35.349099);

        // Create a mapView
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                new ParseCSUBGeoJson().execute();

                /*
                // University Advancement #7
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350418, -119.106344))
                        .title("University Advancement")
                        .snippet("University Advancement"));

                // Administration West #8
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350424, -119.106000))
                        .title("Administration West")
                        .snippet("Administration West"));

                // Administration East #5
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350634, -119.105040))
                        .title("Administration East")
                        .snippet("Administration East"));

                // Administration #9
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350371, -119.105524))
                        .title("Administration")
                        .snippet("Administration"));

                // Education #34
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350200, -119.104463))
                        .title("Education")
                        .snippet("Education Building"));

                // Student Services #10
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350212, -119.104998))
                        .title("Student Services")
                        .snippet("Student Services"));

                // Dorothy Donahoe Hall #32
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350418, -119.103598))
                        .title("Dorothy Donahoe Hall")
                        .snippet("Dorothy Donahoe Hall"));

                // Science I #30
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349666, -119.103787))
                        .title("SCI-I")
                        .snippet("Science I"));

                // Science II #38
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349600, -119.103222))
                        .title("SCI-II")
                        .snippet("Science II"));

                // Science III #48
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348849, -119.103579))
                        .title("SCI-III")
                        .snippet("Science III"));

                // Walter Stiern Library #43
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-119.103211, 35.351264))
                        .title("Library")
                        .snippet("Walter Stiern Library"));

                // Plant Operation #11
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349692, -119.105106))
                        .title("Plant Operation")
                        .snippet("Plant Operation"));

                // Lecture Building #3
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350909, -119.105053))
                        .title("Lecture Building")
                        .snippet("Lecture Building"));

                // Performing Arts #4
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350996, -119.104625))
                        .title("Performing Arts")
                        .snippet("Performing Arts"));

                // Romberg Nursing Center #31
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349699, -119.104625))
                        .title("Romberg Nursing Center")
                        .snippet("Romberg Nursing Center"));

                // Computing/Telecom Center #65
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.351315, -119.102745))
                        .title("Computing/Telecom Center")
                        .snippet("Computing/Telecom Center"));

                // Runner Cafe #38
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350780, -119.102374))
                        .title("Runner Cafe")
                        .snippet("Runner Cafe"));

                // Classroom Building #1
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.351211, -119.105479))
                        .title("Classroom Building")
                        .snippet("Classroom Building"));

                // Fine Arts #2
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.351323, -119.104985))
                        .title("Fine Arts")
                        .snippet("Fine Arts"));

                // Faculty Towers #6
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350682, -119.105916))
                        .title("Faculty Towers")
                        .snippet("Faculty Towers"));

                // Business Developement Center Admin & Faculty Offices #44A
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349086, -119.105032))
                        .title("Business Admin & Faculty Offices")
                        .snippet("Business Developement Center Admin & Faculty Offices"));

                // Business Developement Center Classrooms #44B
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348793, -119.104765))
                        .title("Business Developement Center Classrooms")
                        .snippet("Business Developement Center Classrooms"));

                // Business Developement Center Extended University #44C
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348381, -119.104897))
                        .title("Extended University")
                        .snippet("Extended University"));

                // Business Developement Center Dezember Leadership Development Center #44D
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348473, -119.105294))
                        .title("Dezember Leadership Development Center")
                        .snippet("Dezember Leadership Development Center"));

                // Business Developement Center Office of the President #44E
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348827, -119.105289))
                        .title("Office of the President")
                        .snippet("Office of the President"));

                // Engineering Modulars #83
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348057, -119.104919))
                        .title("Engineering Modulars")
                        .snippet("Engineering Modulars FAB Lab"));

                // Student Health Services #35
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.347882, -119.103873))
                        .title("Student Health Services")
                        .snippet("Student Health Services"));

                // SPhysical Education #33
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348283, -119.102726))
                        .title("Physical Education")
                        .snippet("Physical Education"));

                // Icardo Center #52
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.347566, -119.102673))
                        .title("Icardo Center")
                        .snippet("Icardo Center"));

                // Coffee House #68
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349937, -119.104365))
                        .title("Coffee House")
                        .snippet("Coffee House - Peets Coffee"));

                // University Police #60
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348785, -119.102974))
                        .title("University Police")
                        .snippet("University Police"));

                // Student Recreation Center #67
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348866, -119.101842))
                        .title("REC Center")
                        .snippet("Student Recreation Center"));

                // Student Union #67
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349827, -119.101502))
                        .title("Student Union")
                        .snippet("Student Union"));

                // Greenhouse #66
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350395, -119.101187))
                        .title("Greenhouse")
                        .snippet("Greenhouse"));

                // J. Antonino Sports Center #61
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348290, -119.101745))
                        .title("Sports Center")
                        .snippet("J. Antonino Sports Center"));

                // Handball Courts #40
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.348067, -119.101514))
                        .title("Handball Courts")
                        .snippet("Handball Courts"));

                // J.R. Hillman Aquatic Center #45
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.347874, -119.101487))
                        .title("Aquatic Center")
                        .snippet("J.R. Hillman Aquatic Center"));

                // Doré Theatre #39
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.352230, -119.105465))
                        .title("Doré Theatre")
                        .snippet("Doré Theatre"));

                // Music Building #39a
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.351849, -119.105892))
                        .title("Music Building")
                        .snippet("Music Building"));

                // University Grill #23
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350202, -119.106847))
                        .title("University Grill")
                        .snippet("University Grill"));

                // University Grill #13
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.349099, -119.107183))
                        .title("Modular West")
                        .snippet("Modular West"));

                // Hardt Field
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.347644, -119.107519))
                        .title("Hardt Field")
                        .snippet("Hardt Field"));

                // Amphitheatre #62
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.353452, -119.102502))
                        .title("Amphitheatre")
                        .snippet("Amphitheatre"));

                // Rowdy
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(35.350047, -119.101774))
                        .title("Rowdy")
                        .snippet("Rowdy"));
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

                        // set destination to marker clicked and center
                        destination = Position.fromCoordinates(marker.getPosition().getLongitude(),marker.getPosition().getLatitude());
                        map.setCameraPosition(new CameraPosition.Builder()
                                .target(marker.getPosition())
                                .build());

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

                floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (map != null) {
                            toggleGps(!map.isMyLocationEnabled());
                            if(map.getMyLocation() != null) {
                                // Set the origin as user location only if we can get their location
                                origin = Position.fromCoordinates(map.getMyLocation().getLongitude(), map.getMyLocation().getLatitude());
                            }else{
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

                        // intent to launch building list
                        /*Intent i = new Intent(getApplicationContext(), BuildingsActivity.class);
                        i.putExtra("Buildings", (Serializable) buildings);
                        startActivity(i);*/
                    }
                });
                searchActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (map != null) {
                            Intent i = new Intent(getApplicationContext(), BuildingsActivity.class);
                            i.putExtra("Buildings", (Serializable) buildings);
                            startActivity(i);
                        }

                        // intent to launch building list
                        /*Intent i = new Intent(getApplicationContext(), BuildingsActivity.class);
                        i.putExtra("Buildings", (Serializable) buildings);
                        startActivity(i);*/
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

    private class ParseCSUBGeoJson extends AsyncTask<Void, Void, List<LatLng>> {

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<LatLng> markers = new ArrayList<>();
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
                                buildings.add(new BuildingItem(title,title,lng,lat));
                                titles.add(title);
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
            /*map.addMarker(new MarkerOptions()
                    .position(new LatLng(35.349827, -119.101502))
                    .title("Student Union")
                    .snippet("Student Union"));*/
            if (markers.size() > 0) {
                for (int i = 0; i < points.size(); i++) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(markers.get(i))
                            .title(titles.get(i))
                            .snippet(titles.get(i)));

                }
            }
        }
    }


    private void drawBorder(LinearLayout parent) {
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white bg
        border.setStroke(3, 0xFF000000); //black border
        border.setCornerRadii(new float[] { 8, 8, 8, 8, 8, 8, 8, 8 }); // rounded corner

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
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
            floatingActionButton.setImageResource(R.drawable.ic_menu_mylocation);
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_menu_mylocation);
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