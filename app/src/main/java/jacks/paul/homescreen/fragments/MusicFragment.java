package jacks.paul.homescreen.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.WebStayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    WebView spotifyPlayer;

    public MusicFragment() {
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
        View v = inflater.inflate(R.layout.fragment_music, container, false);
        spotifyPlayer = (WebView)v.findViewById(R.id.spotifyWeb);
        // In order to make the view stay in the app
        spotifyPlayer.setWebViewClient(new WebStayView());
        runWebSettings();

        return v;

    }

    private void runWebSettings() {
        spotifyPlayer.loadUrl("http://google.com");
    }


}
