//
//  Controller.swift
//  nativehealth
//
//  Created by raksha on 19/1/2021.
//

import Foundation
import HealthKit
 
extension Date {
    static func mondayAt12AM() -> Date {
        return Calendar(identifier: .iso8601).date(from: Calendar(identifier: .iso8601).dateComponents([.yearForWeekOfYear, .weekOfYear], from: Date()))!
    }
}

 // Swift Decorator to export Class to Objective-C
 // Declares Controller Class
@objc(Controller)
class Controller: NSObject {
  // Declares HealthKit store for our Project
 let healthStore = HKHealthStore()
 
  // Function Method to request Authorizations from Healthkit
 @objc
 func requestAuthorization() {
  let allTypes = Set([HKObjectType.workoutType(),
                      HKObjectType.quantityType(forIdentifier: .activeEnergyBurned)!,
                      HKObjectType.quantityType(forIdentifier: .stepCount)!,
                      HKObjectType.quantityType(forIdentifier: .distanceCycling)!,
                      HKObjectType.quantityType(forIdentifier: .distanceWalkingRunning)!,
                      HKObjectType.quantityType(forIdentifier: .heartRate)!])
    
     // Request authorization for those quantity types.
     healthStore.requestAuthorization(toShare: allTypes, read: allTypes) { (success, error) in
         // Handle error.
     }
 }
  
  @objc
  func updateStepsCount(_ statisticsCollection: HKStatisticsCollection, _ resolve: @escaping RCTPromiseResolveBlock,
                        rejecter reject: @escaping (RCTPromiseRejectBlock) -> Void) {
      
      let stepType = HKQuantityType.quantityType(forIdentifier: HKQuantityTypeIdentifier.stepCount)!
      
      let startDate = Calendar.current.date(byAdding: .day, value: -7, to: Date())
      
      let anchorDate = Date.mondayAt12AM()
      
      let daily = DateComponents(day: 1)
      
      let predicate = HKQuery.predicateForSamples(withStart: startDate, end: Date(), options: .strictStartDate)
      
      let query = HKStatisticsCollectionQuery(quantityType: stepType, quantitySamplePredicate: predicate, options: .cumulativeSum, anchorDate: anchorDate, intervalComponents: daily)

          healthStore.execute(query)
      
  }
  
  @objc
  func updateSteps(
      _ resolve: @escaping RCTPromiseResolveBlock,
      rejecter reject: @escaping RCTPromiseRejectBlock,
    _ statisticsCollection: HKStatisticsCollection
    ) -> Void {
    struct Step {
        let id = UUID()
      var count: Int?
        var date: Date?
    }
   
      let startDate = Calendar.current.date(byAdding: .day, value: -7, to: Date())!
      let endDate = Date()
      
      statisticsCollection.enumerateStatistics(from: startDate, to: endDate) { (statistics, stop) in
          
          let count = statistics.sumQuantity()?.doubleValue(for: .count())
          let steps = Int(count ?? 0)
        
        var stepCount = [Step]()
        var tempStepCount = Step(count: steps, date: Date())
          
        tempStepCount.count = steps
        tempStepCount.date = startDate
        stepCount.append(tempStepCount)
      }
    print("steps:", Step())
    resolve(Step())
      
  }
  
  @objc
 static func requiresMainQueueSetup() -> Bool {
   return true
 }
}

