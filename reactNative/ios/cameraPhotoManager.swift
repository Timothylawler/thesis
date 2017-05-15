//
//  cameraPhoto.swift
//  Tutorial
//
//  Created by Exjobb on 2017-04-10.
//  Copyright © 2017 Facebook. All rights reserved.
//

import Foundation

@available(iOS 10.0, *)
@objc(CameraPhotoManager) class CameraPhotoManager: RCTViewManager{
  
  var cameraPhotoView: CameraPhotoView?;
  
  override func view() -> UIView! {
    cameraPhotoView = CameraPhotoView.init();
    
    // camera capture button
    //  Add button for capture
    let captureButton = UIButton(type: .roundedRect);
    captureButton.setTitle("Capture", for: .normal);
    captureButton.frame = CGRect(x: 0, y: 400, width: 200, height: 50);
    captureButton.addTarget(self, action: #selector(self.captureButtonPressed), for: .touchUpInside);
    captureButton.backgroundColor = UIColor.blue;
    
    cameraPhotoView?.addSubview(captureButton);
    
    //  //
    
    return cameraPhotoView;
  }
  
  @IBAction func captureButtonPressed(){
    cameraPhotoView?.captureButtonPressed();
  }
  
}
