import React, { Component } from 'react';
import { View,Text, TouchableOpacity, TextInput, StyleSheet, SafeAreaView, ScrollView } from 'react-native';
import { NativeModules, NativeEventEmitter } from 'react-native'
import  HealthkitController from "./HealthKit.js";
import { postFhirData } from './fhir.js';
import jsonFile from './bundle.json';

export default class App extends Component {

  static navigationOptions =({navigation})=> {
    const params = navigation.state.params || {};
    return{
        title: "Health Data",
    }
};

  constructor(props) {
    super(props)
    this.state = {
      name: "John Doe",
      sex: "nil",
      age: "nil",
      bloodType: "nil",
      systolic: "nil",
      diastolic: "nil",
      step: "nil",
      heartRate: "nil",
      bmi: "nil",
      bloodGlucose: "nil",
      bodyTemp: "nil"
    }
   }

  clickHandlerBp = async() => {
    const log = await HealthkitController.getBloodPressure()
    .then(result => {
      let res = JSON.parse(result)
      for (let i = 0; i < res.length; i++) {
        let final = res[i];
        let finalValue = final.value.toString()
        let sys = parseInt(finalValue.substring(0, finalValue.indexOf("|")), 10);
        jsonFile.entry[2].resource.component[0].valueQuantity.value = sys
        let dia = parseInt(finalValue.substring(finalValue.indexOf("|") + 1), 10);
        jsonFile.entry[2].resource.component[1].valueQuantity.value = dia
        let systolic = sys.toString()
        let diastolic = dia.toString()
        this.setState({systolic : systolic, diastolic: diastolic}) 
    }});
  }

  clickHandlerStep = async() => {
    const log = await HealthkitController.getSteps()
    .then(result => {
      let res = JSON.parse(result)
      for (let i = 0; i < res.length; i++) {
        let final = res[i];
        final = parseInt(final.value, 10)
        final = final.toString()
        this.setState({step : final}) 
    }});
  }

  clickHandlerHearRate = async() => {
    const log = await HealthkitController.getHeartRate()
    .then(result => {
      let res = JSON.parse(result)
      for (let i = 0; i < res.length; i++) {
        let final = res[i];
        final = final.value.toString()
        this.setState({heartRate : final}) 
    }});
  }

  clickHandlerBMI = async() => {
    const log = await HealthkitController.getBMI()
    .then(result => {
      let res = JSON.parse(result)
      for (let i = 0; i < res.length; i++) {
        let final = res[i];
        final = final.value.toString()
        this.setState({bmi : final}) 
    }});
  }

  clickHandlerBloodGlucose() {
    const log = HealthkitController.getBloodGlucose()
    .then(result => {
      let res = JSON.parse(result)
      for (let i = 0; i < res.length; i++) {
        let final = res[i];
        final = final.value.toString()
        let finalVal = final.includes("mg")  ? final.substring(0, final.indexOf("mg")) : final.substring(0, final.indexOf("mmol"))
        finalVal = finalVal.replaceAll(" ", "")
        jsonFile.entry[1].resource.valueQuantity.value = finalVal
        this.setState({bloodGlucose : final}) 
    }});
  }

  clickHandlerBodyTemperature() {
    const log = HealthkitController.getBodyTemperature()
    .then(result => {
      let res = JSON.parse(result)
      for (let i = 0; i < res.length; i++) {
        let final = res[i];
        final = final.value.toString()
        let finalVal = final.includes("degC")  ? final.substring(0, final.indexOf("degC")) : final.substring(0, final.indexOf("degF"))
        finalVal = finalVal.replaceAll(" ", "")
        jsonFile.entry[3].resource.valueQuantity.value = finalVal
        this.setState({bodyTemp : final}) 
    }});
  }

  clickHandlerBloodType() {
    const log = HealthkitController.getBloodType()
    .then(result => {
        this.setState({bloodType : result}) 
    });
  }

  clickHandlerAge() {
    const log = HealthkitController.getAge()
    .then(result => {
      result = result.toString()
        this.setState({age : result}) 
    });
  }


  // clickHandlerSex = async() => {
  //   const log = await HealthkitController.sampleMethod()
  //   .then(result => {
  //     // let res = result.toString()
  //     console.warn("Sex:", result)
  //     // setSex(res)
  //   });

