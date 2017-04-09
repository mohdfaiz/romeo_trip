package romeotrip.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.example.mohdfaiz.romeotrip.R;
import com.vstechlab.easyfonts.EasyFonts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash_Screen extends AppCompatActivity {

    TextView splash_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new SplashMethod().execute();

        printKeyHash(Splash_Screen.this);

        splash_text = (TextView) findViewById(R.id.splash_text);
        splash_text.setTypeface(EasyFonts.caviarDreams(Splash_Screen.this));
    }

    private class SplashMethod extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            if (CustomPreference.readString(SplashScreen.this, CustomPreference.USER_ID, "").equals("")) {
//                startActivity(new Intent(SplashScreen.this, GetOtp.class));
//                finish();
//            } else {
                startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                finish();
//            }
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
                System.out.println("KeyHash=" + key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
