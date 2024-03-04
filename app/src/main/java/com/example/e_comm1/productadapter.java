package com.example.e_comm1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class productadapter extends RecyclerView.Adapter<productadapter.ViewHolder> {
    Context context;
    ArrayList<z_productmodel> data;

    public productadapter(Context context, ArrayList<z_productmodel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.productview,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p=position;
        z_productmodel data1=data.get(p);
        String id=data1.getId1();
        holder.txtPName.setText(data1.getName());
        holder.txtPPrice.setText("₹ "+data1.getPrice());
        Glide.with(context).load(data1.getImage()).into(holder.imgPviewImage);
        fav(holder,id,data1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, prodDetails.class);
                intent.putExtra("data",data1);
                intent.putExtra("click","adapter");
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPviewImage,imgPheart;
        TextView txtPPrice,txtPName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPviewImage=itemView.findViewById(R.id.imgPviewImage);
            imgPheart=itemView.findViewById(R.id.imgPviewHeart);
            txtPName=itemView.findViewById(R.id.txtPName);
            txtPPrice=itemView.findViewById(R.id.txtPPrice);
        }
    }
    private void fav(ViewHolder holder, String id, z_productmodel data12) {
        FirebaseFirestore.getInstance().collection("fav").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc=task.getResult();
                            if (doc.exists()){
                                    holder.imgPheart.setImageResource(R.drawable.redheart);
                                    holder.imgPheart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FirebaseFirestore.getInstance().collection("fav").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+id).delete().addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(context, "removed from fav", Toast.LENGTH_SHORT).show();
                                                            fav(holder,id,data12);
                                                        }
                                                    }
                                            );
                                            holder.imgPheart.setImageResource(R.drawable.heart);
                                        }
                                    });
                            }else { holder.imgPheart.setImageResource(R.drawable.heart);
                                holder.imgPheart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                        data12.setUser(user);
                                        FirebaseFirestore.getInstance().collection("fav").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+id).set(data12)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "added to favorite", Toast.LENGTH_SHORT).show();
                                                        holder.imgPheart.setImageResource(R.drawable.redheart);
                                                        fav(holder,id,data12);
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    }
                });
    }
}
