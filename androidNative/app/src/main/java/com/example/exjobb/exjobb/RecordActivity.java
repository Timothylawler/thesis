package com.example.exjobb.exjobb;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import com.example.exjobb.exjobb.cameraStuff.CameraDeviceCallback;
import com.example.exjobb.exjobb.cameraStuff.CaptureCallback;
import com.example.exjobb.exjobb.recordingStuff.RecordDeviceCallback;
import com.example.exjobb.exjobb.recordingStuff.RecordStateCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private SurfaceView recordView;
    private Button recordCaptureBtn;
    private RecordDeviceCallback cameraDeviceCallback;
    private String[] availableCameras;
    private MediaRecorder mediaRecorder;
    private CameraDevice mCamera;
    private CaptureRequest captureRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


        //  Bind views
        recordView = (SurfaceView) findViewById(R.id.recordSurface);
        recordCaptureBtn = (Button) findViewById(R.id.recordCaptureBtn);


    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera(recordView.getWidth(), recordView.getHeight());
    }

    private void openCamera(int width, int height) {
        //  Obtain camera manager instance
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);



        //  Get list of available cameras on the device
        try {
            availableCameras = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //  Check for permission and open the camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            mediaRecorder = new MediaRecorder();
            //  Instantiate callback for the camera device
            cameraDeviceCallback = new RecordDeviceCallback(getApplicationContext(), recordView, recordCaptureBtn, mediaRecorder);

            cameraManager.openCamera(availableCameras[0], cameraDeviceCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(getApplicationContext(), "Cannot open camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeCamera(){
        mediaRecorder.release();
        mediaRecorder = null;
    }






}
