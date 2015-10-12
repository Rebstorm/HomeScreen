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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.LoadingDialogue;
import jacks.paul.homescreen.adapters.NoteButton;
import jacks.paul.homescreen.download.DownloadInterface;
import jacks.paul.homescreen.download.DownloadWeather;
import jacks.paul.homescreen.parsing.ParseWeather;
import jacks.paul.homescreen.parsing.WeatherInterface;
import jacks.paul.homescreen.types.NoteData;
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

    //UI
    TextView homeText;
    TextView homeTextDesc;
    TextView dateText;

    ImageView weatherImg;

    // FAB
    FloatingActionButton fabRefresh;
    FloatingActionButton fabAdd;

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

        //WeatherIcon
        weatherImg = (ImageView) v.findViewById(R.id.weatherImage);

        // HorizontalScrollView for notes
        noteList = (HorizontalScrollView)v.findViewById(R.id.noteScroller);
        // The HorizontalScrollView child (Holds the noteButtons)
        noteItems = (LinearLayout)v.findViewById(R.id.scroll_items);

        // TODO: FOR TESTING ONLY!! REMOVE IN PRODUCTION
        NoteData data = new NoteData();
        data.header = "Butts, so much butts";
        data.importance = NoteData.Importance.Middle;
        NoteButton asd = new NoteButton(getActivity(), data);

        NoteData data1 = new NoteData();
        data1.header = "Butts, so much butts";
        data1.importance = NoteData.Importance.Very;
        NoteButton asd1 = new NoteButton(getActivity(), data1);

        NoteData data2 = new NoteData();
        data2.header = "Butts, so much butts";
        data2.importance = NoteData.Importance.Very;
        NoteButton asd2 = new NoteButton(getActivity(), data2);

        NoteData data3 = new NoteData();
        data3.header = "Butts, so much butts";
        data3.importance = NoteData.Importance.Low;
        NoteButton asd3 = new NoteButton(getActivity(), data3);

        NoteData data4 = new NoteData();
        data4.header = "Butts, so much butts";
        data4.importance = NoteData.Importance.Very;
        NoteButton asd4 = new NoteButton(getActivity(), data4);

        NoteData data5 = new NoteData();
        data5.header = "Butts, so much butts";
        data5.importance = NoteData.Importance.Very;
        NoteButton asd5 = new NoteButton(getActivity(), data5);

        NoteData data6 = new NoteData();
        data6.header = "Butts, so much butts";
        data6.importance = NoteData.Importance.Very;
        NoteButton asd6 = new NoteButton(getActivity(), data6);

        noteItems.addView(asd);
        noteItems.addView(asd1);
        noteItems.addView(asd2);
        noteItems.addView(asd3);
        noteItems.addView(asd4);
        noteItems.addView(asd5);
        noteItems.addView(asd6);

        // Current DateTime
        dateText = (TextView)v.findViewById(R.id.date_text);

        // The load animation window
        loadWindow = new LoadingDialogue(getActivity());

        fabAdd = (FloatingActionButton)v.findViewById(R.id.fabAdd);
        fabAdd.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray)));
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(getActivity(), "Here is the add function for new notes", Toast.LENGTH_SHORT).show();
            }
        });

        fabRefresh = (FloatingActionButton)v.findViewById(R.id.fabRefresh);
        fabRefresh.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray)));
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

