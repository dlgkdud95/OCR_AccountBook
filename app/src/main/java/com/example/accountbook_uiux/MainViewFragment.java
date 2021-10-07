package com.example.accountbook_uiux;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class MainViewFragment extends Fragment  {

    private View view_main;

    //frame_main (메인화면) 변수
    CalendarView calendarView;
    FloatingActionButton fab_main, fab_camera, fab_writing;
    TextView txt_outlay, outlay, txt_income, income, txt_total, total;

    //카메라 - 파일
    File file;

    //fab_main 버튼의 상태, 기본값 -> 선택하지 않은 상태태
    private boolean isFabOpen = false;

    private Uri uri; //file, 웹 주소 관련 처리하는 객체


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_main = inflater.inflate(R.layout.frame_main, container, false);
        //frame_main
        calendarView = (CalendarView) view_main.findViewById(R.id.calendarView);
        txt_income = (TextView) view_main.findViewById(R.id.txt_income);
        txt_outlay = (TextView) view_main.findViewById(R.id.txt_outlay);
        txt_total = (TextView) view_main.findViewById(R.id.txt_total);
        income = (TextView) view_main.findViewById(R.id.income);
        outlay = (TextView) view_main.findViewById(R.id.outlay);
        total = (TextView) view_main.findViewById(R.id.total);

        MoneyInputActivity moneyInputActivity = new MoneyInputActivity();

        income.setText(String.valueOf(moneyInputActivity.INCOMETOTAL)); // INCOMETOTAL 텍스트화함
        outlay.setText(String.valueOf(moneyInputActivity.OUTLAYTOTAL)); //OUTLAYTOTAL 텍스트화함
        total.setText(String.valueOf(moneyInputActivity.SUMTOTAL));     //SUMTOTAL 텍스트화함

        floatingActionButton(view_main); //fab버튼 작동 할 수 있게 해주는 메소드

        return view_main;
    }

    private void floatingActionButton (View view_main) {

        FloatingActionButton fab_main = (FloatingActionButton) view_main.findViewById(R.id.fab_main);
        FloatingActionButton fab_camera = (FloatingActionButton) view_main.findViewById(R.id.fab_camera);
        FloatingActionButton fab_writing = (FloatingActionButton) view_main.findViewById(R.id.fab_writing);

        //(플로팅액션버튼)버튼을 누르면 화면이 넘어감(fab_main 제외)
        fab_main.setOnClickListener(new View.OnClickListener() { //버튼 클릭시 수행할 동작 지정
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "누름", Toast.LENGTH_SHORT).show();
                toggleFab();
            }
        });
        fab_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //수입,지출 화면 전환
                Intent intent = new Intent(getActivity().getApplicationContext(), MoneyInputActivity.class);
                startActivity(intent);
            }
        });
        fab_camera.findViewById(R.id.fab_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takePicture(); //카메라 연동 메소드(찍은 사진 저장할 파일 위치 지정 및 카메라 앱 열기), CameraActivity화면으로 바로 넘어갈 지 사진을 찍고 넘어갈 지 결정x
                Intent intent = new Intent(getActivity().getApplicationContext(), CameraActivity.class);
                startActivity(intent); //CameraActivity로 화면 전환
            }
        });
    }
    public void toggleFab() {
        if (isFabOpen) { //닫힌상태
            //writing, camera y축으로 0만큼 움직임
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_writing), "translationY", 0f).start();
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_camera), "translationY", 0f).start();
        } else {
            //writing -100, camera -200 만큼 y축으로움직임
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_writing), "translationY", -150f).start();
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_camera), "translationY", -290f).start();
        }

        isFabOpen = !isFabOpen;
    }

    //카메라 연동 매소드
    /*public void takePicture() {
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
            uri = FileProvider.getUriForFile(this.context, BuildConfig.APPLICATION_ID, file); //file에 대해 uri 생성
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
        File outFile = new File(getActivity().getFilesDir(), filename); //저장 경로 반환, 새로운 파일 만들고 저장
        Log.d("Main", "File path : " + outFile.getAbsolutePath());  //입력된 절대경로 그대로 표현

        return outFile;
    }

    ImageView camera_receipt = CameraActivity().findViewById(R.id.camera_receipt);

    //카메라 앱 종료 후 다시 돌아올 때 호출되는 메소드, 카메라로 찍은 사진 파일에서 확인 할 수 있음
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {   //101은 takePicture()메소드에서 받은 값
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;           //해상도가 높은 경우 bitmap객체의 크기도 커져서 임의로 비율을 1/8크기로 축소
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options); //이미지 파일을 Bitmap객체로 만듦, Bitmap객체는 메모리에 만들어지는 이미지

            camera_receipt.setImageBitmap(bitmap); ////Bitmap객체로 만들어진 파일이 이미지뷰로 보임
        }
    } */
}
