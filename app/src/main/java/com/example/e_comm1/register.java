package com.example.e_comm1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class register extends AppCompatActivity {
    TextView txtLoginRedirect;
    private FirebaseAuth mAuth;
    EditText edtNameRegister,edtEmailRegister,edtPassRegister,edtPassConf;
    Button btnRegister;
    String email,password,name,confpass;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i=new Intent(register.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtLoginRedirect=findViewById(R.id.txtLoginRedirect);
        edtNameRegister=findViewById(R.id.edtNameRegister);
        edtPassRegister=findViewById(R.id.edtPassRegister);
        edtPassConf=findViewById(R.id.edtPassConfRegister);
        edtEmailRegister=findViewById(R.id.edtEmailRegister);
        btnRegister=findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        txtLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(register.this,login.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=edtNameRegister.getText().toString();
                email=edtEmailRegister.getText().toString();
                password=edtPassRegister.getText().toString();
                confpass=edtPassConf.getText().toString();
                if (name.isEmpty()||email.isEmpty()||password.isEmpty()){
                    Toast.makeText(register.this, "enter valid data", Toast.LENGTH_SHORT).show();
                    return;
                }if (!password.equals(confpass)){
                    Toast.makeText(register.this, "enter same pass as abeve", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent i=new Intent(register.this,updateuserdata.class);
                                    i.putExtra("mode","add");
                                    i.putExtra("name",name);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}