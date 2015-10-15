package jacks.paul.homescreen.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.types.NoteData;

/**
 * Created by Paul on 15/10/2015.
 */



public class AddDialogue extends Dialog {

    Dialog addWindow;

    public AddDialogue(Context context) {
        super(context);

        addWindow =  new Dialog(context);
        addWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addWindow.setContentView(R.layout.overlay_addnote);


    }

    public void show(){
        addWindow.show();
    }

    public void close(){
        addWindow.dismiss();
    }


}
