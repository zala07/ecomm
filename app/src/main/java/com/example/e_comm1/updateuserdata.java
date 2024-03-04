package com.example.e_comm1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class updateuserdata extends AppCompatActivity {

    private ImageView imgUpdateIcon;
    private EditText edtUpadteName,edtUpadteAddress,edtUpadtePhone;
    private Button btnUpdate;
    String name,address,phone;
    String mode;
    z_profilimodel data;
    Uri image_uri;
    String field,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuserdata);
        imgUpdateIcon=findViewById(R.id.imgUpdateimage);
        edtUpadteName=findViewById(R.id.edtUpadateName);
        edtUpadtePhone=findViewById(R.id.edtUpdatePhone);
        edtUpadteAddress=findViewById(R.id.edtupdateAddress);
        btnUpdate=findViewById(R.id.btnUpdate);

        mode=getIntent().getStringExtra("mode");
        update();
        adduserdata();
    }


    private void update() {
        if (mode.equals("update")){
            getSupportActionBar().setTitle("UPDATE");
            data= (z_profilimodel) getIntent().getSerializableExtra("pdata");
            setdata();
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name=edtUpadteName.getText().toString();
                    address=edtUpadteAddress.getText().toString();
                    phone=edtUpadtePhone.getText().toString();
                    if (!data.getName().equals(name)||!name.isEmpty()){
                        field="name";
                        value=name;
                        updatedat(field);
                    }if (!data.getAddress().equals(address)||!address.isEmpty()) {
                        field="address";
                        value=address;
                        updatedat(field);
                    }if (!data.getPhone().equals(phone)||!phone.isEmpty()){
                        field="phone";
                        value=phone;
                        updatedat(field);
                    }
                    finish();
                }
            });
        }
    }

    private void updatedat(String field) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        int index=email.indexOf("@");
        String trm_email=email.substring(0,index);
        FirebaseFirestore.getInstance().collection(trm_email).document("profile").update(field,value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(updateuserdata.this, "data updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void imageget() {
        imgUpdateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(updateuserdata.this)
                        .crop()
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }

    private void setdata() {
        edtUpadteName.setText(data.getName());
        edtUpadtePhone.setText(data.getPhone());
        edtUpadteAddress.setText(data.getAddress());
        Glide.with(updateuserdata.this).load(data.getImageUri()).into(imgUpdateIcon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            Uri uri=data.getData();
            image_uri=uri;
            Glide.with(updateuserdata.this).load(image_uri).into(imgUpdateIcon);
        }
    }
    @SuppressLint("SetTextI18n")
    private void adduserdata() {
        if (mode.equals("add")){
            btnUpdate.setText("SUBMIT");
            getSupportActionBar().setTitle("WELCOME");
            name=getIntent().getStringExtra("name");
            edtUpadteName.setText(name);
            imageget();
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name=edtUpadteName.getText().toString();
                    address=edtUpadteAddress.getText().toString();
                    phone=edtUpadtePhone.getText().toString();
                    if (name.isEmpty()||address.isEmpty()||phone.isEmpty()||String.valueOf(image_uri).isEmpty()){
                        Toast.makeText(updateuserdata.this, "enter valid data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ProgressDialog dialog=new ProgressDialog(updateuserdata.this);
                    dialog.setTitle("uploader");
                    dialog.setCancelable(false);
                    dialog.show();
                    //storage images
                    FirebaseStorage fbs=FirebaseStorage.getInstance();
                    StorageReference sr=fbs.getReference().child("products").child(image_uri.getLastPathSegment());


                    sr.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri uriimage=uriTask.getResult();
                            String imageUri=uriimage.toString();
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            String email=user.getEmail();
                            int index=email.indexOf("@");
                            String trm_email=email.substring(0,index);

                            z_profilimodel data=new z_profilimodel(name,imageUri,address,phone);
                            FirebaseFirestore.getInstance().collection(trm_email)
                                    .document("profile").set(data);
                            finish();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float per= 100*snapshot.getBytesTransferred() /snapshot.getTotalByteCount();
                            dialog.setMessage("uploaded :"+(int)per+"%");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}