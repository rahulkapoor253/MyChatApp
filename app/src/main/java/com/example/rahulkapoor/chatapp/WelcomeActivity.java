package com.example.rahulkapoor.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnHaveAccount, btnCreateAccount;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    /**
     * initiasations made;
     */
    private void init() {

        btnHaveAccount = (Button) findViewById(R.id.btn_have_account);
        btnCreateAccount = (Button) findViewById(R.id.create_account);
        btnCreateAccount.setOnClickListener(this);
        btnHaveAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.btn_have_account:
                loginUser();
                break;
            case R.id.create_account:
                registerUser();
                break;
            default:
                break;
        }
    }

    /**
     * register user;
     */
    private void registerUser() {
        intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * user login with email password;
     */
    private void loginUser() {
        intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
