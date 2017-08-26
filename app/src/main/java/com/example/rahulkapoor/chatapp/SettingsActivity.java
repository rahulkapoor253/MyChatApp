package com.example.rahulkapoor.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView civUserImage;
    private TextView tvUserName, tvStatus;
    private Button btnChangeImage, btnChangeStatus;
    private DatabaseReference myRef;
    private FirebaseUser mCurrentUser;
    private String currentUserID;
    private String userName, userStatus, userImage, userThumbnail;
    private Toolbar mToolbar;
    private Intent intent;
    private StorageReference mStorageRef;

    private static final int GALLERY_CHOOSER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        init();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        getData();

        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                intent = new Intent(SettingsActivity.this, StatusActivity.class);
                intent.putExtra("status", userStatus);
                startActivity(intent);

            }
        });

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

//                //to pick an iamge from gallery chooser;
//                intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(intent, "Gallery Chooser"), GALLERY_CHOOSER);

                //we would rather use an image cropper dependency;
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                //to store the profile image to firebase storage in real time;
                StorageReference fileReference = mStorageRef.child("images").child("profile_image.jpg");
                fileReference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error in uploading image to firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
