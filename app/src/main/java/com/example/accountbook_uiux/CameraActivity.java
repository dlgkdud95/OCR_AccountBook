package com.example.accountbook_uiux;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    ImageView camera_receipt;
    Button buttonSelected, buttonCamera;
    //카메라 - 파일
    File file;
    private Uri uri; //file, 웹 주소 관련 처리하는 객체

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_receiptview);

        camera_receipt = findViewById(R.id.camera_receipt);
        buttonSelected = findViewById(R.id.buttonSelected);
        buttonCamera = findViewById(R.id.buttonCamera);

        buttonCamera.setOnClickListener(new View.OnClickListener() {    //촬영 버튼 누르면 카메라 실행됨
            @Override                                                   //이 버튼 말고 메인화면에서 카메라버튼(fab)누르면 바로 카메라 촬영 후 사진이 보이게 하고 싶은데 카메라 화면에서 넘어가질 않음. 나중에 수정
            public void onClick(View view) {
                takePicture();
            }
        });

        buttonSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectIntent = new Intent(getApplicationContext(), MoneyInputActivity.class); //buttonSelected(확인)을 누르면 수입, 지출입력 화면으로 이동
                startActivity(selectIntent);
            }
        });
    }

    //카메라 연동 매소드
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

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //내장 되어 있는 카메라 기능을 intent로 선언, 카메라 앱 실행
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //기존 frag에 새 frag추가, 종료 할 때까지 URI 접근에 대한 허가를 받음
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //FileProvider.getUriForFile에서 받은 uri객체, EXTRA_OUTPUT키를 사용해 인텐트에 부가 데이터로 추가
        startActivityForResult(cameraIntent, 101); //intent(카메라 앱 실행)에서 받은 request 값 시스템으로 전달

    }

    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(getFilesDir(), filename); //저장 경로 반환, 새로운 파일 만들고 저장
        Log.d("Main", "File path : " + outFile.getAbsolutePath());  //입력된 절대경로 그대로 표현

        return outFile;
    }

    //카메라 앱 종료 후 다시 돌아올 때 호출되는 메소드, 카메라로 찍은 사진 파일에서 확인 할 수 있음
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {   //101은 takePicture()메소드에서 받은 값
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;           //해상도가 높은 경우 bitmap객체의 크기도 커져서 임의로 비율을 1/8크기로 축소
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options); //이미지 파일을 Bitmap객체로 만듦, Bitmap객체는 메모리에 만들어지는 이미지

            camera_receipt.setImageBitmap(bitmap); ////Bitmap객체로 만들어진 파일이 이미지뷰로 보임
        }
    }
}
