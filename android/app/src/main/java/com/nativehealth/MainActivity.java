package com.nativehealth;

import com.facebook.react.ReactActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


    public class MainActivity extends ReactActivity {
              private static String TAG = "nativehealth";

              @Override
              protected String getMainComponentName() {
                return "nativehealth";
              }

              private FitnessOptions fitnessOptions;
              int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1001;

              public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);



              }

              public void checkFitInstalled() {
                if (isGoogleFitPermissionGranted()) {
                    readHRHistoryData();
                } else {
                  requestGoogleFitPermission();
                    readHRHistoryData();
                }
              }

              public boolean isGoogleFitPermissionGranted() {
                if (GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
                  return true;
                } else {
                  return false;
                }
              }

              public void setFitnessOption() {
                fitnessOptions =
                        FitnessOptions.builder()
                                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                                .addDataType(DataType.AGGREGATE_HEART_RATE_SUMMARY, FitnessOptions.ACCESS_READ)
                                .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
                                .addDataType(HealthDataTypes.AGGREGATE_BLOOD_PRESSURE_SUMMARY, FitnessOptions.ACCESS_READ)
                                .addDataType(HealthDataTypes.TYPE_BLOOD_GLUCOSE, FitnessOptions.ACCESS_READ)
                                .build();
              }

            //      public void signIn() {
            //        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //                .addExtension(fitnessOptions).requestEmail().build();
            //
            //        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
            //        Intent signIntent = googleSignInClient.getSignInIntent();
            //        GoogleSignIn.getSignedInAccountFromIntent(signIntent);
            //        requestGoogleFitPermission();
            //      }

              public void requestGoogleFitPermission() {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BODY_SENSORS},
                        GOOGLE_FIT_PERMISSIONS_REQUEST_CODE);

                GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
                GoogleSignIn.requestPermissions(
                        this,
                        GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                        account,
                        fitnessOptions);
              }

//    private void accessGoogleFit() {
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        cal.setTime(now);
//        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.DATE, -1);
//        long startTime = cal.getTimeInMillis();
//
//        DataReadRequest readRequest = new DataReadRequest.Builder()
//                .aggregate(DataType.TYPE_HEART_RATE_BPM)
//                .setTimeRange(startTime, endTime, TimeUnit.SECONDS)
//                .bucketByTime(1, TimeUnit.DAYS)
//                .build();
//        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
//        Fitness.getHistoryClient(this, account)
//                .readData(readRequest)
//                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
//                    @Override
//                    public void onSuccess(DataReadResponse dataReadResponse) {
//                        Log.d("TAG_F", "onSuccess: 1 " + dataReadResponse.toString());
//                        Log.d("TAG_F", "onSuccess: 1 " + dataReadResponse.getStatus());
//                        Log.d("TAG_F", "onSuccess: 1 " + dataReadResponse.getDataSet(DataType.TYPE_HEART_RATE_BPM));
//                        Log.d("TAG_F", "onSuccess: 1 " + dataReadResponse.getBuckets().get(0));
//                        Log.d("TAG_F", "onSuccess: 1 " + dataReadResponse.getBuckets().get(0).getDataSets().size());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("TAG_F", "onFailure: 1 " + e.getMessage());
//                    }
//                })
//                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataReadResponse> task) {
//                        Log.d("TAG_F", "onComplete: 1 ");
//                    }
//                });
//    }

            private Task<DataReadResponse> readHRHistoryData() {

                DataReadRequest readRequest = queryHRFitnessData();
                return Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .readData(readRequest)
                        .addOnSuccessListener(
                                new OnSuccessListener<DataReadResponse>() {
                                    @Override
                                    public void onSuccess(DataReadResponse dataReadResponse) {
                                        Log.v(TAG, "Success" + dataReadResponse);
                                        printData(dataReadResponse);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.v(TAG, "Failture");
                                    }
                                });
            }

            public static DataReadRequest queryHRFitnessData() {
//                Calendar cal = Calendar.getInstance();
//                Calendar cal1 = Calendar.getInstance();
//                Date now = new Date();
//                cal1.setTime(now);
//                long endTime = cal1.getTimeInMillis();
//
//                cal.add(Calendar.DATE, -1);
//                long startTime = cal.getTimeInMillis();
//                java.text.DateFormat dateFormat = DateFormat.getDateInstance();

                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                Date now = new Date();
                cal1.setTime(now);
                long endTime = cal1.getTimeInMillis();

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
                c.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
                c.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
                c.set(Calendar.AM_PM, 00);
                c.set(Calendar.HOUR, 00);
                c.set(Calendar.MINUTE, 00);
                c.set(Calendar.SECOND, 00);
                c.set(Calendar.MILLISECOND, 00);

                long startTime = c.getTimeInMillis();
                java.text.DateFormat dateFormat = DateFormat.getDateInstance();

                DataReadRequest readRequest = new DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                                .bucketByTime(1, TimeUnit.DAYS)
                                .build();
                Log.v(TAG, "readRequest: " + readRequest);
                return readRequest;
              }



            public static void printData(DataReadResponse dataReadResult) {
                        Log.v(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
                        if (dataReadResult.getBuckets().size() > 0) {
                            for (Bucket bucket : dataReadResult.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                Log.v(TAG, "Datasets: " + dataSets);
                                for (DataSet dataSet : dataSets) {
                                    dumpDataSet(dataSet);
                                }
                            }
                        } else if (dataReadResult.getDataSets().size() > 0) {
                            System.out.print("Number of returned DataSets is: " + dataReadResult.getDataSets().size());
                            for (DataSet dataSet : dataReadResult.getDataSets()) {
                                dumpDataSet(dataSet);
                            }
                        }
                    }

            private static void dumpDataSet(DataSet dataSet) {
                Log.v(TAG, "Name: " + dataSet.getDataType().getName());
                DateFormat dateFormat = DateFormat.getTimeInstance();
                Log.v(TAG, "Fields: " + dataSet.getDataSource().getDataType().getFields());

                Log.v(TAG, "Data Point Values :" + dataSet.getDataPoints());
                for (DataPoint dp : dataSet.getDataPoints()) {
                    Log.v(TAG, "Data Point:");
                    Log.v(TAG, "Type: " + dataSet.getDataType().getName());
                    Log.v(TAG, "Start: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                    Log.v(TAG, "End: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                    for (Field field : dp.getDataType().getFields()) {
                        Log.v(TAG, "Field: " + field.getName() + ", Value : " + dp.getValue(field).asFloat());
                    }
                }
            }


   }
