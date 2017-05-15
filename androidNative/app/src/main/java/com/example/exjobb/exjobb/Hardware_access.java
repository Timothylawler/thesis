package com.example.exjobb.exjobb;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Hardware_access extends AppCompatActivity {

    private Button longListBtn, contactBtn, cameraRecordBtn, cameraPhotoBtn;
    private FloatingActionButton contactFab, cameraFab, recordFab;
    private TextView gpsTextView, networkTextView, phoneStateTextView, carrierTextView, phoneSmsTextView;

    //  GPS
    private LocationManager locationManager;
    private LocationListener locationListener;
    //  PHONE STATE
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneListener;
    //  SMS
    private SmsListener smsListener;


    private final int GPS_UPDATE_FREQ = 1000*10*1; // 10 seconds

    int permFineLocation;

    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_access);

        gpsTextView = (TextView) findViewById(R.id.gpsTextView);
        networkTextView = (TextView) findViewById(R.id.networkInformation);

        phoneStateTextView = (TextView) findViewById(R.id.phoneStateTextView);
        carrierTextView = (TextView) findViewById(R.id.carrierTextView);
        phoneSmsTextView = (TextView) findViewById(R.id.phoneSmsTextView);

        contactBtn = (Button) findViewById(R.id.contactsBtn);
        cameraPhotoBtn = (Button) findViewById(R.id.cameraPhotoBtn);
        cameraRecordBtn = (Button) findViewById(R.id.cameraVideoBtn);
        longListBtn = (Button) findViewById(R.id.longListBtn);

        smsListener = new SmsListener(phoneSmsTextView);
        //  Register to listen to sms received broadcasts
        registerReceiver(smsListener, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        //  Set up action listeners
        setUpListeners();

        //  Get gps location
        doGps();

        //  Get network status
        doNetwork();

        //  Get carrier information
        doCarrier();

        //  Get phone status
        doPhoneStatus();

    }

    private void doCarrier() {
        CarrierProvider carrierProvider = new CarrierProvider();
        CarrierModel carrierModel = carrierProvider.getCarrier(getApplicationContext());
        String carrierText = carrierModel.carrierName + ", " + carrierModel.countryCode;
        this.carrierTextView.setText(carrierText);
    }

    private void doPhoneStatus() {
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void setUpListeners() {
        /**
         * Start contact intent
         */
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //  Launch new intent with contacts
            Intent contactIntent = new Intent(getApplicationContext(), activityContacts.class);
            startActivity(contactIntent);
            }
        });

        /**
         * Start camera intent
         */
        cameraPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Launch camera intent
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraIntent);
            }
        });

        cameraRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Launch record intent
                Intent recordIntent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(recordIntent);
            }
        });

        /**
         * Start long list intent
         */
        longListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent longListIntent = new Intent(getApplicationContext(), LongList.class);
            startActivity(longListIntent);
            }
        });

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
            //  Set text
            phoneStateTextView.setText(phoneState);
            }
        };

    }

    private void doNetwork() {
        //  Get access to connectivity manage
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            //  Get information of the current connected network
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                //  Network is connected
                //  get network type
                int networkType = networkInfo.getType();
                switch (networkType) {
                    case ConnectivityManager.TYPE_WIFI:
                        //  Wifi
                        final WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        if(wifiInfo != null && !wifiInfo.getSSID().isEmpty()) {
                            networkTextView.setText("WIFI: ssid:" + wifiInfo.getSSID());
                        } else{
                            networkTextView.setText("WIFI, SSID not available.");
                        }
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        networkTextView.setText("Mobile data");
                        break;
                    default:
                        networkTextView.setText("N/A");
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //  Handle requests granted here
        }
    }

    private void doGps(){
        //  get access to the systems location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new myLocationListener(gpsTextView);

        //  Check permission and start to request gps data for both cellular and gps
        permFineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permFineLocation != PackageManager.PERMISSION_GRANTED) {
            //  Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(getApplicationContext(), "No permission granted", Toast.LENGTH_SHORT).show();
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_UPDATE_FREQ, 0, locationListener);
            Toast.makeText(getApplicationContext(), "Got gps access", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_FREQ, 0, locationListener);
        }
    }

    /**
     * Broadcast reciver for receiving text messages
     * Reads the message and outputs the content to a textview passed to constructor
     */
    public static class SmsListener extends BroadcastReceiver{

        private TextView smsView;

        public SmsListener(){}
        public SmsListener(TextView smsView){
            this.smsView = smsView;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
                String messageBody = "";
                for(SmsMessage message: Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                    messageBody+=message.getMessageBody();
                }

                smsView.setText(messageBody);
            }
        }
    }

    public class myLocationListener implements LocationListener{

        TextView textView;
        public myLocationListener(TextView tw){
            this.textView = tw;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("location","Location: " + location);
            textView.setText("lat: " + Double.toString(location.getLatitude()) +
                    ", long: " + Double.toString(location.getLongitude()));
        }

        @Override
        public void onProviderDisabled(String provider) {
            textView.setText("GPS disabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            textView.setText("GPS enabled");
        }


    }
}
