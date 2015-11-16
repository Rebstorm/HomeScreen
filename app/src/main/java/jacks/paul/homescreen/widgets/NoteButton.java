package jacks.paul.homescreen.widgets;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.adapters.NoteInterface;
import jacks.paul.homescreen.types.NoteData;

/**
 * Created by Paul on 12/10/2015.
 * ITS NOT A DEFAULT FILE TEMPLATE NO MORE, GOD MOTHER F*CKING DAMN IT.
 */
public class NoteButton extends RelativeLayout{

    private Paint colorUrgency;
    private int height = 400;
    private int width = 300;
    public NoteData data;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public NoteInterface buttonListener;

    Context context;
    Button noteButton;
    Button exitButton;
    TextView subText;

    public NoteButton(Context context, NoteData data) {
        super(context);

        this.data = data;
        this.context = context;
        noteButton = new Button(context);
        exitButton = new Button(context);
        subText = new Button(context);

        neouFat = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Thin.ttf");

        // Setup everything
        setupButton(data);
    }

    private void setupButton(final NoteData data){

        // Setting the design.
        RelativeLayout.LayoutParams buttonDesign = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonDesign.setMargins(10,0,10,0);

        // Height and Width
        buttonDesign.height = height;
        buttonDesign.width = width;

        // Setting the params
        noteButton.setLayoutParams(buttonDesign);

        // header text
        if(data.header != null)
            noteButton.setText(data.header);
        noteButton.setTextColor(Color.WHITE);
        noteButton.setTypeface(neouFat);
        noteButton.setTextSize(24);


        // Set imporance of note
        switch (data.importance){
            case Very:
                noteButton.setBackgroundResource(R.drawable.note_important);
                break;
            case Middle:
                noteButton.setBackgroundResource(R.drawable.note_middle);
                break;
            case Low:
                noteButton.setBackgroundResource(R.drawable.note_low);
                break;
        }

        noteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, data.text, Toast.LENGTH_LONG).show();
            }
        });


        RelativeLayout.LayoutParams subTextDesign = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        // Add below notebutton
        subTextDesign.width = width;
        subTextDesign.height = 250;
        subTextDesign.setMargins(0,200,0,0);

        if(data.text != null)
            subText.setText(data.text);

        // Layout
        subText.setPadding(15,0,15,0);
        subText.setBackgroundColor(Color.TRANSPARENT);
        subText.setGravity(Gravity.TOP | Gravity.CENTER);
        subText.setTextColor(Color.GRAY);
        subText.setTypeface(neouFat);
        subText.setTextSize(18);
        subText.setLayoutParams(subTextDesign);


        exitButton.setBackgroundResource(R.drawable.note_exit);

        RelativeLayout.LayoutParams exitButtonDesign = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        exitButtonDesign.setMargins(250,10,0,0);
        exitButtonDesign.width = 60;
        exitButtonDesign.height = 60;
        exitButton.setLayoutParams(exitButtonDesign);


        exitButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    exitButton.setBackgroundResource(R.drawable.note_exit_inverted);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    exitButton.setBackgroundResource(R.drawable.note_exit);
                    Toast.makeText(context, "Removed: " + data.header, Toast.LENGTH_LONG).show();
                    buttonListener.removeNote(data);
                }

                return false;
            }

        });

        this.addView(noteButton);
        this.addView(subText);
        this.addView(exitButton);
    }




}
