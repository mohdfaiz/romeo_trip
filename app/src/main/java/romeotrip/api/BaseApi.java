package romeotrip.api;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import romeotrip.AppController;
import romeotrip.utils.Util;

public abstract class BaseApi {

    public abstract void responseApi(JSONObject response);

    public abstract void errorApi(VolleyError error);

    public void postJsonApi(final Context context, String url, final JSONObject headerObj, final String tag, final boolean bool) {
        if (Util.isNetConnected(context)) {
            final ProgressDialog pDialog = new ProgressDialog(context);
            final AlertDialog dialog = new SpotsDialog(context);
            if (bool) {
//                pDialog.setCancelable(false);
//                pDialog.show();
                dialog.setCancelable(false);
                dialog.show();
            }

            final Request.Priority mPriority = Request.Priority.HIGH;

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, headerObj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e(tag, response.toString());
                    if (bool) {
//                        if ((pDialog != null) && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
                        if ((dialog != null) && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    responseApi(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(tag, "Error: " + error.getMessage());
                    if (bool) {
//                        pDialog.dismiss();
                        dialog.dismiss();
                    }
                    errorApi(error);
                }
            }) {
                @Override
                public Priority getPriority() {
                    return mPriority;
                }

            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(3 * 60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
        else {
            errorApi(new VolleyError("Check Internet Connection."));
        }
    }

    public void getJsonApi(final Context context, String url, final String tag, final boolean bool) {
        if (Util.isNetConnected(context)) {
//            final ProgressDialog pDialog = new ProgressDialog(context);
            final AlertDialog dialog = new SpotsDialog(context);
//            if (bool) {
//                pDialog.setCancelable(false);
//                pDialog.show();
//            }
            if (bool) {
                dialog.setCancelable(false);
                dialog.show();
            }

            final Request.Priority mPriority = Request.Priority.HIGH;

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e(tag, response.toString());
                    if (bool) {
//                        if ((pDialog != null) && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
                        if ((dialog != null) && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    responseApi(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(tag, "Error: " + error.getMessage());
                    if (bool) {
//                        pDialog.dismiss();
                        dialog.dismiss();
                    }
                    errorApi(error);
                }
            }) {
                @Override
                public Priority getPriority() {
                    return mPriority;
                }

            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(3 * 60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
        else {
            errorApi(new VolleyError("Check Internet Connection."));
        }
    }
}