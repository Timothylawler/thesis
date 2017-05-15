//
//  phoneStateListener.swift
//  Tutorial
//
//  Created by Exjobb on 2017-04-07.
//  Copyright © 2017 Facebook. All rights reserved.
//

import Foundation
import CallKit;


@available(iOS 10.0, *)
@objc(PhoneStateListener) class PhoneStateListener: NSObject, CXCallObserverDelegate {
  
  let callManager = CXCallObserver();
  var callback: RCTResponseSenderBlock?;
  
  //  Start the call listener and take a callback as parameter
  @objc func startListening(_ cb:RCTResponseSenderBlock!){
    self.callback = cb!;
    
    callManager.setDelegate(self, queue: nil);
    
  }
  
  
  //  MARK: CXCallObserverDelegate
  func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
    if(call.hasEnded){
      //  Nothing
      //phoneStateLabel.text = "No Call happening.";
      self.callback!(["ended"]);
    }
    
    if(call.isOutgoing && !call.hasConnected){
      //  Dialing out
      //phoneStateLabel.text = "Calling from this unit.";
      self.callback!(["outgoing"]);
    }
    if(!call.isOutgoing && !call.hasConnected && !call.hasEnded){
      //  Call is incoming
      //phoneStateLabel.text = "Incoming call.";
      self.callback!(["incoming"]);
    }
    if(call.hasConnected && !call.hasEnded){
      //  Call is ongoing
      //phoneStateLabel.text = "Call is ongoing.";
      self.callback!(["ongoing"]);
    }
  }
  
  
}

