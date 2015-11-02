package jacks.paul.homescreen.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.hue.HueInterface;
import jacks.paul.homescreen.types.HueData;

public class HueBulbListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    List<PHAccessPoint> data;

    Context context;

    TextView hueIP;
    TextView hueMac;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public HueInterface conHueInterface = null;

    public HueBulbListAdapter(Context context, List<PHAccessPoint> accessPointList) {
        mInflater = LayoutInflater.from(context);

        this.data = accessPointList;
        this.context = context;

        neouFat = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Bold.ttf");
        neou = Typeface.createFromAsset(context.getAssets(), "fonts/Neou-Thin.ttf");
    }

    void setFont(){

        hueIP.setTypeface(neouFat);
        hueIP.setTextColor(Color.WHITE);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.hue_list_item, parent, false);

            hueMac = (TextView) convertView.findViewById(R.id.hue_mac);
            hueIP = (TextView) convertView.findViewById(R.id.hue_ip);
            ImageView hueIcon = (ImageView) convertView.findViewById(R.id.hue_icon);
        }

        final PHAccessPoint cData = data.get(position);

        hueIP.setText("IP: " + cData.getIpAddress() + " Username:" + cData.getUsername());
        hueMac.setText("MAC-Address: " + cData.getMacAddress() + " Hue ID: " + cData.getBridgeId());

        hueIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Connecting to Hue", Toast.LENGTH_LONG).show();
                conHueInterface.ConnectAP(cData);

            }
        });

        // Hue Icon color
        //hueIcon.setBackgroundColor(data.get(position).color);

        setFont();

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public  void updateData(List<PHAccessPoint> accesspoints){
        this.data = accesspoints;
        notifyDataSetChanged();

    }
} 