package com.example.moviereview;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ReviewModifyActivity extends AppCompatActivity {
    TextView titleTxt;
    TextView dateTxt;
    TextView memoTxt;
    ImageButton imgBtn;
    RatingBar ratingBar;
    final int GET_GALLERY_IMAGE = 200;
    static ReviewDAO dao;
    int reviewId;
    static ReviewVO review;
    ReviewDBHelper dbHelper;
    static Uri selectedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Intent intent = getIntent();

        //전달 받은 id값 받기
        reviewId = intent.getExtras().getInt("reviewId");

        //DAO 열기
        dao = ReviewDAO.open(this);
        review = dao.getReview(String.valueOf(reviewId));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Review");

        titleTxt = findViewById(R.id.titleTxt);
        dateTxt = findViewById(R.id.dateTxt);
        memoTxt = findViewById(R.id.memoTxt);
        imgBtn = findViewById(R.id.imgBtn);
        ratingBar = findViewById(R.id.ratingBar);


        //View에 전달 받은 객체의 값 세팅
        titleTxt.setText(review.getTitle().toString());
        dateTxt.setText(review.getDate().toString());
        ratingBar.setRating((float) review.getRating());
        memoTxt.setText(review.getMemo());
        Uri imageUri = getUriFromPath(review.getImage());;
        imgBtn.setImageURI(imageUri);
//        setBitmapImg(ImageUri);

    }

    //메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify, menu);
        return true;
    }

    //메뉴 선택
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                //수정 기능
                dao = ReviewDAO.open(this);
                ReviewVO updateMovie = new ReviewVO();
                updateMovie.setTitle(titleTxt.getText().toString());
                updateMovie.setDate(dateTxt.getText().toString());
                updateMovie.setRating(ratingBar.getRating());
                updateMovie.setMemo(memoTxt.getText().toString());
                updateMovie.setImage(review.getImage());
                dao.updateReview(String.valueOf(review.getId()), updateMovie);
                toastMsg("Save Review");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.action_delete:
                //삭제 기능
                dao = ReviewDAO.open(this);
                dao.delReview(String.valueOf(review.getId()));
                toastMsg("Delete Review");
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.action_share:
                //공유 기능
                shareMenuClick(item);
                //toastMsg("Share Review");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //공유 메뉴 클릭
    public void shareMenuClick(MenuItem item){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, titleTxt.getText().toString() + "은(는) 별점 " + ratingBar.getRating() +"점 입니다!");

        Intent chooser = Intent.createChooser(intent, "공유");
        startActivity(chooser);
    }

    //이미지 버튼 클릭
    public void imgBtnClick(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImgUri = data.getData();
            setBitmapImg(selectedImgUri);
        }
    }

    //절대 경로 -> Uri
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }

    //Uri -> 절대 경로
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void setBitmapImg(Uri uri){
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            imgBtn.setImageBitmap(bitmap);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }


    //관람일 선택
    public void onDateTxtClicked(View v){
        DatePickerDialog dialog = new DatePickerDialog(this);
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateTxt.setText(year + ". " + (month+1) + ". " + dayOfMonth);
            }
        });
        dialog.show();
    }

    //이벤트 확인용 토스트 메시지 출력 메소드
    void toastMsg(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();}
}
