package com.example.exjobb.exjobb.cameraStuff;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.SurfaceView;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * Created by exjobb on 2017-03-22.
 */

public class CaptureCallback extends CameraCaptureSession.CaptureCallback {

    private SurfaceView surface;
    private ImageSaver imageSaver;
    private ImageReader reader;

    /* SHOULD PROBABLY SAVE THE IMAGE HERE */
    /*  CHECK THE REQEUST SO THAT IT IS STILL */

    public CaptureCallback(SurfaceView surface, ImageReader reader) {
        super();
        this.surface = surface;
        this.reader = reader;
    }

    @Override
    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
        super.onCaptureStarted(session, request, timestamp, frameNumber);
    }

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        Image image = null;
        image = reader.acquireLatestImage();
        int h = reader.getHeight();
        int w = reader.getWidth();
        reader.setOnImageAvailableListener(imageListener, new Handler());

        //imageSaver = new ImageSaver(image, file);
        //imageSaver.run();
    }

    //  Will be called whenever the user takes a snap shot
    private ImageReader.OnImageAvailableListener imageListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            File file = new File(Environment.getExternalStorageDirectory()+ "/picture.jpg");
            ImageSaver imageSaver = new ImageSaver(image, file);
            imageSaver.run();
        }
    };



}
