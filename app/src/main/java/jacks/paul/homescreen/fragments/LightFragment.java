package jacks.paul.homescreen.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import java.util.ArrayList;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.HueBulbListAdapter;
import jacks.paul.homescreen.hue.HueSharedPreferences;
import jacks.paul.homescreen.types.HueData;

/**
 * Music Fragment, details from Spotify:
 *
 * https://developer.spotify.com/my-applications/#!/applications/c30927946e87452e8101595852c75588
 *
 * Im putting this on hiatus until I have the time and bother to figure out the authentication stuff in Spotify.
 *
 */
public class LightFragment extends Fragment{

    private PHHueSDK phHueSDK;
    private static final String TAG = "HueSDK";
    private HueSharedPreferences preferences;

    private boolean lastSearchWasIPScan = false;

    ListView hueBulbList;

    ArrayList<HueData> data = new ArrayList<HueData>();


    public LightFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_light, container, false);

        //phHueSDK = PHHueSDK.create();


        HueData newData = new HueData();
        newData.name = "Boobies";
        newData.color = Color.rgb(125,125,125);
        data.add(newData);

        hueBulbList = (ListView)v.findViewById(R.id.hueList);
        HueBulbListAdapter adapter = new HueBulbListAdapter(getActivity(), data);
        hueBulbList.setAdapter(adapter);



        return v;

    }

    public void onResume(){
        super.onResume();


    }

    void setupBridge() {

        PHBridge bridge = phHueSDK.getSelectedBridge();


    }



}
