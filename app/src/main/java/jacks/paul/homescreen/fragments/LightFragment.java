package jacks.paul.homescreen.fragments;

import android.graphics.Typeface;
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
import android.widget.SeekBar;
import android.widget.TextView;
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

import jacks.paul.homescreen.MainActivity;
import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.HueBridgeAdapter;
import jacks.paul.homescreen.adapters.HueGridAdapter;
import jacks.paul.homescreen.hue.ColorPicker;
import jacks.paul.homescreen.hue.HueConnected;
import jacks.paul.homescreen.hue.HueInterface;
import jacks.paul.homescreen.types.HueConnectionData;
import jacks.paul.homescreen.types.HueData;
import jacks.paul.homescreen.widgets.AuthDialogue;
import jacks.paul.homescreen.widgets.ConnectionDialog;

/**
 *
 * Light Fragment - Let there be light!
 *
 * Im putting this on hiatus until I have the time and bother to figure out the authentication stuff in Spotify.
 *
 */
public class LightFragment extends Fragment implements HueInterface{

    ListView hueBridgeList;
    GridView hueLightGrid;
    Button hueOffButton;
    TextView hueTitle;
    TextView hueProgressText;
    SeekBar hueBrightness;

    private List<HueConnectionData> bridgeList;
    private List<PHLight> lights;

    ArrayList<HueData> allData = new ArrayList<>();

    HueBridgeAdapter adapter;
    HueGridAdapter gridAdapter;

    PHBridge phBridge;
    private PHHueSDK phHueSDK;


    // Auth Dialog
    AuthDialogue authDialogue;


    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    // Delegate interface for closing auth dialog
    public HueConnected buttonpressed = null;


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

        neouFat = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Neou-Thin.ttf");

        hueTitle = (TextView)v.findViewById(R.id.hueTextDesc);
        hueTitle.setText("Hue (Connected)");

        hueProgressText = (TextView)v.findViewById(R.id.hue_gradient_text_progress);
        hueProgressText.setText("");



        setFontStyles();

        hueBrightness = (SeekBar)v.findViewById(R.id.hue_gradientbar);
        hueBrightness.setProgressDrawable(getActivity().getDrawable(R.drawable.overlay_seeker_bar));
        hueBrightness.setOnSeekBarChangeListener(seekbarListener);


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
                        phBridge.updateLightState(phLight, lightState, phLightListener);
                    }
                }
            }
        });

        if(bridgeList == null) {
            hueBridgeList.setVisibility(View.INVISIBLE);
            hueTitle.setVisibility(View.VISIBLE);
        }
        else {
            hueTitle.setVisibility(View.INVISIBLE);
            hueBridgeList.setVisibility(View.VISIBLE);
        }


        return v;

    }

    public void onResume(){
        super.onResume();

    }

    SeekBar.OnSeekBarChangeListener seekbarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            double percent = progress / 2.54 ;
            int percentInt = (int)percent;
            hueProgressText.setText(Integer.toString(percentInt) + "%");

            try {
                lights = phBridge.getResourceCache().getAllLights();
                for (int i = 0; i < lights.size(); i++) {
                    PHLight phLight = lights.get(i);
                    PHLightState lightState = new PHLightState();
                    lightState.setBrightness(progress);
                    phBridge.updateLightState(phLight, lightState, phLightListener);
                }
            }catch(ArrayIndexOutOfBoundsException e) {
                Toast.makeText(getActivity(), "Oops! Too few lamps are reachable", Toast.LENGTH_LONG).show();
            }catch(NullPointerException e){

            }



        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void getThemeList(){
        allData.clear();
        ColorPicker colors = new ColorPicker();
        allData.addAll(colors.getAllThemes());
    }

    private void getBundledArgs(){


    }

    private void setFontStyles() {
        hueTitle.setTypeface(neouFat);
        hueProgressText.setTypeface(neouFat);

    }

    public void setupAdapter(List<PHAccessPoint> list, PHHueSDK phHueSDK){
        this.phHueSDK = phHueSDK;
        ArrayList<HueConnectionData> convertedList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            HueConnectionData data = new HueConnectionData();
            data.name = "Name";
            data.accessPoint = list.get(i);
            convertedList.add(data);
        }
        this.bridgeList = convertedList;
        if(hueBridgeList != null) {
            adapter = new HueBridgeAdapter(getActivity(), bridgeList);
            hueBridgeList.setAdapter(adapter);
        }
    }

    @Override
    public void connectAP(PHAccessPoint accessPoint) {
        phHueSDK.connect(accessPoint);

        authDialogue = new AuthDialogue(getActivity(), LightFragment.this);
        authDialogue.open();

    }

    public void connectionComplete(){
        if(phBridge != null)
            buttonpressed.buttonPressed(true);

        hueBridgeList.setVisibility(View.INVISIBLE);
        hueTitle.setVisibility(View.VISIBLE);
        hueTitle.setText("Hue Connected");
    }

    @Override
    public void themeSelected(HueData theme) {
        setTheme(theme);
    }

    public void updateHueBridge(PHBridge phBridge){
        this.phBridge = phBridge;
        try {
            hueBrightness.setProgress(phBridge.getResourceCache().getAllLights().get(0).getLastKnownLightState().getBrightness());

        }catch(ArrayIndexOutOfBoundsException e){

        }catch(NullPointerException e){

        }
        lights = phBridge.getResourceCache().getAllLights();
    }

    PHLightListener phLightListener = new PHLightListener() {

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
        // For consistency with the ui
        try {
            lights = phBridge.getResourceCache().getAllLights();
                for (int i = 0; i < theme.colors.size(); i++) {
                    PHLight phLight = lights.get(i);
                    PHLightState lightState = new PHLightState();
                    lightState.setOn(true);
                    lightState.setHue(theme.colors.get(i));
                    lightState.setBrightness(hueBrightness.getProgress());
                    phBridge.updateLightState(phLight, lightState, phLightListener);
                    Thread.sleep(500);
                }



        }catch(ArrayIndexOutOfBoundsException e) {
            Toast.makeText(getActivity(), "Oops! Too few lamps are reachable", Toast.LENGTH_LONG).show();
        }catch(NullPointerException e){

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
