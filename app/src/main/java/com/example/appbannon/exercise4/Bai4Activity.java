package com.example.appbannon.exercise4;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbannon.R;

public class Bai4Activity extends AppCompatActivity {
    private Switch swBongDen;
    private LinearLayout lBongDen;
    private ImageView imgBongDenBat, imgBongDenTat;
    private ToggleButton tgBatTat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4);
        setControl();
        setEvent();
    }

    private void setEvent() {
        swBongDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swBongDen.isChecked()) {
                    lBongDen.setVisibility(View.VISIBLE);
                    imgBongDenBat.setVisibility(View.GONE);
                    imgBongDenTat.setVisibility(View.VISIBLE);
                } else {
                    lBongDen.setVisibility(View.GONE);

                }
            }
        });

        tgBatTat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgBongDenBat.setVisibility(View.VISIBLE);
                    imgBongDenTat.setVisibility(View.GONE);
                } else {
                    imgBongDenBat.setVisibility(View.GONE);
                    imgBongDenTat.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setControl() {
        swBongDen = findViewById(R.id.swBongDen);
        lBongDen = findViewById(R.id.lBongDen);
        imgBongDenBat = findViewById(R.id.imgBongDenBat);
        imgBongDenTat = findViewById(R.id.imgBongDenTat);
        tgBatTat = findViewById(R.id.tgBatTat);
    }
}