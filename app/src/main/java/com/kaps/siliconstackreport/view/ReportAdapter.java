package com.kaps.siliconstackreport.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaps.siliconstackreport.R;
import com.kaps.siliconstackreport.model.Callback.DeleteImageCallback;
import com.kaps.siliconstackreport.model.Callback.DeleteReportCallback;
import com.kaps.siliconstackreport.model.Report;
import com.kaps.siliconstackreport.model.helper.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context context;
    private List<Report> reportList;
    private DeleteReportCallback deleteReportCallback;

    public ReportAdapter(Context context, List<Report> reportList, DeleteReportCallback deleteReportCallback) {
        this.context = context;
        this.reportList = reportList;
        this.deleteReportCallback = deleteReportCallback;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.textViewTitle.setText(report.getReport());
        holder.textViewTime.setText(report.getCreated_at());

        List<Uri> uriList = new ArrayList<>();
        AppConstants.PrintLog("ReportAdapter", "Path :- " + report.getImagePaths());
        if (!report.getImagePaths().equals("")) {
            if (report.getImagePaths().contains(",")) {
                String[] imageUri = report.getImagePaths().split(",");
                for (int i = 0; i < imageUri.length; i++) {
                    String sPath = imageUri[i];
                    Uri uri = Uri.parse(sPath);
                    uriList.add(uri);
                    AppConstants.PrintLog("ReportAdapter", "Path : " + sPath);
                }
                ImageAdapter imageAdapter = new ImageAdapter(context, uriList);
                holder.recyclerViewReportImages.setAdapter(imageAdapter);
            } else {
                Uri uri = Uri.parse(report.getImagePaths());
                uriList.add(uri);
                ImageAdapter imageAdapter = new ImageAdapter(context, uriList);
                holder.recyclerViewReportImages.setAdapter(imageAdapter);
            }
        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogDelete = new Dialog(context, android.R.style.Theme_Translucent);
                dialogDelete.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialogDelete.getWindow().setGravity(Gravity.CENTER);
                dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDelete.setCancelable(false);
                dialogDelete.setContentView(R.layout.layout_dialog_alert);
                TextView textViewCancel = dialogDelete.findViewById(R.id.textViewCancel);
                TextView textViewSubmit = dialogDelete.findViewById(R.id.textViewSubmit);

                textViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDelete.cancel();
                    }
                });
                textViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDelete.cancel();
                        deleteReportCallback.deleteReport(report);
                    }
                });
                dialogDelete.show();
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddUpdateReportActivity.class);
                intent.putExtra(AppConstants.Keys.FROM, AppConstants.Status.UPDATE);
                intent.putExtra(AppConstants.Keys.REPORT, report);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewTime)
        TextView textViewTime;
        @BindView(R.id.imageViewEdit)
        ImageView imageViewEdit;
        @BindView(R.id.imageViewDelete)
        ImageView imageViewDelete;
        @BindView(R.id.recyclerViewReportImages)
        RecyclerView recyclerViewReportImages;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
