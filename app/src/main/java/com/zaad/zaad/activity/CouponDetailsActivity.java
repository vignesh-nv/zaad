package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.Coupon;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class CouponDetailsActivity extends AppCompatActivity {

    ImageView couponImage;
    TextView couponCode;

    Coupon coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);
        couponImage = findViewById(R.id.coupon_image);
        couponCode = findViewById(R.id.coupon_code);

        coupon = (Coupon) getIntent().getSerializableExtra("COUPON");
        ImageLoader imageLoader = Coil.imageLoader(this);

        ImageRequest request = new ImageRequest.Builder(this)
                .data(coupon.getImageUrl())
                .crossfade(true)
                .target(couponImage)
                .build();
        imageLoader.enqueue(request);

        couponCode.setText(coupon.getCouponCode());
    }
}