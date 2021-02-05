import { NativeModules, NativeEventEmitter } from 'react-native'

// class Controller extends NativeEventEmitter {
//   constructor(nativeModule) {
//     super(nativeModule);

//     this.requestAuthorization = nativeModule.requestAuthorization,
//     this.getBloodType = nativeModule.getBloodType,
//     this.getAge = nativeModule.getAge,
//     this.getSteps = nativeModule.getSteps,
//     this.getBloodPressure = nativeModule.getBloodPressure,
//     this.getHeartRate = nativeModule.getHeartRate,
//     this.getBMI = nativeModule.getBMI,
//     this.getBloodGlucose = nativeModule.getBloodGlucose,
//     this.getBodyTemperature = nativeModule.getBodyTemperature
//   }
// }

// export default new Controller(NativeModules.Controller)


// const Encryptor = NativeModules.Encryptor;

// export const encrypt = (plainText) => {
//   // Add your additional custom logic here
//   return Encryptor.encrypt(plainText);
// };

// export const decrypt = (encrptedText) => {
//   // Add your additional custom logic here
//   return Encryptor.decrypt(encrptedText);
// };

// // You can directly export this and access it 
// // like Encryptor.enrypt/Encryptor.decrypt
// export default Encryptor; 