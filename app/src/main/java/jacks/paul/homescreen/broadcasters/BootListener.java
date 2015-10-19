package jacks.paul.homescreen.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import jacks.paul.homescreen.MainActivity;

/**
 * Created by Paul on 2015-10-19.
 */

// This is a WIP, basically autostart the app when the system is ready with boot
public class BootListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MainActivity.class();
        }

    }
}
