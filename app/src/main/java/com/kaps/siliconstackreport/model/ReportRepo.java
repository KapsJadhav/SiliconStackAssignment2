package com.kaps.siliconstackreport.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportRepo {

    private ReportDao reportDao;
    private LiveData<List<Report>> allReports;

    public ReportRepo(Context context) {
        DatabaseHelper database = DatabaseHelper.getInstance(context);
        reportDao = database.reportDao();
        allReports = reportDao.getAllReports();
    }

    public void insert(Report report) {
        new InsertReportAsyncTask(reportDao).execute(report);
    }
    public void update(Report report) {
        new UpdateReportAsyncTask(reportDao).execute(report);
    }
    public void delete(Report report) {
        new DeleteReportAsyncTask(reportDao).execute(report);
    }
    public void deleteAllReports() {
        new DeleteAllReportsAsyncTask(reportDao).execute();
    }
    public LiveData<List<Report>> getAllReports() {
        return allReports;
    }


    private static class InsertReportAsyncTask extends AsyncTask<Report, Void, Void> { 
        private ReportDao reportDao;
        private InsertReportAsyncTask(ReportDao reportDao) {
            this.reportDao = reportDao;
        }
        @Override
        protected Void doInBackground(Report... reports) { 
            reportDao.Insert(reports[0]); //single Report
            return null;
        }
    }
    
    private static class UpdateReportAsyncTask extends AsyncTask<Report, Void, Void> {
        private ReportDao reportDao;
        private UpdateReportAsyncTask(ReportDao reportDao) { //constructor as the class is static
            this.reportDao = reportDao;
        }
        @Override
        protected Void doInBackground(Report... Reports) {
            reportDao.Update(Reports[0]);
            return null;
        }
    }
    private static class DeleteReportAsyncTask extends AsyncTask<Report, Void, Void> {
        private ReportDao reportDao;
        private DeleteReportAsyncTask(ReportDao reportDao) {
            this.reportDao = reportDao;
        }
        @Override
        protected Void doInBackground(Report... Reports) {
            reportDao.Delete(Reports[0]);
            return null;
        }
    }
    private static class DeleteAllReportsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReportDao reportDao;
        private DeleteAllReportsAsyncTask(ReportDao reportDao) {
            this.reportDao = reportDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            reportDao.DeleteAllReports();
            return null;
        }
    }

}
