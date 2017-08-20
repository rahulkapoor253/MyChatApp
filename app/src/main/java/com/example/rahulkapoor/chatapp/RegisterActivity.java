package com.example.rahulkapoor.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPassword, etUserName, etEmail;
    private ImageView ivBack;
    private TextView tvHeader;
    private Button btnRegister;
    private String mPassword, mUserName, mEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

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
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvHeader = (TextView) findViewById(R.id.tv_title);
        ivBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvHeader.setText("REGISTER");


    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
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
                if (!task.isSuccessful()) {
                    //to hide the progress bar when registering user fails;
                    mProgressDialog.hide();
                    Toast.makeText(RegisterActivity.this, "Fail to Signin", Toast.LENGTH_SHORT).show();
                } else {
                    //to dismiss the progress bar once the task is successful;
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

    }
}
