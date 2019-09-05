package com.example.android.taskdo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    /**
     * Updating only price
     * By order id
     */
    @Query("UPDATE tasks SET task_status=:status WHERE id = :id")
    void updateTaskStatus(Boolean status, Long id);

    @Insert
    void insertTasks(Task... tasks);


    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteTaskById(Long id);

    @Query("DELETE FROM tasks")
    void nukeTable();


}
