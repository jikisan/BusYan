package com.example.busyancapstone.Helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.busyancapstone.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Helper {

    private static Context context;

    public Helper(Context context) {
        this.context = context;
    }

    public static void showAlert(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showToastShort(String message) {

        Toast currentToast = null;

        // Cancel the previous toast if it exists
        if (currentToast != null) {
            currentToast.cancel();
        }

        // Create and show the new toast
        currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        currentToast.show();
    }

    public void showToastLong(String message) {

        Toast currentToast = null;

        // Cancel the previous toast if it exists
        if (currentToast != null) {
            currentToast.cancel();
        }

        // Create and show the new toast
        currentToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        currentToast.show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void loadImage(Uri imageUri, ImageView imageView) {

        Picasso.get().load(imageUri)
                .placeholder(R.drawable.logo)
                .fit()
                .centerCrop()
                .rotate(90)
                .into(imageView);
    }

    public static String getCurrentDate() {

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static long getDateInMillis(String dateInString) throws ParseException {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = dateFormat.parse(dateInString);
        long millis = date.getTime();

        return millis;
    }

    public static String getTimeDiff(String currentTime, String dateCreated) {

        Date currentDate = parseDateString(currentTime);
        Date resultDate = parseDateString(dateCreated);
        long timeDifferenceMinutes = Math.abs((resultDate.getTime() - currentDate.getTime()) / (60 * 1000));


        return formatTimeDifference(timeDifferenceMinutes);
    }

    public static String sendOtp(Context context, String phoneNumber){
        int randonNumber;

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

            Toast.makeText(context, "OTP request sent!", Toast.LENGTH_SHORT).show();

            return String.valueOf(randonNumber);
        }

        catch (Exception e) {
            Toast.makeText(context, "Error SMS "+e, Toast.LENGTH_SHORT).show();
            Log.d("SENT OTP", "String builder: " + "Error SMS "+e);


            System.out.println();

            return null;
        }
    }

    // Function to capitalize all the letters of words
    public static String capitalizeAllWords(String input) {
        StringBuilder result = new StringBuilder();

        if (input != null && !input.isEmpty()) {
            String[] words = input.split("\\s+");

            for (String word : words) {
                if (word.length() > 0) {
                    result.append(word.toUpperCase()).append(" ");
                }
            }
            // Remove the trailing space
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    // Function to capitalize only the first letter of each word
    public static String capitalizeFirstLetter(String input) {
        StringBuilder result = new StringBuilder();

        if (input != null && !input.isEmpty()) {
            String[] words = input.split("\\s+");

            for (String word : words) {
                if (word.length() > 0) {
                    result.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1).toLowerCase()).append(" ");
                }
            }
            // Remove the trailing space
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private static Date parseDateString(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String formatTimeDifference(long timeDifferenceMinutes) {
        if (timeDifferenceMinutes < 5) {
            return "Just now";
        } else if (timeDifferenceMinutes >= 5 && timeDifferenceMinutes <= 59) {
            return timeDifferenceMinutes + " min ago";
        } else if (timeDifferenceMinutes >= 60 && timeDifferenceMinutes < 24 * 60) {
            return (timeDifferenceMinutes / 60) + " hours ago";
        } else {
            return (timeDifferenceMinutes / (24 * 60)) + " days ago";
        }
    }

}
