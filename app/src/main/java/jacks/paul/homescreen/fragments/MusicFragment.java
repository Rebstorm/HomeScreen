package jacks.paul.homescreen.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.PlaybackBitrate;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;

import java.util.Arrays;
import java.util.List;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.WebStayView;

/**
 * Music Fragment, details from Spotify:
 *
 * https://developer.spotify.com/my-applications/#!/applications/c30927946e87452e8101595852c75588
 *
 * Im putting this on hiatus until I have the time and bother to figure out the authentication stuff in Spotify.
 *
 */
public class MusicFragment extends Fragment implements PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "c30927946e87452e8101595852c75588";
    private static final String REDIRECT_URI = "testschema://callback";

    // Request code that will be passed together with authentication result to the onAuthenticationResult
    private static final int REQUEST_CODE = 1337;

    /*
        Test Song
     */
    private static final String TEST_SONG_URI = "spotify:track:6KywfgRqvgvfJc3JRwaZdZ";
    private static final String TEST_SONG_MONO_URI = "spotify:track:1FqY3uJypma5wkYw66QOUi";
    private static final String TEST_SONG_48kHz_URI = "spotify:track:3wxTNS3aqb9RbBLZgJdZgH";
    private static final String TEST_PLAYLIST_URI = "spotify:user:sqook:playlist:0BZvnsfuqmnLyj6WVRuSte";
    private static final String TEST_QUEUE_SONG_URI = "spotify:track:5EEOjaJyWvfMglmEwf9bG3";

    // Test Album
    private static final List<String> TEST_ALBUM_TRACKS = Arrays.asList(
            "spotify:track:2To3PTOTGJUtRsK3nQemP4",
            "spotify:track:0tDoBMgyAzGgLhs73KPrJL",
            "spotify:track:5YkSQuB8i7J4TTyj0xw6ol",
            "spotify:track:3WpLfCkrlQxj8SISLzhs06",
            "spotify:track:2lGNTC3NKCG1d4lR8x3611",
            "spotify:track:0kdSj5REwpHjTBaBsm1wv8",
            "spotify:track:3BgnZiGnnRlXfeGR8ryKzT",
            "spotify:track:00cVWQIFyQnIdsgoVy7qAG",
            "spotify:track:6eEEoowHpnaD3q83ZhYmhZ",
            "spotify:track:1HFBn8S30ndZ7lLb9HbENU",
            "spotify:track:1I9VibKgJTqGfrh8fEK3sL",
            "spotify:track:6rXSPMgGIyOYiMhsj3eSAi",
            "spotify:track:2xwuXthwdNGbPyEqifPQNW",
            "spotify:track:5vRuWI48vKn4TV7efrYtJL",
            "spotify:track:4SEDYSBDd4Ota125LjHa2w",
            "spotify:track:2bVTnSTjLWAizyj4XcU5bf",
            "spotify:track:4gQzqlFuqv6l4Ka633Ue7T",
            "spotify:track:0SLVmM7IrrtkPNa1Fi3IKT"
    );


    // Player
    private Player mPlayer;

    // State of Player
    private PlayerState mCurrentPlayerState = new PlayerState();

    // Broadcast Receiver
    private BroadcastReceiver mNetworkStateReceiver;

    // Logging text message
    private TextView mStatusText;

    // ScrollView for bottom scrolling text
    private ScrollView mStatusTextScrollView;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onResume(){
        super.onResume();

        // Set up the broadcast receiver for network events. Note that we also unregister
        // this receiver again in onPause().
        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mPlayer != null) {

                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //registerReceiver(mNetworkStateReceiver, filter);

        if (mPlayer != null) {
            mPlayer.addPlayerNotificationCallback(MusicFragment.this);
            mPlayer.addConnectionStateCallback(MusicFragment.this);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music, container, false);

        // Get a reference to any UI widgets that we'll need to use later
        mStatusText = (TextView) v.findViewById(R.id.spotifyText);
        mStatusTextScrollView = (ScrollView) v.findViewById(R.id.spotifyScroll);

        return v;

    }


    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }
}
