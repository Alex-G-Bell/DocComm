package au.edu.uts.doccomm;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mix on 27/8/18.
 */

public class NearbyMedicalFacilities extends AsyncTask<Object, String, String> {

   private String MapsLocationData, url;
   private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) { //prepopulated method
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        FetchMapData fetchURLData = new FetchMapData();
        try {
            MapsLocationData = fetchURLData.urlToRead(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return MapsLocationData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyFacilitiesList;
        ParseDataClass dataParser=new ParseDataClass();
        nearbyFacilitiesList = dataParser.parseJSONData(s); //something wrong???
        DisplayNearbyFacilities(nearbyFacilitiesList);
    }

    private void DisplayNearbyFacilities(List<HashMap<String,String>> nearbyFacilitiesList){
        for(int i=0; i<nearbyFacilitiesList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String,String> nearbyFacility = nearbyFacilitiesList.get(i);
            String facilityName = nearbyFacility.get("Location_name");
            String facilityDistanceVicinity = nearbyFacility.get("Distance_Vicinity");
            double lat = Double.parseDouble(nearbyFacility.get("lat"));
            double lng = Double.parseDouble(nearbyFacility.get("lng"));



            LatLng LatitudeLong = new LatLng(lat,lng);
            markerOptions.position(LatitudeLong); //setting new position to updated lat/long
            markerOptions.title(facilityName + " : " + facilityDistanceVicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            //set colour , title, etc
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatitudeLong));
            //new camera position
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10)); //zooms into user location by 10 points


        }
    }
}
