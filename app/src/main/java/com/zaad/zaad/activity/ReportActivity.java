package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.R;
import com.zaad.zaad.model.Report;

import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    EditText messageTxt;
    Button submitBtn;
    TextInputEditText subjectTxt;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        messageTxt = findViewById(R.id.message_txt);
        submitBtn = findViewById(R.id.submit_btn);
        subjectTxt = findViewById(R.id.subject_txt);

        firestore = FirebaseFirestore.getInstance();
        submitBtn.setOnClickListener(view -> {
            submitReport();
        });
    }

    private void submitReport() {
        String subject = subjectTxt.getText().toString();
        String message = messageTxt.getText().toString();
        if (subject == null || subject.equals("")) {
            Toast.makeText(this, "Subject is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (message == null || message.equals("")) {
            Toast.makeText(this, "Message is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Report report = new Report();
        report.setMessage(message);
        report.setSubject(subject);
        report.setReportedDate(new Date());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        report.setUserId(firebaseUser.getEmail());
        firestore.collection("report").add(report).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ReportActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                    subjectTxt.setText("");
                    messageTxt.setText("");
                } else {
                    Log.i("ReportActivity", task.getResult().toString());
                    Log.i("ReportActivity", task.getException().toString());
                }
            }
        });
    }
}