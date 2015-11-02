package jacks.paul.homescreen.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import java.util.ArrayList;
import java.util.List;

import jacks.paul.homescreen.MainActivity;
import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.HueBulbListAdapter;
import jacks.paul.homescreen.hue.HueInterface;
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
public class LightFragment extends Fragment implements HueInterface{

    ListView hueBulbList;
    private List<PHAccessPoint> bridgeList;
    HueBulbListAdapter adapter;
    PHBridge phBridge;
    private PHHueSDK phHueSDK;



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

        getBundledArgs();


        adapter = new HueBulbListAdapter(getActivity(), bridgeList);
        adapter.conHueInterface = this;

        hueBulbList = (ListView)v.findViewById(R.id.hueList);
        if(adapter != null)
            hueBulbList.setAdapter(adapter);

        return v;

    }

    public void onResume(){
        super.onResume();


    }

    private void getBundledArgs(){


    }

    public void setupAdapter(List<PHAccessPoint> list, PHHueSDK phHueSDK){
        this.phHueSDK = phHueSDK;
        this.bridgeList = list;
        if(hueBulbList != null) {
            adapter = new HueBulbListAdapter(getActivity(), bridgeList);
            hueBulbList.setAdapter(adapter);
        }


    }

    @Override
    public void ConnectAP(PHAccessPoint accessPoint) {
        phHueSDK.connect(accessPoint);
    }



}
