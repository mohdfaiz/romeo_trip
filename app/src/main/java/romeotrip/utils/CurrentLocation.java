package romeotrip.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CurrentLocation {

    private static String TAG = "ash_json_check";
    private static Location location = null;
    private static String DEFAULT_DISTANCE = "0.00";

    //=====
    public static void fetchLastKnownLocation(Context ctx) {

        LocationManager _locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (loc == null) {
            loc = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        Log.e("Current","CureentLoc");
        setLocation(loc, ctx);
    }

    //=====
    public static void requestLocationUpdate(LocationListener listener, Context ctx) {

        LocationManager _locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Log.e("insiderequestLocation","reachedhere");
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

    }

    //=====
    public static Location getLocation() {
        return location;
    }

    //=====
    public static void setLocation(Location location, Context ctx) {

        if (location != null) {
            CurrentLocation.location = location;
            //Toast.makeText(ctx, "Latitude = "+location.getLatitude()+" Longitude"+location.getLongitude(), Toast.LENGTH_SHORT).show();
            //Log.d("LocationHotel Set ===> "+"Latitude = "+location.getLatitude()+" Longitude"+location.getLongitude());
        }

    }

    //=====
    public String distance(String latStr, String lonStr, char unit) {
        double lat1, lat2;
        double lon1, lon2;
//		 Log.i("ash_deal","LocationHotel = "+location);
        //************CURRENT LOCATION VALIDATION*************//
        try {
            if (location != null) {

                lat1 = location.getLatitude();
                lon1 = location.getLongitude();
                lat2 = Double.parseDouble(latStr);
                lon2 = Double.parseDouble(lonStr);

            } else {
                return DEFAULT_DISTANCE;
            }
        } catch (NumberFormatException e) {

            return DEFAULT_DISTANCE;
        }

        //**************************************************//

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {

            dist = dist * 1.609344;
        } else if (unit == 'N') {

            dist = dist * 0.8684;
        }
        if (unit == 'M') {

            dist = dist * 1.609344 * 1000;
        }

        Log.d("distance = ", dist + "            ;   ");

        return (dist + "");
    }

    //======
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
     /*::  This function converts decimal degrees to radians             :*/
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static double deg2rad(double deg) {

        return (deg * Math.PI / 180.0);
    }

    //======
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
     /*::  This function converts radians to decimal degrees             :*/
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static double rad2deg(double rad) {

        return (rad * 180.0 / Math.PI);
    }

    //=====
    public String drivingDistance(String latStr, String lonStr) {
        double lat1, lat2;
        double lon1, lon2;
        String distance = null;
        String url;
//		 Log.i("ash_deal","LocationHotel = "+location);
        //************CURRENT LOCATION VALIDATION*************//
        try {
            if (location != null) {

                lat1 = location.getLatitude();
                lon1 = location.getLongitude();
                lat2 = Double.parseDouble(latStr);
                lon2 = Double.parseDouble(lonStr);

            } else {
                return DEFAULT_DISTANCE;
            }
        } catch (NumberFormatException e) {

            return DEFAULT_DISTANCE;
        }


        try {

            url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + lat1 + "," + lon1 + "&destinations=" + lat2 + "," + lon2 + "&units=Meter&sensor=false";
            distance = parsingDistanceFromJSON(getJSONFromURL(url));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            distance = DEFAULT_DISTANCE;
        }

        return distance;

    }

    //=====
    public static JSONObject getJSONFromURL(String url) {
        JSONObject json = null;

        try {

            URL googleMapDistance = new URL(url);
            URLConnection gmd = googleMapDistance.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(gmd.getInputStream(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
                sb.append(line + "\n");

            in.close();

            json = new JSONObject(sb.toString());

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Log.d(TAG, "MalformedURLException");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, "IOException");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, "JSONException");

        }

        return json;

    }

    //=====
    public static String parsingDistanceFromJSON(JSONObject js) throws JSONException {

        Log.d(TAG, "json object = " + js);
        String distance = "";
        JSONArray elements;

        if (js == null)
            return DEFAULT_DISTANCE;
        Log.d(TAG, "response status = " + js.getString("status"));

        elements = js.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

        Log.d(TAG, "distance response status = " + elements.getJSONObject(0).getString("status"));

        if (!js.getString("status").equals("OK") || !elements.getJSONObject(0).getString("status").equals("OK"))
            return DEFAULT_DISTANCE;

        Log.d(TAG, "Rows array = " + elements.getJSONObject(0).getJSONObject("distance").toString());

        distance = elements.getJSONObject(0).getJSONObject("distance").getString("value");


        Log.d(TAG, "distance = " + distance);
        if (distance.length() > 0) {
            distance = String.valueOf(Integer.parseInt(distance) / 1000.0);
            return distance;
        } else
            return DEFAULT_DISTANCE;

    }


}
