package romeotrip.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.mohdfaiz.romeotrip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import romeotrip.api.GetPlacesApi;
import romeotrip.data.PlacesAroundData;

public class HomeFragment extends Fragment {

    RecyclerView placesList;
    LinearLayoutManager manager;
    public ArrayList<PlacesAroundData> placesAroundDatas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placesList = (RecyclerView) view.findViewById(R.id.placesList);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        placesList.setLayoutManager(manager);
    }

    ArrayList<PlacesAroundData> allPlacesList = new ArrayList<>();

//    void getPlacesApi() {
//        new GetPlacesApi(getActivity()) {
//            @Override
//            public void responseApi(JSONObject response) {
//                super.responseApi(response);
//
//                try {
//                    JSONObject header1 = response.getJSONObject("mohdfaiz.in");
//                    String res_code = header1.getString("res_code");
//                    String res_msg = header1.getString("res_msg");
//
//                    if (res_code.equals("1")) {
//                        allAddList = new ArrayList<>();
//                        JSONArray addressArray = header1.getJSONArray("CustomerAddress");
//
//                        for (int i = 0; i < addressArray.length(); i++) {
//                            JSONObject obj = addressArray.getJSONObject(i);
//                            PlacesAroundData item = new PlacesAroundData();
//                            item.setId(obj.getString("id"));
//                            item.setFull_name(obj.getString("full_name"));
//                            item.setStreet(obj.getString("street"));
//                            item.setContact_no(obj.getString("contact_no"));
//                            item.setCity(obj.getString("city"));
//                            item.setState(obj.getString("state"));
//                            item.setCountry(obj.getString("country"));
//
//                            item.setZip(obj.getString("zip"));
//                            item.setStatus(obj.getString("status"));
//                            item.setAdd_date(obj.getString("add_date"));
//                            item.setModify_date(obj.getString("modify_date"));
//
//                            item.setDefaultt(obj.getString("default"));
//                            item.setAdd_type(obj.getString("add_type"));
//                            item.setSelectorAdd(false);
//
//                            allAddList.add(item);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void errorApi(VolleyError error) {
//                super.errorApi(error);
//            }
//        };
//    }
}
