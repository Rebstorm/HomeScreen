package jacks.paul.homescreen.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.widgets.WebStayView;

/**
 * Created by User on 2015-10-20.
 */
public class WebFragment extends Fragment {


    WebView spotifyPlayer;

    public WebFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web, container, false);
        spotifyPlayer = (WebView)v.findViewById(R.id.spotifyWeb);
        // In order to make the view stay in the app - Look at it as an adapter.
        spotifyPlayer.setWebViewClient(new WebStayView());
        runWebSettings();

        return v;

    }

    private void runWebSettings() {
        spotifyPlayer.loadUrl("http://google.com");
    }
}
