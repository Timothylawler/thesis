package com.tutorial;

import android.view.ViewGroup;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import camera.CameraView;


public class CameraViewManager extends SimpleViewManager<ViewGroup>{

    public static final String CLASS_NAME = "RCTCameraView";

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @Override
    protected ViewGroup createViewInstance(ThemedReactContext reactContext) {
        CameraView cameraView = new CameraView(reactContext);

        return cameraView.getViewGroup();
    }




}
