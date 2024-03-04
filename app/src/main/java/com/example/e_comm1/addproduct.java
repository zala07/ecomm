package com.example.e_comm1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class addproduct extends AppCompatActivity {
    ImageView imgAdd;
    EditText edtAdd_name,edtAdd_disc,edtAdd_price;
    Spinner spiner_category;
    String name,disc,category,price,imageUri;
    Uri image;
    Button btnAdd_pro;
    z_productmodel data;
    String mode="add";
    int image_clicked=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        imgAdd=findViewById(R.id.imgAdd_image);
        edtAdd_name=findViewById(R.id.edtAdd_name);
        edtAdd_disc=findViewById(R.id.edtAdd_disc);
        edtAdd_price=findViewById(R.id.edtAdd_price);
        spiner_category=findViewById(R.id.edtAdd_category);

        settingUpSpinner();

        btnAdd_pro=findViewById(R.id.btnAdd_product);
        mode=getIntent().getStringExtra("mode");

        imageclick();
        if (mode.equals("add")){
            add();
        }else {
            data=(z_productmodel) getIntent().getSerializableExtra("data");
            update();
        }
    }


    @SuppressLint("SetTextI18n")
    private void update() {
        btnAdd_pro.setText("UPDATE");
        edtAdd_name.setText(data.getName());
        edtAdd_disc.setText(data.getDiscription());
        edtAdd_price.setText(data.getPrice());
        spiner_category.setVisibility(View.GONE);
        Glide.with(addproduct.this).load(data.getImage()).into(imgAdd);

        btnAdd_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=edtAdd_name.getText().toString();
                disc=edtAdd_disc.getText().toString();
                price=edtAdd_price.getText().toString();
                category=data.getCategory();

                if (name.isEmpty()){
                    Toast.makeText(addproduct.this,"enter  name",Toast.LENGTH_SHORT).show();
                    return;
                }if(disc.isEmpty()){
                    Toast.makeText(addproduct.this,"enter disc",Toast.LENGTH_SHORT).show();
                    return;
                }if(price.isEmpty()){
                    Toast.makeText(addproduct.this,"enter price",Toast.LENGTH_SHORT).show();
                    return;
                }
                //image
                imageup();
            }
        });
    }


    private void imageclick() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(addproduct.this)
                        .crop()
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
                image_clicked=1;
            }
        });
    }

    private void add() {
        btnAdd_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=edtAdd_name.getText().toString();
                disc=edtAdd_disc.getText().toString();
                price=edtAdd_price.getText().toString();
                category=spiner_category.getSelectedItem().toString();
                //category=edtAdd_category.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(addproduct.this,"enter  name",Toast.LENGTH_SHORT).show();
                    return;
                }if(disc.isEmpty()){
                    Toast.makeText(addproduct.this,"enter disc",Toast.LENGTH_SHORT).show();
                    return;
                }if(price.isEmpty()){
                    Toast.makeText(addproduct.this,"enter price",Toast.LENGTH_SHORT).show();
                    return;
                }if (category.isEmpty()){
                    Toast.makeText(addproduct.this,"enter valid category",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageup();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            image= data.getData();
            imgAdd.setImageURI(image);
        }
    }
    private void imageup() {
        ProgressDialog dialog=new ProgressDialog(addproduct.this);
        dialog.setTitle("uploader");
        dialog.setCancelable(false);
        dialog.show();
        //storage images
        if (image_clicked==1){
            FirebaseStorage fbs=FirebaseStorage.getInstance();
            StorageReference sr=fbs.getReference().child("product").child(image.getLastPathSegment());
            sr.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    String id1= UUID.randomUUID().toString();
                    //geting path of image
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri uriimage=uriTask.getResult();
                    imageUri=uriimage.toString();

                    if (mode.equals("add")){
                        z_productmodel data=new z_productmodel(name,imageUri,price,category,id1,disc,null);
                        FirebaseFirestore.getInstance().collection("product").document(id1).set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(addproduct.this, "Data Added Succesfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }else {
                        updatedata();
                        finish();
                    }
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
                    dialog.dismiss();
                    Toast.makeText(addproduct.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            imageUri=data.getImage();
            updatedata();
            dialog.dismiss();
            finish();
        }

    }

    private void updatedata() {
        String id1=data.getId1();
       // z_productmodel data=new z_productmodel(name,imageUri,price,category,id1,disc,null);
        HashMap<String, Object> data=new HashMap<>();
        data.put("name",name);
        data.put("image",imageUri);
        data.put("price",price);
        data.put("discription",disc);
        FirebaseFirestore.getInstance().collection("product").document(id1).update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(addproduct.this, "data updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void settingUpSpinner() {
        String[] items=new String[]{"new","mobile","footwear","toy","watches","fashion","book"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,items);
        spiner_category.setAdapter(adapter);
    }
}