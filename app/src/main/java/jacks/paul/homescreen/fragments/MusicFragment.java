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
                    Connectivity connectivity = getNetworkConnectivity(getActivity());
                    logStatus("Network state changed: " + connectivity.toString());
                    mPlayer.setConnectivityStatus(connectivity);
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


    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    private void logStatus(String status) {
        Log.i("SpotifySdkDemo", status);
        if (!TextUtils.isEmpty(mStatusText.getText())) {
            mStatusText.append("\n");
        }
        mStatusText.append(status);
        mStatusTextScrollView.post(new Runnable() {
            @Override
            public void run() {
                // Scroll to the bottom
                mStatusTextScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public Intent registerReceiver(
            BroadcastReceiver receiver, IntentFilter filter) {
        return getActivity().registerReceiver(receiver, filter);
    }

    private void updateButtons() {
        boolean loggedIn = isLoggedIn();

        // Login button should be the inverse of the logged in state
        Button loginButton = (Button) getActivity().findViewById(R.id.spotifyLoginButton);
        loginButton.setText(loggedIn ? "Log in" : "Logged in");

    }



    private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    onAuthenticationComplete(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    logStatus("Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    logStatus("Auth result: " + response.getType());
            }
        }
    }

    private void onAuthenticationComplete(AuthenticationResponse authResponse) {
        // Once we have obtained an authorization token, we can proceed with creating a Player.
        logStatus("Got authentication token");
        if (mPlayer == null) {
            Config playerConfig = new Config(getActivity(), authResponse.getAccessToken(), CLIENT_ID);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.
            mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                @Override
                public void onInitialized(Player player) {
                    logStatus("-- Player initialized --");
                    mPlayer.setConnectivityStatus(getNetworkConnectivity(getActivity()));
                    mPlayer.addPlayerNotificationCallback(MusicFragment.this);
                    mPlayer.addConnectionStateCallback(MusicFragment.this);
                    // Trigger UI refresh
                    updateButtons();
                }

                @Override
                public void onError(Throwable error) {
                    logStatus("Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(authResponse.getAccessToken());
        }
    }

    public void onShowPlayerStateButtonClicked(View view) {
        mPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                logStatus("-- Current player state --");
                logStatus("Playing? " + playerState.playing);
                logStatus("Position: " + playerState.positionInMs + "ms");
                logStatus("Shuffling? " + playerState.shuffling);
                logStatus("Repeating? " + playerState.repeating);
                logStatus("Active device? " + playerState.activeDevice);
                logStatus("Track uri: " + playerState.trackUri);
                logStatus("Track duration: " + playerState.durationInMs);
            }
        });
    }

    public void onPlayButtonClicked(View view) {
        if (view.getId() == R.id.play_album_button) {
            logStatus("Starting playback the list of tracks");
            mPlayer.play(TEST_ALBUM_TRACKS);
        } else {
            String uri;
            switch (view.getId()) {
                case R.id.play_track_button:
                    uri = TEST_SONG_URI;
                    break;
                case R.id.play_mono_track_button:
                    uri = TEST_SONG_MONO_URI;
                    break;
                case R.id.play_48khz_track_button:
                    uri = TEST_SONG_48kHz_URI;
                    break;
                case R.id.play_playlist_button:
                    uri = TEST_PLAYLIST_URI;
                    break;
                default:
                    throw new IllegalArgumentException("View ID does not have an associated URI to play");
            }

            logStatus("Starting playback for " + uri);
            mPlayer.play(uri);
        }
    }

    public void onPauseButtonClicked(View view) {
        if (mCurrentPlayerState.playing) {
            mPlayer.pause();
        } else {
            mPlayer.resume();
        }
    }

    public void onSkipToPreviousButtonClicked(View view) {
        mPlayer.skipToPrevious();
    }

    public void onSkipToNextButtonClicked(View view) {
        mPlayer.skipToNext();
    }

    public void onQueueSongButtonClicked(View view) {
        mPlayer.queue(TEST_QUEUE_SONG_URI);
        Toast toast = Toast.makeText(getActivity(), "Queue button clicked", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onToggleShuffleButtonClicked(View view) {
        mPlayer.setShuffle(!mCurrentPlayerState.shuffling);
    }

    public void onToggleRepeatButtonClicked(View view) {
        mPlayer.setRepeat(!mCurrentPlayerState.repeating);
    }

    public void onSeekButtonClicked(View view) {
        // Skip to 10 seconds in the current song
        mPlayer.seekToPosition(10000);
    }

    public void onLowBitrateButtonPressed(View view) {
        mPlayer.setPlaybackBitrate(PlaybackBitrate.BITRATE_LOW);
    }

    public void onNormalBitrateButtonPressed(View view) {
        mPlayer.setPlaybackBitrate(PlaybackBitrate.BITRATE_NORMAL);
    }

    public void onHighBitrateButtonPressed(View view) {
        mPlayer.setPlaybackBitrate(PlaybackBitrate.BITRATE_HIGH);
    }

    private boolean isLoggedIn() {
        return mPlayer != null && mPlayer.isLoggedIn();
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
