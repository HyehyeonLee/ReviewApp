package com.example.moviereview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ReviewDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public ReviewDBHelper(Context context) {
        super(context, "moviedb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE moviedb "+
                "(_id integer primary key autoincrement,"+
                "title text, date date, rate real, memo text, image text)";
        db.execSQL(sql);

        //insert sample data
//        db.execSQL("INSERT INTO moviedb (title, date, rate, memo) VALUES ('라라랜드', '2020. 3. 25', 3.5, '코엑스 재상영')");
        //db.execSQL("INSERT INTO moviedb (title, date, rate, memo) VALUES ('토이스토리4', '2019. 7. 18', 5.0, '역곡 CGV')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("DROP TABLE moviedb");
            onCreate(db);
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
