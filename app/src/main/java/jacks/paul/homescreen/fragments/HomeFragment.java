package jacks.paul.homescreen.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.LoadingDialogue;
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
   // DownloadWeather task = new DownloadWeather();
    ParseWeather xml = new ParseWeather();

    HorizontalScrollView noteList;

    LinearLayout noteItems;

    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    //UI
    TextView homeText;
    TextView homeTextDesc;
    TextView dateText;

    ImageView weatherImg;

    // FAB
    FloatingActionButton fabRefresh;

    //Overlay
    LoadingDialogue loadWindow;



    public HomeFragment() {
        // Why dont I never use these? Thats sad. I like constructors.
    }

    // TODO: Find out difference between onCreate & onCreateView.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // task.delegate = this;
        xml.delegate = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        homeText = (TextView)v.findViewById(R.id.home_text);
        homeTextDesc = (TextView)v.findViewById(R.id.home_text_desc);

        weatherImg = (ImageView) v.findViewById(R.id.weatherImage);

        fabRefresh = (FloatingActionButton)v.findViewById(R.id.fabRefresh);
        fabRefresh.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray)));

        noteList = (HorizontalScrollView)v.findViewById(R.id.noteScroller);

        noteItems = (LinearLayout)v.findViewById(R.id.scroll_items);

        // TODO: Only for testing!
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));
        noteItems.addView(new ImageButton(getActivity()));



        dateText = (TextView)v.findViewById(R.id.date_text);

        loadWindow = new LoadingDialogue(getActivity());


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Updating weather information..", Toast.LENGTH_LONG).show();
                getWeatherInformation("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=50.9;lon=6.9");

                // Loading window
                loadWindow.open();


            }
        });

        // Update UI
        getWeatherInformation("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=50.9;lon=6.9");
        getDate();


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
                homeText.setText(String.valueOf(data.temperature + "°C - Home(outside)"));
                homeTextDesc.setText(data.windDirection);
                setWeatherIcon(data.weatherIcon);

                loadWindow.close();
            }
        });
    }


    void getWeatherInformation(String xmlURL){


        // This is fucking dirty, but since AsyncTasks can only be run once, you always
        // have to make it new for each time. Which means I have to redefine the delegate/interface
        DownloadWeather task = new DownloadWeather();
        task.delegate = this;
        task.execute(xmlURL);

    }

    private void getDate(){

        DateFormat dateNow = new SimpleDateFormat("EEE, dd/MM - yyyy");
        Date currentTime = new Date();

        dateText.setText(dateNow.format(currentTime));
    }

    // Sets weather icon
    private void setWeatherIcon(TemperatureData.WeatherIcon weatherIcon) {
        switch (weatherIcon) {
            case Sunny:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.sunny));
                break;
            case Rain:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drizzle));
                break;
            case Cloudy:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.cloudy));
                break;
            case Haze:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.haze));
                break;
            case Mostly_Cloudy:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mostly_cloudy));
                break;
            case Slight_Drizzle:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.slight_drizzle));
                break;
            case Thunderstorm:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.thunderstorms));
                break;
            case Snow:
                weatherImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.snow));
                break;
        }
    }



}

