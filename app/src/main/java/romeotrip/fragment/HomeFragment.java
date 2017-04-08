package romeotrip.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.example.mohdfaiz.romeotrip.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import romeotrip.adapter.PlacesAdapter;
import romeotrip.api.GetPlacesApi;
import romeotrip.data.PlacesAroundData;
import romeotrip.utils.CustomPreference;

public class HomeFragment extends Fragment {

    RecyclerView placesListView;
    LinearLayoutManager manager;
    Button user_location, header_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        header_text = (Button) view.findViewById(R.id.header_text);
        user_location = (Button) view.findViewById(R.id.user_location);
        header_text.setTypeface(EasyFonts.caviarDreams(getActivity()));
        user_location.setTypeface(EasyFonts.caviarDreams(getActivity()));
        String current_loc = CustomPreference.readString(getActivity(), CustomPreference.CITY, "") + ", " + CustomPreference.readString(getActivity(), CustomPreference.STATE, "")  + ", " +  CustomPreference.readString(getActivity(), CustomPreference.COUNTRY, "");
        user_location.setText("Your location is : " + current_loc);

        placesListView = (RecyclerView) view.findViewById(R.id.placesListView);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        placesListView.setLayoutManager(manager);

        getPlacesApi();
    }

    ArrayList<PlacesAroundData> allPlacesList = new ArrayList<>();
    public void getPlacesApi() {
        new GetPlacesApi(getActivity()) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
            }

            @Override
            public void responseArrayApi(JSONArray response) {
                super.responseArrayApi(response);

                try {

//                    JSONArray details = response.getJSONArray("fearfighter");

                    allPlacesList = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        PlacesAroundData data = new PlacesAroundData();
                        data.setCo(obj.getString("co"));
                        data.setSt(obj.getString("st"));

                        allPlacesList.add(data);
                    }

                    PlacesAdapter trigger = new PlacesAdapter(getActivity(), allPlacesList);
                    placesListView.setAdapter(trigger);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }
}
