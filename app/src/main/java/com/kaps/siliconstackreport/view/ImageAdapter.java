package com.kaps.siliconstackreport.view;

import android.app.Dialog;
import android.content.Context;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Uri> uriList;
    private DeleteImageCallback deleteImageCallback;

    public ImageAdapter(Context context, List<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    public DeleteImageCallback getDeleteImageCallback() {
        return deleteImageCallback;
    }

    public void setDeleteImageCallback(DeleteImageCallback deleteImageCallback) {
        this.deleteImageCallback = deleteImageCallback;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = uriList.get(position);
        holder.imageViewReport.setImageURI(uri);
        if (deleteImageCallback != null) {
            holder.imageViewDelete.setVisibility(View.VISIBLE);
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
                TextView textViewTitle = dialogDelete.findViewById(R.id.textViewTitle);
                TextView textViewLabel = dialogDelete.findViewById(R.id.textViewLabel);
                TextView textViewCancel = dialogDelete.findViewById(R.id.textViewCancel);
                TextView textViewSubmit = dialogDelete.findViewById(R.id.textViewSubmit);
                textViewTitle.setText(context.getString(R.string.delete_image));
                textViewLabel.setText(context.getString(R.string.are_you_sure_you_want_to_delete_this_image));

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
                        deleteImageCallback.deleteImage(position);
                    }
                });
                dialogDelete.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewReport)
        ImageView imageViewReport;
        @BindView(R.id.imageViewDelete)
        ImageView imageViewDelete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
