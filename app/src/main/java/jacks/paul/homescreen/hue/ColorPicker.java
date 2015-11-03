package jacks.paul.homescreen.hue;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.types.HueData;

/**
 * Created by Paul on 03/11/2015.
 */
public class ColorPicker {

    public ArrayList<HueData> allItems = new ArrayList<>();

    public ColorPicker(){


    }

    public ArrayList<HueData> getAllThemes(){
        //Hot
        HueData themeHot = new HueData();
        themeHot.themeName = "Warm";
        themeHot.icon = R.drawable.theme_warm;
        themeHot.colors.add(65280);
        themeHot.colors.add(65280);
        themeHot.colors.add(65280);

        allItems.add(themeHot);

        //Cold
        HueData themeCold = new HueData();
        themeCold.themeName = "Cold";
        themeCold.icon = R.drawable.theme_cold;
        themeCold.colors.add(45600);
        themeCold.colors.add(45600);
        themeCold.colors.add(45600);

        allItems.add(themeCold);

        // Green
        HueData themeGreen = new HueData();
        themeGreen.themeName = "Yellow";
        themeGreen.icon = R.drawable.dunno;
        themeGreen.colors.add(8000);
        themeGreen.colors.add(8000);
        themeGreen.colors.add(8000);

        allItems.add(themeGreen);

        return allItems;

    }




    public int[] rgbColorToInt(Byte[] byteArray)
    {
        int[] iarray = new int[byteArray.length];
        int i = 0;
        for (Byte b : byteArray)
            iarray[i++] = b & 0xff;
        return iarray;
    }



}
