//
//  CameraRecordManager.swift
//  Tutorial
//
//  Created by Exjobb on 2017-04-18.
//  Copyright © 2017 Facebook. All rights reserved.
//

import Foundation


@available(iOS 10.0, *)
@objc(CameraRecordManager) class CameraRecordManager: RCTViewManager{

  var cameraRecordView: CameraRecordView?;
  
  override func view() -> UIView! {
    cameraRecordView = CameraRecordView.init();
    
    // camera capture button
    //  Add button for capture
    let captureButton = UIButton(type: .roundedRect);
    captureButton.setTitle("Record", for: .normal);
    captureButton.frame = CGRect(x: 0, y: 400, width: 200, height: 50);
    captureButton.addTarget(self, action: #selector(self.captureButtonPressed), for: .touchUpInside);
    captureButton.backgroundColor = UIColor.blue;
    
    cameraRecordView?.addSubview(captureButton);
    
    
    //  //
    
    return cameraRecordView;
  }
  
  @IBAction func captureButtonPressed(){
    cameraRecordView?.toggleRecord()
  }


}
