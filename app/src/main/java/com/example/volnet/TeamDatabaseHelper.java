package com.example.volnet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// TeamDatabaseHelper.java
public class TeamDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "teams.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TEAM = "teams";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "team_name";
    private static final String COLUMN_LOGO = "team_logo";

    public TeamDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_LOGO + " TEXT)";
        db.execSQL(CREATE_TEAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        onCreate(db);
    }

    public void addTeam(String teamName, String teamLogo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, teamName);
        values.put(COLUMN_LOGO, teamLogo);
        db.insert(TABLE_TEAM, null, values);
        db.close();
    }

    public List<Team> getAllTeams() {
        List<Team> teamList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAM, null);
        if (cursor.moveToFirst()) {
            do {
                teamList.add(new Team(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teamList;
    }
}
