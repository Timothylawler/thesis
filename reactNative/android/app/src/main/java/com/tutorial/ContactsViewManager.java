package com.tutorial;

import android.content.Context;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import camera.CameraView;

/**
 * Created by exjobb on 2017-04-11.
 */

public class ContactsViewManager extends SimpleViewManager<ViewGroup> {

    public static final String CLASS_NAME = "RCTContactsView";
    private Context context;

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @Override
    protected ViewGroup createViewInstance(ThemedReactContext reactContext) {
        ContactsView contactsView = new ContactsView(reactContext);

        return contactsView.getViewGroup();
    }
}
