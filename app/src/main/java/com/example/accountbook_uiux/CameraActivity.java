package com.example.accountbook_uiux;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    ImageView camera_receipt;
    Button buttonSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_receiptview);

        camera_receipt = findViewById(R.id.camera_receipt);
        buttonSelected = findViewById(R.id.buttonSelected);


        buttonSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MoneyInputActivity.class); //buttonSelected(확인)을 누르면 수입, 지출입력 화면으로 이동
            }
        });
    }
}
