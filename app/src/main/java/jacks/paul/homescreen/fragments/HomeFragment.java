package jacks.paul.homescreen.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.AddDialogue;
import jacks.paul.homescreen.adapters.LoadingDialogue;
import jacks.paul.homescreen.adapters.NoteButton;
import jacks.paul.homescreen.adapters.NoteInterface;
import jacks.paul.homescreen.download.DownloadInterface;
import jacks.paul.homescreen.download.DownloadWeather;
import jacks.paul.homescreen.parsing.ParseWeather;
import jacks.paul.homescreen.parsing.WeatherInterface;
import jacks.paul.homescreen.types.NoteData;
import jacks.paul.homescreen.types.TemperatureData;

/*
       HOME FRAGMENT, CONTAINS WEATHER AND NOTES
 */
public class HomeFragment extends Fragment implements DownloadInterface, WeatherInterface, NoteInterface {


    // DownloadWeather task = new DownloadWeather();
    public ParseWeather xml = new ParseWeather();

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
    AddDialogue addWindow;

    //Animation
    AnimationSet animation;

    // Timer & Auto refresh
    Timer weatherTimer;

    public HomeFragment() {
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

        // Current DateTime
        dateText = (TextView)v.findViewById(R.id.date_text);

        // The load animation window
        loadWindow = new LoadingDialogue(getActivity());

        // The Add window + Delegate
        addWindow = new AddDialogue(getActivity());
        addWindow.noteReceived = this;

        fabAdd = (FloatingActionButton)v.findViewById(R.id.fabAdd);
        fabAdd.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray)));
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWindow.show();
            }
        });

        fabRefresh = (FloatingActionButton)v.findViewById(R.id.fabRefresh);
        fabRefresh.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray)));
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Updating weather information..", Toast.LENGTH_LONG).show();
                getWeatherInformation("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=50.9;lon=6.9");
                getDate();
                // Loading window
                loadWindow.open();
            }
        });

        // Timer that updates automatically every hour.
        weatherTimer = new Timer();
        weatherTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getWeatherInformation("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=50.9;lon=6.9");
                        getDate();

                    }
                });

            }// 3600000 ms = 1 hr
        }, 0, 3600000);

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
                // clear the animation
                animation.setAnimationListener(null);

                homeText.setText(String.valueOf(data.temperature + "°C - Outside"));
                homeTextDesc.setText(data.windDirection + "\n" + data.humidity);
                setWeatherIcon(data.weatherIcon);

                loadWindow.close();
            }
        });
    }



    public void getWeatherInformation(String xmlURL){
        // This is fucking dirty, but since AsyncTasks can only be run once, you always
        // have to make it new for each time. Which means I have to redefine the delegate/interface
         DownloadWeather task = new DownloadWeather();
        task.delegate = this;
        task.execute(xmlURL);
        animateWeatherUpdate();
    }


    //Animation! WOAAH!
    void animateWeatherUpdate(){

        homeText.setText("Updating...");

        // Fade in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        // Fade out
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                homeText.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        homeText.startAnimation(animation);
        weatherImg.startAnimation(animation);




    }

    public void getDate(){
        DateFormat dateNow = new SimpleDateFormat("HH:mm, EEE, dd/MM - yyyy");
        Date currentTime = new Date();

        dateText.setText("Last updated: " + dateNow.format(currentTime));
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

    // Note data
    @Override
    public void newNote(NoteData data) {
       noteItems.addView(new NoteButton(getActivity(), data));
    }
}

