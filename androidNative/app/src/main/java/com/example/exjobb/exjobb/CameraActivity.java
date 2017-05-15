package com.example.exjobb.exjobb;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.exjobb.exjobb.cameraStuff.CameraDeviceCallback;

import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String[] availableCameras;
    private SurfaceView cameraView;
    private ImageReader imageReader;
    //  Separated
    private Button cameraCaptureBtn;
    private CameraDeviceCallback cameraDeviceCallback;
    private Handler cameraHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //  Obtain camera manager instance
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        //  Bind views
        cameraView = (SurfaceView) findViewById(R.id.cameraSurfaceView);
        cameraCaptureBtn = (Button) findViewById(R.id.cameraCaptureBtn);

        //  Instantiate callback for the camera device
        cameraDeviceCallback = new CameraDeviceCallback(getApplicationContext(), cameraView, cameraCaptureBtn);

        //  Get list of available cameras on the device
        try {
            availableCameras = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (availableCameras != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                //  Fire up the camera, [0] should be back camera if available
                cameraManager.openCamera(availableCameras[0], cameraDeviceCallback, cameraHandler);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
