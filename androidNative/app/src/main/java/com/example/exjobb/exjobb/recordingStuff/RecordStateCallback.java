package com.example.exjobb.exjobb.recordingStuff;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by exjobb on 2017-03-23.
 */

public class RecordStateCallback extends CameraCaptureSession.StateCallback {

    private SurfaceView recordView;
    private Context context;
    private MediaRecorder mediaRecorder;
    private Button recordButton;
    private CaptureCallback captureCallback;

    public RecordStateCallback(Context context, SurfaceView surface, Button captureButton, MediaRecorder mediaRecorder){
        this.recordView = surface;
        this.recordButton = captureButton;
        this.context = context;
        this.mediaRecorder = mediaRecorder;
        captureCallback = new CaptureCallback();
    }

    @Override
    public void onConfigured(@NonNull CameraCaptureSession session) {
        System.out.print(1);



        //mediaRecorder.prepare();

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                mediaRecorder.reset();

            }
        });

        CaptureRequest.Builder builder = null;
        try {
            builder = session.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            //builder.addTarget(mediaRecorder.getSurface());
            builder.addTarget(recordView.getHolder().getSurface());

            session.setRepeatingRequest(builder.build(), captureCallback, new Handler());
            //mediaRecorder.start();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReady(@NonNull CameraCaptureSession session) {
        super.onReady(session);


    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

    }
}
