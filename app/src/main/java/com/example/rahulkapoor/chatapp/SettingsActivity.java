package com.example.rahulkapoor.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView civUserImage;
    private TextView tvUserName, tvStatus;
    private Button btnChangeImage, btnChangeStatus;
    private DatabaseReference myRef;
    private FirebaseUser mCurrentUser;
    private String currentUserID;
    private String userName, userStatus, userImage, userThumbnail;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        init();

        getData();

    }

    /**
     * get data in real time from firebase;
     */
    private void getData() {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrentUser != null) {
            currentUserID = mCurrentUser.getUid();
        }

        //to get reference for user;
        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        //to retreive data from firebase in real time;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                Log.i("snapshot", dataSnapshot.toString());

                userName = dataSnapshot.child("name").getValue().toString();
                userStatus = dataSnapshot.child("status").getValue().toString();
                userImage = dataSnapshot.child("image").getValue().toString();
                userThumbnail = dataSnapshot.child("thumb_image").getValue().toString();

                tvUserName.setText(userName);
                tvStatus.setText(userStatus);


            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, databaseError.getMessage() + "", Toast.LENGTH_SHORT).show();

            }
        });

    }


    /**
     * init;
     */
    private void init() {
        civUserImage = (CircleImageView) findViewById(R.id.civ_userimage);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvUserName = (TextView) findViewById(R.id.tv_username);

        btnChangeImage = (Button) findViewById(R.id.btn_change_image);
        btnChangeStatus = (Button) findViewById(R.id.btn_change_status);

        mToolbar = (Toolbar) findViewById(R.id.app_bar_settings);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.account_settings_title);

    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {


            default:
                break;

        }

    }
}
