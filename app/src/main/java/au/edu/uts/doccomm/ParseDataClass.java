package au.edu.uts.doccomm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mix on 29/8/18.
 */

public class ParseDataClass {

    private HashMap<String, String> getSingleFacilityLocation(JSONObject locationJSONObject){ //convert jsonarray/object to hashmap and add to list of maps
        HashMap<String, String> mapLocations = new HashMap<>();
        String locationName = "-NA-";
        String DistanceVicinity = "-NA-"; //not sure for name??
        String Lat = "";
        String Long = "";
        String reference = "";

        try {
            if(!locationJSONObject.isNull("name")){
                locationName = locationJSONObject.getString("name");

            }

            if(!locationJSONObject.isNull("vicinity")){
                DistanceVicinity = locationJSONObject.getString("vicinity");

            }

            Lat = locationJSONObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
            Long = locationJSONObject.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = locationJSONObject.getString("reference");

            mapLocations.put("Location_name", locationName);
            mapLocations.put("Distance_Vicinity", DistanceVicinity);
            mapLocations.put("lat", Lat);
            mapLocations.put("lng", Long);
            mapLocations.put("reference", reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapLocations;
    }


    private List<HashMap<String, String>> getAllNearbyFacilities(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String, String>> nearbyFacilitiesList = new ArrayList<>();

        HashMap<String, String> nearbyFacilitiesMap = null;

        for (int i =0; i<count; i++){
            try {
                nearbyFacilitiesMap = getSingleFacilityLocation((JSONObject) jsonArray.get(i));
                nearbyFacilitiesList.add(nearbyFacilitiesMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nearbyFacilitiesList;

    }

    public List<HashMap<String, String>> parseJSONData(String JSONDATA1){
        JSONArray jsonArray = null;
        JSONObject jsonObject;


        try {
            jsonObject = new JSONObject(JSONDATA1);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllNearbyFacilities(jsonArray);

    }
}
