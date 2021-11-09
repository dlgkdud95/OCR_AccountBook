package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {


    public static final String FILE_NAME = "temp.jpg";

    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;


    public static int RECEIPT_COST = 0; // 영수증에서 받아온 COST 값
    public static String RECEIPT_DATE = ""; // 영수증에서 받아온 DATE 값값

    private TextView receiptText;
    private ImageView receiptImage;
    private TextView tv_ocrResult;
    //private EditText et_date, et_storeName, et_storeAdr, et_totalPrice;
    private Spinner spinner_category;
    private Spinner spinner_payment;


    //fab_main 버튼의 상태, 기본값 -> 선택하지 않은 상태
    private boolean isFabOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiptscan_view);

        FloatingActionButton fab_receiptSelect = findViewById(R.id.fab_receiptSelect);
        FloatingActionButton fab_camera = findViewById(R.id.fab_camera);
        FloatingActionButton fab_album = findViewById(R.id.fab_album);
        tv_ocrResult = (TextView) findViewById(R.id.tv_ocrResult);
        /*
        et_date = (EditText) findViewById(R.id.et_date);
        et_storeName = (EditText) findViewById(R.id.et_storeName);
        et_storeAdr = (EditText) findViewById(R.id.et_storeAdr);
        et_totalPrice = (EditText) findViewById(R.id.et_totalPrice);

         */
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_payment = (Spinner) findViewById(R.id.spinner_payment);

        //(플로팅액션버튼)버튼을 누르면 화면이 넘어감(fab_main 제외)
        fab_receiptSelect.setOnClickListener(new View.OnClickListener() { //버튼 클릭시 수행할 동작 지정
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera(); //카메라로 넘어감
            }
        });
        fab_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGalleryChooser();  //앨범으로 넘어감
            }
        });

        Button bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cateogory = spinner_category.getSelectedItem().toString();
                String payment = spinner_payment.getSelectedItem().toString();
                if(RECEIPT_COST == 0 && RECEIPT_DATE == "") // 만약 초기값이라면 아무것도 안하기
                {
                    Log.d("alert", "아무것도 안하기");
                }
                else
                {
                    if(spinner_category.getSelectedItemPosition() == 0) // 아무것도 선택안할시
                    {
                        dbHelper.InsertDB("지출", RECEIPT_COST, "기타", RECEIPT_DATE, payment, "기타");
                    }
                    else dbHelper.InsertDB("지출", RECEIPT_COST, cateogory, RECEIPT_DATE, payment, "기타");
                }
                // 전역변수로 데이터 전달 후 다시 초기화
                RECEIPT_COST = 0;
                RECEIPT_DATE = "";

                // 영수증에서 입력하면 지출경고 안뜸. 뜨게 수정

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);   //확인 버튼을 누르면 메인 클래스로 넘어감
                startActivity(intent);
            }
        });


        receiptText = findViewById(R.id.receiptText);
        receiptImage = findViewById(R.id.receiptImage);
    }
    //fab 움직이게 하는 메소드
    public void toggleFab() {
        if (isFabOpen) { //닫힌상태
            //writing, camera y축으로 0만큼 움직임
            ObjectAnimator.ofFloat(findViewById(R.id.fab_album), "translationY", 0f).start();
            ObjectAnimator.ofFloat(findViewById(R.id.fab_camera), "translationY", 0f).start();
        } else {
            //writing -100, camera -200 만큼 y축으로움직임
            ObjectAnimator.ofFloat(findViewById(R.id.fab_album), "translationY", -150f).start();
            ObjectAnimator.ofFloat(findViewById(R.id.fab_camera), "translationY", -290f).start();
        }

        isFabOpen = !isFabOpen;
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);    //비트맵 만들기

                String imagecode = BitmapToBase64(bitmap);

                new Thread()
                {
                    public void run()
                    {
                        String ocrMessage = "";
                        try {

                            String apiURL = "https://73c0c93018884e1fbd86063424423faf.apigw.ntruss.com/custom/v1/11734/5f2e21b4e8b549e5b7f1976359ebd77b007088536d663b5765c3ce876a807c70/document/receipt";
                            String secretKey = "Wm9NRGFrSnRxeGpiYVZ1d1ZwTm9iYUlOd2lOTXZ0Umo=";

                            String objectStorageURL = imagecode; // 여기에 사진

                            URL url = new URL(apiURL);

                            String message = getReqMessage(objectStorageURL);
                            System.out.println("##" + message);

                            long timestamp = new Date().getTime();

                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("Content-Type", "application/json;UTF-8");
                            con.setRequestProperty("X-OCR-SECRET", secretKey);

                            // post request
                            con.setDoOutput(true);
                            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                            wr.write(message.getBytes("UTF-8"));
                            wr.flush();
                            wr.close();

                            int responseCode = con.getResponseCode();

                            if(responseCode==200) { // 정상 호출
                                System.out.println(con.getResponseMessage());

                                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                                con.getInputStream()));
                                String decodedString;
                                while ((decodedString = in.readLine()) != null) {
                                    ocrMessage = decodedString;
                                }
                                //chatbotMessage = decodedString;
                                in.close();

                            } else {  // 에러 발생
                                ocrMessage = con.getResponseMessage();
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }



                        //JSON PARSING START
                        try
                        {
                            String json = ocrMessage;
                            JSONObject jsonObject = new JSONObject(json);
                            String images = jsonObject.getString("images");
                            JSONArray jsonArray = new JSONArray(images);
                            JSONObject subJsonObject = jsonArray.getJSONObject(0);

                            String receipt = subJsonObject.getString("receipt");
                            JSONObject subJsonObject2 = new JSONObject(receipt); // receipt object

                            String result = subJsonObject2.getString("result");
                            JSONObject subJsonObject3 = new JSONObject(result); //result object

                            String paymentInfo = subJsonObject3.getString("paymentInfo");
                            JSONObject subJsonObject4_3 = new JSONObject(paymentInfo);

                            String date = subJsonObject4_3.getString("date");
                            JSONObject subJsonObject5_3 = new JSONObject(date);

                            String formatted_date = subJsonObject5_3.getString("formatted");
                            JSONObject formatteddateJsonObject = new JSONObject(formatted_date);

                            String formatted_year = formatteddateJsonObject.getString("year");
                            String formatted_month = formatteddateJsonObject.getString("month");
                            String formatted_day = formatteddateJsonObject.getString("day");

                            String storeInfo = subJsonObject3.getString("storeInfo");
                            JSONObject subJsonObject4 = new JSONObject(storeInfo); //storeInfo object

                            String totalPrice = subJsonObject3.getString("totalPrice");
                            JSONObject subJsonObject4_2 = new JSONObject(totalPrice); // totalPrice object

                            String price = subJsonObject4_2.getString("price");
                            JSONObject subJsonOBject5_2 = new JSONObject(price); // price object

                            String text_price = subJsonOBject5_2.getString("text"); // 가격


                            String formatted_price = subJsonOBject5_2.getString("formatted");
                            JSONObject subJsonObject6 = new JSONObject(formatted_price);


                            String name = subJsonObject4.getString("name");
                            JSONObject subJsonObject5 = new JSONObject(name); // name object

                            String addresses = subJsonObject4.getString("addresses");
                            JSONArray subJsonArray = new JSONArray(addresses); // address array
                            JSONObject addressJsonObject = subJsonArray.getJSONObject(0);


                            String text_storeName = subJsonObject5.getString("text"); // 가게 이름


                            String text_storeAddress = addressJsonObject.getString("text");


                            String date_value = formatted_year+"-"+formatted_month+"-"+formatted_day; // 날짜 값 -> DB저장용
                            String formatted_price_value = subJsonObject6.getString("value"); // RAW PRICE VALUE -> DB저장용

                            RECEIPT_COST = Integer.parseInt(formatted_price_value);
                            RECEIPT_DATE = date_value;


                            tv_ocrResult.setText("날짜 : "+date_value+"\n\n가게 이름 : "+text_storeName+"\n\n가게 주소 : "+text_storeAddress+"\n\n가격 : "+text_price); // OCR_RESULT VIEW
                            /*
                            et_date.setText(date_value);
                            et_storeName.setText(text_storeName);
                            et_storeAdr.setText(text_storeAddress);
                            et_totalPrice.setText(text_price);
                             */




                            // 문제점 : Base64로 인코딩할때 Base64의 문제점인 너무 긴 문자열로 인해 json 값이 거의 무조건 중간에서 짤림
                            //         영수증에서 읽어올 데이터 적을 시엔 문제가 되지 않지만 만약 상품이 많은 경우
                            //         totalPrice 값까지 json값을 불러오지 않아 parsing 중 오류가 발생


                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                        System.out.println(">>>>>>>>>>"+ ocrMessage);
                    }

                }.start();

                receiptImage.setImageBitmap(bitmap);  //이미지뷰에 bitmap 저장

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public static String getReqMessage(String objectStorageURL) {

        String requestBody = "";

        try {

            long timestamp = new Date().getTime();

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", Long.toString(timestamp));
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("data", objectStorageURL);

            image.put("name", "test_ocr");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);

            requestBody = json.toString();

        } catch (Exception e){
            System.out.println("## Exception : " + e);
        }

        return requestBody;

    }


    public String BitmapToBase64(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bImage = baos.toByteArray();
        String base64 = Base64.encodeToString(bImage, Base64.DEFAULT);
        return base64;
    }


    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {   //카메라 해상도가 높을 수도 있어서 적당한 비율로 이미지 축소

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


}
