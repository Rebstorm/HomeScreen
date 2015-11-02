package jacks.paul.homescreen.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.HueBridgeAdapter;
import jacks.paul.homescreen.hue.HueInterface;

/**
 * Music Fragment, details from Spotify:
 *
 * https://developer.spotify.com/my-applications/#!/applications/c30927946e87452e8101595852c75588
 *
 * Im putting this on hiatus until I have the time and bother to figure out the authentication stuff in Spotify.
 *
 */
public class LightFragment extends Fragment implements HueInterface{

    ListView hueBridgeList;
    GridView hueLightGrid;

    private List<PHAccessPoint> bridgeList;
    private List<PHLight> lights;

    HueBridgeAdapter adapter;
    ArrayAdapter<String> gridAdapter;

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

        hueLightGrid = (GridView)v.findViewById(R.id.hueLampView);
        hueLightGrid.setAdapter(gridAdapter);


        adapter = new HueBridgeAdapter(getActivity(), bridgeList);
        adapter.conHueInterface = this;

        hueBridgeList = (ListView)v.findViewById(R.id.hueList);
        hueBridgeList.setAdapter(adapter);




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
        if(hueBridgeList != null) {
            adapter = new HueBridgeAdapter(getActivity(), bridgeList);
            hueBridgeList.setAdapter(adapter);
        }
    }

    @Override
    public void connectAP(PHAccessPoint accessPoint) {
        phHueSDK.connect(accessPoint);
    }


    public void updateHueBridge(PHBridge phBridge){
        this.phBridge = phBridge;
        lights = phBridge.getResourceCache().getAllLights();

        PHAccessPoint point = new PHAccessPoint();
        point.setIpAddress("IP");
        point.setUsername("Butthole");
        point.setBridgeId("Butthole2");
        point.setMacAddress("Yourmom");
        bridgeList.add(point);
        adapter.updateData(bridgeList);

        ArrayList<String> lampnames = new ArrayList<String>();

        for(PHLight phLight : lights){
            lampnames.add(phLight.getName());

        }

        gridAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lampnames);
        hueLightGrid.setAdapter(gridAdapter);



    }



}
