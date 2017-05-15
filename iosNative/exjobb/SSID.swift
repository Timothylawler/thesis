//
//  SSID.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-27.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import Foundation
import SystemConfiguration.CaptiveNetwork;

class SSID{
    
    func getSSID() -> [String] {
        guard let interfaceNames = CNCopySupportedInterfaces() as? [String] else {
            return [];
        }
        
        return interfaceNames.flatMap(name in guard let info = CNCopyCurrentNetworkInfo(name as CFSString) as ?
        )
        
        
    }
}
