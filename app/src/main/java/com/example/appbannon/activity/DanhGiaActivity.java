package com.example.appbannon.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbannon.Interface.ImageUploadCallback;
import com.example.appbannon.R;
import com.example.appbannon.model.DanhGia;
import com.example.appbannon.model.HinhAnhDanhGia;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.networking.RatingApiCalls;
import com.example.appbannon.utils.CompleteSentenceWathcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DanhGiaActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText edtChatLuongSanPham, edtDungVoiMoTa, edtNhanXet;
    TextView tvMucDoHaiLong, tvMore;

    MaterialButton btnThemAnh;
    Toolbar toolbar;
    TextView btnGuiDanhGia;
    LinearLayout linearPreview;
    ImageView imageView1, imageView2, imageView3, btnRemove1, btnRemove2, btnRemove3;
    FrameLayout frameLayout1, frameLayout2, frameLayout3;

    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int maDonHang;
    List<String> mediaPaths = new ArrayList<>();
    List<HinhAnhDanhGia> hinhAnhDanhGiaList = new ArrayList<>();
    private int numStars = 5;

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RatingApiCalls.initialize(this);
        setContentView(R.layout.activity_danh_gia);
        setControl();
        initData();
        ActionToolBar();
        setEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("maDonHang")) {
            maDonHang = intent.getIntExtra("maDonHang", -1);
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @SuppressLint("SetTextI18n")
    private void setEvent() {

        // Luôn luôn tồn tại chữ được set trước đó
        edtChatLuongSanPham.addTextChangedListener(new CompleteSentenceWathcher(edtChatLuongSanPham.getText().toString()));
        edtDungVoiMoTa.addTextChangedListener(new CompleteSentenceWathcher(edtDungVoiMoTa.getText().toString()));


        // Xử lý sự kiện button remove ảnh được nhấn
        btnRemove1.setOnClickListener(v -> {
            mediaPaths.remove(0);
            displayImages();
        });

        btnRemove2.setOnClickListener(v -> {
            mediaPaths.remove(1);
            displayImages();
        });

        btnRemove3.setOnClickListener(v -> {
            mediaPaths.remove(2);
            displayImages();
        });

        // Xử lý sự kiện nút thêm ảnh được nhấn
        btnThemAnh.setOnClickListener(v -> ImagePicker.with(DanhGiaActivity.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        // Xử lý sự kiện đánh giá được gửi đi
        btnGuiDanhGia.setOnClickListener(v -> {

            CountDownLatch latch = new CountDownLatch(mediaPaths.size());
            DanhGia danhGia = new DanhGia();

            String chatLuongSanPham = edtChatLuongSanPham.getText().toString();
            String dungVoiMoTa = edtDungVoiMoTa.getText().toString();
            String nhanXet = edtNhanXet.getText().toString();

            String isCorrectWithDescribeHint = getString(R.string.isCorrectWithDescribeHint);
            String productQualityTitle = getString(R.string.productQualityTitle);

            danhGia.setDungVoiMoTa(dungVoiMoTa.replace(isCorrectWithDescribeHint, ""));
            danhGia.setChatLuongSanPham(chatLuongSanPham.replace(productQualityTitle, ""));
            danhGia.setNhanXet(nhanXet);
            danhGia.setSoSao(numStars);
            danhGia.setMaDonHang(maDonHang);

            progressBar.setVisibility(View.VISIBLE);

            // số lượng hình ảnh
            int numberOfImages = mediaPaths.size();
            if (numberOfImages > 0) {
                // Upload những hình ảnh đánh giá
                for (int i = 0; i < mediaPaths.size(); i++) {
                    String mediaPath = mediaPaths.get(i);
                    Uri uri = Uri.parse(mediaPath);
                    File file = new File(getPath(uri));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RatingApiCalls.uploadImage(fileToUpload, new ImageUploadCallback() {
                        @Override
                        public void onSuccess(ResponseObject<String> imageFile) {
                            hinhAnhDanhGiaList.add(new HinhAnhDanhGia(imageFile.getResult()));
                            latch.countDown();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(DanhGiaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                // Đợi cho đến khi upload ảnh đánh giá xong
                new Thread(() -> {
                    try {
                        latch.await();
                        runOnUiThread(() ->
                                createDanhGia(danhGia)
                        );
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            } else {
                createDanhGia(danhGia);
            }
            progressBar.setVisibility(View.GONE);

        });

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            numStars = Math.round(rating);
            // Set mức độ hài lòng dựa vào số sao
            switch (numStars) {
                case 1:
                    tvMucDoHaiLong.setText("Tệ");
                    break;
                case 2:
                    tvMucDoHaiLong.setText("Không hài lòng");
                    break;
                case 3:
                    tvMucDoHaiLong.setText("Bình thường");
                    break;
                case 4:
                    tvMucDoHaiLong.setText("Hài lòng");
                    break;
                case 5:
                    tvMucDoHaiLong.setText("Tuyệt vời");
                    break;
                case 0:
                    break;
            }
        });


    }

    private String getPath(Uri uri) {
        String result;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            String mediaPath = data.getDataString();
            if (mediaPath != null) {
                // thêm hình ảnh tại vị trí bắt đầu của màng
                mediaPaths.add(0, mediaPath);
                displayImages();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("DefaultLocale")
    private void displayImages() {
        // Xác định số hình ảnh được hiển thị
        int numImagesToShow = Math.min(4, mediaPaths.size()); // Tối đa 3 hình được hiển thị

        switch (numImagesToShow) {
            case 0:
                frameLayout1.setVisibility(View.GONE);
                frameLayout2.setVisibility(View.GONE);
                frameLayout3.setVisibility(View.GONE);

                tvMore.setVisibility(View.GONE);
                break;

            case 1:
                frameLayout1.setVisibility(View.VISIBLE);
                imageView1.setImageURI(Uri.parse(mediaPaths.get(0)));

                frameLayout2.setVisibility(View.GONE);
                frameLayout3.setVisibility(View.GONE);
                tvMore.setVisibility(View.GONE);
                break;
            case 2:

                frameLayout1.setVisibility(View.VISIBLE);
                frameLayout2.setVisibility(View.VISIBLE);
                imageView1.setImageURI(Uri.parse(mediaPaths.get(0)));
                imageView2.setImageURI(Uri.parse(mediaPaths.get(1)));

                frameLayout3.setVisibility(View.GONE);
                tvMore.setVisibility(View.GONE);

                break;
            case 3:

                frameLayout1.setVisibility(View.VISIBLE);
                frameLayout2.setVisibility(View.VISIBLE);
                frameLayout3.setVisibility(View.VISIBLE);
                imageView1.setImageURI(Uri.parse(mediaPaths.get(0)));
                imageView2.setImageURI(Uri.parse(mediaPaths.get(1)));
                imageView3.setImageURI(Uri.parse(mediaPaths.get(2)));

                tvMore.setVisibility(View.GONE);
                break;
            case 4:
                tvMore.setVisibility(View.VISIBLE);
                int more = mediaPaths.size() - 3;
                tvMore.setText(String.format("%d+", more));

        }
    }

    private void createDanhGia(DanhGia danhGia) {
        Log.d("myLog", danhGia.toString());
        RatingApiCalls.create(danhGia, danhGiaModel -> {
            if (danhGiaModel.getStatus() == 200) {
                Log.d("myLog", "success created");
                Toast.makeText(this, "Thêm đánh giá thành công", Toast.LENGTH_SHORT).show();
                // reset tất cả các field của đánh giá
                edtChatLuongSanPham.setText("");
                edtDungVoiMoTa.setText("");
                edtNhanXet.setText("");
                ratingBar.setRating(5f);
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, danhGiaModel.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("myLog", danhGiaModel.getMessage());
            }
        }, compositeDisposable);
    }

    @SuppressLint("SetTextI18n")
    private void setControl() {
        // rating
        ratingBar = findViewById(R.id.ratingBar);

        // Text view
        tvMucDoHaiLong = findViewById(R.id.tvMucDoHaiLong);
        tvMore = findViewById(R.id.tvMore);

        // Button
        btnGuiDanhGia = findViewById(R.id.btnGuiDanhGia);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        btnRemove1 = findViewById(R.id.btnRemove1);
        btnRemove2 = findViewById(R.id.btnRemove2);
        btnRemove3 = findViewById(R.id.btnRemove3);


        // Toolbar
        toolbar = findViewById(R.id.toolbar);

        // Edit text
        edtChatLuongSanPham = findViewById(R.id.edtChatLuongSanPham);
        edtDungVoiMoTa = findViewById(R.id.edtDungVoiMoTa);
        edtNhanXet = findViewById(R.id.edtNhanXet);

        // Linear
        linearPreview = findViewById(R.id.linearPreview);

        // Image View preview
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        // Frame Layout
        frameLayout1 = findViewById(R.id.frameLayout1);
        frameLayout2 = findViewById(R.id.frameLayout2);
        frameLayout3 = findViewById(R.id.frameLayout3);

        // Progress Bar
        progressBar = findViewById(R.id.progressBarRating);


        // Set rating for rating
        ratingBar.setRating(5);
        ratingBar.setStepSize(1);


        // Set initial text for EditTexts
        edtChatLuongSanPham.setText(R.string.productQualityTitle);

        edtDungVoiMoTa.setText(R.string.isCorrectWithDescribeHint);

    }


}