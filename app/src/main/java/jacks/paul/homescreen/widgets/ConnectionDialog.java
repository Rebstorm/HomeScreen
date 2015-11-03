package jacks.paul.homescreen.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import jacks.paul.homescreen.MainActivity;
import jacks.paul.homescreen.R;

/**
 * Created by Paul on 03/11/2015.
 */
public class ConnectionDialog {

    Dialog connDialog;
    Context context;
    MainActivity activity;

    Button yesButton;
    Button noButton;

    TextView messageView;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public ConnectionDialog(Context context, MainActivity activity, final String message){
        this.context = context;
        this.activity = activity;
        connDialog = new Dialog(context);
        connDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        connDialog.setContentView(R.layout.overlay_connectscreen);
        connDialog.setCancelable(false);

        neouFat = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Thin.ttf");

        yesButton = (Button)connDialog.findViewById(R.id.connect_overlay_yes_button);
        noButton = (Button)connDialog.findViewById(R.id.connect_overlay_no_button);

        messageView = (TextView)connDialog.findViewById(R.id.connect_overlay_text);

        if(message.equals("auth")){
            messageView.setText("Press the Authentication button!");
        }else{
            messageView.setText(message);
        }
        setFontStyles();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.equals("auth")){
                    connDialog.dismiss();
                }else{
                    doBridgeSearch();
                    connDialog.dismiss();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connDialog.dismiss();
            }
        });
    }

    public void doBridgeSearch() {
        activity.doBridgeSearch();
    }

    public void open(){
        connDialog.show();
    }

    public void close(){
        connDialog.dismiss();
    }

    private void setFontStyles() {
        messageView.setTypeface(neouFat);
    }
}
