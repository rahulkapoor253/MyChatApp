package com.example.rahulkapoor.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private Intent intent;
    private ImageView ivBack, ivLogout;
    private TextView tvHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to get notify when user signin or signout;

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //user is not logged in and the whole process is followed;
                    sendToStart();
                } else {
                    //user is logged in and inflate the UI;
                    init();
                }
            }
        };


    }

    /**
     * init made;
     */
    private void init() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivLogout = (ImageView) findViewById(R.id.iv_logout);
        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText("MY CHAT APP");
        ivBack.setVisibility(View.GONE);
        ivLogout.setVisibility(View.VISIBLE);
        ivLogout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_logout:
                logoutUser();
                break;
            default:
                break;
        }
    }

    /**
     * logout user;
     */
    private void logoutUser() {

        firebaseAuth.getInstance().signOut();
        sendToStart();

    }

    /**
     * send to welcome screen on user null or logout user;
     */
    private void sendToStart() {

        intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();

    }
}
