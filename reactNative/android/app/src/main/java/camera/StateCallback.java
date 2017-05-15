package camera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * Created by exjobb on 2017-04-11.
 */

public class StateCallback extends CameraCaptureSession.StateCallback {

    private SurfaceView surface;
    private ImageReader reader;
    private Button captureButton;
    boolean prev = true;
    private CaptureCallback captureCallback;
    private Handler handler;
    private Context context;

    public StateCallback (Context context, SurfaceView surface, ImageReader reader, Button captureButton){
        this.context = context;
        this.surface = surface;
        this.captureButton = captureButton;
        captureCallback = new CaptureCallback(surface, reader);
        this.reader = reader;
        handler = new Handler();
    }

    @Override
    public void onConfigured(@NonNull final CameraCaptureSession session) {


        /**
         * Prepare Surface and allocate buffer
         */
        try {
            session.prepare(surface.getHolder().getSurface());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        /**
         * Set up listener for the image reader
         * Listens for available images
         */
        reader.setOnImageAvailableListener(imageListener, new Handler());

        /**
         * Listen for capture event
         */
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CaptureRequest.Builder snapBuilder =
                            session.getDevice().createCaptureRequest(
                                    CameraDevice.TEMPLATE_STILL_CAPTURE);
                    //  Dont know if we want to add the surface view as a target too.
                    //snapBuilder.addTarget(surface.getHolder().getSurface());
                    snapBuilder.addTarget(reader.getSurface());
                    prev = false;
                    session.stopRepeating();
                    session.capture(snapBuilder.build(), captureCallback, handler);

                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*
     *  Called whenever there is an image ready on the image reader
     *  Only called when taking a snap shot
     *  Reads the loaded image and calls to save it as "picture.jpg"
     */
    private ImageReader.OnImageAvailableListener imageListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            if(null != image) {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + new Date() + ".jpg");
                ImageSaver imageSaver = new ImageSaver(image, file);
                imageSaver.run();
            }
            //  Dno if close is needed here since image saver closes the image
            image.close();
        }
    };

    /**
     * Shows a preview of the camera if a picture has not been taken
     * @param session
     */
    @Override
    public void onReady(@NonNull CameraCaptureSession session) {
        super.onReady(session);
        if(prev) {
            try {
                CaptureRequest.Builder prevBuilder = session.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                prevBuilder.addTarget(surface.getHolder().getSurface());
                session.setRepeatingRequest(prevBuilder.build(), null, handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActive(@NonNull CameraCaptureSession session) {
        super.onActive(session);
    }

    /**
     *  Cleanup
     * @param session   Camera session
     */
    @Override
    public void onClosed(@NonNull CameraCaptureSession session) {
        super.onClosed(session);
        try {
            session.stopRepeating();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Could not open the camera
     *  Prompt the user with a toast
     * @param session   camera session
     */
    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        Toast.makeText(context, "Could not capture", Toast.LENGTH_SHORT).show();
    }
}
