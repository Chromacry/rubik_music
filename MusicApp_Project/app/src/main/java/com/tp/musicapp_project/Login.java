package com.tp.musicapp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("");
    private FirebaseAuth mAuth;
    private EditText username, password;
    private ImageButton loginBtn, loginBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.txt_Username);
        password = findViewById(R.id.txt_Password);
        loginBtn = findViewById(R.id.btnLogin);
        loginBackBtn = findViewById(R.id.btnLoginBack);

    }

    //Disabled as Logout page not made
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getUid().equals(mAuth.getUid())) {
            startActivity(new Intent(Login.this, HomePage.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
//                startActivity(new Intent(this, HomePage.class));
                userLogin();
                break;
            case R.id.btnLoginBack:
                startActivity(new Intent(this, LoginSignup.class));
        }
    }

    private void userLogin() {
        // get data
        String usernameTxt = username.getText().toString().trim();
        String passwordTxt = password.getText().toString().trim();

        // validation
        if (usernameTxt.isEmpty() || passwordTxt.isEmpty()) {
            Toast.makeText(Login.this, "Please enter your username or password!", Toast.LENGTH_SHORT).show();
        } else {

            //Check Sign in with Firebase
            mAuth.signInWithEmailAndPassword(usernameTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Login.this, HomePage.class));
                    } else {
                        Toast.makeText(Login.this, "Login Credentials are not correct!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
