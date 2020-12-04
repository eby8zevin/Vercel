package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class ScannerkasirActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int currentApiVersion = Build.VERSION.SDK_INT;

    public ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannerkasir);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("QR or Barcode Scanner");

        if (this.currentApiVersion >= 23) {
            requestCameraPermission();
        }
        ZXingScannerView zXingScannerView = new ZXingScannerView(this);
        scannerView = zXingScannerView;
        setContentView(zXingScannerView);
        scannerView.startCamera();
        scannerView.setResultHandler(this);
    }

    public void onResume() {
        super.onResume();
        this.scannerView.setResultHandler(this);
        this.scannerView.startCamera();
    }

    public void onDestroy() {
        super.onDestroy();
        this.scannerView.stopCamera();
    }

    public void handleResult(Result result) {
        KasirActivity.etSearch.setText(result.getText());
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        onBackPressed();
    }

    private void requestCameraPermission() {
        Dexter.withContext(ScannerkasirActivity.this)
                .withPermission("android.permission.CAMERA")
                .withListener(new PermissionListener() {
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ScannerkasirActivity.this.scannerView = new ZXingScannerView(ScannerkasirActivity.this);
                        ScannerkasirActivity scannerKasirActivity = ScannerkasirActivity.this;
                        scannerKasirActivity.setContentView(scannerKasirActivity.scannerView);
                        ScannerkasirActivity.this.scannerView.startCamera();
                        ScannerkasirActivity.this.scannerView.setResultHandler(ScannerkasirActivity.this);
                    }

                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Toasty.info(ScannerkasirActivity.this, R.string.camera_permission, 1).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }

}