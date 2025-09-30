package com.example.volnet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// MatchDatabaseHelper.java
public class MatchDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "matches.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MATCH = "matches";
    private static final String COLUMN_ID = "match_id";
    private static final String COLUMN_TEAM_A_NAME = "team_a_name";
    private static final String COLUMN_TEAM_A_LOGO = "team_a_logo";
    private static final String COLUMN_TEAM_B_NAME = "team_b_name";
    private static final String COLUMN_TEAM_B_LOGO = "team_b_logo";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_SCORE_A = "score_a";
    private static final String COLUMN_SCORE_B = "score_b";
    private static final String COLUMN_SET_NUMBER = "set_number";
    private static final String COLUMN_TIMEOUT_A = "timeout_a";
    private static final String COLUMN_TIMEOUT_B = "timeout_b";
    private static final String COLUMN_WINNER = "winner";

    public MatchDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MATCH_TABLE = "CREATE TABLE " + TABLE_MATCH + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEAM_A_NAME + " TEXT,"
                + COLUMN_TEAM_A_LOGO + " TEXT,"
                + COLUMN_TEAM_B_NAME + " TEXT,"
                + COLUMN_TEAM_B_LOGO + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_SCORE_A + " INTEGER,"
                + COLUMN_SCORE_B + " INTEGER,"
                + COLUMN_SET_NUMBER + " INTEGER,"
                + COLUMN_TIMEOUT_A + " INTEGER,"
                + COLUMN_TIMEOUT_B + " INTEGER,"
                + COLUMN_WINNER + " TEXT)";
        db.execSQL(CREATE_MATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
        onCreate(db);
    }

    public long createMatch(String teamAName, String teamALogo,
                            String teamBName, String teamBLogo,
                            String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_A_NAME, teamAName);
        values.put(COLUMN_TEAM_A_LOGO, teamALogo);
        values.put(COLUMN_TEAM_B_NAME, teamBName);
        values.put(COLUMN_TEAM_B_LOGO, teamBLogo);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_SCORE_A, 0);
        values.put(COLUMN_SCORE_B, 0);
        values.put(COLUMN_SET_NUMBER, 1);
        values.put(COLUMN_TIMEOUT_A, 2);
        values.put(COLUMN_TIMEOUT_B, 2);
        values.put(COLUMN_WINNER, "");
        long id = db.insert(TABLE_MATCH, null, values);
        db.close();
        return id;
    }

    public Cursor getMatchById(long matchId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MATCH + " WHERE " + COLUMN_ID + "=?",
                new String[]{String.valueOf(matchId)});
    }

    public void updateScore(long matchId, int scoreA, int scoreB, int setNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE_A, scoreA);
        values.put(COLUMN_SCORE_B, scoreB);
        values.put(COLUMN_SET_NUMBER, setNumber);
        db.update(TABLE_MATCH, values, COLUMN_ID + "=?", new String[]{String.valueOf(matchId)});
        db.close();
    }

    public void endMatch(long matchId, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WINNER, winner);
        db.update(TABLE_MATCH, values, COLUMN_ID + "=?", new String[]{String.valueOf(matchId)});
        db.close();
    }
}
