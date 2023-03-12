package com.zaad.zaad.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.ReferralData;
import com.zaad.zaad.model.User;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.model.Withdrawal;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private static final String TAG = "UserRepository";

    FirebaseFirestore mFirestore;
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Withdrawal>> withdrawalMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Coupon>> couponsMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Boolean> isValidReferralCode = new MutableLiveData<>();

    public UserRepository() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void saveUser(final User user) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);
        mFirestore.collection("user").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    }
                });
    }

    public void updateUser(final Map<String, Object> updateMap) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);
        mFirestore.collection("user").document(userId).update(updateMap).addOnSuccessListener(unused -> {

        });
    }

    public MutableLiveData<User> getUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);

        mFirestore.collection("user").document(userId).addSnapshotListener((value, error) -> {
            User user = value.toObject(User.class);
            userMutableLiveData.postValue(user);
        });

        return userMutableLiveData;
    }

    public MutableLiveData<List<Withdrawal>> getWithdrawalList() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);

        mFirestore.collection("user").document(userId)
                .collection("transactions").orderBy("requestedDate", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                    List<Withdrawal> withdrawalList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            withdrawalList.add(doc.toObject(Withdrawal.class));
                            Log.i("FirestoreRepository", "Data -> " + doc.getData());
                        }
                    }
                    withdrawalMutableLiveData.postValue(withdrawalList);
                });

        return withdrawalMutableLiveData;
    }

    public void makeWithdrawTransaction(final Withdrawal withdrawal) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);

        mFirestore.collection("user").document(userId)
                .collection("transactions").add(withdrawal);
    }


    public MutableLiveData<Boolean> isValidReferralCode(final String referralCode) {
        mFirestore.collection("referralCode").document(referralCode).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    isValidReferralCode.setValue(true);
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                    isValidReferralCode.setValue(false);
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
        return isValidReferralCode;
    }

    public void saveReferralData(final ReferralData referralData) {
        mFirestore.collection("referralCode").document(referralData.getReferralCode()).set(referralData)
                .addOnSuccessListener(unused -> {
                    Log.i(TAG, "referralCode saved");
                });
    }

    public void reduceUserAmount(final int amount) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);

        mFirestore.collection("user").document(userId)
                .update("amount", amount)
                .addOnSuccessListener(unused -> {
                    Log.i(TAG, "Amount updated");
                });
    }

    public MutableLiveData<List<Coupon>> getMyCoupons() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);

        mFirestore.collection("user").document(userId)
                .collection("coupons").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Coupon> coupons = new ArrayList<>();
                    for (QueryDocumentSnapshot data : queryDocumentSnapshots) {
                        coupons.add(data.toObject(Coupon.class));
                    }
                    couponsMutableLiveData.postValue(coupons);
                });

        return couponsMutableLiveData;

    }

    public void redeemCoupon(final Coupon coupon) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getPhoneNumber().substring(3);

        mFirestore.collection("user").document(userId).collection("coupons")
                .add(coupon).addOnSuccessListener(documentReference -> {
                    Log.i(TAG, "Coupon Added");
                });
    }
}
