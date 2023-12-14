package com.example.busyancapstone.Manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class SendSmsTask extends AsyncTask<Void, Void, String> {

    private static final String BASE_URL = "https://api.txtlocal.com/";
    private Context context;
    private String phoneNumber;
    private int randonNumber;

    interface SmsApi {
        @POST("send/")
        Call<ResponseBody> sendSms(
                @Query("apikey") String apiKey,
                @Query("numbers") String numbers,
                @Query("message") String message,
                @Query("sender") String sender
        );
    }

    public SendSmsTask() {
    }

    public SendSmsTask(Context context, String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
    }

    @SerializedName("status")
    private String status;

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Construct data
            Random random = new Random();
            randonNumber = random.nextInt(999999);

            String apiKey = "apikey=" + "Njg0ZTUwMzg0YTY1NzU3NzU4NjI2ZjUyNTUzNzUwMzI=";
            String message = "&message=" + "Your OTP (One-Time Password) is:" + randonNumber +
                    "\nPlease use this code to verify your identity. " +
                    "\nDo not share this code with anyone.";
            String sender = "&sender=" + "BUSYAN OTP";
            String numbers = "&numbers=" + phoneNumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));

            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            Log.d("SENT OTP", "String builder: " + stringBuffer.toString());

//            Toast.makeText(context, "OTP request sent!", Toast.LENGTH_SHORT).show();

            return String.valueOf(randonNumber);
        }

        catch (Exception e) {
//            Toast.makeText(context, "Error SMS "+e, Toast.LENGTH_SHORT).show();
            Log.d("SENT OTP", "String builder: " + "Error SMS "+e);


            System.out.println();

            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Handle the result as needed
    }
}
