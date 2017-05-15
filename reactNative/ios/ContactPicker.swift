//
//  ContactPicker.swift
//  Tutorial
//
//  Created by Exjobb on 2017-04-19.
//  Copyright © 2017 Facebook. All rights reserved.
//

import Foundation
import ContactsUI;


@available(iOS 9.0, *)
@objc(ContactPicker) class ContactPicker: NSObject, CNContactPickerDelegate {
  
  var resolve: RCTResponseSenderBlock?;
  var reject: RCTResponseSenderBlock?;
  
  //  Contact
  let contactStore = CNContactStore();
  let contactPicker = CNContactPickerViewController();
  
  @objc func launchPicker(_ success: @escaping RCTResponseSenderBlock){
    self.resolve = success;
    //self.reject = error;
    
    //  Check contact permission and read contacts
    requestContacts();
    
    
  }
  
  //  MARK: Private methods
  /*  ------------ CONTACTS ------------ */
  private func requestContacts(){
    let status = CNContactStore.authorizationStatus(for: .contacts);
    switch(status){
    case .notDetermined:
      //  Request permission
      contactStore.requestAccess(for: .contacts, completionHandler: { (authorized:Bool, error: Error?) in
        if(authorized){
          self.openContactPicker();
        }
      });
      break;
      
    case .denied:
      respond(message: "Contacts not allowed");
      break;
    case .restricted:
      respond(message: "Contacts not allowed");
      break;
    case .authorized:
      openContactPicker();
      break;
    default:
      break;
    }
  }
  
  
  private func openContactPicker(){
    contactPicker.delegate = self;
    
    //  THIS IS HOW YOU GET THE ACTUAL ROOT VIEW CONTROLLER. DAMN IMPORTANT TO REMEBER THIS ONE
    let asd = UIApplication.shared.keyWindow?.rootViewController;
    
    asd?.present(contactPicker, animated: true, completion: nil);
  }
  
  private func respond(message: String){
    //  Reject
    self.resolve!([message]);
  }
  
  /*  ------ CONTACT PICKER DELEGATE ------- */
  func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
    let name = CNContactFormatter.string(from: contact, style: .fullName);
    respond(message: name!);
  }
  
  func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
    respond(message: "Contact picker cancelled");
  }
  
}
