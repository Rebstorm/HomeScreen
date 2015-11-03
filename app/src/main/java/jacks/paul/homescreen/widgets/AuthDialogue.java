package jacks.paul.homescreen.widgets;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;

import jacks.paul.homescreen.MainActivity;
import jacks.paul.homescreen.R;
import jacks.paul.homescreen.fragments.LightFragment;
import jacks.paul.homescreen.hue.HueConnected;

/**
 * Created by Paul on 02/11/2015.
 */
public class AuthDialogue implements HueConnected{

    Dialog authDialog;
    LightFragment lightFragment;

    public AuthDialogue(Context appContext, LightFragment lightFragment){
        this.lightFragment = lightFragment;
        this.lightFragment.buttonpressed = this;
        authDialog = new Dialog(appContext);
        authDialog.setContentView(R.layout.overlay_authscreen);

        authDialog.setCancelable(false);

    }

    public void open(){
        authDialog.show();
    }

    public void close(){
        authDialog.dismiss();
    }

    @Override
    public void buttonPressed(Boolean pressed) {
        if(pressed)
            authDialog.dismiss();
    }
}
