import { Component, OnInit, ElementRef, ViewChild } from "@angular/core";

import * as camera from "nativescript-camera";
import { Image } from "ui/image";




@Component({
  selector: "my-app",
  templateUrl: "pages/cameraPhoto/cameraPhoto.html",
  styleUrls: ["pages/cameraPhoto/cameraPhoto-common.css"]
})
export default class CameraPhotoPage implements OnInit{

	constructor(){

	}

	ngOnInit(){
		var isAvailable = camera.isAvailable(); 
		if(isAvailable){
			this.requestCameraPermission();
		}
	}

	requestCameraPermission(){
		camera.requestPermissions();
		this.startCamera();
	}

	startCamera(){
		camera.takePicture({saveToGallery: true})
		.then((imageAsset)=>{
			var image = new Image();
			image.src = imageAsset;
			this.startCamera();
		})
		.catch(this.handleErrors);
	}

	handleErrors(error){
		alert("Error using camera");
	}


}