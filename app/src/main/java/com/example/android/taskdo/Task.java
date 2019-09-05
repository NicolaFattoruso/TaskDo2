package com.example.android.taskdo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entity: Represents a table within the database.
 */
@Entity(tableName = "tasks")
public class Task {
    private static final String TAG="Task";


    @PrimaryKey(autoGenerate = true)
    private Long ID;

    /**
     * Name of the task
     */
    @ColumnInfo(name = "task_name")
    private String taskName;

    /**
     * Variable to keep of the status of the task [Done or Not Done]
     */
    @ColumnInfo(name = "task_status")
    private Boolean taskStatus;
    /**
     * Each task has its own time
     */
    @ColumnInfo(name = "hour")
    private int hour;
    @ColumnInfo(name = "minute")
    private int minute;

    public Task(String taskName, Boolean taskStatus, int hour, int minute) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.hour = hour;
        this.minute = minute;
    }


    //GETTERS

    /**
     * @return the name of the Task
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @return true if task has been done, false if task has not been done yet.
     */
    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public Long getID() {
        return ID;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    //SETTERS

    public void setID(@NonNull Long ID) {
        this.ID = ID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskHour(int hour) {
        if (hour > 0 && hour < 24)
            this.hour = hour;
        else
        {
            Log.d(TAG, "Hour is not valid.");
        }
    }

    public void setTaskMinute(int minute) {
        if (minute >= 0 && minute <= 60)
            this.minute = minute;
        else
        {
            Log.d(TAG, "Minute is not valid.");
        }
    }

    //PUBLIC METHODS
    public void changeTaskStatus() {
        if (taskStatus == false)
            taskStatus = true;
        else
            taskStatus = false;
    }

    @Override
    public String toString() {
        return "Task Name: " + taskName;
    }
}


