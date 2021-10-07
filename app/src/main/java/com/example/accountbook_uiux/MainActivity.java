package com.example.accountbook_uiux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //private FragmentStateAdapter fragmentStateAdapter;

    //frame_stats 변수

    //activity_main 화면
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MainViewFragment mainViewFragment;
    private StatsViewFragment statsViewFragment;

    //카메라 - 파일
    File file;

    //fab_main 버튼의 상태, 기본값 -> 선택하지 않은 상태태
    private boolean isFabOpen = false;

    private Uri uri; //file, 웹 주소 관련 처리하는 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //하단 네비게이션 눌렀을 때 화면 변경 됨, onNavi~() 메소드 통해 setFrag() 메소드가 해당하는 Fragment로 교체함
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_calender:
                        setFrag(0);
                        break;
                    case R.id.action_stats:
                        setFrag(1);
                        break;
                }
                return true;
            }
        });
        mainViewFragment = new MainViewFragment();
        statsViewFragment = new StatsViewFragment();
        setFrag(0);  // 메인 화면 선택
    }

    //하단 네비게이션 프래그먼트 교체 , onNavigationItemSelected()메소드 안에서 사용
    private void setFrag(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (i) {
            case 0:
                ft.replace(R.id.frame, mainViewFragment); //FragMainView 클래스로 이동
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frame, statsViewFragment); //FragStatsView 클래스로 이동
                ft.commit();
                break;
        }
    }

   /* public void toggleFab() {
        if (isFabOpen) { //닫힌상태
            //writing, camera y축으로 0만큼 움직임
            ObjectAnimator.ofFloat(fab_writing, "translationY", 0f).start();
            ObjectAnimator.ofFloat(fab_camera, "translationY", 0f).start();
        } else {
            //writing -100, camera -200 만큼 y축으로움직임
            ObjectAnimator.ofFloat(fab_writing, "translationY", -100f).start();
            ObjectAnimator.ofFloat(fab_camera, "translationY", -200f).start();
        }

        isFabOpen = !isFabOpen;
    }*/

   /* //카메라 연동 매소드
    public void takePicture() {
        try {
            file = createFile(); //사진 찍고 결과물 저장할 파일 만듦
            if (file.exists()) {
                file.delete(); //파일이 존재한다면 삭제
            }

            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace(); //에러 발생 경로 찍어줌
        }

        //file객체로부터 Uri 객체 만들기
        if (Build.VERSION.SDK_INT >= 24) { // API 레벨이  24이상일 때
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file); //file에 대해 uri 생성
        }                                                                                    //카메라 앱에서 공유하여 쓸 수 있는 파일 정보를 uri객체로 만듦
        else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //내장 되어 있는 카메라 기능을 intent로 선언, 카메라 앱 실행
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //기존 frag에 새 frag추가, 종료 할 때까지 URI 접근에 대한 허가를 받음
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //FileProvider.getUriForFile에서 받은 uri객체, EXTRA_OUTPUT키를 사용해 인텐트에 부가 데이터로 추가
        startActivityForResult(intent, 101); //intent(카메라 앱 실행)에서 받은 request 값 시스템으로 전달

    }

    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(getFilesDir(), filename); //저장 경로 반환, 새로운 파일 만들고 저장
        Log.d("Main", "File path : " + outFile.getAbsolutePath());  //입력된 절대경로 그대로 표현

        return outFile;
    }

    ImageView camera_receipt = findViewById(R.id.camera_receipt);

    //카메라 앱 종료 후 다시 돌아올 때 호출되는 메소드, 카메라로 찍은 사진 파일에서 확인 할 수 있음
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {   //101은 takePicture()메소드에서 받은 값
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;           //해상도가 높은 경우 bitmap객체의 크기도 커져서 임의로 비율을 1/8크기로 축소
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options); //이미지 파일을 Bitmap객체로 만듦, Bitmap객체는 메모리에 만들어지는 이미지

            camera_receipt.setImageBitmap(bitmap); ////Bitmap객체로 만들어진 파일이 이미지뷰로 보임
        }
    } */
}