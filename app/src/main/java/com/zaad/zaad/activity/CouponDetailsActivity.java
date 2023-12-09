package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.R;
import com.zaad.zaad.model.Coupon;

import java.text.SimpleDateFormat;
import java.util.Locale;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class CouponDetailsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 154;
    ImageView couponImage, copyImageBtn;
    TextView couponCode, productNameTxt, discountTxt, expiryDateTxt, websiteOrShopNameTxt;
    Coupon coupon;
    Button productLinkBtn, purchasedBtn, mapButton, contactButton;
    View couponCodeLayout, contactMapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);
        couponImage = findViewById(R.id.coupon_image);
        couponCode = findViewById(R.id.coupon_code);
        productNameTxt = findViewById(R.id.product_name_txt);
        discountTxt = findViewById(R.id.discount_txt);
        expiryDateTxt = findViewById(R.id.expiry_date_txt);

        couponCodeLayout = findViewById(R.id.coupon_code_layout);
        copyImageBtn = findViewById(R.id.copy_image);
        productLinkBtn = findViewById(R.id.productLinkBtn);
        purchasedBtn = findViewById(R.id.purchasedBtn);
        contactMapLayout = findViewById(R.id.map_contact_layout);
        mapButton = findViewById(R.id.map_btn);
        contactButton = findViewById(R.id.contact_btn);
        websiteOrShopNameTxt = findViewById(R.id.website_or_shop_name_txt);

        coupon = (Coupon) getIntent().getSerializableExtra("COUPON");
        Log.i("Coupon ID", coupon.getCouponId() + " ");
        ImageLoader imageLoader = Coil.imageLoader(this);

        ImageRequest request = new ImageRequest.Builder(this)
                .data(coupon.getImageUrl())
                .crossfade(true)
                .target(couponImage)
                .build();
        imageLoader.enqueue(request);


        couponCode.setText(coupon.getCouponCode());

        productLinkBtn.setOnClickListener(view -> {
            Uri uri = Uri.parse(coupon.getProductLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        copyImageBtn.setOnClickListener(view ->  {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(coupon.getCouponCode());
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        });

        contactButton.setOnClickListener(view -> {
            checkPhonePermission();
        });

        mapButton.setOnClickListener(view -> {
            if (coupon.getMapUrl() == null) {
                return;
            }
            Uri uri = Uri.parse(coupon.getMapUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        purchasedBtn.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle("Product Purchased ?")
                    .setMessage("Have you purchased this product?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        updateCouponAsPurchased();
                    })
                    .setNegativeButton("No", ((dialogInterface, i) -> {

                    }))
                    .show();
        });

        if (coupon.getShopName() != null) {
            websiteOrShopNameTxt.setText(coupon.getShopName());
        }

        if (coupon.getAvailability().equals("OFFLINE")) {
            productLinkBtn.setVisibility(View.GONE);
            couponCodeLayout.setVisibility(View.GONE);

            if (coupon.isProductPurchased()) {
                purchasedBtn.setText("Already Purchased");
                purchasedBtn.setClickable(false);

                purchasedBtn.setBackgroundColor(getResources().getColor(R.color.light_black));
                purchasedBtn.setTextColor(getResources().getColor(R.color.white));
            }
        } else {
            purchasedBtn.setVisibility(View.GONE);
            contactMapLayout.setVisibility(View.GONE);
        }
        productNameTxt.setText(coupon.getProductName());
        discountTxt.setText(coupon.getDiscount());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        if (coupon.getExpiryDate()!=null)
            expiryDateTxt.setText(sdf.format(coupon.getExpiryDate()));
    }

    private void checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + coupon.getPhoneNumber()));
            startActivity(callIntent);
        }
    }
    private void updateCouponAsPurchased() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("user").document(firebaseUser.getEmail())
                .collection("coupons")
                .document(coupon.getCouponId())
                .update("productPurchased", true)
                .addOnSuccessListener(unused -> {
                    purchasedBtn.setText("Already Purchased");
                    purchasedBtn.setBackgroundColor(getResources().getColor(R.color.light_black));
                    purchasedBtn.setTextColor(getResources().getColor(R.color.white));
                    purchasedBtn.setClickable(false);
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}