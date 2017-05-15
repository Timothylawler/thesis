//
//  LocationController.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-27.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import UIKit;
import CoreLocation;


class LocationController: NSObject, CLLocationManagerDelegate {
    //  MARK: Properties
    private var locationManager:CLLocationManager = CLLocationManager();
    
    override init() {
        super.init();
        
        locationManager.delegate = self;
        locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
        locationManager.distanceFilter = 10;
        
        locationManager.requestWhenInUseAuthorization();
        locationManager.startUpdatingLocation();
        
        
    }
    
    private func startUpdate(){
        let status = CLLocationManager.authorizationStatus();
        
        switch(status){
        case .notDetermined:
            locationManager.requestWhenInUseAuthorization();
            break;
            
        case .restricted:
            alertGPSnotAvailable();
            break;
        case .denied:
            alertGPSnotAvailable();
            break;
            
        default:
            locationManager.delegate = self;
            locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
            locationManager.distanceFilter = 10;
            locationManager.startUpdatingLocation();
            break;
        }
    }
    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
    }
    
}
