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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class favadapter extends RecyclerView.Adapter<favadapter.ViewHolder> {
    ArrayList<z_productmodel> datalist;
    Context context;
    String datafrom;

    public favadapter(ArrayList<z_productmodel> datalist, Context context,String datafrom) {
        this.datalist = datalist;
        this.context = context;
        this.datafrom = datafrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.favview,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p=position;
        z_productmodel data=datalist.get(position);
        Glide.with(context).load(data.getImage()).into(holder.imgFavImage);
        holder.txtFavName.setText(data.getName());
        holder.txtFavPrice.setText("₹ "+data.getPrice());
        if (datafrom.equals("cart")){
            holder.imgFavHeart.setImageResource(R.drawable.baseline_delete_24);
            holder.imgFavHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(data);
                }
            });
            itemclick(holder,data,datafrom);
        }
        if (datafrom.equals("fav")){
            holder.imgFavHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(data);
                }
            });
            itemclick(holder,data,datafrom);
        }
        if (datafrom.equals("order")){
            holder.imgFavHeart.setVisibility(View.GONE);
            itemclick(holder,data,datafrom);
        }
    }

    private void itemclick(ViewHolder holder, z_productmodel data,String datafrom) {
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(context, prodDetails.class);
               intent.putExtra("data",data);
               intent.putExtra("click",datafrom);
               context.startActivity(intent);
           }
       });
    }

    private void delete(z_productmodel data1) {
        FirebaseFirestore.getInstance().collection(datafrom).document(FirebaseAuth.getInstance().getCurrentUser().getEmail()+data1.getId1()).delete();
        datalist.remove(data1);
        Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
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
        ImageView imgFavImage,imgFavHeart;
        TextView txtFavName,txtFavPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavHeart=itemView.findViewById(R.id.imgFavHeart);
            imgFavImage=itemView.findViewById(R.id.imgFavImage);
            txtFavName=itemView.findViewById(R.id.txtFavName);
            txtFavPrice=itemView.findViewById(R.id.txtFavPrice);
        }
    }
}
