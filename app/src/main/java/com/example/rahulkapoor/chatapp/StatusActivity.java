package com.example.rahulkapoor.chatapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private EditText etStatus;
    private Button btnSave;
    private String userID;
    private DatabaseReference mStatusRef;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        init();

        etStatus.setText(getIntent().getStringExtra("status"));

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            userID = mCurrentUser.getUid();
        }
        mStatusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);


        mProgressDialog = new ProgressDialog(StatusActivity.this);

        //to update the status of the user;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                mProgressDialog.setTitle("Saving changes to Database...");
                mProgressDialog.show();

                String status = etStatus.getText().toString();

                if (!status.isEmpty()) {
                    //to upload data to firebase in real time;
                    mStatusRef.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull final Task<Void> task) {

                            if (task.isSuccessful()) {

                                mProgressDialog.dismiss();

                            } else {
                                Toast.makeText(StatusActivity.this, "Error in updating changes to database", Toast.LENGTH_LONG).show();
                                mProgressDialog.dismiss();
                            }


                        }
                    });

                } else {
                    Toast.makeText(StatusActivity.this, "Enter some text.", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }


            }
        });

    }

    /**
     * init made;
     */
    private void init() {

        etStatus = (EditText) findViewById(R.id.et_status);
        btnSave = (Button) findViewById(R.id.btn_save);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
