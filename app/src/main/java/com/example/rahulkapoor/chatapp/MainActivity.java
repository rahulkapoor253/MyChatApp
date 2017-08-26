package com.example.rahulkapoor.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private Intent intent;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private android.support.v4.view.PagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;


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

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("CHAT APP");

        //setting up view pager;
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

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


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout) {

            logoutUser();
        } else if (item.getItemId() == R.id.settings) {
            //fire intent to settings activity;
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
