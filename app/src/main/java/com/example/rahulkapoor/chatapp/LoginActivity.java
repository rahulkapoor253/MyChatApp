package com.example.rahulkapoor.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPassword, etEmail;
    private ImageView ivBack;
    private TextView tvHeader;
    private Button btnLogin;

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
                loginUser();
                break;
            default:
                break;
        }

    }

    /**
     * login user;
     */
    private void loginUser() {
    }
}
