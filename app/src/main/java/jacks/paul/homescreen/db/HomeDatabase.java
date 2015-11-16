package jacks.paul.homescreen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import jacks.paul.homescreen.types.NoteData;
import jacks.paul.homescreen.types.TemperatureData;

/**
 * Created by Paul on 19/10/2015.
 */
public class HomeDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "HomeDatabase";
    // Database Table
    private  static final String NOTE_TABLE = "notes";
    private static final String WEATHER_TABLE = "weather";

    // Vars Notes
    private static final String ID = "id";
    private static final String HEADER = "header";
    private static final String TEXT = "text";
    private static final String DATE = "date";
    private static final String IMPORTANCE = "importance";

    // DB
    SQLiteDatabase db;

    public HomeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // Gets the DB in the Constructor
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_NOTES = "CREATE TABLE " + NOTE_TABLE +
                "(" + ID + " INTEGER PRIMARY KEY," + HEADER + " TEXT," +
                TEXT + " TEXT," + DATE + " TEXT," + IMPORTANCE + " TEXT)";

        db.execSQL(CREATE_TABLE_NOTES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
        onCreate(db);
    }

    public void addNote(NoteData data){
        // Puts everything into a value var
        ContentValues contentValues = new ContentValues();
        contentValues.put(HEADER, data.header);
        contentValues.put(TEXT, data.text);
        contentValues.put(DATE, data.expiryDate.toString());

        // Convert enum to int
        if(data.importance == NoteData.Importance.Very)
            contentValues.put(IMPORTANCE, 1);
        else if(data.importance == NoteData.Importance.Middle)
            contentValues.put(IMPORTANCE, 2);
        else if(data.importance == NoteData.Importance.Low)
            contentValues.put(IMPORTANCE, 3);

        // Adds the stuff into the data values
        db.insert(NOTE_TABLE, null, contentValues);

    }

    // Pulls data according to id
    public NoteData getNote(int id){

        Cursor cursor = db.query(NOTE_TABLE,
            new String[] {
                ID, HEADER, TEXT, DATE, IMPORTANCE
            },
            ID + "=?",new String[]{
                String.valueOf(id),
            }, null, null, null, null);

        if(cursor!= null)
            cursor.moveToFirst();

        NoteData data = new NoteData();
        data.header = cursor.getString(1);
        data.text = cursor.getString(2);

        if(cursor.getString(4).equals("1"))
            data.importance = NoteData.Importance.Very;
        else if(cursor.getString(4).equals("2"))
            data.importance = NoteData.Importance.Middle;
        else if(cursor.getString(4).equals("3"))
            data.importance = NoteData.Importance.Low;

        return data;
    }

    // Like, get all of them DB Entries!
    public List<NoteData> getAllNotes(){
        List<NoteData> allNotes = new ArrayList<NoteData>();

        String selectQuery = "SELECT * FROM " + NOTE_TABLE;
        Cursor cQuestion =  db.rawQuery(selectQuery, null);

        if(cQuestion.moveToFirst()){

            do {
                NoteData data = new NoteData();
                data.id = Integer.parseInt(cQuestion.getString(0));
                data.header = cQuestion.getString(1);
                data.text = cQuestion.getString(2);

                if (cQuestion.getString(4).equals("1"))
                    data.importance = NoteData.Importance.Very;
                else if(cQuestion.getString(4).equals("2"))
                    data.importance = NoteData.Importance.Middle;
                else if(cQuestion.getString(4).equals("3"))
                    data.importance = NoteData.Importance.Low;

                allNotes.add(data);
            }while(cQuestion.moveToNext());

        }
        return  allNotes;
    }

    // Remove
    public void removeNote(NoteData remove){
        db.delete(NOTE_TABLE, ID + "=?", new String[]{String.valueOf(remove.id)});
    }



}
