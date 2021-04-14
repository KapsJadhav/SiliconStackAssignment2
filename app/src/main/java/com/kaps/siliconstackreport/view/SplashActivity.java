package com.kaps.siliconstackreport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kaps.siliconstackreport.R;
import com.kaps.siliconstackreport.databinding.ActivitySplashBinding;
import com.kaps.siliconstackreport.viewmodel.SplashViewModel;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 123;
    private ActivitySplashBinding activitySplashBinding;
    private Snackbar snackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        activitySplashBinding.setViewModel(new SplashViewModel(this));
        activitySplashBinding.executePendingBindings();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted && readStorageAccepted) {
                        activitySplashBinding.getViewModel().onProgress();
                    } else {
                        if (snackbar != null) {
                            snackbar.dismiss();
                        }
                        snackbar = Snackbar
                                .make(activitySplashBinding.constraintLayout, "You need to allow access the permissions", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA,READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                    PERMISSION_REQUEST_CODE);
                                        } else {
                                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{CAMERA,
                                                    READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                        }
                                    }
                                });
                        snackbar.setActionTextColor(Color.YELLOW);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                        return;
                    }
                }
                break;
        }
    }
}