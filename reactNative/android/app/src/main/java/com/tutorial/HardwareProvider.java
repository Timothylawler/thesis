package com.tutorial;

import android.Manifest;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;

/**
 * Created by exjobb on 2017-04-12.
 */

public class HardwareProvider extends ReactContextBaseJavaModule {

    public static final String CLASS_NAME = "RCTHardwareProvider";
    private ReactApplicationContext context;
    private static final String TEXT_EVENT = "TEXT_MESSAGE";
    private static final String PHONE_EVENT = "PHONE_STATE";
    private static final String GPS_EVENT = "GPS_CHANGE";

    //  Keys
    private static final String PHONE_STATE_KEY = "PHONESTATE";
    private static final String TEXT_KEY = "TEXTMESSAGE";

    //  PHONE STATE
    private TelephonyManager telephonyManager = null;
    private PhoneStateListener phoneListener;

    //  Text message
    private SmsListener smsListener;

    //  GPS
    private LocationManager locationManager;
    private LocationListener locationListener; //   Custom class for listening on gps changes
    private final int GPS_UPDATE_FREQ = 1000*10*1; // 10 seconds


    public HardwareProvider(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @ReactMethod
    public void getCarrier(Callback successCallback){
        if(telephonyManager == null) {
            telephonyManager = (TelephonyManager) getReactApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        }
        WritableMap arguments = Arguments.createMap();
        arguments.putString("name", telephonyManager.getSimOperatorName());
        arguments.putString("countryCode", telephonyManager.getSimCountryIso());


        successCallback.invoke(arguments);
    }

    @ReactMethod
    public void listenOnGPS(){
        //  get access to the systems location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new myLocationListener(context);

        //  Check permission and start to request gps data for both cellular and gps
        int permFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permFineLocation == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_UPDATE_FREQ, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_FREQ, 0, locationListener);
        }
    }

    @ReactMethod
    public void listenOnText(){
        smsListener = new SmsListener(context);
        //  Register the broadcast receiver
        context.registerReceiver(smsListener, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }

    /**
     * Cleanup for what react native cant handle
     */
    @ReactMethod
    public void unmount(){
        context.unregisterReceiver(smsListener);
        locationManager.removeUpdates(locationListener);


        //  Null the objects to recycle them faster
        locationListener = null;
        smsListener = null;
        telephonyManager = null;
        phoneListener = null;
    }

    @ReactMethod
    public void listenOnPhone(){
        /**
         * Cellular states
         */
        phoneListener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
            String phoneState = "N/A";
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //  Nothing special
                    phoneState = "Idle.";
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //  Incoming call, Should prob get the number here too
                    phoneState = "Incoming call: " + incomingNumber;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //  Call on going, dno if we get the number from incoming number or not yet.
                    //TODO check if incoming number displays number off hook
                    phoneState = "Ongoing call: " + incomingNumber;
                    break;
            }
            //  emit event
            WritableMap arguments = Arguments.createMap();
            arguments.putString(PHONE_STATE_KEY, phoneState);
            sendEvent(PHONE_EVENT, arguments);
            }
        };

        //  Start listener for phone state
        if(telephonyManager == null) {
            telephonyManager = (TelephonyManager) getReactApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        }
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    /**
     * Used to emit events that the js can listen for
     * @param eventName
     * @param params
     */
    private void sendEvent(String eventName, @Nullable WritableMap params){
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    /**
     * Broadcast reciver for receiving text messages
     * Reads the message and outputs the content to a textview passed to constructor
     */
    public static class SmsListener extends BroadcastReceiver {

        ReactApplicationContext context;

        public SmsListener(ReactApplicationContext context){
            this.context = context;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
                String messageBody = "";
                for(SmsMessage message: Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                    messageBody+=message.getMessageBody();
                }
                WritableMap arguments = Arguments.createMap();
                arguments.putString(TEXT_KEY, messageBody);
                sendEvent(TEXT_EVENT, arguments);
            }
        }

        private void sendEvent(String eventName, @Nullable WritableMap params){
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }

    public class myLocationListener implements LocationListener {

        private ReactApplicationContext context;
        public myLocationListener(ReactApplicationContext context){
            this.context = context;
        }

        @Override
        public void onLocationChanged(Location location) {
            WritableMap arguments = Arguments.createMap();
            arguments.putString("lat", Double.toString(location.getLatitude()));
            arguments.putString("long", Double.toString(location.getLongitude()));
            sendEvent(GPS_EVENT, arguments);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        private void sendEvent(String eventName, @Nullable WritableMap params){
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }


    }
}
