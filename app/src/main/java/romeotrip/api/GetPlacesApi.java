package romeotrip.api;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPlacesApi extends BaseApi {

    public GetPlacesApi(Context context) {
        String url = "http://build2.ixigo.com/action/content/zeus/autocomplete?searchFor=tpAutoComplete&neCategories=City&query=query";

        try {

            getJsonApi(context, url, "GetPlacesApi", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
