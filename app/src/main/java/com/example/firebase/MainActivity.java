package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements Dialog.DialogListener, DialogEmail.DialogEmailListener {
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String password;
    private String operation;

    Button btn_signOut, btn_remove_user, btn_change_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiating instance
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //setting up the toolbar
        setToolbar();

        //ui
        btn_signOut = (Button) findViewById(R.id.btn_sing_out);
        btn_remove_user = (Button) findViewById(R.id.btn_remove_user);
        btn_change_email = (Button) findViewById(R.id.btn_change_email);

        //event sign out
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        //event remove account
        btn_remove_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation = "remove";
                openDialog();
            }
        });

        //event change email
        btn_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation = "updateEmail";
                openEmailDialog();
                openDialog();
            }
        });



    }

    //setting up the toolbar
    public void setToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    public void logOut(){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    public void openEmailDialog(){
        DialogEmail dialog = new DialogEmail();
        dialog.show(getSupportFragmentManager(), "DialogEmail");
    }


    @Override
    public void getPassword(String password) {
        //validating input data
        if(password.equals("")){
            Toast.makeText(getApplicationContext(), "Error! Password required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(operation == "remove"){
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Account Deleted!", Toast.LENGTH_SHORT).show();
                                                goLoginActivity();
                                            }
                                        }

                                    });
                        }
                    });
        } else if(operation == "updateEmail"){
            this.password = password;
        }

    }

    //method that help us to redirect to login activity
    public void goLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void getEmail(final String email) {
        //validating input data
        if(email.equals("")){
            Toast.makeText(getApplicationContext(), "Error! new email required", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), this.password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Email Updated!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }
}
