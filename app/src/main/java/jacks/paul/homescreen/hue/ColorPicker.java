package jacks.paul.homescreen.hue;

import java.util.ArrayList;

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
        HueData themeYellow = new HueData();
        themeYellow.themeName = "Yellow";
        themeYellow.icon = R.drawable.dunno;
        themeYellow.colors.add(8000);
        themeYellow.colors.add(8000);
        themeYellow.colors.add(8000);

        allItems.add(themeYellow);

        HueData testcolor1 = new HueData();
        testcolor1.themeName = "test1";
        testcolor1.icon = R.drawable.dunno;
        testcolor1.colors.add(1000);
        testcolor1.colors.add(1000);
        testcolor1.colors.add(1000);

        allItems.add(testcolor1);


        HueData testcolor2 = new HueData();
        testcolor2.themeName = "test2";
        testcolor2.icon = R.drawable.dunno;
        testcolor2.colors.add(2000);
        testcolor2.colors.add(2000);
        testcolor2.colors.add(2000);

        allItems.add(testcolor2);


        HueData testcolor3 = new HueData();
        testcolor3.themeName = "test3";
        testcolor3.icon = R.drawable.dunno;
        testcolor3.colors.add(3000);
        testcolor3.colors.add(3000);
        testcolor3.colors.add(3000);

        allItems.add(testcolor3);


        HueData testcolor4 = new HueData();
        testcolor4.themeName = "test4";
        testcolor4.icon = R.drawable.dunno;
        testcolor4.colors.add(4000);
        testcolor4.colors.add(4000);
        testcolor4.colors.add(4000);

        allItems.add(testcolor4);


        HueData testcolor5 = new HueData();
        testcolor5.themeName = "test5";
        testcolor5.icon = R.drawable.dunno;
        testcolor5.colors.add(5000);
        testcolor5.colors.add(5000);
        testcolor5.colors.add(5000);

        allItems.add(testcolor5);


        HueData testcolor6 = new HueData();
        testcolor6.themeName = "test6";
        testcolor6.icon = R.drawable.dunno;
        testcolor6.colors.add(6000);
        testcolor6.colors.add(6000);
        testcolor6.colors.add(6000);

        allItems.add(testcolor6);


        HueData testcolor7 = new HueData();
        testcolor7.themeName = "test7";
        testcolor7.icon = R.drawable.dunno;
        testcolor7.colors.add(7000);
        testcolor7.colors.add(7000);
        testcolor7.colors.add(7000);

        allItems.add(testcolor7);


        HueData testcolor8 = new HueData();
        testcolor8.themeName = "test8";
        testcolor8.icon = R.drawable.dunno;
        testcolor8.colors.add(8000);
        testcolor8.colors.add(8000);
        testcolor8.colors.add(8000);

        allItems.add(testcolor8);




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
