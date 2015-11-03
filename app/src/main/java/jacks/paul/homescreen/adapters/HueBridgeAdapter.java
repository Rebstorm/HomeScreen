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
import jacks.paul.homescreen.types.HueConnectionData;
import jacks.paul.homescreen.types.HueData;
import jacks.paul.homescreen.widgets.ConnectionDialog;

public class HueBridgeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    List<HueConnectionData> data;

    Context context;

    TextView hueIP;
    TextView hueMac;
    ImageView hueIcon;

    // Typeface/Font
    Typeface neouFat;
    Typeface neou;

    public HueInterface conHueInterface = null;

    public HueBridgeAdapter(Context context, List<HueConnectionData> accessPointList) {
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
            hueIcon = (ImageView) convertView.findViewById(R.id.hue_icon);
        }

        final PHAccessPoint cData = data.get(position).accessPoint;

        hueIP.setText("Click to pair");
        hueMac.setText("IP: " + cData.getIpAddress() + " Username:" + cData.getUsername());

        hueIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conHueInterface.connectAP(cData);
            }
        });

        // Hue Icon color
        hueIcon.setBackgroundResource(R.drawable.pushlink_image);

        setFont();

        return convertView;
    }

    @Override
    public int getCount() {
        if(data!=null)
            return data.size();
        else
            return 0;
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
        ArrayList<HueConnectionData> convertedList = new ArrayList<>();
        for(int i = 0; i < accesspoints.size(); i++){
            HueConnectionData data = new HueConnectionData();
            data.name = "Name";
            data.accessPoint = accesspoints.get(i);
            convertedList.add(data);
        }
        this.data = convertedList;

        notifyDataSetChanged();
        notifyDataSetInvalidated();

    }
} 