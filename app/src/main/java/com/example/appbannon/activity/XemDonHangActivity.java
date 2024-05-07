package com.example.appbannon.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.appbannon.R;
import com.example.appbannon.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class XemDonHangActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don_hang);
        setControl();
        ActionToolBar();
    }


    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Tạo Adapter cho ViewPager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Kết nối ViewPager với TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}