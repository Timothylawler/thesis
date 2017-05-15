//
//  TelephonyManager.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-28.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import Foundation;
import CoreTelephony;
import CallKit

class TelephonyManager: NSObject {
    
    var carrier: CTCarrier;
    var telephonyNetwork: CTTelephonyNetworkInfo;
    

    override init() {
        self.telephonyNetwork = CTTelephonyNetworkInfo();
        self.carrier = telephonyNetwork.subscriberCellularProvider!;
    }
    
    
    func getCarrier() -> CarrierModel{
        
        let model = CarrierModel();
        model.allowVoip = carrier.allowsVOIP;
        model.carrierName = carrier.carrierName;
        model.countryCode = carrier.isoCountryCode;
        model.mobileCountryCode = carrier.mobileCountryCode;
        model.mobileNetworkCode = carrier.mobileNetworkCode;
        
        return model;
    }
    
    
}

class CarrierModel{
    var allowVoip: Bool?;
    var carrierName: String?;
    var countryCode: String?;
    var mobileCountryCode: String?;
    var mobileNetworkCode: String?;
}
