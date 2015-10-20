package jacks.paul.homescreen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import jacks.paul.homescreen.fragments.HomeFragment;
import jacks.paul.homescreen.fragments.MusicFragment;
import jacks.paul.homescreen.fragments.WebFragment;
import jacks.paul.homescreen.parsing.WeatherInterface;
import jacks.paul.homescreen.types.TemperatureData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //Fragments
    MusicFragment musicFragment;
    HomeFragment homeFragment;
    WebFragment webFragment;

    //Fragment Manager
    FragmentManager fragmentManager;


    //FABs
    FloatingActionButton fab;

    // NavDrawer
    DrawerLayout drawer;

    TemperatureData data = new TemperatureData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Toolbar
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */

        // Music Fragment
        musicFragment = new MusicFragment();
        homeFragment = new HomeFragment();
        webFragment = new WebFragment();

        //Fragment manager
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, homeFragment).commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        fab = (FloatingActionButton) findViewById(R.id.fabMenu);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.gray )));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener();
        //toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


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

        if (id == R.id.nav_music) {
            fragmentManager.beginTransaction().replace(R.id.content_main, musicFragment).commit();
        } else if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_main, homeFragment).commit();
        } else if (id == R.id.nav_web) {
            fragmentManager.beginTransaction().replace(R.id.content_main, webFragment).commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
