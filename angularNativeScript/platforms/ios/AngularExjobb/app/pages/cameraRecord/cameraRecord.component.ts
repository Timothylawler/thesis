import { Component, OnInit, ElementRef, ViewChild } from "@angular/core";

import { Image } from "ui/image";

var cameraRecord = require("../../native-modules/cameraVideo/");


@Component({
  selector: "my-app",
  templateUrl: "pages/cameraRecord/cameraRecord.html",
  styleUrls: ["pages/cameraPhoto/cameraPhoto-common.css"]
})
export default class CameraRecordPage implements OnInit{

	recordButtonText:string;
	isRecording:boolean;

	constructor(){

	}

	ngOnInit(){
		this.isRecording = false;
		this.recordButtonText = "Start";
		cameraRecord.initCamera().then(()=>{
			cameraRecord.openCamera();
		}).catch((error)=>{
			alert("Error starting camera, " + error.error);
		});

		
	}

	closeCameraButtonClicked(){
		cameraRecord.closeCamera();
	}

	recordButtonClicked(){
		if(this.isRecording){
			this.stopRecording();
		}
		else{
			this.startRecording();
		}
	}

	startRecording(){
		this.recordButtonText = "Stop";
		this.isRecording = true;
		cameraRecord.startCapturing().then(()=>{
			console.log("capture stopped");
		}).catch((error) => {
			alert("Error capturing video, " + error.error);
		});
	}

	stopRecording(){
		this.recordButtonText = "Start";
		this.isRecording = false;
		//	Hopefully this does not crash
		cameraRecord.stopCapturing();
	}





}