package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.R;
import com.zaad.zaad.model.AdBanner;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class AdDetailActivity extends AppCompatActivity {

    ImageView adImage;
    String id;
    FirebaseFirestore firestore;
    TextView adEmailTxt, adAddressTxt, adContactNumberTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);

        adImage = findViewById(R.id.ad_image);
        adEmailTxt = findViewById(R.id.ad_email);
        adAddressTxt = findViewById(R.id.ad_address);
        adContactNumberTxt = findViewById(R.id.ad_contact_number);

        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        id = getIntent().getStringExtra("AD_ID");
        ImageLoader imageLoader = Coil.imageLoader(this);

        ImageRequest request = new ImageRequest.Builder(this)
                .data(imageUrl)
                .crossfade(true)
                .target(adImage)
                .build();
        imageLoader.enqueue(request);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("imageBanner").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AdBanner adBanner = documentSnapshot.toObject(AdBanner.class);
                if (adBanner != null)
                    updateUI(adBanner);
            }
        });
        firestore.collection("imageBanner")
                .document(id)
                .update("views", FieldValue.increment(1));
    }

    private void updateUI(AdBanner adBanner) {
        String address = "Address: " + adBanner.getAddress();
        String email = "Email: " + adBanner.getEmail();
        String contactNumber = "Contact Number: " + adBanner.getPhoneNumber();
        adAddressTxt.setText(address);
        adEmailTxt.setText(email);
        adContactNumberTxt.setText(contactNumber);
    }
}