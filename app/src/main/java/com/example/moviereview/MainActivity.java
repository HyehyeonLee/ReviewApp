package com.example.moviereview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Context CONTEXT;
    List<ReviewVO> reviews;
    ListView rlistview;
    ReviewDAO dao;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = this;

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Movie Review");


//        genSampleData();

        dao = ReviewDAO.open(this);
        reviews = dao.getAll();

        //리스트뷰 불러오기
        rlistview = findViewById(R.id.item_view);
        final ReviewAdapter adapter = new ReviewAdapter(this, R.layout.list_item, reviews);
        rlistview.setAdapter(adapter);

        //리스트뷰 아이템 선택
        rlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                toastMsg(view.getId() + "입니다.");
                Intent intent = new Intent(MainActivity.this, ReviewModifyActivity.class);

                intent.putExtra("reviewId", reviews.get(position).getId());


                startActivity(intent);
            }
        });
    }

    //메뉴 등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // 추가 버튼 클릭
            case R.id.action_new:
                Intent intent = new Intent(this, ReviewAddActivity.class);
                startActivity(intent);
                //toastMsg("New Review");
                break;
            case R.id.action_hidden:
                toastMsg("안녕?!");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ReviewAdapter adapter = (ReviewAdapter)rlistview.getAdapter();
        adapter.reviews = dao.getAll();
        adapter.notifyDataSetChanged();
    }

    //이벤트 확인용 토스트 메시지 출력 메소드
    void toastMsg(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();}

    //데이터 추가 화면
    public void openSubActivity(){
        Intent intent = new Intent(this, ReviewAddActivity.class);
        startActivity(intent);
    }

    void genSampleData(){
        reviews = new ArrayList<>();

        ReviewVO m = new ReviewVO("라라랜드", "2020.3.25", 3.5, "코엑스 재상영", null);
        reviews.add(m);
        m = new ReviewVO("토이스토리4", "2019.7.18", 5.0, "역곡 CGV", null);
        reviews.add(m);
    }
}
