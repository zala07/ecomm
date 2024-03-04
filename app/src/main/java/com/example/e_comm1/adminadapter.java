package com.example.e_comm1;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adminadapter extends RecyclerView.Adapter<adminadapter.ViewHolder> {
    Context context;
    ArrayList<z_productmodel> datalist;

    public adminadapter(Context context, ArrayList<z_productmodel> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.productview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p=position;
        z_productmodel data1=datalist.get(p);
        holder.txtAdName.setText(data1.getName());
        holder.txtAdPrice.setText("₹ "+data1.getPrice());
        Glide.with(context).load(data1.getImage()).into(holder.imgAdviewImage);
        holder.imgAdheart.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, prodDetails.class);
                intent.putExtra("data",data1);
                intent.putExtra("click","admin");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void searchdata(ArrayList<z_productmodel> sdata) {
        datalist=sdata;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAdviewImage,imgAdheart;
        TextView txtAdPrice,txtAdName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAdviewImage=itemView.findViewById(R.id.imgPviewImage);
            imgAdheart=itemView.findViewById(R.id.imgPviewHeart);
            txtAdName=itemView.findViewById(R.id.txtPName);
            txtAdPrice=itemView.findViewById(R.id.txtPPrice);
        }
    }
}
