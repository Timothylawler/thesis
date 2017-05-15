//
//  carrierInfo.swift
//  Tutorial
//
//  Created by Exjobb on 2017-04-06.
//  Copyright © 2017 Facebook. All rights reserved.
//

import Foundation;
import CoreTelephony;


@objc(CarrierInfo) class CarrierInfo: NSObject {
  
  @objc func getCarrierInfo(_ callback:RCTResponseSenderBlock){
    //NSLog( "Hello native");
    
    let telephonyNetwork = CTTelephonyNetworkInfo();
    let carrier = telephonyNetwork.subscriberCellularProvider!;
    
    var data = [[String: Any]]();
    data.append(["voip":carrier.allowsVOIP]);
    data.append(["name":carrier.carrierName ?? "N/A"]);
    data.append(["countryCode":carrier.isoCountryCode ?? "N/A"]);
    data.append(["mobileCountryCode":carrier.mobileCountryCode ?? "N/A"]);
    data.append(["mobileNetworkCode":carrier.mobileNetworkCode ?? "N/A"]);
    
    callback([data]);
    
    
  }
  
  
}
