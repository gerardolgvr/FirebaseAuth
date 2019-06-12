package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;

    private TextInputEditText editEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //initiating instance
        mAuth = FirebaseAuth.getInstance();

        //ui
        editEmail = (TextInputEditText) findViewById(R.id.edit_email_reset_activity);
        btnReset = (Button) findViewById(R.id.btn_reset_activity);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetEmailPassword();
                goLogin();
            }
        });
    }

    public void sendResetEmailPassword(){
        String email = editEmail.getText().toString().trim();
        if(email.equals("")){
            Toast.makeText(getApplicationContext(), "Error! email required", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goLogin(){
        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
        finish();
    }

    public void goBack(View view){
        finish();
    }
}
