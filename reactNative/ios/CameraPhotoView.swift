//
//  CameraPhotoView.swift
//  Tutorial
//
//  Created by Exjobb on 2017-04-10.
//  Copyright © 2017 Facebook. All rights reserved.
//

import UIKit
import AVFoundation;

@available(iOS 10.0, *)
class CameraPhotoView: UIView, AVCapturePhotoCaptureDelegate {
  
  //  MARK: Properties
  var captureSession: AVCaptureSession?;
  var activeCaptureDevice: AVCaptureDevice?;
  var captureInput: AVCaptureInput?;
  var captureOutput: AVCapturePhotoOutput?;
  var previewLayer: AVCaptureVideoPreviewLayer?;
  var captureConnection: AVCaptureConnection?;
  var availableFrame: CGRect?;
  

  override init(frame: CGRect) {
    super.init(frame: frame);
    self.availableFrame = frame;
    initCamera();
    
  }
  
  
  
  private func initCamera(){
    captureSession = AVCaptureSession();
    //  No need to continue if we cant preset photos
    guard captureSession!.canSetSessionPreset(AVCaptureSessionPresetPhoto) else {
      //  Cant capture photo
      fatalError("Get a phone that can capture photos please");
    }
    captureSession!.sessionPreset = AVCaptureSessionPresetPhoto;
    
    //  Check for devices with built in wide angle camera (kinda standard for taking photos or video) and also the back camera.
    let availableDevices = AVCaptureDeviceDiscoverySession.init(deviceTypes: [AVCaptureDeviceType.builtInWideAngleCamera], mediaType: AVMediaTypeVideo, position: .back).devices;
    
    guard availableDevices!.count > 0 else {
      //  This should never hit because of prev guard but hey. Who am i to decide that
      fatalError("How did you end up here?");
    }
    
    //  Take the first device as the one we want to use
    activeCaptureDevice = availableDevices![0]; 

    
    //  Attempt to bind input and add to the session
    do {
      //  Input
      captureInput = try AVCaptureDeviceInput.init(device: activeCaptureDevice);
      
      if(captureSession!.canAddInput(captureInput)){
        captureSession?.addInput(captureInput);
      } else {
        //  I believe this is how you throw exceptions manually in swift?
        NSException(name: .objectNotAvailableException, reason: "Cant add input device", userInfo: nil).raise();
      }
      
      //  Output
      captureOutput = AVCapturePhotoOutput();
      if(captureSession!.canAddOutput(captureOutput)){
        captureSession?.addOutput(captureOutput);
      } else {
        NSException(name: .objectNotAvailableException, reason: "Cant add input device", userInfo: nil).raise();
      }
      //  Preview layer
      previewLayer = AVCaptureVideoPreviewLayer.init(session: captureSession);
      
      
      
      previewLayer?.frame = CGRect(x: 0, y: 0, width: 350, height: 300);
      
      let prevView = UIView(frame: previewLayer!.frame);
      prevView.layer.addSublayer(previewLayer!);
      prevView.isUserInteractionEnabled = false;
     
      
      //  Add the layers to the screen and start projecting
      //self.addSubview(prevView);
      self.layer.addSublayer(previewLayer!);
      
      captureSession?.startRunning();
      
      
      
    } catch {
      
    }

  }
  
  
  public func captureButtonPressed(){
    //  Capture image
    captureOutput?.capturePhoto(with: .init(), delegate: self);
  }
  
  //  MARK: AVPhotoCaptureDelegate
  //  Processed photo. such as jpeg
  func capture(_ captureOutput: AVCapturePhotoOutput, didFinishProcessingPhotoSampleBuffer photoSampleBuffer: CMSampleBuffer?, previewPhotoSampleBuffer: CMSampleBuffer?, resolvedSettings: AVCaptureResolvedPhotoSettings, bracketSettings: AVCaptureBracketedStillImageSettings?, error: Error?) {
    
    //  Convert the buffer to an UIImage since UIImageWriteToSavedPhotosAlbum takes an UIImage...
    let imageData = AVCapturePhotoOutput.jpegPhotoDataRepresentation(forJPEGSampleBuffer: photoSampleBuffer!, previewPhotoSampleBuffer: previewPhotoSampleBuffer);
    let image = UIImage(data: imageData!);
    UIImageWriteToSavedPhotosAlbum(image!, nil, nil, nil);
  }
  
  
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }

}
