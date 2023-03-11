package com.zaad.zaad.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DailyTaskDAO {

    @Query("SELECT * FROM dailytask")
    LiveData<List<DailyTask>> getAll();

    @Query("SELECT task_id FROM dailytask")
    LiveData<List<String>> getAllCompletedTaskIds();
    @Query("SELECT * FROM dailytask WHERE task_id IN (:taskIds)")
    List<DailyTask> loadAllByIds(int[] taskIds);

    @Insert
    void insertTask(DailyTask dailyTask);

}
