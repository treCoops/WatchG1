package com.treCoops.watchg1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView txtTitle = findViewById(R.id.txtTitle);
        ImageView imgLogo = findViewById(R.id.imgLogo);

        Animation slide_down_enter = AnimationUtils.loadAnimation(this, R.anim.slide_down_enter);
        Animation item_animation_fall_down = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);


        final String headingString = "SMART WATCH G1";
        final SpannableString spanHeadingString = new SpannableString(headingString);
        spanHeadingString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanHeadingString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 12, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle.setText(spanHeadingString);

        imgLogo.startAnimation(slide_down_enter);
        txtTitle.startAnimation(item_animation_fall_down);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Error", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("mobile").child("FCM");
                        myRef.setValue(task.getResult());

                    }
                });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                Animatoo.animateSlideLeft(SplashActivity.this);
                finish();
            }
        },2500);
    }
}