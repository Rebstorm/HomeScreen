package jacks.paul.homescreen.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.types.NoteData;

/**
 * Created by Paul on 12/10/2015.
 */
public class NoteButton extends Button {

    private Paint colorUrgency;
    private int height = 400;
    private int width = 200;
    public NoteData data;


    Context context;

    public NoteButton(Context context, NoteData data) {
        super(context);

        this.data = data;
        this.context = context;

        // Setup everything
        setupButton(data);
    }

    private void setupButton(final NoteData data){


        // Setting the design.
        LinearLayout.LayoutParams buttonDesign = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonDesign.setMargins(10, 0, 10, 0);

        // Height and Width
        buttonDesign.height = height;
        buttonDesign.width = width;

        // Setting the params
        this.setLayoutParams(buttonDesign);

        // header text
        this.setText(data.header);
        this.setTextColor(Color.WHITE);

        // Set imporance of note
        switch (data.importance){
            case Very:
                this.setBackgroundResource(R.drawable.note_important);
                break;
            case Middle:
                this.setBackgroundResource(R.drawable.note_middle);
                break;
            case Low:
                this.setBackgroundResource(R.drawable.note_low);
                break;
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked button" + data.header, Toast.LENGTH_LONG).show();
            }
        });

    }




}
