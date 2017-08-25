package com.example.rahulkapoor.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPassword, etUserName, etEmail;
    private Toolbar mToolbar;
    private Button btnRegister;
    private String mPassword, mUserName, mEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private String uID;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        init();
    }

    /**
     * initialisations made;
     */
    private void init() {

        mProgressDialog = new ProgressDialog(this);
        etPassword = (EditText) findViewById(R.id.et_password);
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        btnRegister = (Button) findViewById(R.id.btn_register);
        //ivBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register Activity");


    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (validateData()) {
                    registerUser();
                }
                break;
            default:
                break;
        }

    }

    /**
     * validate data
     *
     * @return boolean;
     */
    private boolean validateData() {
        if (!TextUtils.isEmpty(etUserName.getText().toString()) && !TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())) {
            return true;
        }
        return false;
    }

    /**
     * register user;
     */
    private void registerUser() {

        mUserName = etUserName.getText().toString();
        mEmail = etEmail.getText().toString();
        mPassword = etPassword.getText().toString();

        mProgressDialog.setTitle("Registering User, please wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                //it tells us about the signin failure or success;
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        uID = currentUser.getUid();
                    }

                    //setting up the realtime database;
                    database = FirebaseDatabase.getInstance();
                    //root->child object of users -> child with UID ->setting up fields;
                    myRef = database.getReference().child("Users").child(uID);

                    //we are using thumbnail image and complete image because we dont want user to load full image;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", mUserName);
                    map.put("image", "default");
                    map.put("thumb_image", "default");
                    map.put("status", "Hi there! I am using Chat App");

                    //to check whether my task has been completed/ not;
                    myRef.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull final Task<Void> task) {

                            if (task.isSuccessful()) {
                                //    to dismiss the progress bar once the task is successful;
                                mProgressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                //to hide the progress bar when registering user fails;
                                mProgressDialog.hide();
                                Toast.makeText(RegisterActivity.this, "Fail to Register User, Try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    //to hide the progress bar when registering user fails;
                    mProgressDialog.hide();
                    Toast.makeText(RegisterActivity.this, "Fail to Register User, Try again!", Toast.LENGTH_SHORT).show();


                }


            }
        });

    }
}
