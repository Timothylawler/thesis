package camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.media.ImageReader;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by exjobb on 2017-04-11.
 */

public class CameraDeviceCallback extends CameraDevice.StateCallback  {


    private SurfaceView surface;
    private StateCallback cameraStateCallback;
    private Handler handler;
    private Context context;
    private ImageReader reader;

    public CameraDeviceCallback(Context context, SurfaceView surface, Button captureButton){
        this.context = context;
        this.surface = surface;
        this.handler = new Handler();
        reader = ImageReader.newInstance(400,800, ImageFormat.JPEG, 1);
        cameraStateCallback = new StateCallback(context, surface, reader, captureButton);
    }

    /**
     * onOpened: Create one surface for viewing the camera preview and one surface to hold the
     *  captured image. Then start the capture session.
     * @param camera
     */
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        List<Surface> surfaces = new ArrayList<>();

        surfaces.add(surface.getHolder().getSurface());
        surfaces.add(reader.getSurface());
        try {
            camera.createCaptureSession(surfaces, cameraStateCallback, new Handler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleanup when disconnecting
     * @param camera    active camera disconnected
     */
    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        camera.close();
    }

    /**
     * Error opening the camera (shutter?)
     *  Prompt the user with a message
     * @param camera    the problematic camera
     * @param error     Error
     */
    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
        Toast.makeText(context, "Could not open camera", Toast.LENGTH_SHORT).show();
    }
}
