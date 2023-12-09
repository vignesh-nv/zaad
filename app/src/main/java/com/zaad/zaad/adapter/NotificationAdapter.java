package com.zaad.zaad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.listeners.AdCompleteListener;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotififcationViewHolder> {

    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotififcationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.notification_item,
                        viewGroup, false);

        return new NotififcationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotififcationViewHolder viewHolder, int position) {

        Notification notification = notificationList.get(position);
        viewHolder.messageTxt.setText(notification.getNotificationText());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        viewHolder.dateTxt.setText(sdf.format(notification.getNotificationDate()));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotififcationViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTxt, dateTxt;

        NotififcationViewHolder(final View itemView) {
            super(itemView);
            messageTxt = itemView.findViewById(R.id.notification_text);
            dateTxt = itemView.findViewById(R.id.notification_date);
        }
    }
}
