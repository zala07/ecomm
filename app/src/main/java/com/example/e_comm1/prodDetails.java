

package com.example.e_comm1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class prodDetails extends AppCompatActivity {
    private ImageView imgDetMain,imgDetHeart;
    private TextView txtdetPrice,txtDetName,txtDetDesc;
    private Button btnDetAddToCart,btnDetBuy;

    private String click;
    private z_productmodel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_details);

        //getting id
        imgDetHeart=findViewById(R.id.imgDetHeart);
        imgDetMain=findViewById(R.id.imgDetMain);
        txtDetDesc=findViewById(R.id.txtDescDet);
        txtDetName=findViewById(R.id.txtDetName);
        txtdetPrice=findViewById(R.id.txtDetPrice);
        btnDetAddToCart=findViewById(R.id.btnDetAddToCart);
        btnDetBuy=findViewById(R.id.btnDetBuy);

        data=(z_productmodel) getIntent().getSerializableExtra("data");
        click=getIntent().getStringExtra("click");
        getSupportActionBar().setTitle(data.getName());

        settingUpData();
    }

    @SuppressLint("SetTextI18n")
    private void settingUpData() {
        samedata();
        if (click.equals("admin")){
            btnDetAddToCart.setText("UPDATE");
            btnDetBuy.setText("DELETE");
            //delete
            btnDetBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete();
                    finish();
                }
            });
            //update
            btnDetAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   update();
                   finish();
                }
            });
        }else if (click.equals("order")){
            btnDetAddToCart.setText("Cancel Order");
            btnDetBuy.setVisibility(View.GONE);
            btnDetAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id1=data.getId1();
                    String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    FirebaseFirestore.getInstance().collection("order").document(user+id1).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(prodDetails.this, "Order Canceled", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
            });
            fav();
        }else if (click.equals("cart")){
            btnDetAddToCart.setText("Remove Cart");
            btnDetAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id1=data.getId1();
                    String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    FirebaseFirestore.getInstance().collection("cart").document(user+id1).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(prodDetails.this, "Removed From Cart", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
            });
            btnDetBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buy();
                    finish();
                }
            });fav();
        }else {
            btnDetAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart();
                }
            });
            btnDetBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buy();
                }
            });
            fav();
        }
    }


    private void buy() {
        String id1=data.getId1();
        String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        data.setUser(user);
        FirebaseFirestore.getInstance().collection("order").document(user+id1).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(prodDetails.this, "Order Placed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToCart() {
        String id1=data.getId1();
        String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        data.setUser(user);
        FirebaseFirestore.getInstance().collection("cart").document(user+id1).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(prodDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //update in only admin view not in cart and fav
    private void update(){
        Intent intent=new Intent(prodDetails.this, addproduct.class);
        intent.putExtra("data",data);
        intent.putExtra("mode","update");
        startActivity(intent);
    }

    //admin delet from all fav view..
    private void delete() {
        FirebaseFirestore.getInstance().collection("product").document(data.getId1()).delete();
        //deletin from
        FirebaseFirestore.getInstance().collection("fav").whereEqualTo("id1",data.getId1()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> data1=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot ds:data1){
                    z_productmodel ds1=ds.toObject(z_productmodel.class);
                    String del=ds1.getUser()+data.getId1();
                    FirebaseFirestore.getInstance().collection("fav").document(del).delete();
                }
            }
        });

        //deleting from cart
        FirebaseFirestore.getInstance().collection("cart").whereEqualTo("id1",data.getId1()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> data1=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot ds:data1){
                    z_productmodel ds1=ds.toObject(z_productmodel.class);
                    String del=ds1.getUser()+data.getId1();
                    FirebaseFirestore.getInstance().collection("fav").document(del).delete();
                }
            }
        });
        finish();

    }

    private void fav() {
        FirebaseFirestore.getInstance().collection("fav").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+data.getId1())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc=task.getResult();
                            if (doc.exists()){
                                imgDetHeart.setImageResource(R.drawable.redheart);
                                imgDetHeart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseFirestore.getInstance().collection("fav").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+data.getId1()).delete().addOnCompleteListener(
                                                new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(prodDetails.this, "removed from fav", Toast.LENGTH_SHORT).show();
                                                        fav();
                                                    }
                                                }
                                        );
                                        imgDetHeart.setImageResource(R.drawable.heart);
                                    }
                                });
                            }else { imgDetHeart.setImageResource(R.drawable.heart);
                                imgDetHeart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                        data.setUser(user);
                                        FirebaseFirestore.getInstance().collection("fav").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+data.getId1()).set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(prodDetails.this, "added to favorite", Toast.LENGTH_SHORT).show();
                                                        imgDetHeart.setImageResource(R.drawable.redheart);
                                                        fav();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void samedata() {
        Glide.with(prodDetails.this).load(data.getImage()).into(imgDetMain);
        txtDetName.setText(data.getName());
        txtdetPrice.setText("₹ "+data.getPrice());
        txtDetDesc.setText(data.getDiscription());
    }
}