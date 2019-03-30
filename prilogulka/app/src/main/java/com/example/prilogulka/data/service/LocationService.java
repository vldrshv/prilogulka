package com.example.prilogulka.data.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.prilogulka.data.managers.GeofenceManager;

public class LocationService extends AppCompatActivity implements LocationListener {
    private String CLASS_TAG = "LocationService";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private Context context;
    private Activity activity;
    private LocationManager locationManager;

    private float lat;
    private float longt;

    public LocationService(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        startLocationService();
    }

    private void startLocationService() {
        checkLocationPermission();

    }


    private void checkLocationPermission() {
        int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.i(CLASS_TAG, rc + "");
        if (rc == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            requestGpsPermission();
            getLocation();
        }
    }

    private void getLocation() {
        Log.i(CLASS_TAG, "HANDLE");
        int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
    }

    private void requestGpsPermission() {

        Log.w(CLASS_TAG, "GPS permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST_LOCATION);
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {

                    final Snackbar snackbar = Snackbar.make(
                            activity.getCurrentFocus(),
                            "Мы используем Геопозицию для определения коэффициентов при просмотре видео. Пожалуйста, разрешите геолокацию в настроках для перерасчета коэффициента.",
                            Snackbar.LENGTH_INDEFINITE);
                    View snackbarView = snackbar.getView();
                    TextView snackBarTextView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    snackBarTextView.setSingleLine(false);
                    snackbar.setAction("Понятно", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (float) location.getLatitude();
        longt = (float) location.getLongitude();

        Log.i(CLASS_TAG, "Latitude:" + lat + ", Longitude:" + longt);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Latitude", "status");
    }

    public String getCity(){
        return new GeofenceManager(lat, longt).getCity();
    }
}
