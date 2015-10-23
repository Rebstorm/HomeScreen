package jacks.paul.homescreen.adapters;

import jacks.paul.homescreen.types.NoteData;

/**
 * Created by Paul on 19/10/2015.
 */
public interface NoteInterface {

    void newNote(NoteData data);

    void removeNote(NoteData data);

}
