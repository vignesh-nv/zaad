package com.zaad.zaad.ui.dailytask;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.zaad.zaad.database.AppDatabase;
import com.zaad.zaad.database.DailyTask;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.repository.DatabaseRepository;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class DailyTaskViewModel extends AndroidViewModel {

    private FirestoreRepository firestoreRepository;
    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Video>> dailyTasks;

    private DatabaseRepository databaseRepository;

    public DailyTaskViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        dailyTasks = new MutableLiveData<>();
        firestoreRepository = new FirestoreRepository();
        databaseRepository = new DatabaseRepository(application);
    }

    public MutableLiveData<List<DailyTaskVideo>> getDailyTaskVideos() {
        return firestoreRepository.getDailyTasks();
    }

    public LiveData<List<DailyTask>> getCompletedTasks() {
        return databaseRepository.getAllTasks();
    }

    public LiveData<List<String>> getCompletedTasksIds() {
        return databaseRepository.getAllCompletedTaskIds();
    }

    public MutableLiveData<List<DailyTaskVideo>> getDailyTaskShorts() {
        return firestoreRepository.getDailyTaskShorts();
    }

    public void insertCompletedTask(DailyTaskVideo video) {
        DailyTask dailyTask = new DailyTask();
        dailyTask.taskId = video.getTaskId();
        databaseRepository.insert(dailyTask);
    }
}