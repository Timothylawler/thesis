//
//  ViewController.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-27.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import UIKit;
//  Location service
import CoreLocation;
//  CXCall - Call status
import CallKit;
import SystemConfiguration.CaptiveNetwork;
import os.log;

class ViewController: UIViewController, CLLocationManagerDelegate, CXCallObserverDelegate{

    //  MARK: Properties
    @IBOutlet weak var gpsDataLabel: UILabel!
    @IBOutlet weak var networkInformationLabel: UILabel!
    @IBOutlet weak var phoneStateLabel: UILabel!
    @IBOutlet weak var phoneNetworkLabel: UILabel!
    @IBOutlet weak var textMessageLabel: UILabel!
    @IBOutlet weak var carrierInfoLabel: UILabel!
    
    //  Location manager for GPS
    let locationManager = CLLocationManager();
    let telephonyManager = TelephonyManager();
    let callManager = CXCallObserver();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        //  Start GPS
        startGpsListener();
        
        //  Get cellInfo
        doCarrierStuff();
        
        //  Set call delegate
        callManager.setDelegate(self, queue: nil);
        
        //  Get ssid
        getSSID();
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //  Mark: Private methods
    private func doCarrierStuff(){
        let carrier = telephonyManager.getCarrier();
        
        carrierInfoLabel.text = carrier.carrierName ?? "Carrier N/A";
        carrierInfoLabel.text?.append(" ,\(carrier.countryCode ?? "country N/A")");
    }
    
    
    /*  ------------ GPS ------------ */
    private func startGpsListener(){
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
    
    /*  Called when GPS has not been allowed or non existing
     *  Alerts the user that the GPS is not available
     */
    private func alertGPSnotAvailable(){
        let alert = UIAlertController(title: "Not Available", message: "GPS is not available", preferredStyle: .alert);
        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil);
        
        alert.addAction(okAction);
        
        present(alert, animated: true, completion: nil);
        return;
    }
    /*  ------------ /GPS ------------ */
    
    private func getSSID(){
        
        
        if let availableInterfaces = CNCopySupportedInterfaces() as? NSArray{
            for interface in availableInterfaces{
                if let interfaceInfo = CNCopyCurrentNetworkInfo(interface as! NSString) as? NSDictionary{
                    
                    if let ssid = interfaceInfo[kCNNetworkInfoKeySSID as String] as? String{
                        networkInformationLabel.text = "wifi: " + ssid;
                    }
                }
                else {
                    networkInformationLabel.text = "Wifi not available.";
                }
            }
        }
    }
    
    //  MARK: CLLocationDelegate
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        //  Get the gps position and assign the lat and long value to gpsDataLabel
        let position = locations.last!;
        
        gpsDataLabel.text = "Lat: \(position.coordinate.latitude), Long: \(position.coordinate.longitude)";
    }

    //  MARK: Actions
    @IBAction func contactButtonPressed(_ sender: UIButton) {
        performSegue(withIdentifier: "ShowContacts", sender: nil);
    }
    
    @IBAction func longListButtonPressed(_ sender: UIButton) {
        performSegue(withIdentifier: "ShowLongList", sender: nil);
    }

    @IBAction func captureImageButtonPressed(_ sender: UIButton) {
        performSegue(withIdentifier: "ShowCameraPhoto", sender: nil);
    }
    @IBAction func recordVideoButtonPressed(_ sender: UIButton) {
        performSegue(withIdentifier: "ShowRecord", sender: nil);
    }
    
    //  MARK: CXCallObserverDelegate
    func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
        if(call.hasEnded){
            //  Nothing
            phoneStateLabel.text = "No Call happening.";
        }
        
        if(call.isOutgoing && !call.hasConnected){
            //  Dialing out
            phoneStateLabel.text = "Calling from this unit.";
        }
        if(!call.isOutgoing && !call.hasConnected && !call.hasEnded){
            //  Call is incoming
            phoneStateLabel.text = "Incoming call.";
        }
        if(call.hasConnected && !call.hasEnded){
            //  Call is ongoing
            phoneStateLabel.text = "Call is ongoing.";
        }
    }
    
    
}




















