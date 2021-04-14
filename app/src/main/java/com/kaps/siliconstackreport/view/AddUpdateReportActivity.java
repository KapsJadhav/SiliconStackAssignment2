package com.kaps.siliconstackreport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.kaps.siliconstackreport.R;
import com.kaps.siliconstackreport.databinding.ActivityAddUpdateReportBinding;
import com.kaps.siliconstackreport.model.Callback.DeleteImageCallback;
import com.kaps.siliconstackreport.model.Report;
import com.kaps.siliconstackreport.model.helper.AppConstants;
import com.kaps.siliconstackreport.viewmodel.AddUpdateViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddUpdateReportActivity extends AppCompatActivity implements DeleteImageCallback {

    private static final int PICK_IMAGES = 111;
    private static final int CAPTURE_IMAGES = 222;
    private static final String TAG = "AddUpdateReportActivity";
    private ActivityAddUpdateReportBinding activityAddUpdateReportBinding;
    private List<Uri> pathList = new ArrayList<>();
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddUpdateReportBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_update_report);
        activityAddUpdateReportBinding.setViewModel(new AddUpdateViewModel(this));
        activityAddUpdateReportBinding.executePendingBindings();
        activityAddUpdateReportBinding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String sFrom = getIntent().getStringExtra(AppConstants.Keys.FROM);
        if (sFrom.equals(AppConstants.Status.UPDATE)) {
            report = getIntent().getParcelableExtra(AppConstants.Keys.REPORT);
            if (!report.getImagePaths().equals("")) {
                String sPath = "";
                if (report.getImagePaths().contains(",")) {
                    String[] imageUri = report.getImagePaths().split(",");
                    for (int i = 0; i < imageUri.length; i++) {
                        String sUri = imageUri[i];
                        Uri uri = Uri.parse(sUri);
                        pathList.add(uri);
                        sPath += uri + ",";
                        AppConstants.PrintLog("ReportAdapter", "Path : " + sUri);
                    }
                    ImageAdapter imageAdapter = new ImageAdapter(AddUpdateReportActivity.this, pathList);
                    imageAdapter.setDeleteImageCallback(this::deleteImage);
                    activityAddUpdateReportBinding.recyclerViewReportImages.setAdapter(imageAdapter);
                } else {
                    Uri uri = Uri.parse(report.getImagePaths());
                    pathList.add(uri);
                    sPath += uri + ",";
                    ImageAdapter imageAdapter = new ImageAdapter(AddUpdateReportActivity.this, pathList);
                    imageAdapter.setDeleteImageCallback(this::deleteImage);
                    activityAddUpdateReportBinding.recyclerViewReportImages.setAdapter(imageAdapter);
                }
                activityAddUpdateReportBinding.getViewModel().setsImagePaths(sPath);
            }
            activityAddUpdateReportBinding.getViewModel().setUpdate(true);
            activityAddUpdateReportBinding.getViewModel().setReport(report);
            activityAddUpdateReportBinding.editTextReport.setText(report.getReport());
            activityAddUpdateReportBinding.buttonSubmit.setText(getString(R.string.update_report));
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGES) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        Uri tempUri = Uri.parse(getGalleryRealPathFromURI(AddUpdateReportActivity.this, uri));
                        // display your images
                        pathList.add(tempUri);
                        AppConstants.PrintLog(TAG, "Image Uri : " + uri);
//                        imageView.setImageURI(uri);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    Uri tempUri = Uri.parse(getGalleryRealPathFromURI(AddUpdateReportActivity.this, uri));
                    pathList.add(tempUri);
                    AppConstants.PrintLog(TAG, "Image Uri : " + uri);
//                    imageView.setImageURI(uri);
                }
            } else if (requestCode == CAPTURE_IMAGES) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                File finalFile = new File(getRealPathFromURI(tempUri));
                pathList.add(tempUri);
            }
            ImageAdapter imageAdapter = new ImageAdapter(this, pathList);
            imageAdapter.setDeleteImageCallback(this::deleteImage);
            activityAddUpdateReportBinding.recyclerViewReportImages.setAdapter(imageAdapter);
            String sPath = "";
            for (int i = 0; i < pathList.size(); i++) {
                sPath += pathList.get(i) + ",";
            }
            AppConstants.PrintLog(TAG, sPath.substring(0, sPath.length() - 1));
            activityAddUpdateReportBinding.getViewModel().setsImagePaths(sPath.substring(0, sPath.length() - 1));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public String getGalleryRealPathFromURI(Context context, Uri contentUri) {
        OutputStream out;
        File file = new File(getFilename(context));

        try {
            if (file.createNewFile()) {
                InputStream iStream = context != null ? context.getContentResolver().openInputStream(contentUri) : context.getContentResolver().openInputStream(contentUri);
                byte[] inputData = getBytes(iStream);
                out = new FileOutputStream(file);
                out.write(inputData);
                out.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String getFilename(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "patient_data");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }

    @Override
    public void deleteImage(int position) {
        if (pathList != null && pathList.size() != 0) {
            pathList.remove(position);
            ImageAdapter imageAdapter = new ImageAdapter(this, pathList);
            activityAddUpdateReportBinding.recyclerViewReportImages.setAdapter(imageAdapter);
            String sPath = "";
            for (int i = 0; i < pathList.size(); i++) {
                sPath += pathList.get(i) + ",";
            }
            AppConstants.PrintLog(TAG, sPath.substring(0, sPath.length() - 1));
            activityAddUpdateReportBinding.getViewModel().setsImagePaths(sPath.substring(0, sPath.length() - 1));
        }
    }
}