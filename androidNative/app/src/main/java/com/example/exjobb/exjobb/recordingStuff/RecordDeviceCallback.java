package com.example.exjobb.exjobb.recordingStuff;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Button;

import com.example.exjobb.exjobb.cameraStuff.StateCallback;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by exjobb on 2017-03-23.
 */

public class RecordDeviceCallback extends CameraDevice.StateCallback {

    private RecordStateCallback stateCallback;
    private Handler handler;
    private SurfaceView recordView;
    private Context context;
    private CameraDevice camera;
    private Button recordButton;
    private MediaRecorder mediaRecorder;

    public RecordDeviceCallback(Context context, SurfaceView surface, Button captureButton, MediaRecorder mediaRecorder){
        this.context = context;
        this.recordView = surface;
        this.handler = new Handler();
        this.mediaRecorder = mediaRecorder;
        this.recordButton = captureButton;

    }

    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        this.camera = camera;

        try {
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            //mediaRecorder.setPreviewDisplay(recordView.getHolder().getSurface());
            mediaRecorder.setVideoSize(recordView.getWidth(),recordView.getHeight());
            //mediaRecorder.setVideoFrameRate(30);



            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/" + new Date() + ".mp4");
            mediaRecorder.prepare();
            //mediaRecorder.start();

            //  Prepare surfaces
            List<Surface> surfaces = new ArrayList<>();
            surfaces.add(recordView.getHolder().getSurface());
            surfaces.add(mediaRecorder.getSurface());

            stateCallback = new RecordStateCallback(context, recordView, recordButton, mediaRecorder);


            camera.createCaptureSession(surfaces, stateCallback, new Handler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        camera.close();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    @Override
    public void onError(@NonNull CameraDevice camera, int error){
        camera.close();
        mediaRecorder.release();
        mediaRecorder = null;
    }
}
