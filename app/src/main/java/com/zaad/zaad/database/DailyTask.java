package com.zaad.zaad.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyTask {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "task_id")
    public String taskId;

    @Override
    public String toString() {
        return "DailyTask{" +
                "taskId='" + taskId + '\'' +
                '}';
    }
}
