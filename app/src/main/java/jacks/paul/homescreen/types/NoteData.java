package jacks.paul.homescreen.types;

import java.util.Date;

/**
 * Created by Paul on 12/10/2015.
 */
public class NoteData {

    public enum Importance {
        Very,
        Middle,
        Low
    }

    public int id = 0;
    public String header = "";
    public String text = "";
    public Importance importance = Importance.Low;



}
