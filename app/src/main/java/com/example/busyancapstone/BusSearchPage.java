package com.example.busyancapstone;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.busyancapstone.Adapter.CustomAdapterSearchAddress;
import com.example.busyancapstone.Enum.PlacesApiService;
import com.example.busyancapstone.Manager.MapsManager;
import com.example.busyancapstone.Model.PlacesInfo;
import com.example.busyancapstone.Model.SeatReservation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusSearchPage extends AppCompatActivity {

    private static final String TAG = "TAGBusSearchPage";

    private static final int START_REQUEST_CODE = 1;
    private static final int DESTINATION_REQUEST_CODE = 2;

    private TextView tv_destination, tv_start;
    private ImageView iv_mapsBtn;
    private AutoCompleteTextView auto_destination, auto_start;
    private BottomNavigationView bottomNavigationView;
    private ListView addressListview;
    private Button btn_save;
    private ProgressBar progress_circular;

    private String apiKey;
    private String startAddress, destinationAddress;
    private double startLat = 0.0, startLong = 0.0;
    private double destinationLat = 0.0, destinationLong = 0.0;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private PlacesApiRequest currentTask;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search_page);

        references();
        clickActions();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        auto_start.setAdapter(adapter);

    }

    private void clickActions() {

        bottomNavigationView.setSelectedItemId(R.id.passenger_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.passenger_home) {

                return true;
            }
            else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.passenger_notification) {
                startActivity(new Intent(getApplicationContext(), PassengerNotification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        tv_start.setOnClickListener( v -> {
            Intent intent = MapsManager.searchPlace(this);
            startActivityForResult(intent, START_REQUEST_CODE);
        });

        tv_destination.setOnClickListener( v -> {
            Intent intent = MapsManager.searchPlace(this);
            startActivityForResult(intent, DESTINATION_REQUEST_CODE);
        });

        auto_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // Cancel the previous task if it's running
                if (currentTask != null && currentTask.getStatus() == AsyncTask.Status.RUNNING) {
                    currentTask.cancel(true);
                }

                // Start a new task
                if (charSequence.length() > 3) { // You might want to set a threshold to start suggestions
                    // Start the API call with the current text
                    currentTask = new PlacesApiRequest(apiKey, addressListview, progress_circular);
                    currentTask.execute(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        auto_destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                // Cancel the previous task if it's running
                if (currentTask != null && currentTask.getStatus() == AsyncTask.Status.RUNNING) {
                    currentTask.cancel(true);
                }

                // Start a new task
                if (charSequence.length() > 3) { // You might want to set a threshold to start suggestions
                    // Start the API call with the current text
                    currentTask = new PlacesApiRequest(apiKey, addressListview, progress_circular);
                    currentTask.execute(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_save.setOnClickListener( v -> {

            Toast.makeText(this, "Searching for bus...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PassengerBus.class));
        });

        addressListview.setOnItemClickListener( ((adapterView, view, i, l) -> {

            PlacesInfo placesInfo = (PlacesInfo) adapterView.getItemAtPosition(i);
            SeatReservation reservation = SeatReservation.getInstance();

            if(auto_start.isFocused()){

                auto_start.setText(placesInfo.getPlace());
                reservation.setStartLocation(placesInfo.getPlace());
                reservation.setStartLat(placesInfo.getLatitude());
                reservation.setStartLong(placesInfo.getLongitude());

            }
            else if(auto_destination.isFocused()){

                auto_destination.setText(placesInfo.getPlace());
                reservation.setDestination(placesInfo.getPlace());
                reservation.setDestinationLat(placesInfo.getLatitude());
                reservation.setDestinationLong(placesInfo.getLongitude());


            }
            else {
                return;
            }

            Log.d(TAG, "Start Address:" + reservation.getStartLocation());
            Log.d(TAG, "Start Latitude:" + reservation.getStartLat());
            Log.d(TAG, "Start Longitude:" + reservation.getStartLong());

            Log.d(TAG, "End Address:" + reservation.getDestination());
            Log.d(TAG, "End Latitude:" + reservation.getDestinationLat());
            Log.d(TAG, "End Longitude:" + reservation.getDestinationLong());
        }));

        iv_mapsBtn.setOnClickListener( view -> {

            if(addressListview.isEnabled()){
                addressListview.setEnabled(false);

            }

        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == START_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK) {
//
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                startAddress = place.getName();
//                tv_start.setText(startAddress);
//
//                startLat = place.getLatLng().latitude;
//                startLong = place.getLatLng().longitude;
//
//
//            }
//            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//            }
//            else if (resultCode == RESULT_CANCELED) {
//            }
//        }
//        else if (requestCode == DESTINATION_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK) {
//
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                destinationAddress = place.getName();
//                tv_destination.setText(destinationAddress);
//
//                destinationLat = place.getLatLng().latitude;
//                destinationLong = place.getLatLng().longitude;
//
//
//            }
//            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//            }
//            else if (resultCode == RESULT_CANCELED) {
//            }
//        }
//    }

    private void asyncMap(Double latitude, Double longitude){


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


            }
        });
    }

    private void references() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        tv_destination = findViewById(R.id.tv_destination);
        tv_start = findViewById(R.id.tv_startLocation);
        iv_mapsBtn = findViewById(R.id.iv_mapsBtn);

        auto_destination = findViewById(R.id.auto_destination);
        auto_start = findViewById(R.id.auto_start);

        addressListview = findViewById(R.id.addressListview);
        progress_circular = findViewById(R.id.progress_circular);
        btn_save = findViewById(R.id.btn_save);

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            Places.initialize(getApplicationContext(), apiKey);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private class PlacesApiRequest extends AsyncTask<String, Void, List<PlacesInfo>> {

        private ProgressBar progress_circular;

        private WeakReference<ListView> autoCompleteTextViewWeakReference;
        private WeakReference<ProgressBar> progressBarWeakReference;

        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/";
        private final String apiKey;

        public PlacesApiRequest(String apiKey, ListView listView, ProgressBar progress_circular) {
            this.apiKey = apiKey;
            this.autoCompleteTextViewWeakReference = new WeakReference<>(listView);
            this.progressBarWeakReference = new WeakReference<>(progress_circular);
            this.progress_circular = progressBarWeakReference.get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress_circular.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<PlacesInfo> doInBackground(String... input) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PLACES_API_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PlacesApiService service = retrofit.create(PlacesApiService.class);
            String components = "country:PH";

            Call<JsonObject> call = service.getPlaceAutocompleteWithFilter(input[0], apiKey, components);

            try {
                Response<JsonObject> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    List<PlacesInfo> placeInfos = parseJsonAndFetchDetails(jsonResponse, service, apiKey);

                    // Do something with the placeInfos, such as updating the UI or storing in a data structure
//                    for (PlacesInfo placeInfo : placeInfos) {
//                        Log.d("PlaceInfo", "Place: " + placeInfo.getName() + ", Latitude: " + placeInfo.getLatitude() + ", Longitude: " + placeInfo.getLongitude());
//                    }

                    // Parse the JSON response and extract place descriptions
                    return placeInfos;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<PlacesInfo> result) {
            super.onPostExecute(result);

            ListView resultsList = autoCompleteTextViewWeakReference.get();
            progress_circular.setVisibility(View.GONE);

            CustomAdapterSearchAddress adapter = new CustomAdapterSearchAddress(BusSearchPage.this, result);

            if (resultsList != null) {
                addressListview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                // Handle the case where the result is null
            }
        }



        private List<String> parseJson(JsonObject jsonResponse) {
            // Implement JSON parsing logic to extract place descriptions
            // Modify this method based on the actual JSON structure
            List<String> suggestions = new ArrayList<>();

            if (jsonResponse != null && jsonResponse.has("predictions")) {
                JsonArray predictions = jsonResponse.getAsJsonArray("predictions");

                for (int i = 0; i < predictions.size(); i++) {
                    JsonObject prediction = predictions.get(i).getAsJsonObject();
                    String placeDescription = prediction.get("description").getAsString();
                    suggestions.add(placeDescription);
                }
            }

            return suggestions;
        }

        private List<PlacesInfo> parseJsonAndFetchDetails(JsonObject autocompleteJson, PlacesApiService service, String apiKey) {
            List<PlacesInfo> placeInfos = new ArrayList<>();

            if (autocompleteJson != null && autocompleteJson.has("predictions")) {
                JsonArray predictions = autocompleteJson.getAsJsonArray("predictions");

                for (int i = 0; i < predictions.size(); i++) {
                    JsonObject prediction = predictions.get(i).getAsJsonObject();
                    String placeDescription = prediction.get("description").getAsString();
                    String placeId = prediction.get("place_id").getAsString();

                    // Fetch place details using the place ID
                    Call<JsonObject> detailsCall = service.getPlaceDetails(placeId, apiKey);
                    try {
                        Response<JsonObject> detailsResponse = detailsCall.execute();

                        if (detailsResponse.isSuccessful() && detailsResponse.body() != null) {
                            JsonObject detailsJson = detailsResponse.body();

                            // Parse the details JSON to extract latitude and longitude
                            LatLng location = parseLocation(detailsJson);

                            // Create a PlaceInfo object with the obtained information
                            PlacesInfo placeInfo = new PlacesInfo(placeDescription, location.latitude, location.longitude);

                            placeInfos.add(placeInfo);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return placeInfos;
        }

        private LatLng parseLocation(JsonObject detailsJson) {
            double latitude = 0.0;
            double longitude = 0.0;

            if (detailsJson != null && detailsJson.has("result")) {
                JsonObject result = detailsJson.getAsJsonObject("result");

                // Check if the result has a geometry object
                if (result.has("geometry")) {
                    JsonObject geometry = result.getAsJsonObject("geometry");

                    // Check if the geometry object has a location object
                    if (geometry.has("location")) {
                        JsonObject location = geometry.getAsJsonObject("location");

                        // Extract latitude and longitude
                        if (location.has("lat") && location.has("lng")) {
                            latitude = location.get("lat").getAsDouble();
                            longitude = location.get("lng").getAsDouble();
                        }
                    }
                }
            }

            return new LatLng(latitude, longitude);
        }

    }

}