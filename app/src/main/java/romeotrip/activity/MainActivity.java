package romeotrip.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.mohdfaiz.romeotrip.R;

import java.util.List;
import java.util.Locale;

import romeotrip.fragment.PagerFragment;
import romeotrip.utils.CurrentLocation;
import romeotrip.utils.CustomPreference;
import romeotrip.utils.StaticItems;

public class MainActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //marshallow permissions
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permissionsToRequest = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(this, permissionsToRequest) == PackageManager.PERMISSION_GRANTED) {
                // Proceed with your code execution
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            permissionsToRequest = Manifest.permission.ACCESS_COARSE_LOCATION;
            if (ActivityCompat.checkSelfPermission(this, permissionsToRequest) == PackageManager.PERMISSION_GRANTED) {
                // Proceed with your code execution
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        CustomPreference.writeBoolean(this, CustomPreference.LOCATIONENABLE, false);
        setContentView(R.layout.activity_main);

        DisplayMetrics display = getResources().getDisplayMetrics();
        StaticItems.displayHeight = display.heightPixels;
        StaticItems.displayWidth = display.widthPixels;

        fragmentManager = getSupportFragmentManager();
        addFragment(new PagerFragment());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    void addFragment(Fragment fragment) {
        currentFragment = fragment;
        fragmentManager.beginTransaction().add(R.id.container, fragment).commit();
    }

    public void replaceFragment(Fragment fragment) {
        currentFragment = fragment;
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity.this);
        exitDialog.setTitle("Exit");
        exitDialog.setMessage("Do you want to exit?");
        exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        exitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        exitDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchLocationLast();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public double latitude, longitude;

    public void fetchLocationLast() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean networkLocationEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!networkLocationEnabled) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.app_name));
            alertDialogBuilder
                    .setMessage("Allow Romeo Trip to access your location while you use the app?"
                            + "We use this to help you discover places you wish to visit.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialog.dismiss();
                            CustomPreference.readBoolean(MainActivity.this, CustomPreference.LOCATIONENABLE, true);
                        }

                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            CustomPreference.writeBoolean(MainActivity.this, CustomPreference.LOCATIONENABLE, false);
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            CustomPreference.writeBoolean(MainActivity.this, CustomPreference.LOCATIONENABLE, false);

        } else {
            CurrentLocation.fetchLastKnownLocation(this);
            Location loc = CurrentLocation.getLocation();
            if (loc != null) {

                double lon = 0;
                double lat = 0;

                lon = CurrentLocation.getLocation().getLongitude();
                lat = CurrentLocation.getLocation().getLatitude();
                Log.e("lon " + lon, "lat " + lat);

                latitude = Double.parseDouble(String.valueOf(lat));
                longitude = Double.parseDouble(String.valueOf(lon));

                CustomPreference.writeBoolean(this, CustomPreference.LOCATIONENABLE, true);

                CustomPreference.writeString(this, CustomPreference.LATITUDE, String.valueOf(latitude));
                CustomPreference.writeString(this, CustomPreference.LONGITUDE, String.valueOf(longitude));

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    assert addresses != null;
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String countryName = addresses.get(0).getCountryName();
                    String landMark = addresses.get(0).getLocality();

                    CustomPreference.writeString(this, CustomPreference.COUNTRY, String.valueOf(countryName));
                    CustomPreference.writeString(this, CustomPreference.STATE, String.valueOf(stateName));
                    CustomPreference.writeString(this, CustomPreference.CITY, String.valueOf(cityName));
                    CustomPreference.writeString(this, CustomPreference.LANDMARK, String.valueOf(landMark));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
