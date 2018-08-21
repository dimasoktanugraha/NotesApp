package com.artera.dimasdicoding25mynotesappsqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.artera.dimasdicoding25mynotesappsqlite.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.TITLE;
import static com.artera.dimasdicoding25mynotesappsqlite.db.DatabaseContract.NoteColumns.DATE;
import static com.artera.dimasdicoding25mynotesappsqlite.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.artera.dimasdicoding25mynotesappsqlite.db.DatabaseContract.TABLE_NOTE;

public class NoteHelper {

    private static String DATABASE_TABLE = TABLE_NOTE;
    private Context context;
    private DataBaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLException{
        dataBaseHelper = new DataBaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public ArrayList<Note> query() {
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = database.query(DATABASE_TABLE, null,null,null,null,null,
                _ID +" DESC", null);
        cursor.moveToFirst();

        Note note;

        if (cursor.getCount()>0){
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert (Note note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, note.getTitle());
        initialValues.put(DESCRIPTION, note.getDescription());
        initialValues.put(DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(Note note) {
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());
        return database.update(DATABASE_TABLE, args, _ID +"= '" + note.getId() + "'",null);
    }

    public int delete(int id) {
        return database.delete(TABLE_NOTE, _ID +" = '"+ id+ "'", null);
    }
}
