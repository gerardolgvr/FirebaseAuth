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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    //firebase
    private FirebaseAuth mAuth;
    //ui
    private Button btnRegister;
    TextInputEditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //initiating instance
        mAuth = FirebaseAuth.getInstance();

        editEmail = (TextInputEditText) findViewById(R.id.edit_register_email);
        editPassword = (TextInputEditText) findViewById(R.id.edit_register_password);
        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method that allow us create a user account
                createAccount();
            }
        });

    }

    //method that help us to redirect to login activity
    public void goLoginActivity(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //method that allow us to create an account
    public void createAccount(){
        //getting data
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //validating inputs
        if(email.equals("")){
            Toast.makeText(getApplicationContext(), "Email required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals("")){
            Toast.makeText(getApplicationContext(), "Password required", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //verifying the operation result
                        if(!task.isSuccessful()){
                            Toast.makeText(CreateAccountActivity.this, "Error! Try again", Toast.LENGTH_SHORT).show();;
                        } else {
                            startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });



    }
}
