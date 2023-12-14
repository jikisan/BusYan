package com.example.busyancapstone.Enum;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {
    @GET("place/autocomplete/json")
    Call<JsonObject> getPlaceAutocomplete(
            @Query("input") String input,
            @Query("key") String apiKey
    );

    @GET("place/autocomplete/json")
    Call<JsonObject> getPlaceAutocompleteWithFilter(
            @Query("input") String input,
            @Query("key") String apiKey,
            @Query("components") String components
    );

    @GET("place/details/json")
    Call<JsonObject> getPlaceDetails(
            @Query("place_id") String placeId,
            @Query("key") String apiKey
    );
}
