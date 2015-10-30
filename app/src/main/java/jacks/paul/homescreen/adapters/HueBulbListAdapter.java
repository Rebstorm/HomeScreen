package jacks.paul.homescreen.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.types.HueData;

public class HueBulbListAdapter extends ArrayAdapter<HueData> {
    private final Context context;
    ArrayList<HueData> data;

    TextView hueText;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public HueBulbListAdapter(Context context, ArrayList<HueData> data) {
        super(context, R.layout.hue_list_item, data);
        this.context = context;
        this.data = data;


        neouFat = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Thin.ttf");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.hue_list_item, parent, false);
        hueText = (TextView) rowView.findViewById(R.id.hue_text);
        ImageView hueIcon = (ImageView) rowView.findViewById(R.id.hue_icon);
        hueText.setText(data.get(position).name);

        hueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, data.get(position).name, Toast.LENGTH_LONG).show();
            }
        });

        // Hue Icon color
        hueIcon.setBackgroundColor(data.get(position).color);

        setFont();

        return rowView;
    }


    void setFont(){

        hueText.setTypeface(neouFat);
        hueText.setTextColor(Color.WHITE);

    }
} 