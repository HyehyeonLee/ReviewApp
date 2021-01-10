package com.example.moviereview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    private ReviewDAO(Context ctx){
        dbHelper = new ReviewDBHelper(ctx);
    }

    public static ReviewDAO open(Context ctx){return new ReviewDAO(ctx);}

    //DB에 객체 정보 하나 저장
    public void addReview(ReviewVO m){
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("title", m.getTitle());
        v.put("date", m.getDate());
        v.put("rate", m.getRating());
        v.put("memo", m.getMemo());
        v.put("image", m.getImage());

        db.insert("moviedb", null, v);
    }

    //DB에 저장된 모든 객체 불러오기(리스트뷰)
    public List<ReviewVO> getAll(){
        List<ReviewVO> reviews = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM moviedb", null);

        while(cursor.moveToNext()){
            ReviewVO m = new ReviewVO();
            m.setId(cursor.getInt(0));
            m.setTitle(cursor.getString(1));
            m.setDate(cursor.getString(2));
            m.setRating(cursor.getDouble(3));
            m.setMemo(cursor.getString(4));
            m.setImage(cursor.getString(5));

            reviews.add(m);
        }

        return reviews;
    }

    //삭제
    public void delReview(String id){
        db = dbHelper.getWritableDatabase();
        db.delete("moviedb", "_id=?", new String[]{id});
    }

    //수정
    public void updateReview(String id, ReviewVO m){
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();

        v.put("title", m.getTitle());
        v.put("date", m.getDate());
        v.put("rate", m.getRating());
        v.put("memo", m.getMemo());
        v.put("image", m.getImage());

        db.update("moviedb", v, "_id=?", new String[]{id});
    }

    //데이터 하나만 불러오기
    public ReviewVO getReview(String id){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM moviedb WHERE _id=?", new String[]{id});
        cursor.moveToNext();

        ReviewVO r = new ReviewVO();
        r.setId(cursor.getInt(0));
        r.setTitle(cursor.getString(1));
        r.setDate(cursor.getString(2));
        r.setRating(cursor.getDouble(3));
        r.setMemo(cursor.getString(4));
        r.setImage(cursor.getString(5));

        return r;
    }

}
