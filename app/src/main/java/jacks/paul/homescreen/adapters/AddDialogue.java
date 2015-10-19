package jacks.paul.homescreen.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.logging.SimpleFormatter;

import jacks.paul.homescreen.R;
import jacks.paul.homescreen.types.NoteData;

/**
 * Created by Paul on 15/10/2015.
 */


public class AddDialogue extends Dialog {

    Dialog addWindow;
    EditText subjectLine;
    EditText contextText;
    Button addButton;

    NoteData data;

    // Delegate/Interface
    public NoteInterface noteReceived = null;

    public AddDialogue(final Context context) {
        super(context);

        data = new NoteData();

        addWindow =  new Dialog(context);
        addWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addWindow.setContentView(R.layout.overlay_addnote);

        subjectLine = (EditText) addWindow.findViewById(R.id.overlay_add_subject);
        contextText = (EditText) addWindow.findViewById(R.id.overlay_add_text);
        addButton = (Button) addWindow.findViewById(R.id.overlay_add_button);

        // Canceable.
        addWindow.setCancelable(false);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteData data = new NoteData();
                data.text = contextText.getText().toString();
                data.importance = NoteData.Importance.Very;
                data.header = subjectLine.getText().toString();
                data.expiryDate = new Date();

                noteReceived.newNote(data);
                close();
            }
        });


    }

    public void show(){
        addWindow.show();
    }

    public void close(){
        addWindow.dismiss();
    }


}
