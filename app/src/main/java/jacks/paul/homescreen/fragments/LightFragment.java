package jacks.paul.homescreen.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import jacks.paul.homescreen.adapters.HueGridAdapter;
import jacks.paul.homescreen.hue.ColorPicker;
import jacks.paul.homescreen.hue.HueInterface;
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

    ListView hueBridgeList;
    GridView hueLightGrid;
    Button hueOffButton;

    private List<PHAccessPoint> bridgeList;
    private List<PHLight> lights;

    ArrayList<HueData> allData = new ArrayList<>();

    HueBridgeAdapter adapter;
    HueGridAdapter gridAdapter;


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

        getThemeList();

        gridAdapter = new HueGridAdapter(getActivity(), allData);
        gridAdapter.hueInterface = this;

        hueLightGrid = (GridView)v.findViewById(R.id.hueLampView);
        hueLightGrid.setAdapter(gridAdapter);

        adapter = new HueBridgeAdapter(getActivity(), bridgeList);
        adapter.conHueInterface = this;

        hueBridgeList = (ListView)v.findViewById(R.id.hueList);
        hueBridgeList.setAdapter(adapter);

        hueOffButton = (Button)v.findViewById(R.id.hue_turn_off);
        hueOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phBridge != null && lights != null){
                    for(int i = 0; i < lights.size(); i++) {
                        PHLight phLight = lights.get(i);
                        PHLightState lightState = new PHLightState();
                        lightState.setOn(false);
                        lightState.setBrightness(254);
                        phBridge.updateLightState(phLight, lightState, listener);
                    }
                }
            }
        });

        return v;

    }

    public void onResume(){
        super.onResume();

    }

    private void getThemeList(){
        allData.clear();
        ColorPicker colors = new ColorPicker();
        allData.addAll(colors.getAllThemes());
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

    @Override
    public void themeSelected(HueData theme) {
        setTheme(theme);
    }


    public void updateHueBridge(PHBridge phBridge){
        this.phBridge = phBridge;
        lights = phBridge.getResourceCache().getAllLights();
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


    private void setTheme(HueData theme) {

        try {
            lights = phBridge.getResourceCache().getAllLights();
                for (int i = 0; i < theme.colors.size(); i++) {
                    PHLight phLight = lights.get(i);
                    PHLightState lightState = phLight.getLastKnownLightState();
                    if(!lightState.isOn())
                        lightState.setOn(true);
                    lightState.setHue(theme.colors.get(i));
                    lightState.setBrightness(254);
                    phBridge.updateLightState(phLight, lightState, listener);

                }
        }catch(ArrayIndexOutOfBoundsException e) {
            Toast.makeText(getActivity(), "Oops! Too few lamps are reachable", Toast.LENGTH_LONG).show();
        }catch(NullPointerException e){

        }

    }



}
