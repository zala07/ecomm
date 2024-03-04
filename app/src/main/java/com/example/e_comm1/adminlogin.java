package com.example.e_comm1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class adminlogin extends AppCompatActivity {

    TextView edtEmail,edtPass;
    Button btnAdminLogin;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        edtEmail=findViewById(R.id.edtEmailAdminLogin);
        edtPass=findViewById(R.id.edtPassAdminLogin);
        btnAdminLogin=findViewById(R.id.btnAdminLogin);
        getSupportActionBar().setTitle("Welcome");

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=edtEmail.getText().toString();
                password=edtPass.getText().toString();
                if (email.equals("admin@gmail.com")&&password.equals("123456")){
                    Intent intent=new Intent(adminlogin.this, adminview.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(adminlogin.this, "enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}