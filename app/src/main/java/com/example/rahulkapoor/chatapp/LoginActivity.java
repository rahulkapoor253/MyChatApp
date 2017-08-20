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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPassword, etEmail;
    private ImageView ivBack;
    private TextView tvHeader;
    private Button btnLogin;
    private String mEmail, mPassword;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    /**
     * initiasations made;
     */
    private void init() {

        mProgressDialog = new ProgressDialog(this);
        etPassword = (EditText) findViewById(R.id.et_password);
        etEmail = (EditText) findViewById(R.id.et_email);
        btnLogin = (Button) findViewById(R.id.login);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvHeader = (TextView) findViewById(R.id.tv_title);
        ivBack.setOnClickListener(this);
        tvHeader.setText("LOGIN");
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.login:
                if (validateData()) {
                    loginUser();
                }
                break;
            default:
                break;
        }

    }

    /**
     * validations attached;
     *
     * @return return boolean;
     */
    private boolean validateData() {
        if (!TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())) {
            return true;
        }
        return false;
    }

    /**
     * login user;
     */
    private void loginUser() {

        mProgressDialog.setTitle("Loggin In, please wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mEmail = etEmail.getText().toString();
        mPassword = etPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {

                //it tells us about the signin failure or success;
                if (!task.isSuccessful()) {
                    //to hide the progress bar when registering user fails;
                    mProgressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Fail to Signin User, Try again!", Toast.LENGTH_SHORT).show();
                } else {
                    //to dismiss the progress bar once the task is successful;
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }
}
