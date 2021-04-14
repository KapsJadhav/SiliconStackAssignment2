package com.kaps.siliconstackreport.viewmodel;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kaps.siliconstackreport.model.Report;
import com.kaps.siliconstackreport.model.ReportRepo;
import com.kaps.siliconstackreport.model.helper.AppConstants;
import com.kaps.siliconstackreport.view.AddUpdateReportActivity;

import java.util.List;

public class MainViewModel extends ViewModel {

    private ReportRepo reportRepo;
    private LiveData<List<Report>> allReports;
    private Context context;
    private String sUserId = "1";

    public MainViewModel(Context context) {
        this.context = context;
        reportRepo = new ReportRepo(context);
        fetchNotes();
    }

    public void insert(Report report) {
        reportRepo.insert(report);
    }

    public void update(Report report) {
        reportRepo.update(report);
    }

    public void delete(Report report) {
        reportRepo.delete(report);
    }

    public void deleteAllNotes() {
        reportRepo.deleteAllReports();
    }

    public void fetchNotes() {
        allReports = reportRepo.getAllReports();
    }

    public void onAddReportButton() {
        Intent intent = new Intent(context, AddUpdateReportActivity.class);
        intent.putExtra(AppConstants.Keys.FROM, AppConstants.Status.ADD);
        context.startActivity(intent);
    }

    public LiveData<List<Report>> getAllReports() {
        return allReports;
    }
}
