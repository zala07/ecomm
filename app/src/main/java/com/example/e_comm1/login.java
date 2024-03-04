package com.example.e_comm1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class login extends AppCompatActivity {

    EditText edtEmailLogin,edtPassLogin;
    Button btnLogin;
    TextView txtRegisterRedirect;
    private FirebaseAuth mAuth;

    String email,password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i=new Intent(login.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmailLogin=findViewById(R.id.edtEmailLogin);
        edtPassLogin=findViewById(R.id.edtPassLogin);
        btnLogin=findViewById(R.id.btnLogin);
        txtRegisterRedirect=findViewById(R.id.txtRegisterRedirect);

        mAuth = FirebaseAuth.getInstance();

        txtRegisterRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });
    }

    private void userlogin() {
        email=edtEmailLogin.getText().toString();
        password=edtPassLogin.getText().toString();

        if (email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "enter valid data", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i=new Intent(login.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void admin(View view) {
        Intent intent=new Intent(login.this, adminlogin.class);
        startActivity(intent);
    }
}