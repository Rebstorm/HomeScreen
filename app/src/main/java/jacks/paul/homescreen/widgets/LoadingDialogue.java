package jacks.paul.homescreen.widgets;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

import jacks.paul.homescreen.R;

/**
 * Created by Paul on 2015-10-10.
 */
public class LoadingDialogue {

    Dialog loadingDialogue;
    TextView loadingText;

    public LoadingDialogue(Context appContext){

        loadingDialogue = new Dialog(appContext);
        loadingDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialogue.setContentView(R.layout.overlay_loadingscreen);

    }

    // OPEN
    public void open(){
        loadingDialogue.show();
    }

    // CLOSE
    public void close(){
        loadingDialogue.dismiss();
    }

}
