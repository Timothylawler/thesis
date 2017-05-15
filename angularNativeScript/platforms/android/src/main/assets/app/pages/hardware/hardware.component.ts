import { Component, OnInit, OnDestroy, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";

import {isAndroid, isIOS} from "platform";

import connectivity = require("connectivity");						//	Network
import geolocation = require("nativescript-geolocation");	//	Geolocation
import {Accuracy} from "ui/enums";

const CarrierProvider = require("../../native-modules/carrier-provider/");
const smsListener = require("../../native-modules/sms-listener/");
const telephoneListener = require("../../native-modules/phone-listener/");


@Component({
  selector: "my-app",
  templateUrl: "pages/hardware/hardware.html",
  styleUrls: ["pages/hardware/hardware-common.css", "pages/hardware/hardware.css"]
})
export default class HardwarePage implements OnInit, OnDestroy{

	networkInformation: string = "N/A"; 
	gpsData: string = "N/A"; 
	callState: string = "Idle"; 
	textMessage: string = "N/A";
	carrierInformation: Object = {}; 

	geoLocationWatchId;


	constructor(private router: Router){  }

	ngOnInit(){
		this.getNetworkInformation();
		this.getCarrier();
		this.askForLocationPermission();
		this.startPhoneListener();
		if(isAndroid){
			this.startSmsListener();
		} 
	}

	ngOnDestroy(){
		this.cleanUp();
	}

	cleanUp(){
		geolocation.clearWatch(this.geoLocationWatchId);
		if(isAndroid){
			smsListener.clearWatch();
		}
	}

	startPhoneListener(){
		console.log("Starting phone listener");
		telephoneListener.registerListener()
			.then((response) => {
				console.log("call state changed");
				console.log(response.phoneState);
				this.callState = response.phoneState;
				
				setTimeout(()=>this.startPhoneListener(), 1000)	//	Start a new listener after 1 second
			})
			.catch((error)=>{
				console.log("phoneStateError: ", error.error);
			})
	
	}

	startSmsListener(){
		//	Call to start sms listener and repeat when received response
		if(isAndroid){
			smsListener.registerListener()
				.then((response) => {
					this.textMessage = response.message;
				})
				.catch((error)=> alert(error))
				.then(()=> this.startSmsListener());
		}
	}

	getCarrier(){
		CarrierProvider.getCarrierInformation().then((result) => {
			this.carrierInformation = result.carrier;
		});
	}

	askForLocationPermission(){
		if (!geolocation.isEnabled()) {
        geolocation.enableLocationRequest();
    }
		this.listenForLocation();
	}

	/**
	 *	listenForLocation
	 *	Sets up a watcher for nativescript-geolocations' geolocation to watch for GPS location
	 *	Accuracy is set to high to enable GPS and update rate is set to 10 seconds.
	 */
	listenForLocation(){
		this.geoLocationWatchId = geolocation.watchLocation(
    (loc) => {
        if (loc) {
					this.gpsData = "Lat: " + loc.latitude.toFixed(3) + " long: " + loc.longitude.toFixed(3);
        }
    }, 
    (error) => {
        console.log("Error: " + error.message);
    }, 
    {desiredAccuracy: Accuracy.high ,minimumUpdateTime : 1000 * 10});	//	Update every 10 seconds
	}

	getNetworkInformation(){
		let connectionType = connectivity.getConnectionType();
		console.log("ConnectionType: ", connectionType);
		switch(connectionType){
			case connectivity.connectionType.wifi:
				//	Wifi connection
				this.networkInformation = "WIFI";
				break;
			case connectivity.connectionType.mobile:
				//	cellular network
				this.networkInformation = "Mobile network";
				break;
			case connectivity.connectionType.none:
				//	No connection
				this.networkInformation = "No network";
				break;
		}
	}

	/*	View button responders */
	longListBtnPressed(){
		this.cleanUp();
		this.router.navigate(["/list"]);
	}

	contactBtnPressed(){
		this.router.navigate(["/contacts"]);
		
	}

	videoCameraBtnPressed(){
		this.router.navigate(["/cameraRecord"]);

	}

	cameraBtnPressed(){
		this.router.navigate(["/cameraPhoto"]);

	}

	/* 	/View button responders */

}