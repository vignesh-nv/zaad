package com.zaad.zaad.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zaad.zaad.database.AppDatabase;
import com.zaad.zaad.database.DailyTask;
import com.zaad.zaad.database.DailyTaskDAO;

import java.util.List;

public class DatabaseRepository {
    private DailyTaskDAO dailyTaskDAO;
    private LiveData<List<DailyTask>> allTasks;

    private LiveData<List<String>> completedTaskIds;

    public DatabaseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dailyTaskDAO = db.dailyTaskDAO();
        allTasks = dailyTaskDAO.getAll();
        completedTaskIds = dailyTaskDAO.getAllCompletedTaskIds();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<DailyTask>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<String>> getAllCompletedTaskIds() {
        return dailyTaskDAO.getAllCompletedTaskIds();
    }
    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(DailyTask dailyTask) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dailyTaskDAO.insertTask(dailyTask);
        });
    }
}

