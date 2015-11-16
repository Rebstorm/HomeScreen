package jacks.paul.homescreen.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jacks.paul.homescreen.MainActivity;
import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.NoteInterface;
import jacks.paul.homescreen.adapters.NotifyMainActivity;
import jacks.paul.homescreen.db.HomeDatabase;
import jacks.paul.homescreen.types.NoteData;
import jacks.paul.homescreen.types.TemperatureData;
import jacks.paul.homescreen.widgets.AddDialogue;
import jacks.paul.homescreen.widgets.LoadingDialogue;
import jacks.paul.homescreen.widgets.NoteButton;

/*
       HOME FRAGMENT, CONTAINS WEATHER AND NOTES
 */
public class HomeFragment extends Fragment implements NoteInterface{


    HorizontalScrollView noteList;
    LinearLayout noteItems;

    NoteInterface removeNote;

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

    //isitNight?
    Boolean isNight;

    // Using persistent data
    boolean ranPreviously = true;
    TemperatureData previousData = new TemperatureData();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    // DB
    HomeDatabase db;
    List<NoteData> noteAllItems;

    public NotifyMainActivity notifier;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public HomeFragment() {
    }

    // OnCreateView happens after OnCreate() - Therefore, I have decided Im gonna use OnCreateView instead.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        neouFat = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Neou-Thin.ttf");

        db = new HomeDatabase(getActivity());

        // TextViews
        homeText = (TextView)v.findViewById(R.id.home_text);
        homeTextDesc = (TextView)v.findViewById(R.id.home_text_desc);
        dateText = (TextView)v.findViewById(R.id.date_text);
        // Set fonts
        setFontStyles();

        //WeatherIcon
        weatherImg = (ImageView) v.findViewById(R.id.weatherImage);

        // HorizontalScrollView for notes
        noteList = (HorizontalScrollView)v.findViewById(R.id.noteScroller);
        // The HorizontalScrollView child (Holds the noteButtons)
        noteItems = (LinearLayout)v.findViewById(R.id.scroll_items);

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

        getBundledArgs();

        fabRefresh = (FloatingActionButton)v.findViewById(R.id.fabRefresh);
        fabRefresh.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray)));
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
                weatherUpdate();
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
                        getDate();
                        weatherUpdate();
                    }
                });
                // 1 hr in ms.
            }// 3600000 ms = 1 hr
        }, 0, 3600000);
        rebuildNotes();

        // If data exists.
        getDate();
        setWeatherUIFromOld();

        return v;
    }


    private void setFontStyles() {
        homeText.setTypeface(neouFat);
        homeTextDesc.setTypeface(neou);
        dateText.setTypeface(neouFat);
    }


    public void changeUIData(final TemperatureData data) {
        // Making sure the fragment exists.. So we dont get a NPE
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // clear the animation
                    if(animation != null)
                        animation.setAnimationListener(null);

                    homeText.setText(String.valueOf(data.temperature + "°C - Outside"));
                    homeTextDesc.setText(data.windDirection + "\n" + data.humidity);
                    setWeatherIcon(data.weatherIcon, getActivity());
                    loadWindow.close();

                }
            });
        }
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

    private void setWeatherUIFromOld() {


        preferences = this.getActivity().getSharedPreferences("weatherPrefs", Context.MODE_PRIVATE);
        String initialized = preferences.getString("temp", null);

        if(initialized != null) {
            previousData.temperature = Double.valueOf(preferences.getString("temp", null));
            previousData.lastUpdated = preferences.getString("lastupdate", null);
            previousData.humidity = preferences.getString("hum", null);
            previousData.windDirection = preferences.getString("winddir", null);
            previousData.weatherIcon = TemperatureData.WeatherIcon.valueOf(preferences.getString("icon", null));
            changeUIData(previousData);

            // We dont need to update this, as it'll be done automatically.
            ranPreviously = true;
        }
        else {
            ranPreviously = false;
        }
    }


    public void getDate(){
        DateFormat dateNow = new SimpleDateFormat("HH:mm, EEE, dd/MM - yyyy");
        Date currentTime = new Date();
        Calendar cal = Calendar.getInstance();

        if(cal.get(Calendar.HOUR_OF_DAY) < 6 || cal.get(Calendar.HOUR_OF_DAY) > 19)
            isNight = true;
        else
            isNight = false;

        // UI update
        if(!ranPreviously)
            dateText.setText("Last updated: " + dateNow.format(currentTime));
    }

    // Sets weather icon
    private void setWeatherIcon(TemperatureData.WeatherIcon weatherIcon, Context context) {
        switch (weatherIcon) {
            case Sunny:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.sunny_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.sunny));
                break;
            case LightRain:

                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.lightrain_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.lightrain));
                break;
            case Rain:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.rain_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.rain));
                break;
            case Cloudy:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.cloudy_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.cloudy));
                break;
            case LittleCloudy:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.littlecloudy_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.littlecloudy));
                break;
            case PartlyCloudy:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.partlycloudy_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.partlycloudy));
                break;
            case LightRainSun:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.lightrain_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.lightrainsun));
                break;
            case LightRainThunderstorm:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.lightrainthunderstorm_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.lightrainthunderstorm));
                break;
            case Sleet:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.sleet_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.sleet));
                break;
            case SnowSun:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.snow2_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.snowsun));
                break;
            case Haze:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.haze_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.haze_night));
                break;
            case Mostly_Cloudy:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.cloudy_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.cloudy));
                break;
            case RainThunder:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.rainthunder_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.rainthunder));
                break;
            case Thunderstorm:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.lightrainthunderstorm_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.thunderstorm));
                break;
            case Snow:
                if(isNight)
                    weatherImg.setBackground(ContextCompat.getDrawable(context,R.drawable.snowsun_night));
                else
                    weatherImg.setBackground(ContextCompat.getDrawable(context, R.drawable.snowsun));
                break;
            case Dunno:
                break;
        }
    }

    void weatherUpdate(){
        if(!ranPreviously)
            getWeatherInformation("http://api.yr.no/weatherapi/locationforecast/1.9/?lat=50.9;lon=6.9");
        else
            ranPreviously = false;
    }

    public void getWeatherInformation(String xmlURL) {
        // Feels dirty, but it sends it back to main Activity that then stores the data and gives it back to the fragment
        if (notifier != null) {
            notifier.beginDownload(xmlURL);
            animateWeatherUpdate();
        }
    }

    // Gettings persistent data across fragmentation
    public void getBundledArgs(){
        // Has it already been updated? If so, use that information instead
        Bundle previousWeatherData = getArguments();
        if(previousWeatherData != null) {
            TemperatureData data = new TemperatureData();
            data.temperature = previousWeatherData.getDouble("TempData");
            data.humidity = previousWeatherData.getString("TempHum");
            data.windDirection = previousWeatherData.getString("TempDir");
            data.weatherIcon = (TemperatureData.WeatherIcon) previousWeatherData.getSerializable("TempIcon");
            ranPreviously = true;

            changeUIData(data);
        }
    }

    // Note data
    @Override
    public void newNote(NoteData data) {
        // The new note & update the linear view
        db.addNote(data);
        rebuildNotes();
    }

    @Override
    public void removeNote(NoteData data) {
        db.removeNote(data);
        rebuildNotes();
    }

    void rebuildNotes(){
        noteItems.removeAllViews();
        noteAllItems = db.getAllNotes();
        for(int i = 0; i < noteAllItems.size(); i++){
            NoteButton newButton = new NoteButton(getActivity(), noteAllItems.get(i));
            newButton.buttonListener = this;
            noteItems.addView(newButton);
        }
    }

}

