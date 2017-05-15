//
//  CarrierInfoBridge.m
//  Tutorial
//
//  Created by Exjobb on 2017-04-06.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(CarrierInfo, NSObject)

RCT_EXTERN_METHOD(getCarrierInfo:(RCTResponseSenderBlock *)callback);


@end

