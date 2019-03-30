package com.example.prilogulka.data.managers;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeofenceManager {

    String CLASS_TAG = "GeofenceManager";

    private String URL_BASE = "https://geocode-maps.yandex.ru/1.x/?";
    private String URL_API_KEY = "apikey=2f2c12bf-2712-439f-822a-fe10803d4ced";
    private String URL_GEOCODE = "&geocode=%s,%s";
    private String URL_BASE_END = "&format=json&results=1";

    private String REQUEST;
    private OkHttpClient client;

    private String city = "";

    public GeofenceManager(float longtitude, float latitude){
        createManager(""+latitude, ""+longtitude);
    }

    public GeofenceManager(double longtitude, double latitude){
        createManager(""+latitude, ""+longtitude);
    }

    private void createManager(String latitude, String longtitude) {
        String lat = "" + latitude;
        String lon = ""  + longtitude;
        REQUEST = URL_BASE + URL_API_KEY + URL_GEOCODE + URL_BASE_END;
        REQUEST = String.format(REQUEST, lat, lon);

        client = new OkHttpClient();

        setCity();
    }

    private void setCity() {
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            city = getDescription(run(REQUEST));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCity(){
        return city;
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.i(CLASS_TAG, url);
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    private String getDescription(String jsonResponse) throws JSONException {
        JSONObject obj = new JSONObject(jsonResponse);
        JSONObject response = obj.getJSONObject("response");
        JSONObject geoOCollection = response.getJSONObject("GeoObjectCollection");
        JSONArray featureMemberArray = geoOCollection.getJSONArray("featureMember");
        JSONObject geoObjects = featureMemberArray.getJSONObject(0);
        JSONObject geoObject = geoObjects.getJSONObject("GeoObject");
        String s = geoObject.getString("description");

        return s;
    }

}
