package jacks.paul.homescreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public HueGridAdapter(Context C,  ArrayList<HueData> theme){
        context = C;
        themeList = theme;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){

        convertView = inflater.inflate(R.layout.hue_grid_items, null);


        TextView hueTitle = (TextView)convertView.findViewById(R.id.hue_grid_text);
        ImageView hueGridImage = (ImageView)convertView.findViewById(R.id.hue_grid_icon);

        hueTitle.setText(themeList.get(position).themeName);
        hueGridImage.setImageResource(themeList.get(position).icon);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, themeList.get(position).themeName + " will be set.", Toast.LENGTH_LONG).show();
                    hueInterface.themeSelected(themeList.get(position));
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
