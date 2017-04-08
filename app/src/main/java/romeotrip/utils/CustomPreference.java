package romeotrip.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class CustomPreference {


    public static final String PREF_NAME = "LOYALTY_PERKS";

    @SuppressWarnings("deprecation")
    public static final int MODE = Context.MODE_WORLD_WRITEABLE;

    public static final String IS_REGISTER = "REGISTER";
    //public static final String VER_CODE = "VER_CODE";
    public static final String USER_ID = "USER_ID";
    public static final String USERNAME = "USER_NAME";
    //public static final String EMAIL = "EMAIL";
    public static final String MOBILE_NO = "MOBILENO";
    public static final String OTP = "OTP";
    public static final String FIRST_TIME = "first";

    public static final String LOCATIONENABLE = "LOCATION";
    public static final String ADDLOCATION = "ADDLOCATION";

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    public static final String COUNTRY = "COUNTRY";
    public static final String STATE = "STATE";
    public static final String CITY = "CITY";
    public static final String LANDMARK = "LANDMARK";

    public static final Boolean SENT_TOKEN_TO_SERVER = true;

    public static final String REGISTRATION_COMPLETE = "REGISTRATION COMPLETE";
    public static final String TOKEN = "TOKEN";

    //public static final String MESSAGE_ID = "9";

    //public static final String DEVICE_TYPE = "device_type";
    //public static final String DEVICE_ID = "device_id";
    public static final String PROFILEPIC = "PROFILE" ;

    public static final String TOTAL_COUNTERFORNOTIFICATION = "TOTAL";

    public static final String EARN_PTS = "EARN";
    public static final String REDEEM_PTS = "REDEEM";
    public static final String MES_FROM = "MES";

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }

    public static void removeAll(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.commit();
    }
}