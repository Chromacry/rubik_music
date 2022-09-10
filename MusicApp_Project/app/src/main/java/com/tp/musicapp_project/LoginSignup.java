package com.tp.musicapp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
    }

    public void onClickHandler(View view){
        switch (view.getId()){
            case R.id.btnSignUp1:
                startActivity(new Intent(this, SignUp.class));
                break;
            case R.id.btnLogin1:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}