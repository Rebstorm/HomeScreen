package jacks.paul.homescreen.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.download.DownloadInterface;
import jacks.paul.homescreen.download.DownloadWeather;
import jacks.paul.homescreen.parsing.ParseWeather;
import jacks.paul.homescreen.parsing.WeatherInterface;
import jacks.paul.homescreen.types.TemperatureData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DownloadInterface, WeatherInterface {

    public DownloadInterface response;
    DownloadWeather task = new DownloadWeather();
    ParseWeather xml = new ParseWeather();


    //UI
    TextView homeText;

    public HomeFragment() {
        // Why dont I never use these? Thats sad. I like constructors.
    }

    // TODO: Find out difference between onCreate & onCreateView.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task.delegate = this;
        xml.delegate = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        homeText = (TextView)v.findViewById(R.id.home_text);

        task.execute("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=50.9;lon=6.9");

        return v;
    }



    @Override
    public void processFinished(String output) {
        xml.getWeather(output);

    }

    @Override
    public void dataReceived(final TemperatureData data) {

        // The Dispatcher. LOL.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                homeText.setText(String.valueOf(data.temperature));
            }
        });

    }




}

