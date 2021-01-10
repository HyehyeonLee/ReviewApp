package com.example.moviereview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ReviewAddActivity extends AppCompatActivity {
    TextView titleTxt;
    TextView dateTxt;
    TextView memoTxt;
    ImageButton imgBtn;
    RatingBar ratingBar;
    final int GET_GALLERY_IMAGE = 200;
    ReviewDAO dao;
    static Uri selectedImgUri;
    ReviewVO m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Review");

        titleTxt = findViewById(R.id.titleTxt);
        dateTxt = findViewById(R.id.dateTxt);
        memoTxt = findViewById(R.id.memoTxt);
        imgBtn = findViewById(R.id.imgBtn);
        ratingBar = findViewById(R.id.ratingBar);

    }

    //메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    //메뉴 선택
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                //저장 기능
                m = new ReviewVO();
                m.setTitle(titleTxt.getText().toString());
                m.setDate(dateTxt.getText().toString());
                m.setRating(ratingBar.getRating());
                m.setMemo(memoTxt.getText().toString());
                String ImagePath = getRealPathFromURI(selectedImgUri);
                m.setImage(ImagePath);
                dao = ReviewDAO.open(this);
                dao.addReview(m);

                toastMsg("Save Review");

                Intent intent = new Intent(this, MainActivity.class);
                closeActivity();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
            //imgBtn.setImageURI(selectedImgUri);
            setBitmapImg(selectedImgUri);
        }
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

    public void closeActivity () {
        finish();
    }

    //이벤트 확인용 토스트 메시지 출력 메소드
    void toastMsg(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();}
}
