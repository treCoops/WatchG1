package com.treCoops.watchg1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.gcssloop.widget.ArcSeekBar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treCoops.watchg1.Model.Watch;
import com.treCoops.watchg1.Util.AlertBar;

public class SettingActivity extends AppCompatActivity {

    int mins = 0;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int trigger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageView imgBack = findViewById(R.id.imgBack);
        final ArcSeekBar arcSeekBar = findViewById(R.id.seekBar);
        final TextView waitingTime = findViewById(R.id.waitingTime);
        final Switch switchAlert = findViewById(R.id.switchAlert);
        final Switch switchSound = findViewById(R.id.switchSound);
        final Switch switchVibration = findViewById(R.id.switchVibration);
        final ImageView imgDone = findViewById(R.id.imgDone);



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("slippersG1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Watch watch = dataSnapshot.getValue(Watch.class);

                trigger = watch.getTrigger();
                mins = watch.getWaitingTime();

                arcSeekBar.setProgress(watch.getWaitingTime());
                if(watch.getWaitingTime() < 10){
                    waitingTime.setText("0"+String.valueOf(watch.getWaitingTime()));
                }else{
                    waitingTime.setText(String.valueOf(watch.getWaitingTime()));
                }

                if(watch.getAlert() == 1){
                    switchAlert.setChecked(true);
                }else{
                    switchAlert.setChecked(false);
                }

                if(watch.getSound() == 1){
                    switchSound.setChecked(true);
                }else{
                    switchSound.setChecked(false);
                }

                if(watch.getVibration() == 1){
                    switchVibration.setChecked(true);
                }else{
                    switchVibration.setChecked(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase error", "loadPost:onCancelled", databaseError.toException());
                AlertBar.notifyDanger(SettingActivity.this, "Data Fetching Failed!");
            }
        });

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int alert = 0;
                int sound = 0;
                int vibration = 0;

                if(switchAlert.isChecked()){
                    alert = 1;
                }

                if(switchSound.isChecked()){
                    sound = 1;
                }

                if(switchVibration.isChecked()){
                    vibration = 1;
                }

                myRef.child("slippersG1").setValue(new Watch(alert, sound, trigger, vibration, mins))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("Write", "true");
                                AlertBar.notifySuccess(SettingActivity.this, "Setting Updated!");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                                        Animatoo.animateSlideRight(SettingActivity.this);
                                        finish();
                                    }
                                },1500);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firebase Error", e.getLocalizedMessage());
                                AlertBar.notifyDanger(SettingActivity.this, "Operation Failed!");
                            }
                        });

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                Animatoo.animateSlideRight(SettingActivity.this);
                finish();
            }
        });

        arcSeekBar.setOnProgressChangeListener(new ArcSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(ArcSeekBar seekBar, int progress, boolean isUser) {
                mins = progress;
                if(progress < 10){
                    waitingTime.setText("0"+String.valueOf(progress));
                }else{
                    waitingTime.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(ArcSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(ArcSeekBar seekBar) {

            }
        });
    }
}