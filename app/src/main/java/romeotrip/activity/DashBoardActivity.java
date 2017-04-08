package romeotrip.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mohdfaiz.romeotrip.R;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import romeotrip.fragment.HomeFragment;
import romeotrip.utils.CurrentLocation;
import romeotrip.utils.CustomPreference;

public class DashBoardActivity extends AppCompatActivity {

    public FragmentManager FM;
    public FragmentTransaction FT;
    private Fragment currentFragment;
    TextView toolbar_title, yourlocationtxt, currentlocation, homebtn, explorebtn, inboxbtn;
    ImageButton search, clickSearch;
    AutoCompleteTextView searchHotel;

    //for autotext
    ArrayList<String> autoText = new ArrayList<>();

//    public static DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(EasyFonts.caviarDreams(DashBoardActivity.this));

        yourlocationtxt = (TextView) findViewById(R.id.yourlocationtxt);
        yourlocationtxt.setVisibility(View.GONE);
        currentlocation = (TextView) findViewById(R.id.currentlocation);
        currentlocation.setTypeface(EasyFonts.caviarDreams(DashBoardActivity.this));
        currentlocation.setVisibility(View.GONE);

        homebtn = (Button) findViewById(R.id.homebtn);
        homebtn.setTypeface(EasyFonts.caviarDreams(DashBoardActivity.this));
        explorebtn = (Button) findViewById(R.id.explorebtn);
        explorebtn.setTypeface(EasyFonts.caviarDreams(DashBoardActivity.this));
        inboxbtn = (Button) findViewById(R.id.inboxbtn);
        inboxbtn.setTypeface(EasyFonts.caviarDreams(DashBoardActivity.this));
        search = (ImageButton) findViewById(R.id.search);

        FM = getSupportFragmentManager();
        FT = FM.beginTransaction();
        fetchLocationLast();

        searchHotel = (AutoCompleteTextView) findViewById(R.id.searchHotel);
        clickSearch = (ImageButton) findViewById(R.id.clickSearch);
        clickSearch.setVisibility(View.GONE);
        searchHotel.setVisibility(View.GONE);

        replaceFragment(new HomeFragment());
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
                            + "We use this to help you discover places around you.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialog.dismiss();
                            CustomPreference.readBoolean(DashBoardActivity.this, CustomPreference.LOCATIONENABLE, true);
                        }

                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            CustomPreference.writeBoolean(DashBoardActivity.this, CustomPreference.LOCATIONENABLE, false);
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            CustomPreference.writeBoolean(DashBoardActivity.this, CustomPreference.LOCATIONENABLE, false);

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

                    Log.e("cityName"," = " + cityName + ", " + stateName + ", " + countryName);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void replaceFragment(Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }
}
