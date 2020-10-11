package com.example.notesapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notesapp.Classes.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String TABLE_NOTES = "NOTES_TABLE";
    public final static String COLUMN_NOTE_ID = "NOTE_ID";
    public final static String COLUMN_NOTE_CONTENT = "NOTE_CONTENT";
    public final static String COLUMN_NOTE_DATE = "NOTE_DATE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE "+ TABLE_NOTES +" ( "+ COLUMN_NOTE_ID +" INTEGER PRIMARY KEY, "+ COLUMN_NOTE_CONTENT +" TEXT, "+ COLUMN_NOTE_DATE +" TEXT )";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //get All notes
    public List<Note> getAllNotes() {

        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM "+ TABLE_NOTES;

        Cursor cursor = db.rawQuery(getQuery, null);
        List<Note> notes_list = new ArrayList<Note>(0);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE));

                Note note = new Note(id, content, date);
                notes_list.add(0, note);

            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return notes_list;
    }

    //Add one note
    public boolean addOneNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE_ID, String.valueOf(note.getId()));
        cv.put(COLUMN_NOTE_CONTENT, note.getContent());
        cv.put(COLUMN_NOTE_DATE, note.getDate());

        long result = db.insert(TABLE_NOTES, null, cv);

        db.close();
        return result != -1;
    }

    //update one note
    public boolean updateNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_CONTENT, note.getContent());
        cv.put(COLUMN_NOTE_DATE, note.getDate());

        String whereClause = COLUMN_NOTE_ID+ "=" +note.getId();
        long result = db.update(TABLE_NOTES, cv, whereClause, null);

        if(result >= 1) {
            db.close();
            return true;
        } else {
            return false;
        }
    }

    //Delete one note
    public boolean deleteOneNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_NOTE_ID+ "=" +note.getId();
        long result = db.delete(TABLE_NOTES, whereClause, null);

        if(result >= 1) {
            db.close();
            return true;
        } else {
            return false;
        }

    }
}
