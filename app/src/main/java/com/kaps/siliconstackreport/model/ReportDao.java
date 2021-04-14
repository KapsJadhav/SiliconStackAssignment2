package com.kaps.siliconstackreport.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReportDao {

    @Insert
    void Insert(Report report);

    @Update
    void Update(Report report);

    @Delete
    void Delete(Report report);

    @Query("DELETE FROM Report")
    void DeleteAllReports();

    @Query("SELECT * FROM Report")
    LiveData<List<Report>> getAllReports();

}
