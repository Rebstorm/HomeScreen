package jacks.paul.homescreen.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        Random random = new Random();

        for(PHLight phLight : lights){
            PHLightState lightState = new PHLightState();

            lightState.setHue(random.nextInt(65535));
            lightState.setBrightness(100);
            phBridge.updateLightState(phLight, lightState, listener);

        }





    }

    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Toast.makeText(getActivity(), "Lights have been updated", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };



}
