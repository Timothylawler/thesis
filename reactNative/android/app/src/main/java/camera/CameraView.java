package camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by exjobb on 2017-04-10.
 */

public class CameraView {
    private Context context;

    private CameraManager cameraManager;
    private CameraDevice currentCameraDevice;
    private SurfaceView previewLayer;
    private Button captureButton;
    private CameraDeviceCallback cameraDeviceCallback;

    private ViewGroup mViewGroup;
    private LinearLayout mLayout;



    public CameraView(Context context) {
        this.context = context;
        this.mViewGroup = createScreen(context);
        setUpCamera();
    }

    public ViewGroup getViewGroup(){
        return mViewGroup;
    }

    private void setUpCamera(){
        //  Obtain camera manager instance
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String [] availableCameras = null;

        //  Get list of available cameras on the device
        try {
            availableCameras = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //  Instantiate callback for the camera device
        cameraDeviceCallback = new CameraDeviceCallback(context, previewLayer, captureButton);

        if (availableCameras != null) {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
                cameraManager.openCamera(availableCameras[0], cameraDeviceCallback, new Handler());

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private ViewGroup createLayout(Context context){
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setWeightSum(6);
        this.mLayout = ll;
        return ll;
    }

    private ViewGroup createScreen(Context context){
        ViewGroup layout = createLayout(context);
        //  Set up views
        previewLayer = new SurfaceView(context);
        captureButton = new Button(context);
        captureButton.setText("Snap");

        previewLayer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5));
        captureButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        layout.addView(previewLayer, 0);
        layout.addView(captureButton, 1);
        return layout;
    }

}
