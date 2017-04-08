package romeotrip.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static int displayWidth;
    public static int displayHeight;

    public static File imageFile = null;

    public static boolean isNetConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Toast.makeText(ctx, "Enable Internet Connection..",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() != 0;
    }

    public static void openGallery(Context mContext, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        ((Activity) mContext).startActivityForResult(intent, requestCode);
    }

    public static boolean saveImage(Bitmap save_bitmap, Context act, String rootDir) {
        if (isStorageAvailable(act)) {

            String root = Environment.getExternalStorageDirectory().toString();
            File rootFile = new File(root, rootDir);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            String fname = generateUniqueName("pic") + ".jpg";
            imageFile = new File(rootFile, fname);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            FileOutputStream f = null;
            try {
                f = new FileOutputStream(imageFile);
                save_bitmap.compress(Bitmap.CompressFormat.PNG, 90, f);
                f.flush();
                f.close();
                galleryAddPic(imageFile, act);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static void galleryAddPic(File file, final Context context) {
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, (String[]) null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, final Uri uri) {

                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    private static String generateUniqueName(String filename) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        filename = filename + timeStamp;
        return filename;
    }

    public static boolean isStorageAvailable(Context con) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(con, "sd card is not writeable", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(con, "SD is not available..!!", Toast.LENGTH_SHORT)
                    .show();
        }
        return false;
    }

    public static void printLog(String label, String text1, String text2)
    {
        Log.e(label, text1 + text2);
    }
}
