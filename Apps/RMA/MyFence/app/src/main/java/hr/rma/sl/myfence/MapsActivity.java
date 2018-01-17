package hr.rma.sl.myfence;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    protected static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    ArrayList markerList;
    float marker_distance_m;
    Circle activeCircle = null;
    Bitmap markerTargetIcon, markerFenceIcon;
    boolean readFromSP = false;

    LatLng SP_pointTarget;
    LatLng SP_pointFence;
    double SP_target_fence_radius;


    // Provides the entry point to Google Play services.
    protected GoogleApiClient mGoogleApiClient;

    // Geofence used in this sample
    protected Geofence mGeofence;

    // Used when requesting to add (or remove) geofence
    private PendingIntent mGeofencePendingIntent;

    // Geofence is stored in SP for "future use":
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerTargetIcon = resizemapIcons(R.drawable.marker_center, 100, 100);
        markerFenceIcon = resizemapIcons(R.drawable.marker_fence, 100, 100);
        markerList = new ArrayList();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null:
        mGeofencePendingIntent = null;

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
    }


    public Bitmap resizemapIcons(int drawable_id, int w, int h) {
        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), drawable_id);
        return Bitmap.createScaledBitmap(markerBitmap, w, h, false);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("onMapReady...");
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {
            Toast.makeText(this, "App does not use ACCESS_COARSE_LOCATION permission",
                    Toast.LENGTH_SHORT).show();
        }

        // Long tap will erase geofence:
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            public void onMapLongClick(LatLng point) {
                mMap.clear();
                markerList.clear();
                mSharedPreferences.edit().clear().commit();
            }

        });

        // Short tapping will allow for geofence creation (two points are needed):
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                if (markerList.size() == 2) {
                    return;
                } else {
                    if (markerList.size() == 0) {
                        Marker target = mMap.addMarker(new MarkerOptions()
                                .draggable(true)
                                .position(point)
                                .title("TARGET")
                                .icon(BitmapDescriptorFactory.fromBitmap(markerTargetIcon)));
                        markerList.add(target);
                    } else if (markerList.size() == 1) {
                        Marker fence = mMap.addMarker(new MarkerOptions()
                                .draggable(true)
                                .position(point)
                                .title("GEOFENCE")
                                .icon(BitmapDescriptorFactory.fromBitmap(markerFenceIcon)));
                        markerList.add(fence);

                        Location l1 = new Location("");
                        l1.setLatitude(((Marker) markerList.get(0)).getPosition().latitude);
                        l1.setLongitude(((Marker) markerList.get(0)).getPosition().longitude);
                        Location l2 = new Location("");
                        l2.setLatitude(((Marker) markerList.get(1)).getPosition().latitude);
                        l2.setLongitude(((Marker) markerList.get(1)).getPosition().longitude);
                        float distance_real = l1.distanceTo(l2);
                        marker_distance_m = distance_real;

                        // Draw circle on map:
                        drawGeofence(((Marker) markerList.get(0)).getPosition(), distance_real);

                        // Prepare geofence data:
                        prepareGeofence("GEOFENCE_REQUEST_ID",
                                ((Marker) markerList.get(0)).getPosition().latitude,
                                ((Marker) markerList.get(0)).getPosition().longitude,
                                distance_real);

                        // Activate geofence:
                        addGeofence();

                        save_map_state_to_SP();
                    }
                }

            }
        });


        // Dragging two markers around map will invoke geofence change!
        // We consider this geofence change as: (1) remove_old_geofence and
        // (2) create_new_geofence.
        // Code needs some refactoring and better structuring, but the main idea is clear.
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {

                if (marker.equals((Marker) markerList.get(0))) {
                    System.out.println("TARGET marker dragged");
                    remove_circle();

                    Location l1 = new Location("");
                    l1.setLatitude(marker.getPosition().latitude);
                    l1.setLongitude(marker.getPosition().longitude);
                    Location l2 = new Location("");
                    l2.setLatitude(((Marker) markerList.get(1)).getPosition().latitude);
                    l2.setLongitude(((Marker) markerList.get(1)).getPosition().longitude);
                    float distance_real = l1.distanceTo(l2);

                    /*
                    Toast.makeText(MapsActivity.this,
                            "Target marker dragged to: " + marker.getPosition().latitude + ", "
                                    + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    */

                    // remove old
                    removeGeofence();

                    // create new
                    drawGeofence(marker.getPosition(), distance_real);
                    prepareGeofence("GEOFENCE_REQUEST_ID",
                            ((Marker) markerList.get(0)).getPosition().latitude,
                            ((Marker) markerList.get(0)).getPosition().longitude,
                            distance_real);

                    addGeofence();
                    save_map_state_to_SP();

                } else if (marker.equals((Marker) markerList.get(1))) {
                    System.out.println("FENCE marker dragged");
                    remove_circle();

                    Location l1 = new Location("");
                    l1.setLatitude(marker.getPosition().latitude);
                    l1.setLongitude(marker.getPosition().longitude);
                    Location l2 = new Location("");
                    l2.setLatitude(((Marker) markerList.get(0)).getPosition().latitude);
                    l2.setLongitude(((Marker) markerList.get(0)).getPosition().longitude);
                    float distance_real = l2.distanceTo(l1);
                    marker_distance_m = distance_real;

                    /*
                    Toast.makeText(MapsActivity.this,
                            "Target marker dragged to: " +
                                    ((Marker) markerList.get(0)).getPosition().latitude + ", "
                                    + ((Marker) markerList.get(0)).getPosition().longitude,
                            Toast.LENGTH_SHORT).show();
                    */

                    // remove old
                    removeGeofence();

                    // create new
                    drawGeofence(((Marker) markerList.get(0)).getPosition(), distance_real);
                    prepareGeofence("GEOFENCE_REQUEST_ID",
                            ((Marker) markerList.get(0)).getPosition().latitude,
                            ((Marker) markerList.get(0)).getPosition().longitude,
                            distance_real);

                    addGeofence();
                    save_map_state_to_SP();
                } else {
                    // We shouldn't be here whatsoever...
                    System.out.println("unknown marker");
                }
            }
        });

        setUpMap();
    }


    private void setUpMap(){
        // Show riteh if no data are present
        if (!readFromSP) {
            LatLng riteh = new LatLng(45.337735, 14.426009);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(riteh, 16));
        }
    }


    private void remove_circle(){
        if (activeCircle != null) {
            activeCircle.remove();
        }
    }


    private void drawGeofence(LatLng geopoint, double radius) {
        activeCircle = mMap.addCircle(new CircleOptions()
                .center(geopoint)
                .radius(radius)
                .fillColor(0x75ff0000));
    }


    // Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart() {
        System.out.println("onStart...");
        super.onStart();
        mGoogleApiClient.connect();

        load_map_state_from_SP();
    }


    @Override
    protected void onStop() {
        System.out.println("onStop...");
        super.onStop();
        mGoogleApiClient.disconnect();

        save_map_state_to_SP();
    }


    // Load state from Shared Prefs.
    private void load_map_state_from_SP() {
        try {
            Gson mGson = new Gson();
            String mJson = mSharedPreferences.getString("MyObject", "");
            MapState loadedState = mGson.fromJson(mJson, MapState.class);

            SP_pointTarget = new LatLng(loadedState.get_target_latitude(),
                    loadedState.get_target_longitude());
            SP_pointFence = new LatLng(loadedState.get_fence_latitude(),
                    loadedState.get_fence_longitude());

            SP_target_fence_radius = loadedState.get_radius_m();
            readFromSP = true;
            System.out.println("Map state loaded from shared prefs");
        } catch (Exception ex) {
            System.out.println("Cannot load map state from shared prefs");
        }
    }


    // Save state to Shared Prefs.
    private void save_map_state_to_SP(){
        try {
            MapState stateToSave = new MapState(
                    ((Marker) markerList.get(0)).getPosition().latitude,
                    ((Marker) markerList.get(0)).getPosition().longitude,
                    ((Marker) markerList.get(1)).getPosition().latitude,
                    ((Marker) markerList.get(1)).getPosition().longitude,
                    marker_distance_m);

            SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
            Gson mGson = new Gson();
            String mJson = mGson.toJson(stateToSave);
            prefsEditor.putString("MyObject", mJson);
            prefsEditor.commit();
            System.out.println("Map state saved to shared prefs");
        } catch (Exception ex) {
            System.out.println("Cannot save map state to shared prefs");
        }
    }


    // If "regular" data have been read from SP, we can easily re-create map visuals, as well
    // as geofence data. In order to handle starting new activity from various contexts
    // (from notification list, app start, from active-apps button etc), we will pragmatically
    // erase existing map (if exists!) and recreate a new one.
    private void apply_loaded_map_state(){
        markerList.clear();
        mMap.clear();

        Marker target = mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(SP_pointTarget)
                .title("TARGET")
                .icon(BitmapDescriptorFactory.fromBitmap(markerTargetIcon)));
        markerList.add(target);

        Marker fence = mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(SP_pointFence)
                .title("GEOFENCE")
                .icon(BitmapDescriptorFactory.fromBitmap(markerFenceIcon)));
        markerList.add(fence);

        // Ovo ne treba, cak ne treba pamtiti u SP (jer cemo racunati "na ruke") svaki put:
        marker_distance_m = (float)SP_target_fence_radius;

        Location l1 = new Location("");
        l1.setLatitude(((Marker) markerList.get(1)).getPosition().latitude);
        l1.setLongitude(((Marker) markerList.get(1)).getPosition().longitude);
        Location l2 = new Location("");
        l2.setLatitude(((Marker) markerList.get(0)).getPosition().latitude);
        l2.setLongitude(((Marker) markerList.get(0)).getPosition().longitude);
        float distance_real = l2.distanceTo(l1);
        marker_distance_m = distance_real;

        drawGeofence(((Marker) markerList.get(0)).getPosition(), marker_distance_m);

        prepareGeofence("GEOFENCE_REQUEST_ID",
                ((Marker) markerList.get(0)).getPosition().latitude,
                ((Marker) markerList.get(0)).getPosition().longitude,
                marker_distance_m);

        addGeofence();

        LatLng lastKnownTargetPoint = new LatLng(
                ((Marker) markerList.get(0)).getPosition().latitude,
                ((Marker) markerList.get(0)).getPosition().longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownTargetPoint, 16));
    }


    // Runs when a GoogleApiClient object successfully connects.
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if ((readFromSP) && (mMap!=null)){
            apply_loaded_map_state();
            readFromSP = false;
        }

    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended");
        // onConnected() will be called again automatically when the service reconnects
    }


    // Runs when the result of calling addGeofences() and removeGeofences() becomes available.
    // Either method can complete successfully or with an error.
    // Since this activity implements the {@link ResultCallback} interface, we are required to
    // define this method.
    // @param status -- The Status returned through a PendingIntent when addGeofences() or
    // removeGeofences() get called.
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofence add/remove successfully",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
            Toast.makeText(this, "Geofence add/remove NOT successful", Toast.LENGTH_SHORT).show();
        }
    }


   // Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
   // Also specifies how the geofence notifications are initially triggered.
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.

        // Maybe we don't want this initial triggering in our sample:::
        //builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofence(mGeofence);
        //builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest:
        return builder.build();
    }


    // Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
    // issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
    // current list of geofences.
    // @return -- A PendingIntent for the IntentService that handles geofence transitions.
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    // Adds geofence, which sets alerts to be notified when the device enters or exits one of the
    // specified geofence. Handles the success or failure results returned by addGeofence().
    public void addGeofence() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofence(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            Toast.makeText(this, "App does not use ACCESS_FINE_LOCATION permission", Toast.LENGTH_SHORT).show();
        }
    }


    // Dynamically create geofence based on the user's input.
    public void prepareGeofence(String requestID,
                                double in_latitude, double in_longitude,
                                float in_radius) {
        try {
            mGeofence = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this geofence.
                .setRequestId(requestID)
                // Set the circular region of this geofence.
                .setCircularRegion(in_latitude, in_longitude, in_radius)

                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                // Create the geofence.
                .build();
            Toast.makeText(this, "Geofence successfully built with radius set to " +
                                        in_radius + "m.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Geofence NOT built.", Toast.LENGTH_SHORT).show();
        }
    }



    // Removes geofence, which stops further notifications when the device enters or exits
    // previously registered geofence.
    public void removeGeofence() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            Toast.makeText(this, "App does not use ACCESS_FINE_LOCATION permission", Toast.LENGTH_SHORT).show();
        }
    }


}
