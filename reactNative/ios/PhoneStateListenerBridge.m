//
//  PhoneStateListenerBridge.m
//  Tutorial
//
//  Created by Exjobb on 2017-04-07.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(PhoneStateListener, NSObject)

RCT_EXTERN_METHOD(startListening:(RCTResponseSenderBlock *)callback);


@end
