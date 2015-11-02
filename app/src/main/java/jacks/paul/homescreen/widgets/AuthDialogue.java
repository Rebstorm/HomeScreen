package jacks.paul.homescreen.widgets;

import android.app.Dialog;
import android.content.Context;

import jacks.paul.homescreen.R;

/**
 * Created by Paul on 02/11/2015.
 */
public class AuthDialogue{

    Dialog authDialog;

    public AuthDialogue(Context appContext){

        authDialog.setContentView(R.layout.overlay_authscreen);
        authDialog = new Dialog(appContext);

    }

    public void open(){
        authDialog.show();
    }

    public void close(){
        authDialog.dismiss();
    }
}
