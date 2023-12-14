package com.example.busyancapstone.Manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.widget.ImageView;

import com.example.busyancapstone.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsManager {

    private static double distance = 0;

    public static Intent searchPlace(Context context) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(context);

        // Set a custom adapter for the Autocomplete activity

        return intent;

    }

    public static void addMarker(LatLng latlng, GoogleMap mMap){
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlng);

        mMap.addMarker(markerOptions);
    }

    public static void addStartAndEndMarker(LatLng startLatlng, LatLng endLatlng, GoogleMap mMap){

        addPassengerMarker(startLatlng, mMap);
        addMarker(endLatlng, mMap);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startLatlng);
        builder.include(endLatlng);

        LatLngBounds bounds = builder.build();

        int padding = 400; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
    }

    public static Marker addBusMarker(LatLng latlng, GoogleMap mMap){

        MarkerOptions passengerMarker = new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker))
                .anchor(0.5f, 0.5f);

//        markerOptions.rotation(location.getBearing());

        Marker marker = mMap.addMarker(passengerMarker);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));

        return marker;
    }

    public static Marker addPassengerMarker(LatLng latlng, GoogleMap mMap){

        MarkerOptions passengerMarker = new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.passenger_marker))
                .anchor(0.5f, 0.5f);


        Marker marker = mMap.addMarker(passengerMarker);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));

        return marker;
    }

    public static void drawPolyline(LatLng startPoint, LatLng endPoint, GoogleMap mMap) {


        PolylineOptions polylineOptions = new PolylineOptions()
                .add(startPoint, endPoint)
                .width(5) // set the width of the line
                .color(Color.RED); // set the color of the line


        Polyline polyline = mMap.addPolyline(polylineOptions);
    }

    public static void zoomCamera(LatLng latlng, GoogleMap mMap){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
    }

    public static void removeMarker(Marker marker) {
        if (marker != null) {
            marker.remove();
        }
    }

    @SuppressLint("DefaultLocale")
    public static String calculateDistance(LatLng point1, LatLng point2) {

        if (point1 != null || point2 != null) {
            double earthRadius = 6371; // Radius of the Earth in kilometers

            double lat1 = Math.toRadians(point1.latitude);
            double lon1 = Math.toRadians(point1.longitude);
            double lat2 = Math.toRadians(point2.latitude);
            double lon2 = Math.toRadians(point2.longitude);

            // Haversine formula
            double dLat = lat2 - lat1;
            double dLon = lon2 - lon1;
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            distance = earthRadius * c;

            // Determine the appropriate unit
            String unit;
            if (distance >= 1) {
                unit = "km";
            } else {
                distance *= 1000; // Convert to meters
                unit = "m";
            }

            // Format the distance as a string and include the unit
            return String.format("%d %s", (int) distance, unit);
        }

        return null;
    }

    public static String estimateTravelTime(LatLng startPoint, LatLng endPoint, String apiKey) {
        if (startPoint == null || endPoint == null) {
            return "Calculating...";
        }

        try {
            // Initialize the Google Maps API context with your API key
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            // Make a request to the Directions API
            DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                    .origin(new com.google.maps.model.LatLng(startPoint.latitude, startPoint.longitude))
                    .destination(new com.google.maps.model.LatLng(endPoint.latitude, endPoint.longitude))
                    .mode(TravelMode.DRIVING)
                    .await();

            // Get the total travel time in seconds
            long travelTimeInSeconds = directionsResult.routes[0].legs[0].duration.inSeconds;

            // Format the travel time and return
            return formatTravelTime(travelTimeInSeconds);
        } catch (Exception e) {
            e.printStackTrace();
            return "Calculating...";
        }
    }

    private static String formatTravelTime(long travelTimeInSeconds) {
        long hours = TimeUnit.SECONDS.toHours(travelTimeInSeconds);
        long minutes = TimeUnit.SECONDS.toMinutes(travelTimeInSeconds) % 60;
        long seconds = travelTimeInSeconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        if (hours > 0) {
            formattedTime.append(hours).append(" hr ");
        }

        if (minutes > 0) {
            formattedTime.append(minutes).append(" min ");
        }

        formattedTime.append(seconds).append(" sec");

        return formattedTime.toString();
    }

}
