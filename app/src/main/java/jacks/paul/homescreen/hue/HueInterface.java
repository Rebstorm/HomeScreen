package jacks.paul.homescreen.hue;

import com.philips.lighting.hue.sdk.PHAccessPoint;

import jacks.paul.homescreen.types.HueData;

/**
 * Created by Paul on 02/11/2015.
 */
public interface HueInterface {
        void connectAP(PHAccessPoint accessPoint);

        void themeSelected(HueData theme);

}