  // }

  requestAuth = async() => {
    NativeModules.nativehealth.getAuthorizations()
  }

  click = async() => {
        this.clickHandlerAge()
        this.clickHandlerBloodType()
        this.clickHandlerBloodGlucose()
        this.clickHandlerHearRate()
        this.clickHandlerBMI()
        this.clickHandlerStep()
        this.clickHandlerBp()
        this.clickHandlerBodyTemperature()
    // NativeModules.Encryptor.getAuthorizationAndReadData();
    // const decryptText = await decrypt('encrypted text');
  }

  post = async() => {
    console.log(jsonFile)
    let success = await postFhirData()
  }

    render() {
        return (
        <View style={styles.container}>
        <SafeAreaView>
        <ScrollView>
        <View style={{ paddingBottom: 10 }}></View>
        <TouchableOpacity style={styles.text1} onPress={()=>{this.requestAuth()}}>
        <Text style={styles.label}>Get Authorization</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.text1} >
        <Text style={styles.label}>Request Health Data</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.text1} >
          <Text style={styles.label}>POST to fhir</Text>
        </TouchableOpacity>
        <View style={{ paddingBottom: 30 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Name</Text>
        <TextInput value={this.state.name} style={styles.text} textAlign='center'/>
        </View>
        <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between"}}>
          <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Age</Text>
        <TextInput value={this.state.age} style={styles.text} textAlign='center'/>   
        </View>
        <View style={{ paddingRight: 20 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Blood Type</Text>
        <TextInput value={this.state.bloodType} style={styles.text} textAlign='center'/> 
        </View> 
         </View>
         <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between"}}>
          <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Systolic</Text>
        <TextInput value={this.state.systolic} style={styles.text} textAlign='center'/>   
        </View>
        <View style={{ paddingRight: 20 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Diastolic</Text>
        <TextInput value={this.state.diastolic} style={styles.text} textAlign='center'/> 
        </View> 
         </View>
        <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>StepCount</Text>
        <TextInput value={this.state.step} style={styles.text} textAlign='center'/>
        </View>
        <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>HearRate</Text>
        <TextInput value={this.state.heartRate} style={styles.text} textAlign='center'/>
        </View>
        <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>BodyMassIndex</Text>
        <TextInput value={this.state.bmi} style={styles.text} textAlign='center'/>
        </View>
        <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Blood Glucose</Text>
        <TextInput value={this.state.bloodGlucose} style={styles.text} textAlign='center'/>
        </View>
        <View style={{ paddingBottom: 50 }}></View>
        <View style={{flexDirection: "row", alignContent: "space-between", alignItems: "center"}}>
        <Text style={styles.label}>Body Temp</Text>
        <TextInput value={this.state.bodyTemp} style={styles.text} textAlign='center'/>
        </View>
        <View style={{ paddingBottom: 50 }}></View>
        </ScrollView>
        </SafeAreaView>
      </View>
        );
      }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#EAEDED',
    alignItems: 'center',
    justifyContent: 'center',
  },
  rowContainer: {
    flexDirection: 'row',
   justifyContent: "flex-start"
  },
  touch: {
    backgroundColor: 'grey',
    padding: 15
  },
  text1:{
    borderRightWidth: 2,
    borderLeftWidth: 2,
    borderTopWidth:2,
    borderBottomWidth: 2,
    backgroundColor: "#D0D3D4",
    height: 40,
    borderColor: 'black',
    justifyContent: "center",
    alignItems: "center",
    fontSize: 16,
    paddingRight: 10,
    paddingLeft: 10,
    marginLeft:4,
    marginRight: 4
  },
  text: {
   borderRadius: 5, 
    borderRightWidth: 2,
    borderLeftWidth: 2,
    borderTopWidth:2,
    borderBottomWidth: 2,
    backgroundColor: "#D0D3D4",
    height: 40,
    width: 110,
    borderColor: 'black',
    justifyContent: "center",
    alignItems: "center",
    fontSize: 16
  },
  label: {
    fontFamily: 'helvetica',
    paddingRight:10,
    fontSize: 18,
    alignItems: "center"
  }
});