//
//  ContactPickerBridge.m
//  Tutorial
//
//  Created by Exjobb on 2017-04-19.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ContactPicker, NSObject)

RCT_EXTERN_METHOD(launchPicker:(RCTResponseSenderBlock *)success);


@end
