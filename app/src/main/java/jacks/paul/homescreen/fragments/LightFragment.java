package jacks.paul.homescreen.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.hue.HueSharedPreferences;

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

        phHueSDK = PHHueSDK.create();




        return v;

    }

    public void onResume(){
        super.onResume();


    }

    void SetupBridge() {

        PHBridge bridge = phHueSDK.getSelectedBridge();


    }



}
