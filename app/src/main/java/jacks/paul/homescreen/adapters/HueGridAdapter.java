package jacks.paul.homescreen.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.hue.HueInterface;
import jacks.paul.homescreen.types.HueData;

/**
 * Created by Paul on 2015-11-02.
 */
public class HueGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<HueData> themeList;

    public HueInterface hueInterface = null;


    // Typeface/Font
    Typeface neouFat;
    Typeface neou;


    public HueGridAdapter(Context C,  ArrayList<HueData> theme){
        context = C;
        themeList = theme;


        neouFat = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Thin.ttf");
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){

        convertView = inflater.inflate(R.layout.hue_grid_items, null);

        TextView hueTitle = (TextView)convertView.findViewById(R.id.hue_grid_text);
        hueTitle.setTypeface(neouFat);
            hueTitle.setTextColor(Color.WHITE);

        final ImageView hueGridImage = (ImageView)convertView.findViewById(R.id.hue_grid_icon);

        hueTitle.setText(themeList.get(position).themeName);
        hueGridImage.setImageResource(themeList.get(position).icon);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hueInterface.themeSelected(themeList.get(position));
                    hueGridImage.animate().alpha(0).setDuration(500).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            hueGridImage.animate().alpha(1).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();

                }
            });


        }



        return convertView;
    }


    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateList(ArrayList<HueData> data){
        this.themeList = data;
        notifyDataSetChanged();
        notifyDataSetInvalidated();

    }


}
