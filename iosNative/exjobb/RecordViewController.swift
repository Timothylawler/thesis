//
//  RecordViewController.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-30.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import UIKit;
import AVFoundation;

class RecordViewController: UIViewController, AVCaptureFileOutputRecordingDelegate {

    var activeCaptureDevice: AVCaptureDevice?;
    var captureInput: AVCaptureInput?;
    var captureOutput: AVCaptureMovieFileOutput?;
    var captureSession: AVCaptureSession?;
    var previewLayer: AVCaptureVideoPreviewLayer?;
    var fileUrl: NSURL?;
    let fileName = "movie.mov";
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        setup();
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    private func setup(){
        captureSession = AVCaptureSession();
        //  Can crash if we cant set preset
        captureSession?.sessionPreset = AVCaptureSessionPresetMedium;
        
        //  Check for devices with built in wide angle camera (kinda standard for taking photos or video) and also the back camera.
        let availableDevices = AVCaptureDeviceDiscoverySession.init(deviceTypes: [AVCaptureDeviceType.builtInWideAngleCamera], mediaType: AVMediaTypeVideo, position: .back).devices;
        
        guard availableDevices!.count > 0 else {
            //  This should never hit because of prev guard but hey. Who am i to decide that
            fatalError("How did you end up here?");
        }
        
        //  Take the first device as the one we want to use
        activeCaptureDevice = availableDevices![0];
        
        //  Bind input and output to the session
        do{
            captureInput = try AVCaptureDeviceInput.init(device: activeCaptureDevice);
            
            if(captureSession!.canAddInput(captureInput)){
                captureSession?.addInput(captureInput);
            } else {
                //  I believe this is how you throw exceptions manually in swift?
                NSException(name: .objectNotAvailableException, reason: "Cant add input device", userInfo: nil).raise();
            }
            
            captureOutput = AVCaptureMovieFileOutput();
            let maxDuration = CMTime.init(seconds: 100, preferredTimescale: .init());
            //  Set max record time, 100 seconds
            captureOutput?.maxRecordedDuration = maxDuration;
            
            if(captureSession!.canAddOutput(captureOutput)){
                captureSession?.addOutput(captureOutput);
            } else {
                NSException(name: .objectNotAvailableException, reason: "Cant add input device", userInfo: nil).raise();
            }
            
            //  Preview layer
            previewLayer = AVCaptureVideoPreviewLayer.init(session: captureSession);
            
            //  Add the layers to the screen and start projecting
            self.view.layer.addSublayer(previewLayer!);
            previewLayer?.frame = self.view.frame;
            captureSession?.startRunning();

            
        } catch {
            
        }
        
        
    }
    

    //  MARK: Actions
    @IBAction func recordButtonPressed(_ sender: UIButton) {
        if(captureOutput!.isRecording){
            //  Stop recording
            captureOutput?.stopRecording();
        }
        else {
            let path = NSTemporaryDirectory() + fileName;
            //  Start recording
            fileUrl = NSURL.init(fileURLWithPath: path);
            captureOutput?.startRecording(toOutputFileURL: fileUrl as! URL, recordingDelegate: self);
        }
    }
    
    func capture(_ captureOutput: AVCaptureFileOutput!, didStartRecordingToOutputFileAt fileURL: URL!, fromConnections connections: [Any]!) {
        
    }
    
    func capture(_ captureOutput: AVCaptureFileOutput!, didFinishRecordingToOutputFileAt outputFileURL: URL!, fromConnections connections: [Any]!, error: Error!) {
        let path = NSTemporaryDirectory() + fileName;
        UISaveVideoAtPathToSavedPhotosAlbum(path, nil, nil, nil);
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}











