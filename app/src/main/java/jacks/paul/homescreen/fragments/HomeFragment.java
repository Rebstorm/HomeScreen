package jacks.paul.homescreen.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.download.DownloadInterface;
import jacks.paul.homescreen.download.DownloadWeather;
import jacks.paul.homescreen.parsing.XMLParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DownloadInterface {

    public DownloadInterface response;
    DownloadWeather task = new DownloadWeather();


    // Why must I never use these? Thats sad. I like constructors.
    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        task.delegate = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        getWeatherXML();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void getWeatherXML() {
        task.execute("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58;msl=70");

    }


    @Override
    public void processFinished(String output) {
        XMLParser xml = new XMLParser(output);
    }
}

