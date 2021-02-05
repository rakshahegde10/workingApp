//
//  Controller.m
//  nativehealth
//
//  Created by raksha on 19/1/2021.
//


#import "React/RCTBridgeModule.h"
#import <React/RCTLog.h>


// This exports our Controller class in Controller.swift
@interface RCT_EXTERN_MODULE(Controller, NSObject)

// Exports our requestAuthorization method in the class
RCT_EXTERN_METHOD(requestAuthorization)

RCT_EXTERN_METHOD(getAgeSexAndBloodType)

RCT_EXPORT_METHOD(updateStepsCount:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
 RCTLogInfo(@"Pretending to read a step");
}
@end
