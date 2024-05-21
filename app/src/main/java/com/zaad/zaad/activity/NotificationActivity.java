package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.NotificationAdapter;
import com.zaad.zaad.model.Notification;
import com.zaad.zaad.model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notificationRv;
    NotificationAdapter notificationAdapter;

    List<Notification> notificationList;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    User user;

    TextView noNotificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        notificationRv = findViewById(R.id.notification_recycler_view);
        noNotificationText = findViewById(R.id.no_notification_text);

        notificationAdapter = new NotificationAdapter(notificationList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        notificationRv.setLayoutManager(layoutManager);
//        notificationRv.setHasFixedSize(true);
        notificationRv.setAdapter(notificationAdapter);
        getNotificationData();
    }

    private void getNotificationData() {
        firestore.collection("user").document(firebaseUser.getEmail())
                .collection("notifications")
                .orderBy("notificationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                        notificationList.add(snapshot.toObject(Notification.class));
                    }
                    if (notificationList == null || notificationList.size() == 0) {
                        noNotificationText.setVisibility(View.VISIBLE);
                    }
                    notificationAdapter.notifyDataSetChanged();
                });
    }
}