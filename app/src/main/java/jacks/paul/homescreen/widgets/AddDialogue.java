package jacks.paul.homescreen.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.logging.SimpleFormatter;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.NoteInterface;
import jacks.paul.homescreen.types.NoteData;

/**
 * Created by Paul on 15/10/2015.
 */


public class AddDialogue extends Dialog {

    Dialog addWindow;
    EditText subjectLine;
    EditText contextText;
    TextView importanceText;
    ImageButton addButton;
    ImageButton cancelButton;
    DatePicker datePicker;
    SeekBar seeker;

    NoteData data;

    // Delegate/Interface
    public NoteInterface noteReceived = null;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public AddDialogue(final Context context) {
        super(context);

        neouFat = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Thin.ttf");

        data = new NoteData();

        addWindow =  new Dialog(context);
        addWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addWindow.setContentView(R.layout.overlay_addnote);

        subjectLine = (EditText) addWindow.findViewById(R.id.overlay_add_subject);
        contextText = (EditText) addWindow.findViewById(R.id.overlay_add_text);
        importanceText = (TextView) addWindow.findViewById(R.id.overlay_text_importance);
        addButton = (ImageButton) addWindow.findViewById(R.id.overlay_add_button);
        cancelButton = (ImageButton)addWindow.findViewById(R.id.overlay_cancel_button);
        datePicker = (DatePicker)addWindow.findViewById(R.id.overlay_add_date);
        seeker = (SeekBar) addWindow.findViewById(R.id.overlay_add_importance);

        setFontStyles();

        if(Build.VERSION.SDK_INT > 20)
            seeker.setProgressDrawable(getContext().getDrawable(R.drawable.overlay_seeker_bar));
        else
            seeker.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        // Canceable.
        addWindow.setCancelable(false);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteData data = new NoteData();
                data.text = contextText.getText().toString();

                data.header = subjectLine.getText().toString();
                data.expiryDate = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                if (seeker.getProgress() < 30) {
                    data.importance = NoteData.Importance.Low;
                } else if (seeker.getProgress() > 30 && seeker.getProgress() < 60) {
                    data.importance = NoteData.Importance.Middle;
                } else if (seeker.getProgress() > 60) {
                    data.importance = NoteData.Importance.Very;
                }

                // Reset
                seeker.setProgress(0);
                subjectLine.setText("");
                contextText.setText("");

                noteReceived.newNote(data);
                close();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });


    }

    private void setFontStyles() {
        subjectLine.setTypeface(neouFat);
        contextText.setTypeface(neouFat);
        importanceText.setTypeface(neou

    }


    public void show(){
        addWindow.show();
    }

    public void close(){
        addWindow.dismiss();
    }


}
