package jacks.paul.homescreen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

import jacks.paul.homescreen.adapters.NotifyMainActivity;
import jacks.paul.homescreen.db.NoteDatabase;
import jacks.paul.homescreen.download.DownloadInterface;
import jacks.paul.homescreen.download.DownloadWeather;
import jacks.paul.homescreen.fragments.HomeFragment;
import jacks.paul.homescreen.fragments.LightFragment;
import jacks.paul.homescreen.fragments.WebFragment;
import jacks.paul.homescreen.hue.HueSharedPreferences;
import jacks.paul.homescreen.hue.PHWizardAlertDialog;
import jacks.paul.homescreen.parsing.ParseWeather;
import jacks.paul.homescreen.parsing.WeatherInterface;
import jacks.paul.homescreen.types.TemperatureData;
import jacks.paul.homescreen.widgets.AuthDialogue;
import jacks.paul.homescreen.widgets.ConnectionDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DownloadInterface, WeatherInterface, NotifyMainActivity {


    //Hue
    private PHHueSDK phHueSDK;
    private static final String TAG = "HueSDK";
    private HueSharedPreferences preferences;
    private boolean lastSearchWasIPScan = false;
    private List<PHAccessPoint> bridgeList;
    PHBridge phBridge;

    // Delegates
    public ParseWeather xml = new ParseWeather();

    // Temp vars
    TemperatureData data;

    // Sqlite DB
    public NoteDatabase noteDatabase;

    //Fragments
    LightFragment lightFragment;
    HomeFragment homeFragment;
    WebFragment webFragment;

    //Fragment Manager
    FragmentManager fragmentManager;

    //FABs
    FloatingActionButton fab;

    // NavDrawer
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Music Fragment
        lightFragment = new LightFragment();

        // Home Fragment stuff
        homeFragment = new HomeFragment();
        xml.delegate = this;
        homeFragment.notifier = this;

        webFragment = new WebFragment();

        //Fragment manager
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, homeFragment).commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        fab = (FloatingActionButton) findViewById(R.id.fabMenu);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.gray)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // HUE Setup
        setupHUE();




    }

    public void setupHUE(){
        // Gets an instance of the Hue SDK.
        phHueSDK = PHHueSDK.create();

        // Set the Device Name (name of your app). This will be stored in your bridge whitelist entry.
        phHueSDK.setAppName("HomeScreen");
        phHueSDK.setDeviceName(android.os.Build.MODEL);


        // Register the PHSDKListener to receive callbacks from the bridge.
        phHueSDK.getNotificationManager().registerSDKListener(listener);

        // Try to automatically connect to the last known bridge.  For first time use this will be empty so a bridge search is automatically started.
        preferences = HueSharedPreferences.getInstance(getApplicationContext());
        String lastIpAddress   = preferences.getLastConnectedIPAddress();
        String lastUsername    = preferences.getUsername();

        // Automatically try to connect to the last connected IP Address.  For multiple bridge support a different implementation is required.
        if (lastIpAddress !=null && !lastIpAddress.equals("")) {
            PHAccessPoint lastAccessPoint = new PHAccessPoint();
            lastAccessPoint.setIpAddress(lastIpAddress);
            lastAccessPoint.setUsername(lastUsername);

            if (!phHueSDK.isAccessPointConnected(lastAccessPoint)) {
                //PHWizardAlertDialog.getInstance().showProgressDialog(R.string.connecting, PHHomeActivity.this);
                Toast.makeText(getApplicationContext(), "Connecting to bridge..", Toast.LENGTH_LONG).show();
                phHueSDK.connect(lastAccessPoint);
            }
        }
        else {  // First time use, so perform a bridge search.
            doBridgeSearch();
        }

    }
    public void doBridgeSearch() {

        Toast.makeText(getApplicationContext(), "Searching for hue..", Toast.LENGTH_SHORT).show();

        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        // Start the UPNP Searching of local bridges.
        sm.search(true, true);
    }

    // Local SDK Listener
    private PHSDKListener listener = new PHSDKListener() {



        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPoint) {
            // If bridges were found..
            if (accessPoint != null && accessPoint.size() > 0) {
                phHueSDK.getAccessPointsFound().clear();
                phHueSDK.getAccessPointsFound().addAll(accessPoint);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bridgeList = phHueSDK.getAccessPointsFound();
                        lightFragment.setupAdapter(bridgeList, phHueSDK);
                    }
                });
            }
        }

        @Override
        // I have... no idea what this thing does..
        public void onCacheUpdated(List<Integer> arg0, PHBridge bridge) {
            Log.w(TAG, "On CacheUpdated");

        }

        @Override
        public void onBridgeConnected(PHBridge b, String username) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connected to bridge", Toast.LENGTH_LONG).show();
                }
            });

            phHueSDK.setSelectedBridge(b);
            phHueSDK.enableHeartbeat(b, PHHueSDK.HB_INTERVAL);
            phHueSDK.getLastHeartbeat().put(b.getResourceCache().getBridgeConfiguration().getIpAddress(), System.currentTimeMillis());
            preferences.setLastConnectedIPAddress(b.getResourceCache().getBridgeConfiguration().getIpAddress());
            preferences.setUsername(username);


            lightFragment.updateHueBridge(b);
            lightFragment.connectionComplete();

        }

        @Override
        // AUTHENTICATION WILL BE REQUIRED THROUGH A PUSH BUTTON
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
            Log.w(TAG, "Authentication Required.");
            phHueSDK.startPushlinkAuthentication(accessPoint);

            ConnectionDialog dialog = new ConnectionDialog(MainActivity.this, MainActivity.this, "auth");
            dialog.open();

        }

        @Override
        public void onConnectionResumed(PHBridge bridge) {
            if (MainActivity.this.isFinishing())
                return;

            Log.v(TAG, "onConnectionResumed" + bridge.getResourceCache().getBridgeConfiguration().getIpAddress());
            phHueSDK.getLastHeartbeat().put(bridge.getResourceCache().getBridgeConfiguration().getIpAddress(), System.currentTimeMillis());
            for (int i = 0; i < phHueSDK.getDisconnectedAccessPoint().size(); i++) {

                if (phHueSDK.getDisconnectedAccessPoint().get(i).getIpAddress().equals(bridge.getResourceCache().getBridgeConfiguration().getIpAddress())) {
                    phHueSDK.getDisconnectedAccessPoint().remove(i);
                }
            }
            lightFragment.updateHueBridge(bridge);

        }
        @Override
        public void onConnectionLost(PHAccessPoint accessPoint) {
            Log.v(TAG, "onConnectionLost : " + accessPoint.getIpAddress());
            if (!phHueSDK.getDisconnectedAccessPoint().contains(accessPoint)) {
                phHueSDK.getDisconnectedAccessPoint().add(accessPoint);
            }
        }
        @Override
        public void onError(int code, final String message) {
            Log.e(TAG, "on Error Called : " + code + ":" + message);

            if (code == PHHueError.NO_CONNECTION) {
                ConnectionDialog dia = new ConnectionDialog(MainActivity.this, MainActivity.this, message + ".\n Try again?");
                dia.open();
            }
            else if (code == PHHueError.AUTHENTICATION_FAILED || code== PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
                ConnectionDialog dia = new ConnectionDialog(MainActivity.this, MainActivity.this, message + ".\n Try again?");
                dia.open();
            }
            else if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
                Log.w(TAG, "Bridge Not Responding . ");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Bridge not responding..retrying", Toast.LENGTH_LONG).show();
                        doBridgeSearch();
                    }
                });
            }
            else if (code == PHMessageType.BRIDGE_NOT_FOUND) {
                if (!lastSearchWasIPScan) {  // Perform an IP Scan (backup mechanism) if UPNP and Portal Search fails.
                    phHueSDK = PHHueSDK.getInstance();
                    PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
                    sm.search(false, false, true);
                    lastSearchWasIPScan=true;
                }
                else {
                    PHWizardAlertDialog.getInstance().closeProgressDialog();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Context has to be set to class because otherwise it refers to weird token issue - I dont get it.
                            ConnectionDialog dia = new ConnectionDialog(MainActivity.this, MainActivity.this , message + ".\nTry again?");
                            dia.open();
                        }
                    });
                }


            }
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
            for (PHHueParsingError parsingError: parsingErrorsList) {
                Log.e(TAG, "ParsingError : " + parsingError.getMessage());
            }
        }
    };


    /*
    Back pressed-super method
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
    Options Menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
    NavBar eventhandler
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transactionFragment = getFragmentManager().beginTransaction();

        if (id == R.id.nav_light) {
            if(Build.VERSION.SDK_INT < 21) {
                transactionFragment.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
            }else{
                lightFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                lightFragment.setExitTransition(new Slide(Gravity.LEFT));
            }
            Bundle lightBundle = new Bundle();

            transactionFragment.replace(R.id.content_main, lightFragment).commit();




        } else if (id == R.id.nav_home) {
            // Sending data to home fragment fragment
            if(data != null && !homeFragment.isVisible()) {
                Bundle homeBundle = new Bundle();
                homeBundle.putDouble("TempData", data.temperature);
                homeBundle.putString("TempHum", data.humidity);
                homeBundle.putString("TempDir", data.windDirection);
                homeBundle.putSerializable("TempIcon", data.weatherIcon);

                homeFragment.setArguments(homeBundle);
            }

            // Transition & if API lvl is less than <21 it'll use a custm anim
            if(Build.VERSION.SDK_INT < 21) {
                transactionFragment.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
            }else{
                homeFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                homeFragment.setExitTransition(new Slide(Gravity.LEFT));
            }
            transactionFragment.replace(R.id.content_main, homeFragment).commit();

        } else if (id == R.id.nav_web) {
            fragmentManager.beginTransaction().replace(R.id.content_main, webFragment).commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /*
    *
    *        HOME FRAGMENT STUFF
    *      - Persistency for when switching fragments
    *
    *
    *
    *
     */


    @Override
    public void processFinished(String output) {
        // DL finished
        xml.getWeather(output);
    }

    @Override
    public void dataReceived(TemperatureData data) {
        // For when parsing is finished, set it to the ui.
        homeFragment.changeUIData(data);
        this.data = data;
    }

    @Override
    public void beginDownload(String xmlURL) {
        DownloadWeather task = new DownloadWeather();
        task.delegate = this;
        task.execute(xmlURL);
    }

}
