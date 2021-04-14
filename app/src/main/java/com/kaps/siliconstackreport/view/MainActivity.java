package com.kaps.siliconstackreport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.os.Bundle;

import com.kaps.siliconstackreport.R;
import com.kaps.siliconstackreport.databinding.ActivityMainBinding;
import com.kaps.siliconstackreport.model.Callback.DeleteReportCallback;
import com.kaps.siliconstackreport.model.Report;
import com.kaps.siliconstackreport.model.helper.AppConstants;
import com.kaps.siliconstackreport.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DeleteReportCallback {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(new MainViewModel(this));
        activityMainBinding.executePendingBindings();

        activityMainBinding.getViewModel().getAllReports().observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                ReportAdapter reportAdapter = new ReportAdapter(MainActivity.this, reports,MainActivity.this::deleteReport);
                activityMainBinding.recyclerViewReports.setAdapter(reportAdapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityMainBinding.getViewModel().fetchNotes();
    }

    @Override
    public void deleteReport(Report report) {
        activityMainBinding.getViewModel().delete(report);
        AppConstants.showToastMessage(MainActivity.this, "Report Deleted Successfully");
    }
}