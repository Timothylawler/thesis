package com.example.exjobb.exjobb;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by exjobb on 2017-05-04.
 */

public class CarrierProvider {

    //  Returns a CarrierModel with operator name and country code
    public CarrierModel getCarrier(Context context){

        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return new CarrierModel(telephonyManager.getSimOperatorName(), telephonyManager.getSimCountryIso());
    }
}
