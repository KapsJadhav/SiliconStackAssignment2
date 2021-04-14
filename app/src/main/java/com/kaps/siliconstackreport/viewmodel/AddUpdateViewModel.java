package com.kaps.siliconstackreport.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kaps.siliconstackreport.model.Report;
import com.kaps.siliconstackreport.model.ReportRepo;
import com.kaps.siliconstackreport.model.helper.AppConstants;
import com.kaps.siliconstackreport.view.AddUpdateReportActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AddUpdateViewModel extends ViewModel {

    private static final int PICK_IMAGES = 111;
    private static final int CAPTURE_IMAGES = 222;
    private ReportRepo reportRepo;
    private LiveData<List<Report>> allReports;
    private Context context;
    private String sReport = "";
    private String sImagePaths = "";
    private boolean isUpdate = false;
    private Report report;

    public AddUpdateViewModel(Context context) {
        this.context = context;
        reportRepo = new ReportRepo(context);
        fetchNotes();
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
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


    public void afterReportTextChanged(CharSequence s) {
        sReport = s.toString();
    }

    public void setsImagePaths(String sImagePaths) {
        this.sImagePaths = sImagePaths;
    }

    public void onCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, CAPTURE_IMAGES);
    }

    public void onGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        ((Activity) context).startActivityForResult(intent, PICK_IMAGES);
    }

    public void onAddReport() {
        if (sReport.trim().equals("")) {
            AppConstants.showToastMessage(context, "Please Enter Report Details");
        } else {
            if (!isUpdate) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.USER_DATE_TIME_FORMAT);
                String sCreatedAt = simpleDateFormat.format(calendar.getTime());
                Report report = new Report();
                report.setReport(sReport);
                report.setImagePaths(sImagePaths);
                report.setCreated_at(sCreatedAt);
                insert(report);
                fetchNotes();
                AppConstants.showToastMessage(context, "Report Added Successfully");
                ((Activity) context).finish();
            } else {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.USER_DATE_TIME_FORMAT);
                String sCreatedAt = simpleDateFormat.format(calendar.getTime());
                report.setReport(sReport);
                report.setImagePaths(sImagePaths);
                report.setCreated_at(sCreatedAt);
                update(report);
                AppConstants.showToastMessage(context, "Report Updated Successfully");
                fetchNotes();
                ((Activity) context).finish();
            }
        }
    }

    public LiveData<List<Report>> getAllReports() {
        return allReports;
    }
}
