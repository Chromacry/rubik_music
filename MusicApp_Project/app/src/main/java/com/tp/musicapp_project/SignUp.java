package com.tp.musicapp_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tp.musicapp_project.OtherClasses.User;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    // Import Firebase Class Database Reference Repo
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("");
    private FirebaseAuth mAuth;
    private EditText email, username, password, confirmpassword;
    private ImageButton signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        signupBtn = findViewById(R.id.btnSignUpBack);
        email = findViewById(R.id.txtEmail);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        confirmpassword = findViewById(R.id.txtConfirmPassword);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp1:
                Log.d("Signup", "Pressed");
                registerUser();
                break;
            case R.id.btnSignUpBack:
                startActivity(new Intent(this, Login.class));
        }
    }

    private void registerUser(){
        // get data
        String emailTxt = email.getText().toString().trim();
        String usernameTxt = username.getText().toString().trim();
        String passwordTxt = password.getText().toString().trim();
        String confirmpasswordTxt = confirmpassword.getText().toString().trim();

        // validation
        if (emailTxt.isEmpty() || usernameTxt.isEmpty() || passwordTxt.isEmpty() || confirmpasswordTxt.isEmpty()) {
            Toast.makeText(SignUp.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
        }
        // check if password are the same with confirm password
        else if (!passwordTxt.equals(confirmpasswordTxt)) {
            Toast.makeText(SignUp.this, "Password do not match!", Toast.LENGTH_SHORT).show();
        }
        // Sending Data into Firebase for signup first time users
        else {
//                    databaseReference.child("users").child("email").setValue(emailTxt);
            mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(usernameTxt, emailTxt);
                                databaseReference.child("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()){
                                            Toast.makeText(SignUp.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                            // Go to HomePage
                                            Intent intent = new Intent(SignUp.this, HomePage.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(SignUp.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}
