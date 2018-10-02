package au.edu.uts.doccomm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

//import android.location.LocationListener;

public class FacilitiesMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationrequestVar;
    private Location LastUpdatedLocation;
    private Marker CurrentLocation;
    private static final int requestUserLocationCODE = 199;
    private double latitude, longitude;
    private int radiusProximity = 10000;
    private MarkerOptions markerOptions1 = new MarkerOptions();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_maps);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //what does this do???
            verifyUserLocationPermissions();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            buildGoogleApiClient();
            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(false);
            //System.out.print(CurrentLocation.getPosition());

            //LatLng location12 = CurrentLocation.getPosition();
            //System.out.print(location12);
          //  mMap.moveCamera(CameraUpdateFactory.newLatLng(markerOptions1.getPosition()));

            mMap.setMyLocationEnabled(true);
        }


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public boolean verifyUserLocationPermissions(){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestUserLocationCODE);
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestUserLocationCODE);
                }
                return false;
            }
            else
            {
                return true;
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case requestUserLocationCODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Location permission denied by user", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();{

            client.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LastUpdatedLocation =  location;
        if(CurrentLocation != null){
            CurrentLocation.remove();
        }
            LatLng LatitudeLongitude = new LatLng(location.getLatitude(), location.getLongitude());
            markerOptions1.position(LatitudeLongitude); //setting new position to updated lat/long
            markerOptions1.title("User's current updated location");
            markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            //set colour , title, etc

            CurrentLocation = mMap.addMarker(markerOptions1); //set location
            mMap.animateCamera(CameraUpdateFactory.newLatLng(LatitudeLongitude));
            //new camera position
            mMap.animateCamera(CameraUpdateFactory.zoomBy(15)); //zooms into user location by 20 points

            if (client != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
                //stops it being constantly updated if it already has been updated
            }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationrequestVar = new LocationRequest();
        locationrequestVar.setInterval(1100);
        locationrequestVar.setFastestInterval(1100);
        locationrequestVar.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationrequestVar, this);
        }
    }

    public void onClick(View v){
        String hospital = "hospital";
        Object dataTransfer[] = new Object[2]; //1st obj is mMap, and 2nd is url
        NearbyMedicalFacilities nearbyMedicalFacilities = new NearbyMedicalFacilities();


        switch (v.getId()){
            case R.id.searchID:
                EditText searchField = (EditText) findViewById(R.id.searchplace);
                String inputted_address = searchField.getText().toString();

                List<Address> addresses = null;
                MarkerOptions userAddressMarker = new MarkerOptions();

                if(!TextUtils.isEmpty(inputted_address)){
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addresses = geocoder.getFromLocationName(inputted_address, 6);
                        if(addresses!=null){
                            for(int i=0; i<addresses.size(); i++){

                                Address userSearchAddress = addresses.get(i);
                                LatLng LatitudeLong = new LatLng(userSearchAddress.getLatitude(), userSearchAddress.getLongitude());
                                userAddressMarker.position(LatitudeLong); //setting new position to updated lat/long
                                userAddressMarker.title(inputted_address);
                                userAddressMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                //set colour , title, etc
                                mMap.addMarker(userAddressMarker);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatitudeLong));
                                //new camera position
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(18)); //zooms into user location by 15 points
                                searchField.getText().clear();
                            }
                        }
                        else{
                            Toast.makeText(this, "Address/Location not found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(this, "Enter text first", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.hospitalBtn:
                mMap.clear(); //removes existing searched markers
                String URL = getURL(latitude, longitude, hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = URL;
                nearbyMedicalFacilities.execute(dataTransfer);
                Toast.makeText(this, "Locating nearby medical facilities", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Nearby hospitals shown!", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private String getURL(double latitude, double longitude, String nearbyFacilities){
        StringBuilder url_google = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        url_google.append("location=" + latitude + "," + longitude);
        url_google.append("&radius=" + radiusProximity);
        url_google.append("&type=" + nearbyFacilities);
        url_google.append("&sensor=true");
        url_google.append("&key=" + "AIzaSyAhziXqTAkIpVYXOCvfp4c3sqD8H8cj_vA");

        Log.d("FacilitiesMapActivity", "url = " + url_google.toString());

      //  System.out.println("WORKED"); //testing if it worked
        return url_google.toString();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
